# ADR-0003: Arrow `Either` for error handling

- **Status:** Accepted
- **Date:** 2026-06-11

## Context

Operations that can fail (network, auth, persistence) need a consistent error
strategy across layers. Kotlin offers exceptions, `kotlin.Result`, or a
functional `Either`. Firebase throws a variety of platform exceptions that
should never leak into the domain or presentation layers.

## Decision

Model errors as **values** using Arrow's `Either<ErrorBO, T>`.

- `ErrorBO` is a sealed interface in `:core:model` enumerating the domain's
  failure modes (`Server`, `NotFound`, `InvalidCredentials`,
  `EmailAddressAlreadyInUse`, `Unknown`, ...).
- Firebase exceptions are caught at the edge (`:data:remote`) and mapped to
  `ErrorBO` via a single `Throwable.toErrorBO()` mapper.
- Higher layers compose results with `map`/`flatMap`/`fold` and never catch
  exceptions for control flow.

## Consequences

- Failure is part of the type signature: callers cannot forget to handle it, and
  the exhaustive `when` over `ErrorBO` is checked by the compiler.
- Error mapping is centralized and unit-tested, decoupling the domain from
  Firebase's exception taxonomy.
- A small learning curve for `Either`, and a dependency on Arrow — acceptable for
  the safety and clarity gained.

## Alternatives considered

- **Exceptions**: idiomatic in much of Android, but invisible in signatures and
  easy to let escape across architectural boundaries.
- **`kotlin.Result`**: closer to the language, but its left type is fixed to
  `Throwable`, so it cannot express a typed, exhaustive domain error like
  `ErrorBO` without wrapping.
