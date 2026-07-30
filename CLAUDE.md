# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Ringtone Manager** is an Android application built with Jetpack Compose that allows users to browse, play, and manage ringtones. The app uses Firebase for backend services (Authentication, Firestore, Storage, Analytics) and follows Clean Architecture principles with a multi-module structure.

## Build and Development Commands

```bash
# Build / assemble
./gradlew build
./gradlew assembleDebug
./gradlew assembleRelease
./gradlew clean

# Install / uninstall on device
./gradlew installDebug
./gradlew uninstallDebug

# Unit tests (all modules)
./gradlew test

# Unit tests for a specific module
./gradlew :app:test
./gradlew :core:domain:test

# Run a single test class
./gradlew :app:test --tests "com.germandebustamante.ringtonemanager.ui.screen.home.HomeViewModelTest"

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Static analysis
./gradlew detekt
./gradlew lint
```

## Architecture Overview

### Multi-Module Structure

```
:app                    → Presentation layer (Compose UI, ViewModels, Navigation)
:core:model             → Domain models (BOs), ErrorBO, shared test fixtures (Mother objects)
:core:domain            → Business logic interfaces (Use Cases, Repository interfaces)
:data:repository        → Repository implementations
:data:remote            → Remote data sources (Firebase integration)
:bridgeDi               → Dependency Injection composition root (Koin)
:analytics              → Firebase Analytics implementation
build-logic/            → Convention plugins (separate includeBuild)
```

**Module Dependencies:**
- `:app` depends on `:core:domain`, `:core:model`, `:bridgeDi`
- `:bridgeDi` depends on all other modules (composition root)
- `:data:repository` depends on `:core:domain`, `:core:model`, `:data:remote`
- `:data:remote` depends on `:core:domain`, `:core:model`
- `:analytics` depends on `:core:domain`, `:core:model`
- `:core:domain` depends on `:core:model`
- `:core:model` has no internal dependencies (pure JVM, Java 11)

### Clean Architecture + MVVM

- **Presentation** (`:app`): Composables, ViewModels (all extend `BaseViewModel`), Navigation
- **Domain** (`:core:domain`): Use case interfaces + implementations, repository interfaces
- **Models** (`:core:model`): Immutable domain models (`*BO`), `ErrorBO`, test fixtures
- **Data** (`:data:*`): Repository and remote data source implementations, DTOs, Firebase managers

### Key Technology Stack

- **UI**: Jetpack Compose (Material3)
- **Navigation**: Navigation 3 (`androidx.navigation3`) with multi-stack tab support
- **DI**: Koin 4.1.1 (service locator)
- **Backend**: Firebase (Auth, Firestore, Storage, Analytics, App Check)
- **Async**: Kotlin Coroutines + Flow
- **Error Handling**: Arrow-kt `Either<ErrorBO, T>` — errors are values, never exceptions
- **Media**: ExoPlayer (Media3)
- **Auth**: Credential Manager (email/password + Google Sign-In)

## Important Patterns and Conventions

### Naming Conventions

- **Domain Models**: Suffix `BO` (Business Object) — `RingtoneBO`, `UserBO`
- **Error type**: `ErrorBO` (sealed interface in `:core:model`)
- **DTOs**: Suffix `DTO` — `RingtoneDTO`
- **Use Cases**: `[Action][Subject]UseCase` interface + `[Action][Subject]UseCaseImpl`
- **Repositories**: `[Subject]Repository` interface + `[Subject]RepositoryImpl`
- **ViewModels**: `[Screen]ViewModel` — all extend `BaseViewModel`
- **Remote Data Sources**: `[Subject]RemoteDataSource` interface + Firebase implementation

### BaseViewModel Pattern

All ViewModels extend `BaseViewModel` (`app/.../ui/base/BaseViewModel.kt`), which provides:
- `navigateTo(destination)`, `navigateUp()`, `navigateAndClearBackStack()`
- `launchCatching(onError, block)` — coroutine launcher with automatic Firebase exception → `ErrorBO` mapping

### ViewModel Initial State and Startup Requests

- Every ViewModel takes `initialState: UiState = UiState()` as its first constructor parameter and seeds `MutableStateFlow(initialState)` with it, instead of hardcoding `MutableStateFlow(UiState())` inline. This lets tests seed a specific state directly through the constructor rather than reconstructing it via setters/events.
- Because of this, ViewModels can no longer be registered in Koin (`app/.../di/Di.kt`) with `viewModelOf(::X)` — it resolves every constructor parameter via `get()` and does not honor Kotlin default values. Use an explicit `viewModel { X(dep = get(), ...) }` lambda instead (omit `initialState` to keep the default).
- Data-loading use case calls that used to run in `init {}` go in an explicit `fun start()` instead, called once from the screen via `LaunchedEffect(Unit) { viewModel.start() }`. This keeps ViewModel construction side-effect-free and avoids non-deterministic concurrent coroutine launches from `init` in tests — tests call `sut.start()` explicitly after building the ViewModel.
- This project does not implement `SavedStateHandle`-based process-death restoration or a startup-intent queue (`isStateRestored`) — that's out of scope until the app actually needs to preserve in-flight UI state across process death. Don't build that infrastructure speculatively.

### Startup Task Pattern (not adopted)

The app does not use a `StartupTask`/multibinding pattern for app initialization, and shouldn't until there's evidence of need. `App.onCreate()` only installs App Check and calls `startKoin(...)`; there is no `MainActivityViewModel`. Revisit this only if `App.onCreate()` accumulates more than a handful of ad hoc initialization calls, or a future `MainActivityViewModel` grows beyond ~5-6 dependencies used purely for startup/preload work.

### Navigation (Navigation 3 + Multi-Stack)

Destinations implement `NavKey` (Navigation 3):

```kotlin
sealed interface Destination : NavKey {
    data object HomeScreen : Destination
    data object SettingsScreen : Destination
    data class RingtoneDetailScreen(val ringtoneId: String) : Destination
    data object LoginScreen : Destination
    data object RegisterScreen : Destination
    data object ForgotPasswordScreen : Destination
}
```

**Tab navigation** uses `NavigationStateHolder` which manages a separate `NavBackStack<Destination>` per tab, with an "exit through home" pattern — back navigation on a non-start tab switches to the start tab first. Create with `rememberNavigationState(startTab)`.

The `Navigator` interface emits `NavigationAction` values via a Kotlin `Channel` as a `Flow`. `MainActivity` collects these and delegates to the nav state holder.

### Error Handling with Arrow

```kotlin
// All error-prone operations return Either
fun operation(): Flow<Either<ErrorBO, ResultType>>

sealed interface ErrorBO {
    data class Server(val code: Int, val message: String) : ErrorBO
    data object ParcelizeException : ErrorBO
    data object NotFound : ErrorBO
    data object EmailAddressAlreadyInUse : ErrorBO
    data object InvalidCredentials : ErrorBO
    data class Unknown(val message: String) : ErrorBO
}
```

Errors are never thrown as exceptions; they're returned as values and folded/mapped through layers.

### Firebase Integration

**Firebase Managers** in `:data:remote`:
- **`FirebaseAuthManager`**: `execute<T>()` → `Either<ErrorBO, T>`, `executeVoid()` → `Either<ErrorBO, Unit>`
- **`FirestoreManager`**: `getDocumentsFlow<T, R>()`, `getDocument<T, R>()`, `createDocument()`

Firestore collections: `ringtones_v1`, `users_v1`

### Authentication Flow

1. Use Cases: `SignUpUserUseCase`, `SignInUserUseCase`, `ForgotPasswordUseCase`, `GetUserFlowUseCase`, etc.
2. Repository: `AuthenticationRepository` → `AuthenticationRepositoryImpl`
3. Remote: `FirebaseAuthenticationRemoteDataSourceImpl`

Supported methods: email/password, Google Sign-In (Credential Manager), password reset. After auth, user data is persisted to `users_v1`.

### State Management

ViewModels expose state via `StateFlow`. Screens collect it with `collectAsStateWithLifecycle()`:

```kotlin
data class UIState(val isLoading: Boolean = false, val error: String? = null, ...)
private val _state = MutableStateFlow(UIState())
val state: StateFlow<UIState> = _state
// Update: _state.update { it.copy(isLoading = true) }
```

**One-time events** (navigation, notifications) use `Channel<EventType>(Channel.BUFFERED).receiveAsFlow()` consumed in the screen via the `ObserveAsEvent` composable utility.

### Dependency Injection with Koin

Koin modules in `:bridgeDi`: `appModule`, `viewModelModule`, `domainModule`, `repositoryModule`, `remoteModule`, `analyticsModule`.

```kotlin
val viewModel = koinViewModel<MyViewModel>()       // in Composables
class MyUseCase(private val repository: MyRepository)  // constructor injection
```

## Testing

### Framework Stack

All wired automatically by `UnitTestConventionPlugin` in `build-logic`:
- **JUnit 5** (Jupiter) with `useJUnitPlatform()`
- **MockK** for mocking (`@MockK`, `@ExtendWith(MockKExtension::class)`)
- **Turbine** for Flow testing (`.test { awaitItem() }`)
- **Kotlin Coroutines Test** (`runTest`, `UnconfinedTestDispatcher`, `Dispatchers.setMain()`)

### Test Fixtures (Mother Objects)

`:core:model` publishes shared test data via the `java-test-fixtures` plugin:

```kotlin
// core/model/src/testFixtures/
RingtoneBOMother.default()
RingtoneBOMother.random(id)
RingtoneBOMother.randomList(size)
ErrorBOMother.unknown()
UserBOMother.default()
TestDispatcherProvider()   // UnconfinedTestDispatcher for all dispatchers
```

Modules consume these with `testImplementationFixtures(Modules.CORE_MODEL)`.

### DI Graph Verification

`app/src/test/.../di/KoinModulesCheckTest` uses `koin-test`'s `verify()` API to statically verify that all bridgeDi modules (`analyticsModule`, `remoteModule`, `repositoryModule`, `domainModule`) can have their dependencies satisfied. Firebase types provided by `appModule` are declared as `extraTypes`.

### Test Naming and Organization

Use JUnit 5 `@Nested` inner classes to group tests, and backtick names with Given-When-Then:

```kotlin
@Nested inner class WhenInvoked {
    @Test
    fun `GIVEN popular ringtones success WHEN invoked THEN returns success with ringtones list`() = runTest { ... }
}
```

## Build-Logic Convention Plugins

Located in `build-logic/convention/src/main/java/plugins/`:

- **`AndroidLibraryConventionPlugin`** (`id: "android.library.convention.plugin"`) — applies `com.android.library` + `kotlin-android`, sets compileSdk=36, minSdk=24, JVM 17. Used by all Android library modules.
- **`KotlinLibraryConventionPlugin`** (`id: "kotlin.library.convention.plugin"`) — applies `java-library` + `kotlin-jvm`, sets JVM 17. Used by pure JVM modules (`:core:model`, `:core:domain`).
- **`DetektConventionPlugin`** — applies Detekt to all modules; config at `.config/detekt.yml`
- **`UnitTestConventionPlugin`** — standardizes test dependencies (JUnit 5, MockK, Turbine, coroutines-test, testFixtures from `:core:model`) across modules

Module names are centralized in `build-logic/convention/src/main/java/Modules.kt` — use `Modules.CORE_MODEL`, `Modules.CORE_DOMAIN`, etc. instead of string literals.

## Firebase Setup

Requires `google-services.json` in `app/` (not tracked in VCS). Enable: Authentication (Email/Password + Google), Firestore (`ringtones_v1`, `users_v1`), Storage, App Check.

## Code Quality Guidelines

### When Adding Features

1. Domain → Data → Presentation order
2. New models (BOs) go in `:core:model`; use case interfaces and repository interfaces in `:core:domain`
3. DTOs only in `:data:remote`; use `ErrorBO` everywhere else (not exceptions)
4. Extend `BaseViewModel` for all ViewModels
5. Register Koin bindings in `:bridgeDi`
6. Add test fixtures to `:core:model` `testFixtures` source set when adding new BOs

### Common Gotchas

- **Don't use exceptions** for business logic — use `Either<ErrorBO, T>`
- **Don't bypass repositories** — always go through Use Cases
- **Don't hardcode strings** — use string resources (supports `en`, `es`)
- **Don't use direct Firebase SDK calls** — go through `FirebaseAuthManager` / `FirestoreManager`
- **`:domain` no longer exists** — domain logic is split between `:core:model` and `:core:domain`
