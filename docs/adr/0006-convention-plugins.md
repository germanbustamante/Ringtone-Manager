# ADR-0006: Gradle convention plugins for build logic

- **Status:** Accepted
- **Date:** 2026-06-11

## Context

A multi-module project tends to accumulate duplicated build configuration
(compile SDK, JVM target, Kotlin options, test dependencies) copy-pasted across
every `build.gradle.kts`. This drifts over time and is a maintenance hazard.

## Decision

Centralize build logic in a separate included build, `build-logic`, exposing
**convention plugins**:

- `android.library.convention.plugin` — Android library baseline
  (compileSdk 36, minSdk 24, JVM 17).
- `kotlin.library.convention.plugin` — pure-JVM Kotlin modules.
- `detekt.convention.plugin` — Detekt with the shared config.
- `unit.test.convention.plugin` — JUnit 5 + MockK + Turbine + coroutines-test +
  `:core:model` test fixtures, wired identically everywhere.

Module names are centralized in a type-safe `Modules` object instead of string
literals. This is the same approach used by Google's *Now in Android*.

## Consequences

- A module's `build.gradle.kts` is a few lines: apply the relevant convention
  plugins and declare dependencies.
- Cross-cutting changes (e.g. bumping the JVM target) happen in one place.
- A small upfront cost: contributors must learn where build logic lives, and
  `build-logic` is compiled as its own build.

## Alternatives considered

- **`subprojects {}` / `allprojects {}` in the root build**: discouraged by
  Gradle, breaks configuration isolation and project decoupling.
- **`buildSrc`**: works, but invalidates the whole build cache on any change and
  is less flexible than an included build for publishing plugins by id.
