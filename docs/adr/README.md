# Architecture Decision Records

This directory captures the significant architectural decisions made on this
project, using lightweight [ADRs](https://adr.github.io/). Each record states
the context that forced a decision, the decision itself, the consequences, and
the alternatives that were considered and rejected.

ADRs are immutable once accepted: if a decision changes, a new ADR supersedes
the old one rather than editing history.

| # | Title | Status |
|---|-------|--------|
| [0001](0001-modularization-by-layer.md) | Modularization by layer | Accepted |
| [0002](0002-koin-over-hilt.md) | Koin over Hilt for dependency injection | Accepted |
| [0003](0003-either-for-error-handling.md) | Arrow `Either` for error handling | Accepted |
| [0004](0004-navigation3-multistack.md) | Navigation 3 with a custom multi-stack | Accepted |
| [0005](0005-firebase-as-backend.md) | Firebase as the backend | Accepted |
| [0006](0006-convention-plugins.md) | Gradle convention plugins for build logic | Accepted |

See [`template.md`](template.md) for the format.
