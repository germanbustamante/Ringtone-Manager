# ADR-0002: Koin over Hilt for dependency injection

- **Status:** Accepted
- **Date:** 2026-06-11

## Context

The project needs dependency injection across modules. Google officially
recommends Hilt (Dagger), which *Now in Android* uses. The author also maintains
a Kotlin Multiplatform project that shares patterns with this one.

## Decision

Use **Koin** as the DI container.

- Constructor injection everywhere; Koin modules per layer live in `:bridgeDi`
  (`appModule`, `viewModelModule`, `domainModule`, `repositoryModule`,
  `remoteModule`, `analyticsModule`).
- The risk of a service-locator DI (errors surface at runtime, not compile time)
  is mitigated with a `koin-test` `verify()` test that statically checks the
  whole graph can be satisfied — run on every CI build.

## Consequences

- No annotation processing / KSP for DI → faster, simpler builds.
- Consistent with the author's KMP project, where Hilt is not an option (Hilt is
  Android-only); the same mental model transfers.
- We give up Hilt's compile-time graph validation, which is why the
  `KoinModulesCheckTest` is treated as a required safety net, not optional.

## Alternatives considered

- **Hilt/Dagger**: compile-time safety and the official recommendation, but
  Android-only, heavier build setup, and divergent from the author's KMP stack.
  The `verify()` test recovers most of the safety benefit for this project's
  size.
- **Manual DI**: rejected — the multi-module composition root would become
  verbose and error-prone.
