# ADR-0004: Navigation 3 with a custom multi-stack

- **Status:** Accepted
- **Date:** 2026-06-11

## Context

The app uses bottom-tab navigation (Home, Settings) where each tab must keep its
own independent back stack and survive configuration changes and process death —
the standard "multiple back stacks" requirement. The project targets the modern
Navigation 3 (`androidx.navigation3`) API rather than the older
`NavHost`/`NavController`.

## Decision

Adopt **Navigation 3** and implement an explicit multi-stack on top of it.

- Destinations are `NavKey`s in a single sealed `Destination` hierarchy
  (serializable, type-safe arguments).
- A `NavigationStateHolder` owns one `NavBackStack<Destination>` per tab and
  implements an "exit through home" pattern: back navigation on a non-start tab
  switches to the start tab before exiting.
- A `Navigator` abstraction emits `NavigationAction`s through a coroutine
  `Channel` exposed as a `Flow`; `MainActivity` collects and applies them. This
  keeps ViewModels free of navigation framework types.

## Consequences

- Per-tab state and predictable back behavior, fully testable
  (`NavigationStateHolderTest`, `DefaultNavigatorTest`).
- ViewModels depend only on the `Navigator` abstraction, not on Compose
  navigation APIs.
- Navigation 3 is newer and parts of its ecosystem (adaptive integrations) are
  still pre-stable; we accept tracking API evolution in exchange for being on the
  current direction.

## Alternatives considered

- **Navigation Compose (2.x) with `NavController`**: more mature, but the
  multiple-back-stack handling is more implicit and the project explicitly aims
  to demonstrate the current-generation API.
- **A third-party navigation library**: rejected to stay close to first-party
  AndroidX and avoid extra abstractions.
