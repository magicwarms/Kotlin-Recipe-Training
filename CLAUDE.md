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

- **`sharedLogic`** — KMP library holding business logic. Compiles to Android + iOS. Exposes a **static iOS framework named `SharedLogic`** (`iosArm64`, `iosSimulatorArm64`). Dependencies wired here: Ktor client, kotlinx.serialization JSON, coroutines, SQLDelight, AndroidX lifecycle-viewmodel. Internal package layout under `commonMain`:
  - `model/` — `MealModel` (domain), `MealDTO`/`MealResponse` (serializable API DTO with `toModel()`), `UiState` (Loading/Success/Error).
  - `repository/` — `MealRepository` interface + `MealRepositoryImpl` (`fetchMeals(mealName)` calls the [TheMealDB](https://themealdb.com) search API via Ktor and maps `MealResponse` → `MealModel`; `favorites()` is still `TODO()`), `HttpClient.kt` (`expect fun createHttpClient()`).
  - `presentation/` — `MealViewModel` (`androidx.lifecycle.ViewModel`, exposes `MutableStateFlow<UiState>`; `fetchMeals()` is still a **stub** returning empty success — not yet calling the repository).
  - `cache/` — SQLDelight: `Database` (internal wrapper, `getMeals()`), `DatabaseDriverFactory` interface, `RecipeDatabase.sq` schema.
  - `RecipeStorage.kt` — `expect class` for favourites persistence.
- **`sharedUI`** — Compose Multiplatform UI library. Depends on `sharedLogic` via `api(projects.sharedLogic)` (typesafe project accessor). **Currently Android-only** — its `kotlin {}` block declares only `androidLibrary`, no iOS target.
- **`androidApp`** — Android application. Depends on `sharedUI`; `MainActivity` calls the shared `App()` composable.
- **`iosApp/`** — native SwiftUI app. Links the `SharedLogic` framework **directly** and calls `Greeting().greet()` in `ContentView.swift`.

**Key consequence of the topology:** the shared Compose UI (`App()` in `sharedUI`) runs on Android only. iOS does **not** consume `sharedUI` today — it hosts its own SwiftUI and reuses only `sharedLogic`. To share Compose UI on iOS you must add iOS targets + a framework to `sharedUI/build.gradle.kts` and wire it into the Xcode project.

## Platform abstraction

`expect`/`actual` pattern in `sharedLogic` (four pairs):
- `commonMain/Platform.kt` declares `interface Platform` + `expect fun getPlatform()`; `androidMain/Platform.android.kt` + `iosMain/Platform.ios.kt` provide `actual`s.
- `commonMain/RecipeStorage.kt` declares `expect class RecipeStorage` (favourites CRUD); `androidMain/RecipeStorage.android.kt` (SharedPreferences) + `iosMain/RecipeStorage.ios.kt` (`NSUserDefaults`) provide `actual`s.
- `commonMain/repository/HttpClient.kt` declares `expect fun createHttpClient()`; `androidMain/repository/HttpClient.android.kt` (OkHttp engine) + `iosMain/repository/HttpClient.ios.kt` (Darwin engine) provide `actual`s, both installing `ContentNegotiation` + kotlinx JSON.
- `commonMain/cache/DatabaseDriverFactory.kt` is a plain `interface` (not `expect`); `androidMain/cache/AndroidDatabaseDriverFactory.kt` + `iosMain/cache/IOSDatabaseDriverFactory.kt` implement it.

`Greeting.greet()` (commonMain) is the shared entry point both apps call.

**Wiring is partial.** `MealRepositoryImpl.fetchMeals()` now calls `createHttpClient()` and fetches from TheMealDB, but the chain above and below it is not connected: `MealViewModel.fetchMeals()` is still a stub (never calls the repository), `MealRepository.favorites()` is `TODO()`, and `Database`/`RecipeStorage` are not injected anywhere. Known defects: `MealRepositoryImpl` hardcodes `?s=chicken` in `searchUrl` then appends `mealName` (double query fragment); `RecipeStorage.android.kt` instantiates `Application()` directly (no real `Context`) and its save/read keys mismatch (`"favourite"` vs `"favourites"`); `RecipeStorage.ios.kt` has `isFavourite` as `TODO()` and an empty `removeFavourite`.

## Conventions & gotchas

- **Version catalog is the single source of truth** for versions and plugins: `gradle/libs.versions.toml`. Reference deps as `libs.<alias>` and modules as `projects.<module>`. Add/bump dependencies there, not inline in build files.
- Ktor client and SQLDelight are **wired and partially built out, but not yet exercised at runtime**. SQLDelight now has a schema — `commonMain/sqldelight/.../cache/RecipeDatabase.sq` defines a `Meal` table with `insertMeal` / `removeAllMeals` / `selectAllMealsInfo` queries — and platform drivers (`sqldelight-android-driver`/`native-driver`) plus the plugin are applied to `sharedLogic`. Ktor has an implemented `createHttpClient()` factory (OkHttp/Darwin), now consumed by `MealRepositoryImpl` to hit TheMealDB. `ContentNegotiation` requires the separate `ktor-client-content-negotiation` artifact (aliased `libs.ktor.client.content.negotiation` in the catalog) — added alongside `ktor-serialization-json` (which supplies the `json()` DSL). The direct Ktor call in `Greeting.kt` remains commented out. Persistence (`cache/`, `RecipeStorage`) is still scaffolding — not exercised at runtime.
- Gradle **configuration cache and build cache are enabled** (`gradle.properties`). Build-logic changes may require `--no-configuration-cache` while iterating.
- Compose Multiplatform resources are generated under `myawesomerecipe.sharedui.generated.resources` (see imports in `sharedUI/App.kt`) — run a build to regenerate after changing resources.
- All modules target `JvmTarget.JVM_11`.
