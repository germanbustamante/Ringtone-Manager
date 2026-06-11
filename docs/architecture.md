# Architecture

Ringtone Manager follows Clean Architecture with an MVVM presentation layer.
Dependencies point in a single direction toward a framework-free core, so the
domain can be reasoned about and tested without Android or Firebase.

For the *why* behind each choice, see the [ADRs](adr/).

## Module dependency graph

```mermaid
graph TD
    subgraph Presentation
        app[":app"]
    end
    subgraph Composition
        bridgeDi[":bridgeDi"]
    end
    subgraph Data
        dataRepository[":data:repository"]
        dataRemote[":data:remote"]
        analytics[":analytics"]
    end
    subgraph Domain
        coreDomain[":core:domain"]
        coreModel[":core:model"]
    end

    app --> bridgeDi
    app --> coreDomain
    app --> coreModel
    bridgeDi --> dataRepository
    bridgeDi --> dataRemote
    bridgeDi --> analytics
    bridgeDi --> coreDomain
    bridgeDi --> coreModel
    dataRepository --> dataRemote
    dataRepository --> coreDomain
    dataRepository --> coreModel
    dataRemote --> coreDomain
    dataRemote --> coreModel
    analytics --> coreDomain
    analytics --> coreModel
    coreDomain --> coreModel
```

Rules the graph enforces:

- `:core:model` has **no** internal dependencies (pure JVM, Java 11).
- `:core:domain` depends only on `:core:model` and exposes interfaces.
- Presentation (`:app`) never depends on `:data:*` directly — only on domain
  abstractions and the `:bridgeDi` composition root.
- `:bridgeDi` is the single place that sees every concrete implementation and
  binds interfaces to them.

## Layers

| Layer | Modules | Responsibility |
|-------|---------|----------------|
| Presentation | `:app` | Compose UI, ViewModels (extend `BaseViewModel`), navigation host |
| Composition | `:bridgeDi` | Koin modules binding abstractions to implementations |
| Domain | `:core:domain`, `:core:model` | Use cases, repository interfaces, business models (`*BO`), `ErrorBO` |
| Data | `:data:repository`, `:data:remote`, `:analytics` | Repository impls, Firebase data sources, DTOs, analytics |

## Data flow (Unidirectional Data Flow)

State flows down, events flow up. A read of the ringtone detail looks like this:

```mermaid
sequenceDiagram
    participant UI as RingtoneDetailScreen
    participant VM as RingtoneDetailViewModel
    participant UC as GetRingtoneDetailUseCase
    participant Repo as RingtoneItemRepository
    participant DS as Firestore DataSource
    participant FB as Firebase

    UI->>VM: onScreenEntered
    VM->>UC: invoke(ringtoneId)
    UC->>Repo: getRingtoneDetail(id)
    Repo->>DS: getRingtoneDetail(id)
    DS->>FB: collection.document(id).get()
    FB-->>DS: DocumentSnapshot
    DS-->>Repo: Either<ErrorBO, RingtoneBO>
    Repo-->>UC: Either<ErrorBO, RingtoneBO>
    UC-->>VM: Either<ErrorBO, RingtoneBO>
    VM->>VM: _uiState.update { ... }
    VM-->>UI: StateFlow<UIState>
```

- ViewModels expose immutable `UIState` via `StateFlow`, collected with
  `collectAsStateWithLifecycle()`.
- One-time effects (navigation, notifications) are emitted through a
  `Channel(...).receiveAsFlow()` and consumed with the `ObserveAsEvent`
  composable.
- Every fallible call returns `Either<ErrorBO, T>`; the ViewModel folds it into
  either a content state or an error state — no exceptions cross layers.

## Navigation (Navigation 3 + multi-stack)

Each bottom tab owns an independent back stack. Back navigation on a non-start
tab routes "through home" before exiting the app.

```mermaid
graph LR
    subgraph "Home tab stack"
        H1[HomeScreen] --> H2[RingtoneDetailScreen]
    end
    subgraph "Settings tab stack"
        S1[SettingsScreen] --> S2[LoginScreen] --> S3[RegisterScreen]
    end

    Nav["NavigationStateHolder<br/>(one NavBackStack per tab)"]
    Nav --- H1
    Nav --- S1
```

- `Navigator` emits `NavigationAction`s through a coroutine `Channel` exposed as
  a `Flow`; `MainActivity` collects them and delegates to the
  `NavigationStateHolder`.
- ViewModels depend only on the `Navigator` abstraction, never on Compose
  navigation types — keeping them unit-testable.

## Error model

```
ErrorBO  (sealed interface, :core:model)
├── Server(code, message)
├── NotFound
├── EmailAddressAlreadyInUse
├── InvalidCredentials
├── ParcelizeException
└── Unknown(message)
```

Firebase exceptions are mapped to `ErrorBO` once, at the data-layer edge, via
`Throwable.toErrorBO()`. Everything above the edge speaks only `ErrorBO`.
