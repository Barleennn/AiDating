#!/usr/bin/env python3
"""
Figma pull script (Python)

Usage examples:
  export FIGMA_TOKEN=xxxxx
  python3 scripts/figma_pull_tokens_and_assets.py --file UTd0VGhnFYgvkn6NLepGAM --out design
  # with images export (comma-separated node ids):
  python3 scripts/figma_pull_tokens_and_assets.py --file UTd0VGhnFYgvkn6NLepGAM --out design \
      --images 2036:4956,2036:4904 --format png --scale 2

  # split file.json into per-node files (e.g., frames/components/instances)
  python3 scripts/figma_pull_tokens_and_assets.py --file UTd0VGhnFYgvkn6NLepGAM --out design \
      --split --split-types FRAME,COMPONENT,INSTANCE --split-out design/nodes --name-template "{name}.json" --auto-images

Outputs:
  - design/file.json     # full Figma file document
  - design/styles.json   # styles listing
  - design/images/*      # downloaded images (if --images or --auto-images provided)
  - design/nodes/<page>/<name>[__id].json  # per-node files when --split

Env:
  FIGMA_TOKEN=<personal access token>
"""
from __future__ import annotations

import argparse
import json
import os
import re
import sys
from pathlib import Path
from typing import Dict, Any, List, Iterable, Tuple, Set

import requests

API_BASE = "https://api.figma.com/v1"


class FigmaClient:
    def __init__(self, token: str, session: requests.Session | None = None) -> None:
        self.token = token
        self.session = session or requests.Session()
        self.session.headers.update({
            "X-FIGMA-TOKEN": token,
            "User-Agent": "AiDating-FigmaPull/1.1",
        })

    def _get(self, url: str, params: Dict[str, Any] | None = None) -> requests.Response:
        resp = self.session.get(url, params=params, timeout=60)
        if not resp.ok:
            raise RuntimeError(f"GET {url} failed: {resp.status_code} {resp.text[:200]}")
        return resp

    def get_file(self, file_key: str) -> Dict[str, Any]:
        url = f"{API_BASE}/files/{file_key}"
        return self._get(url).json()

    def get_styles(self, file_key: str) -> Dict[str, Any]:
        url = f"{API_BASE}/files/{file_key}/styles"
        return self._get(url).json()

    def get_images_urls(self, file_key: str, node_ids: List[str], img_format: str = "png", scale: int = 1) -> Dict[str, str]:
        url = f"{API_BASE}/images/{file_key}"
        params = {
            "ids": ",".join(node_ids),
            "format": img_format,
            "scale": str(scale),
        }
        data = self._get(url, params=params).json()
        return data.get("images", {})


def save_json(path: Path, data: Dict[str, Any]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)


def sanitize_filename(name: str, fallback: str) -> str:
    # Keep unicode letters/numbers/space/_-., remove reserved characters
    name = name.strip() or fallback
    name = re.sub(r"[\\/:*?\"<>|]", "_", name)
    # Collapse whitespace
    name = re.sub(r"\s+", " ", name)
    # Limit length
    return name[:120]


def short_id(node_id: str) -> str:
    return node_id.replace(":", "_")


def iter_nodes(root: Dict[str, Any]) -> Iterable[Tuple[Dict[str, Any], List[str]]]:
    # Yield (node, breadcrumbs) where breadcrumbs are names: [Document, Page, ..., Parent]
    stack: List[Tuple[Dict[str, Any], List[str]]] = [(root, [root.get("name", "Document")])]
    while stack:
        node, crumbs = stack.pop()
        yield node, crumbs
        for child in node.get("children", [])[::-1]:
            name = child.get("name", child.get("type", "Node"))
            stack.append((child, crumbs + [name]))


def collect_image_node_ids(node: Dict[str, Any]) -> Set[str]:
    ids: Set[str] = set()
    def check_fills(lst: List[Dict[str, Any]]):
        for fill in lst:
            if fill.get("type") == "IMAGE":
                return True
        return False
    if check_fills(node.get("fills", [])) or check_fills(node.get("background", [])):
        if "id" in node:
            ids.add(node["id"])
    for ch in node.get("children", []):
        ids |= collect_image_node_ids(ch)
    return ids


def split_file_json(file_json: Dict[str, Any], out_dir: Path, types: Set[str], name_template: str, include_id_for_dupes: bool, max_files: int | None) -> Tuple[int, List[str], Set[str]]:
    written = 0
    pageset: Set[str] = set()
    names_seen: Dict[Path, int] = {}
    collected_img_ids: Set[str] = set()

    doc = file_json.get("document") or {}
    for node, crumbs in iter_nodes(doc):
        ntype = node.get("type")
        if ntype == "DOCUMENT":
            continue
        if types and ntype not in types and "ALL" not in types:
            continue
        # Determine page folder (first canvas ancestor in crumbs)
        page = None
        # crumbs like [Document, Page, ... current]
        if len(crumbs) >= 2:
            page = crumbs[1]
        page_dir = out_dir / (page or "ROOT")
        pageset.add(page or "ROOT")
        name = node.get("name") or ntype or "node"
        base = name_template.format(name=name, id=node.get("id", ""), type=ntype)
        base = sanitize_filename(base, fallback=f"{ntype}")
        # dedupe within page dir
        dest = page_dir / base
        if dest.suffix.lower() != ".json":
            dest = dest.with_suffix(".json")
        if dest.exists() or dest in names_seen:
            if include_id_for_dupes:
                base2 = sanitize_filename(f"{name}__{short_id(node.get('id',''))}.json", fallback="node.json")
                dest = page_dir / base2
            else:
                idx = names_seen.get(dest, 1)
                while True:
                    candidate = page_dir / (dest.stem + f"__{idx}" + dest.suffix)
                    if not candidate.exists():
                        dest = candidate
                        names_seen[dest] = idx + 1
                        break
                    idx += 1
        dest.parent.mkdir(parents=True, exist_ok=True)
        save_json(dest, node)
        written += 1
        names_seen[dest] = 1
        # collect potential image nodes
        if node.get("fills") or node.get("background") or node.get("children"):
            collected_img_ids |= collect_image_node_ids(node)
        if max_files and written >= max_files:
            break
    return written, sorted(pageset), collected_img_ids


def parse_args(argv: List[str]) -> argparse.Namespace:
    p = argparse.ArgumentParser(description="Pull Figma file, styles and optionally images; optionally split file.json into per-node JSONs")
    p.add_argument("--file", dest="file_key", required=True, help="Figma file key")
    p.add_argument("--out", dest="out", default="design", help="Output directory (default: design)")
    p.add_argument("--images", dest="images", default=None, help="Comma-separated node ids to export images")
    p.add_argument("--format", dest="format", default="png", choices=["png", "jpg", "svg", "pdf"], help="Image format")
    p.add_argument("--scale", dest="scale", type=int, default=1, help="Image scale (png/jpg)")
    # split options
    p.add_argument("--split", action="store_true", help="Split file.json into per-node JSON files")
    p.add_argument("--split-types", default="FRAME,COMPONENT,INSTANCE", help="Comma-separated node types to split (use ALL for everything)")
    p.add_argument("--split-out", default=None, help="Directory to save per-node files (default: <out>/nodes)")
    p.add_argument("--name-template", default="{name}.json", help="Filename template: placeholders {name},{id},{type}")
    p.add_argument("--no-id-in-name", action="store_true", help="Do not append id on duplicate names (use numeric suffixes)")
    p.add_argument("--max-files", type=int, default=None, help="Limit number of per-node files for safety")
    p.add_argument("--auto-images", action="store_true", help="Automatically download images for nodes with IMAGE fills/backgrounds found during split")
    return p.parse_args(argv)


def main(argv: List[str]) -> int:
    args = parse_args(argv)

    token = os.environ.get("FIGMA_TOKEN")
    if not token:
        print("Error: FIGMA_TOKEN env is required", file=sys.stderr)
        return 2

    out_dir = Path(args.out)
    out_dir.mkdir(parents=True, exist_ok=True)

    client = FigmaClient(token)

    # Fetch file JSON
    file_json = client.get_file(args.file_key)
    save_json(out_dir / "file.json", file_json)
    print(f"Saved: {out_dir / 'file.json'}")

    # Fetch styles JSON
    styles_json = client.get_styles(args.file_key)
    save_json(out_dir / "styles.json", styles_json)
    print(f"Saved: {out_dir / 'styles.json'}")

    # Optionally split into per-node files
    collected_auto_img_ids: Set[str] = set()
    if args.split:
        split_dir = Path(args.split_out) if args.split_out else (out_dir / "nodes")
        raw_types = [t.strip().upper() for t in args.split_types.split(",") if t.strip()]
        types_set: Set[str] = set(raw_types)
        written, pages, collected_auto_img_ids = split_file_json(
            file_json=file_json,
            out_dir=split_dir,
            types=types_set,
            name_template=args.name_template,
            include_id_for_dupes=not args.no_id_in_name,
            max_files=args.max_files,
        )
        print(f"Split: wrote {written} files under {split_dir} (pages: {', '.join(pages)})")

    # Explicit images by ids
    images_ids: List[str] = []
    if args.images:
        images_ids.extend([s.strip() for s in args.images.split(",") if s.strip()])
    # Auto images collected from split
    if args.auto_images and collected_auto_img_ids:
        images_ids.extend(sorted(collected_auto_img_ids))

    if images_ids:
        # Chunk requests to 100 ids per call
        img_dir = out_dir / "images"
        for i in range(0, len(images_ids), 100):
            chunk = images_ids[i:i+100]
            urls = client.get_images_urls(args.file_key, chunk, args.format, args.scale)
            for node_id, url in urls.items():
                if not url:
                    print(f"Skip {node_id}: no URL returned", file=sys.stderr)
                    continue
                suffix = args.format.lower()
                dest = img_dir / f"{short_id(node_id)}.{suffix}"
                resp = requests.get(url, timeout=120)
                if not resp.ok:
                    print(f"Failed to download {node_id}: {resp.status_code}", file=sys.stderr)
                    continue
                dest.parent.mkdir(parents=True, exist_ok=True)
                with open(dest, "wb") as f:
                    f.write(resp.content)
                print(f"Downloaded: {dest}")

    # Write helper readme
    readme = (out_dir / "README.txt")
    readme.write_text(
        """
Design dump saved.
- file.json: full document
- styles.json: color/typography styles
- nodes/: per-node JSON files (if --split provided), grouped by page
- images/: exported nodes (if --images or --auto-images provided)

To find node ids:
- Use Figma inspect or GET /v1/files/{file_key} and search by names/layers.
- Map styles to MaterialTheme (colors, typography, shapes) in Compose.
        """.strip()
    )

    print("Done.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main(sys.argv[1:]))
