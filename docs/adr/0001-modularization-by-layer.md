# ADR-0001: Modularization by layer

- **Status:** Accepted
- **Date:** 2026-06-11
  ┌────────────────────────────────┬───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
  │              Rama              │                                                     Qué contiene                                                      │
  ├────────────────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
  │ RM-1.0.0_a11y                  │ Content descriptions en todos los elementos interactivos                                                              │
  ├────────────────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
  │ RM-1.0.0_data_layer            │ DataStore preferences, Room para caché, repositorios offline-first, paginación Firestore                              │
  ├────────────────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
  │ RM-1.0.0_docs                  │ ADRs, diagramas de módulos y flujo de datos, README, PROGRESS.md                                                      │
  ├────────────────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
  │ RM-1.0.0_firebase_and_security │ Crashlytics + Performance Monitoring, reglas Firestore versionadas, App Check con Play Integrity                      │
  ├────────────────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
  │ RM-1.0.0_player_and_actions    │ Audio focus + becoming-noisy, polling por coroutines, compartir via share sheet, establecer como tono del dispositivo │
  ├────────────────────────────────┼───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
  │ RM-1.0.0_user_features         │ Favoritos con subcollección Firestore, selección de idioma in-app, cambio de contraseña con re-autenticación          │
  └────────────────────────────────┴───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

## Context

The app needs a module structure that enforces Clean Architecture boundaries at
compile time (presentation must not reach into Firebase, domain must stay
framework-free) while keeping build times reasonable. Two axes are common:
modularize **by layer** (`:core:domain`, `:data:remote`, ...) or **by feature**
(`:feature:home`, `:feature:auth`, ...).

At its current size the app has a handful of screens and a single backend.

## Decision

Modularize **by layer** for now:

- `:core:model` and `:core:domain` are pure-JVM modules with no Android
  dependencies, making domain logic fast to test and impossible to pollute with
  framework types.
- `:data:remote` and `:data:repository` isolate Firebase and the
  data-orchestration concerns.
- `:app` holds all presentation; `:bridgeDi` is the composition root.

The dependency graph is unidirectional toward the core and is intended to be
enforced by a module-graph assertion in CI.

## Consequences

- Boundaries are explicit and the domain is trivially unit-testable.
- A pure-JVM domain means no Robolectric/instrumentation for business logic.
- All presentation living in `:app` is the documented Google starting point, but
  it will not scale indefinitely — feature modularization is the planned next
  step (and is itself the subject of a future ADR once executed).

## Alternatives considered

- **Feature-first modularization** (the *Now in Android* end state): superior for
  large teams and parallel builds, but premature for the current screen count;
  it adds module boilerplate without a present payoff. It remains the roadmap
  direction.
- **Single module**: rejected — it cannot enforce the architectural boundaries
  that are a core goal of this project.
