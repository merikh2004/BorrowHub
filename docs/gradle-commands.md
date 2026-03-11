# Gradle Command Cheat Sheet

This document provides a quick reference for commonly used `gradlew` (Gradle Wrapper) commands used for building, testing, and managing the BorrowHub Android application.

> **Note:** Run these commands from the `mobile-app/` directory. On Windows, you can use `.\gradlew` or `.\gradlew.bat`. On macOS/Linux, use `./gradlew`.

## Essential Commands

### Build & Clean
Clean the build directory (removes all generated files, useful for resolving weird build errors):
```bash
./gradlew clean
```

Build the debug APK (generates `app-debug.apk` in `app/build/outputs/apk/debug/`):
```bash
./gradlew assembleDebug
```

Build the release APK:
```bash
./gradlew assembleRelease
```

### Testing
Run all local unit tests (executes tests in `src/test/`):
```bash
./gradlew test
```

Run only debug unit tests (faster, skips release tests):
```bash
./gradlew testDebugUnitTest
```

Run instrumented tests on a connected device/emulator (executes tests in `src/androidTest/`):
```bash
./gradlew connectedAndroidTest
```

### Dependencies & Setup
Sync and download all dependencies (useful if Android Studio isn't syncing properly):
```bash
./gradlew build --refresh-dependencies
```

View the project's dependency tree:
```bash
./gradlew app:dependencies
```