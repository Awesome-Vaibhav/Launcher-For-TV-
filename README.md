# Launcher-For-TV

Android TV launcher application built with Jetpack Compose.

## Features
- Android TV / Leanback launcher entrypoint
- D-Pad friendly navigation and focus handling
- Search and favorites for installed apps
- Release APK publishing from GitHub Actions on every push

## Local development
### Prerequisites
- Android Studio (latest stable)
- JDK 17
- Android SDK + NDK (for JNI build)

### Setup
1. Open `/tmp/workspace/AnikethJana/Launcher-For-TV` in Android Studio.
2. Create `.env` from `.env.example` and add `GEMINI_API_KEY`.
3. Build and test with Gradle:
   - `gradle testDebugUnitTest`
   - `gradle assembleRelease`

## CI/CD workflow
Workflow file: `.github/workflows/android-release.yml`

On **every push**, the workflow:
1. Runs unit tests.
2. Builds the release APK.
3. Uploads APK as a workflow artifact.
4. Creates a GitHub Release and attaches the APK asset.

## Signing secrets required in GitHub
Create a GitHub Actions environment named `KEYSTORE` and add these secrets:
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`
- `SIGNING_KEY` (base64 encoded JKS)

The workflow decodes the keystore and passes:
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`
- `KEYSTORE_PATH`

## JNI performance bridge
Native bridge files:
- `app/src/main/java/com/example/NativeStringMatcher.kt`
- `app/src/main/cpp/native-utils.cpp`
- `app/src/main/cpp/CMakeLists.txt`

This bridge is used for faster case-insensitive matching during app filtering, with Kotlin fallback when native loading is unavailable.
