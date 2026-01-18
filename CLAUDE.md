# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Ringtone Manager** is an Android application built with Jetpack Compose that allows users to browse, play, and manage ringtones. The app uses Firebase for backend services (Authentication, Firestore, Storage, Analytics) and follows Clean Architecture principles with a multi-module structure.

## Build and Development Commands

### Build Commands
```bash
# Build the project
./gradlew build

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean build artifacts
./gradlew clean
```

### Running the App
```bash
# Install debug build on connected device/emulator
./gradlew installDebug

# Uninstall the app
./gradlew uninstallDebug
```

### Testing
```bash
# Run all unit tests
./gradlew test

# Run unit tests for a specific module
./gradlew :app:test
./gradlew :domain:test

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Run tests for specific module
./gradlew :app:connectedAndroidTest
```

### Code Quality
```bash
# Run lint checks
./gradlew lint

# Generate lint report
./gradlew lintDebug
```

## Architecture Overview

### Multi-Module Structure

The project uses a modular architecture with clear separation of concerns:

```
:app                    → Presentation layer (Compose UI, ViewModels, Navigation)
:domain                 → Business logic (Use Cases, Repository interfaces, Domain models)
:data:repository        → Repository implementations
:data:remote            → Remote data sources (Firebase integration)
:bridgeDi               → Dependency Injection configuration (Koin modules)
:analytics              → Analytics implementation (Firebase Analytics)
```

**Module Dependencies:**
- `:app` depends on `:domain`, `:bridgeDi`
- `:bridgeDi` depends on all other modules (acts as composition root)
- `:data:repository` depends on `:domain`, `:data:remote`
- `:data:remote` depends on `:domain`
- `:analytics` depends on `:domain`

### Clean Architecture + MVVM

The project implements Clean Architecture with MVVM presentation pattern:

- **Presentation Layer** (`:app`): Composable screens, ViewModels (extending BaseViewModel), Navigation
- **Domain Layer** (`:domain`): Use Cases (business logic), Repository interfaces, Domain models (suffixed with `BO`)
- **Data Layer** (`:data:*`): Repository implementations, Remote data sources, DTOs, Firebase managers

### Key Technology Stack

- **UI**: Jetpack Compose (Material3)
- **Navigation**: Compose Navigation with Kotlin Serialization for type-safe routes
- **DI**: Koin 4.0.1 (service locator pattern)
- **Backend**: Firebase (Auth, Firestore, Storage, Analytics, App Check)
- **Async**: Kotlin Coroutines + Flow
- **Error Handling**: Arrow-kt's `Either` for functional error handling
- **Media**: ExoPlayer (Media3) for audio playback
- **Auth**: Credential Manager for email/password + Google Sign-In

## Important Patterns and Conventions

### Naming Conventions

- **Domain Models**: Suffix with `BO` (Business Object) - e.g., `RingtoneBO`, `UserBO`
- **DTOs**: Suffix with `DTO` - e.g., `RingtoneDTO`
- **Use Cases**: `[Action][Subject]UseCase` + `[Action][Subject]UseCaseImpl`
- **Repositories**: `[Subject]Repository` + `[Subject]RepositoryImpl`
- **ViewModels**: `[Screen]ViewModel` (all extend `BaseViewModel`)
- **Remote Data Sources**: `[Subject]RemoteDataSource` + implementation with Firebase/Firestore

### BaseViewModel Pattern

All ViewModels extend `BaseViewModel` (app/src/main/java/com/germandebustamante/ringtonemanager/ui/base/BaseViewModel.kt), which provides:
- Navigation helpers: `navigateTo()`, `navigateUp()`, `navigateAndClearBackStack()`
- Coroutine management: `launchCatching()` with automatic error handling
- Firebase exception mapping to `CustomError`
- ViewModel lifecycle management

### Navigation

Navigation uses type-safe routes defined in `Destination.kt`:

```kotlin
sealed interface Destination {
    @Serializable data object HomeScreen : Destination
    @Serializable data object SettingsScreen : Destination
    @Serializable data class RingtoneDetailScreen(val ringtoneId: String) : Destination
    @Serializable data object LoginScreen : Destination
    @Serializable data object RegisterScreen : Destination
    @Serializable data object ForgotPasswordScreen : Destination
}
```

Navigation is handled through a `Navigator` interface with `DefaultNavigator` implementation using Kotlin Channels. The `MainActivity` observes navigation actions and updates the `NavController`.

### Error Handling with Arrow

The project uses Arrow's `Either<L, R>` for type-safe error handling:

```kotlin
// Return type pattern
fun operation(): Flow<Either<CustomError, ResultType>>

// CustomError is a sealed interface with variants:
sealed interface CustomError {
    data class Server(val code: Int, val message: String) : CustomError
    data object ParcelizeException : CustomError
    data object NotFound : CustomError
    data object EmailAddressAlreadyInUse : CustomError
    data object InvalidCredentials : CustomError
    data class Unknown(val message: String) : CustomError
}
```

Errors are never thrown as exceptions; they're returned as values and folded/mapped through layers.

### Firebase Integration

**Firebase Managers** provide centralized Firebase operations:

- **FirebaseAuthManager**: Wraps Firebase Auth operations with `Either` error handling
  - `execute<T>()` - Returns `Either<CustomError, T>`
  - `executeVoid()` - Returns `Either<CustomError, Unit>`

- **FirestoreManager**: Generic Firestore operations
  - `getDocumentsFlow<T, R>()` - Reusable flow for any collection
  - `getDocument<T, R>()` - Single document fetch
  - `createDocument()` - Create/update documents

**Collections:**
- `ringtones_v1` - Ringtone data
- `users_v1` - User profiles (email, name, loginType)

### Authentication Flow

Authentication is handled through:
1. **Use Cases**: `SignUpUserUseCase`, `SignInUserUseCase`, `ForgotPasswordUseCase`, etc.
2. **Repository**: `AuthenticationRepository` (implemented by `AuthenticationRepositoryImpl`)
3. **Remote Data Source**: `FirebaseAuthenticationRemoteDataSourceImpl`

Supported methods:
- Email/password authentication
- Google Sign-In (via Credential Manager)
- Password reset via email
- User state as reactive `Flow<Either<CustomError, UserBO?>>`

After successful authentication, user data is saved to Firestore `users_v1` collection.

### State Management

ViewModels use `mutableStateOf()` for UI state:

```kotlin
// Pattern: State is an immutable data class
data class UIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<Item> = emptyList()
)

var state by mutableStateOf(UIState())
    private set

// Update state immutably
state = state.copy(isLoading = true)
```

Screens observe state reactively and recompose automatically.

### Dependency Injection with Koin

Koin modules are organized in `:bridgeDi` by layer:

- **appModule**: App-level singletons (Navigator, Firebase instances, ExoPlayer)
- **viewModelModule**: All ViewModels
- **domainModule**: Use case implementations
- **repositoryModule**: Repository implementations
- **remoteModule**: Remote data source implementations
- **analyticsModule**: Analytics repository

Injection patterns:
```kotlin
// ViewModel injection in Composables
val viewModel = koinViewModel<MyViewModel>()

// Constructor injection
class MyUseCase(private val repository: MyRepository)
```

### Resource Configuration

The app supports multiple languages:
- English (en) - default
- Spanish (es)

Resource configurations are defined in `app/build.gradle.kts`:
```kotlin
resourceConfigurations.addAll(listOf("en", "es"))
```

## Firebase Setup

The project requires a `google-services.json` file in the `app/` directory for Firebase integration. This file is not tracked in version control.

To set up Firebase:
1. Create a Firebase project at https://console.firebase.google.com
2. Add an Android app to your Firebase project
3. Download `google-services.json` and place it in the `app/` directory
4. Enable Firebase Authentication (Email/Password and Google Sign-In)
5. Create Firestore collections: `ringtones_v1`, `users_v1`
6. Enable Firebase Storage
7. Configure Firebase App Check for security

## Code Quality Guidelines

### When Adding Features

1. **Follow Clean Architecture layers**: Domain → Data → Presentation
2. **Create Use Cases** for business logic in `:domain` module
3. **Define Repository interfaces** in `:domain`, implement in `:data:repository`
4. **Use DTOs** in data layer, domain models (BO) everywhere else
5. **Extend BaseViewModel** for all new ViewModels
6. **Use `Either<CustomError, T>`** for error-prone operations
7. **Add Koin bindings** in appropriate modules in `:bridgeDi`

### When Modifying Existing Code

1. **Read BaseViewModel** to understand common ViewModel functionality
2. **Check existing Use Cases** for similar patterns
3. **Understand navigation flow** through `Destination.kt` and `Navigator`
4. **Respect module boundaries** - don't add presentation logic to domain/data layers
5. **Use existing Firebase managers** rather than direct Firebase SDK calls

### Common Gotchas

- **Don't use exceptions** for business logic errors - use `Either<CustomError, T>`
- **Don't bypass repositories** - always go through Use Cases
- **Don't hardcode strings** - use string resources for localization
- **Don't create new ViewModels without extending BaseViewModel** - you'll lose navigation and error handling
- **Don't forget Koin bindings** when adding new classes

## Module-Specific Notes

### :app Module
- Contains only presentation logic (Composables, ViewModels, Navigation)
- All ViewModels extend `BaseViewModel`
- Uses `koinViewModel()` for ViewModel injection
- Target: Android API 24+ (minSdk = 24, targetSdk = 34)

### :domain Module
- Pure Kotlin JVM module (Java 17)
- No Android dependencies
- Contains interfaces only (Use Cases, Repositories)
- Domain models are data classes suffixed with `BO`

### :data:remote Module
- Handles all Firebase integration
- Remote data sources implement interfaces from `:domain`
- DTOs map to domain models
- Uses `FirebaseAuthManager` and `FirestoreManager` for operations

### :data:repository Module
- Implements repository interfaces from `:domain`
- Delegates to remote data sources
- Applies `Dispatchers.IO` for background work
- Maps remote results to domain types

### :bridgeDi Module
- Composition root for dependency injection
- Contains all Koin module definitions
- Wire new dependencies here when adding features
