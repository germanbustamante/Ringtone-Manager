# ADR-0005: Firebase as the backend

- **Status:** Accepted
- **Date:** 2026-06-11

## Context

The app needs authentication, a content database for ringtones, file storage for
audio, and analytics, without standing up and operating a custom backend. This is
a portfolio project optimized for breadth of client-side architecture, not for
server engineering.

## Decision

Use **Firebase** as the managed backend:

- **Auth** (email/password + Google via Credential Manager)
- **Firestore** for ringtone and user data (`ringtones_v1`, `users_v1`)
- **Storage** for audio files
- **Analytics** for product events, **App Check** for attestation

All Firebase access is confined to `:data:remote` behind `FirebaseAuthManager`
and `FirestoreManager`, and exposed to the rest of the app only through
repository interfaces returning `Either<ErrorBO, T>`. No Firebase type crosses
into `:core` or `:app`.

## Consequences

- Zero backend ops; fast iteration on client architecture.
- The data layer is fully swappable: because everything depends on the
  `RemoteDataSource`/repository abstractions, replacing Firebase later touches
  only `:data:remote`.
- Firebase shapes some product constraints — notably no server-side full-text
  search and read-heavy pricing — which are acknowledged where relevant (e.g.
  search will use Firestore prefix queries).
- Security rules must be treated as versioned, reviewed code; App Check must be
  enforced in release builds.

## Alternatives considered

- **Custom backend (Ktor/Spring + Postgres)**: maximum control and richer
  queries, but a large operational cost that does not serve the goal of this
  project.
- **Supabase / AppWrite**: viable managed alternatives; Firebase was chosen for
  its mature Android SDKs and first-party Auth/Analytics integration.
