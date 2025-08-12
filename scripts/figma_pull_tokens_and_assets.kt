#!/usr/bin/env kotlin
@file:DependsOn("com.squareup.okhttp3:okhttp:4.12.0")
@file:DependsOn("com.squareup.moshi:moshi:1.15.1")
@file:DependsOn("com.squareup.moshi:moshi-kotlin:1.15.1")

import okhttp3.*
import java.io.File
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/*
Usage:
  FIGMA_TOKEN=xxxx kotlin scripts/figma_pull_tokens_and_assets.kt --file <FILE_KEY> [--out ./design]

It downloads Figma file JSON, extracts styles, and saves raw JSON + assets list for manual/automated mapping.
*/

data class Args(val fileKey: String, val out: File)

fun parseArgs(argv: Array<String>): Args {
    var fileKey: String? = null
    var out = File("design")
    var i = 0
    while (i < argv.size) {
        when (argv[i]) {
            "--file" -> { fileKey = argv.getOrNull(i+1); i++ }
            "--out" -> { out = File(argv.getOrNull(i+1) ?: "design"); i++ }
        }
        i++
    }
    require(!fileKey.isNullOrBlank()) { "--file <FILE_KEY> is required" }
    out.mkdirs()
    return Args(fileKey!!, out)
}

val token = System.getenv("FIGMA_TOKEN") ?: error("FIGMA_TOKEN env is required")
val client = OkHttpClient()
val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

fun get(url: String): String {
    val req = Request.Builder()
        .url(url)
        .addHeader("X-FIGMA-TOKEN", token)
        .build()
    client.newCall(req).execute().use { resp ->
        if (!resp.isSuccessful) error("HTTP ${'$'}{resp.code}")
        return resp.body?.string() ?: error("Empty body")
    }
}

fun main(argv: Array<String>) {
    val args = parseArgs(argv)
    val base = "https://api.figma.com/v1/files/${'$'}{args.fileKey}"
    val json = get(base)
    File(args.out, "file.json").writeText(json)
    println("Saved: ${'$'}{File(args.out, "file.json").absolutePath}")

    // Save styles and components raw dump for later mapping
    val styles = get("${'$'}base/styles")
    File(args.out, "styles.json").writeText(styles)

    // Images export example: collect nodes and get pngs (user maps ids manually or by name)
    // For now, just leaves placeholders: images mapping to be configured.
    File(args.out, "README.txt").writeText(
        """
        Design dump saved.
        - file.json: full document
        - styles.json: color/typography styles

        To export images:
          Use /images endpoint, e.g. GET /v1/images/{file_key}?ids=NODE_ID1,NODE_ID2&format=png
          Then download URLs returned (temporary).
        """.trimIndent()
    )
}

main(args)
