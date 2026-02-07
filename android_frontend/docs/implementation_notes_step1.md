# Step 1 — Project confirmation notes (android_frontend)

This document captures the key facts confirmed from the existing project files so the next steps (XML login UI + MVI ViewModel/state/intent) can be implemented without re-discovery.

## Confirmed namespaces / packages

- App module Gradle namespace:
  - File: `app/build.gradle.dcl`
  - Value: `namespace = "org.example.app"`

- Kotlin package structure (main sources):
  - Path: `app/src/main/kotlin/org/example/app/`
  - Example: `MainActivity.kt` uses `package org.example.app`

## Entrypoint activity

- Manifest:
  - File: `app/src/main/AndroidManifest.xml`
  - Launcher activity: `.MainActivity` with MAIN/LAUNCHER intent-filter

- Activity implementation:
  - File: `app/src/main/kotlin/org/example/app/MainActivity.kt`
  - Base class: `android.app.Activity` (not AppCompat)
  - Layout inflated: `setContentView(R.layout.activity_main)`

## Current layout

- File: `app/src/main/res/layout/activity_main.xml`
- Uses `RelativeLayout` with a centered `TextView` (`@+id/textView`)

This will be replaced/updated in later steps to implement an XML-based login screen.

## Gradle 9 Declarative DSL (.dcl) constraints (critical)

This project uses Gradle 9 + Declarative Configuration Language (DCL).

Allowed change pattern (per task constraints):
- ONLY add `implementation("group:artifact:version")` inside an EXISTING `dependencies {}` block in a `build.gradle.dcl`.

Forbidden (do NOT do these in `.dcl` files):
- Adding new blocks like `android {}`, `defaultConfig {}`, `testing {}`, etc.
- Adding `testImplementation` or other configurations not already present
- Attempting multiple broad edits to `.dcl` to “fix” syntax issues

Current app dependencies (baseline):
- File: `app/build.gradle.dcl`
- Contains:
  - `implementation("org.apache.commons:commons-text:1.11.0")`
  - `implementation(project(":utilities"))`

If we need AndroidX Lifecycle ViewModel, LiveData, etc., we will add them later as additional `implementation(...)` lines within this block.

## Testing baseline

- Testing dependencies are provided via `settings.gradle.dcl` defaults using JUnit 5:
  - `implementation("org.junit.jupiter:junit-jupiter:5.10.2")`
  - `runtimeOnly("org.junit.platform:junit-platform-launcher")`

- Existing unit test:
  - `app/src/test/kotlin/org/example/app/MessageUtilsTest.kt`

## Next-step preparation (planned, not yet implemented)

- Convert `activity_main.xml` into a login screen layout (XML, AndroidX widgets).
- Introduce an MVI-style structure:
  - `LoginIntent` (sealed interface/class)
  - `LoginViewState` (data class)
  - `LoginViewModel` (AndroidX lifecycle ViewModel)
- Wire `MainActivity` to the ViewModel and render state + send intents.

