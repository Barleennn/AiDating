# AiDating (Android, Kotlin, Jetpack Compose)

Нативное Android-приложение на Kotlin с Jetpack Compose и Navigation для реализации дизайна из Figma.

## Что внутри
- Kotlin, Jetpack Compose, Material 3
- Navigation Compose (Splash → Login → Home → Profile)
- Базовая тема и структура для дальнейшей верстки под макет

## Сборка и запуск
Убедитесь, что установлены JDK 21+ и Android SDK/Android Studio.

### Через Gradle (CLI)
```bash
./gradlew :app:assembleDebug
```
APK будет в `app/build/outputs/apk/debug/`.

### Через Android Studio
- Откройте папку проекта
- Подождите синхронизацию Gradle
- Запустите конфигурацию `app` на эмуляторе/устройстве

## Импорт дизайна из Figma (Python)
1) Создайте персональный токен Figma и экспортируйте переменную:
```bash
export FIGMA_TOKEN=xxxxx
```
2) Запуск скрипта:
```bash
python3 scripts/figma_pull_tokens_and_assets.py --file UTd0VGhnFYgvkn6NLepGAM --out design
```
3) Экспорт изображений (по node ids):
```bash
python3 scripts/figma_pull_tokens_and_assets.py --file UTd0VGhnFYgvkn6NLepGAM \
  --out design --images 2036:4956,2036:4904 --format png --scale 2
```
Результат: `design/file.json`, `design/styles.json`, `design/images/`.

## Далее по дизайну
- Подключить шрифты, цвета и размеры из Figma (tokens)
- Сверстать экраны (auth, карточки свайпа, профиль и т.д.)
- Добавить состояние, сетевой слой (если нужен бекенд/AI)

Ссылка на макет: см. сообщение в чате.