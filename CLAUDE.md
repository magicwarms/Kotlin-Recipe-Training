# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Kotlin Multiplatform (KMP) project targeting **Android** and **iOS**, root project `MyAwesomeRecipe`, shared package `com.example.myawesomerecipe`. Uses Compose Multiplatform for shared UI and a Gradle version catalog for dependency management.

## Build & Run

Prereq: JDK 11+, Android SDK, Xcode (for iOS). Use the Gradle wrapper (`./gradlew`).

- Android app: `./gradlew :androidApp:assembleDebug`
- iOS app: open `iosApp/iosApp.xcodeproj` in Xcode and run (no Gradle task builds the iOS app itself; Xcode drives the `SharedLogic` framework link).

## Tests

- Android host (unit) tests: `./gradlew :sharedLogic:testAndroidHostTest :sharedUI:testAndroidHostTest`
- iOS simulator tests: `./gradlew :sharedLogic:iosSimulatorArm64Test`
- All checks: `./gradlew check`
- Single test (JVM/Android host targets only): append `--tests "com.example.myawesomerecipe.SharedLogicAndroidHostTest.example"` to the module's host-test task.

## Module architecture

Three Gradle modules (`settings.gradle.kts`) plus a native iOS app dir:

- **`sharedLogic`** — KMP library holding business logic. Compiles to Android + iOS. Exposes a **static iOS framework named `SharedLogic`** (`iosArm64`, `iosSimulatorArm64`). Dependencies wired here: Ktor client, kotlinx.serialization JSON, coroutines, SQLDelight.
- **`sharedUI`** — Compose Multiplatform UI library. Depends on `sharedLogic` via `api(projects.sharedLogic)` (typesafe project accessor). **Currently Android-only** — its `kotlin {}` block declares only `androidLibrary`, no iOS target.
- **`androidApp`** — Android application. Depends on `sharedUI`; `MainActivity` calls the shared `App()` composable.
- **`iosApp/`** — native SwiftUI app. Links the `SharedLogic` framework **directly** and calls `Greeting().greet()` in `ContentView.swift`.

**Key consequence of the topology:** the shared Compose UI (`App()` in `sharedUI`) runs on Android only. iOS does **not** consume `sharedUI` today — it hosts its own SwiftUI and reuses only `sharedLogic`. To share Compose UI on iOS you must add iOS targets + a framework to `sharedUI/build.gradle.kts` and wire it into the Xcode project.

## Platform abstraction

`expect`/`actual` pattern in `sharedLogic`:
- `commonMain/Platform.kt` declares `interface Platform` + `expect fun getPlatform()`.
- `androidMain/Platform.android.kt` and `iosMain/Platform.ios.kt` provide the `actual` implementations.

`Greeting.greet()` (commonMain) is the shared entry point both apps call.

## Conventions & gotchas

- **Version catalog is the single source of truth** for versions and plugins: `gradle/libs.versions.toml`. Reference deps as `libs.<alias>` and modules as `projects.<module>`. Add/bump dependencies there, not inline in build files.
- Ktor client and SQLDelight are **wired but unused** — dependencies and platform drivers (`ktor-client-okhttp`/`darwin`, `sqldelight-android-driver`/`native-driver`) are declared and the SQLDelight plugin is applied to `sharedLogic`, but there are no `.sq` files and the Ktor call in `Greeting.kt` is commented out. This is scaffolding for future networking/persistence.
- Gradle **configuration cache and build cache are enabled** (`gradle.properties`). Build-logic changes may require `--no-configuration-cache` while iterating.
- Compose Multiplatform resources are generated under `myawesomerecipe.sharedui.generated.resources` (see imports in `sharedUI/App.kt`) — run a build to regenerate after changing resources.
- All modules target `JvmTarget.JVM_11`.
