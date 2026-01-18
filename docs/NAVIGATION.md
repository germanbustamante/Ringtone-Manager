# Multi-Stack Navigation System with Navigation 3

This document explains step-by-step how the project's navigation system works, which combines Navigation 3 with a testable State Holder pattern.

## Table of Contents

1. [Fundamental Concepts](#fundamental-concepts)
2. [System Architecture](#system-architecture)
3. [Complete Navigation Flow](#complete-navigation-flow)
4. [Back Navigation](#back-navigation)
5. [Multiple BackStacks (Tabs)](#multiple-backstacks-tabs)
6. [State Preservation](#state-preservation)
7. [System Advantages](#system-advantages)
8. [Usage Guide](#usage-guide)

---

## Fundamental Concepts

### 1. NavKey

\`\`\`kotlin
// Destination.kt
sealed interface Destination: NavKey {
    @Serializable
    data object HomeScreen : Destination

    @Serializable
    data class RingtoneDetailScreen(val ringtoneId: String) : Destination
}
\`\`\`

**NavKey** is a Navigation 3 interface that represents a "place" in your app:
- It's **serializable** → can be saved to persistent storage
- Defines a **unique route** → each screen has its identifier
- Can carry **parameters** → like \`ringtoneId\` in \`RingtoneDetailScreen\`

**Analogy:** It's like a postal address. \`RingtoneDetailScreen("123")\` is like saying "RingtoneDetail Street, number 123".

### 2. NavBackStack

\`\`\`kotlin
val homeBackStack = rememberNavBackStack(Destination.HomeScreen)
// Internally: [HomeScreen]

homeBackStack.add(Destination.RingtoneDetailScreen("123"))
// Now: [HomeScreen, RingtoneDetailScreen("123")]

homeBackStack.removeLastOrNull()
// Back to: [HomeScreen]
\`\`\`

**NavBackStack** is an observable list of \`NavKey\`:
- Maintains **navigation history** (like a stack)
- Is **observable** → when it changes, Composables recompose
- **Preserves state** → survives rotations and process death
- Works like a \`SnapshotStateList\` in Compose

**Analogy:** It's like your web browser's history. You can go forward (add) or backward (removeLastOrNull).

### 3. NavEntry

\`\`\`kotlin
data class NavEntry<T : NavKey>(
    val key: T,                              // The route (e.g., RingtoneDetailScreen("123"))
    val content: @Composable () -> Unit      // The composable to display
)
\`\`\`

**NavEntry** connects a **route** with its **visual content**:
- \`key\` → Which screen it is (the \`NavKey\`)
- \`content\` → How to display it (a \`@Composable\`)

**Creating with entryProvider:**
\`\`\`kotlin
val entryProvider = entryProvider {
    entry<Destination.HomeScreen> {
        HomeScreen()  // ← This is the content
    }
    entry<Destination.RingtoneDetailScreen> { ringtoneDetail →
        RingtoneDetailScreen(ringtoneDetail)  // Receives the parameter
    }
}

// When Navigation 3 needs to display Destination.HomeScreen:
val entry = entryProvider(Destination.HomeScreen)
// → NavEntry(key = HomeScreen, content = { HomeScreen() })
\`\`\`

### 4. Decorators

**Decorators** wrap a \`NavEntry\` to add functionality:

\`\`\`kotlin
val saveableDecorator = rememberSaveableStateHolderNavEntryDecorator()
val viewModelDecorator = rememberViewModelStoreNavEntryDecorator()

// Original NavEntry
val original = NavEntry(HomeScreen) { HomeScreen() }

// After decorating with SaveableStateHolder:
val decorated1 = saveableDecorator.decorate(original)
// Now preserves state of TextFields, scroll position, etc.

// After decorating with ViewModelStore:
val decorated2 = viewModelDecorator.decorate(decorated1)
// Now has ViewModel scope, ViewModels survive recompositions
\`\`\`

**SaveableStateHolderNavEntryDecorator:**
- Saves Composable state (TextFields, scroll, etc.)
- Uses \`rememberSaveable\` internally
- Key for surviving process death

**ViewModelStoreNavEntryDecorator:**
- Creates a ViewModel scope for that entry
- ViewModels survive recompositions
- Cleaned up when the entry leaves the backstack

### 5. NavDisplay

\`\`\`kotlin
NavDisplay(
    entries = listOfDecoratedEntries,
    onBack = { /* handle back */ }
)
\`\`\`

**NavDisplay** is the component that **DISPLAYS** the screens:
- Receives a list of decorated \`NavEntry\`
- Displays the **last entry** in the list (the current screen)
- Handles **animated transitions** between screens
- Calls \`onBack\` when the user presses the system back button

---

## System Architecture

### Component Tree

\`\`\`
MainActivity
    ├── NavigationStateHolder (the brain)
    │   ├── backStacks: Map<TabDestination, NavBackStack<Destination>>
    │   │   ├── Home → [HomeScreen, RingtoneDetail("123")]
    │   │   └── Settings → [SettingsScreen]
    │   ├── currentTab: TabDestination.Home
    │   └── startTab: TabDestination.Home
    │
    ├── Navigator (interface injected into ViewModels)
    │   └── Channel<NavigationAction> (action queue)
    │
    ├── Scaffold
    │   ├── TabBar (bottom navigation)
    │   └── NavigationHost
    │       └── NavDisplay (displays current screen)
\`\`\`

### Data Flow

\`\`\`
ViewModel → Navigator (interface)
              ↓
    Channel<NavigationAction>
              ↓
    MainActivity (observes the channel)
              ↓
NavigationStateHolder (modifies state)
              ↓
    toDecoratedEntries()
              ↓
      NavDisplay (displays UI)
\`\`\`

### Key Components

#### NavigationStateHolder

**Location:** \`app/src/main/java/com/germandebustamante/ringtonemanager/core/navigation/action/NavigationStateHolder.kt\`

\`\`\`kotlin
class NavigationStateHolder(
    val startTab: TabDestination,
    val backStacks: Map<TabDestination, NavBackStack<Destination>>
)
\`\`\`

**Responsibilities:**
- Maintains a \`NavBackStack\` per tab (Home, Settings)
- Distinguishes between tab navigation vs. in-tab navigation
- Implements "exit through home" pattern: always returns to initial tab before exiting
- Converts backstacks to decorated entries with \`SaveableStateHolder\` per tab

**Public methods:**
- \`navigate(destination)\`: Navigates to a destination (tab or screen)
- \`navigateBack()\`: Handles back with tab logic
- \`onTabSelected(tab)\`: Switches tabs
- \`toDecoratedEntries(entryProvider)\`: Converts backstacks to NavEntries for display

#### Navigator Interface

**Location:** \`app/src/main/java/com/germandebustamante/ringtonemanager/core/navigation/action/Navigator.kt\`

\`\`\`kotlin
interface Navigator {
    val navigationActions: Flow<NavigationAction>
    suspend fun navigate(destination: Destination)
    suspend fun navigateUp()
}
\`\`\`

**Implementation:**
\`\`\`kotlin
class DefaultNavigator : Navigator {
    private val _navigationActions = Channel<NavigationAction>()
    override val navigationActions = _navigationActions.receiveAsFlow()

    override suspend fun navigate(destination: Destination) {
        _navigationActions.send(NavigationAction.Navigate(destination))
    }

    override suspend fun navigateUp() {
        _navigationActions.send(NavigationAction.Back)
    }
}
\`\`\`

**Why this design:**
- ✅ **Testable**: You can mock \`Navigator\` in ViewModel tests
- ✅ **No callbacks**: ViewModels navigate directly without passing callbacks
- ✅ **Decoupled**: ViewModels don't know about navigation state

---

## Complete Navigation Flow

Example: **User presses button in HomeScreen to view ringtone detail "123"**

### Step 1: User presses the button

\`\`\`kotlin
// HomeScreen.kt
@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()

    Button(onClick = {
        viewModel.onRingtoneClick("123")  // ← User clicks
    }) {
        Text("View Ringtone 123")
    }
}
\`\`\`

### Step 2: ViewModel processes the event

\`\`\`kotlin
// HomeViewModel.kt
class HomeViewModel(
    private val navigator: Navigator  // ← Injected by Koin
) : BaseViewModel(navigator) {

    fun onRingtoneClick(ringtoneId: String) {
        launchCatching {  // ← BaseViewModel helper
            navigator.navigate(
                Destination.RingtoneDetailScreen(ringtoneId)
            )
        }
    }
}
\`\`\`

**What happens here?**
- \`navigator\` is the **interface** \`Navigator\`
- The actual implementation is \`DefaultNavigator\` (singleton in Koin)
- \`navigate()\` is a \`suspend fun\` → uses coroutines

### Step 3: DefaultNavigator sends action via Channel

\`\`\`kotlin
// Navigator.kt
class DefaultNavigator : Navigator {
    private val _navigationActions = Channel<NavigationAction>()

    override suspend fun navigate(destination: Destination) {
        _navigationActions.send(  // ← Sends via channel
            NavigationAction.Navigate(destination = destination)
        )
    }
}
\`\`\`

**What is a Channel?**
- It's like a **message queue** between coroutines
- \`send()\` → Puts a message in the queue
- \`receiveAsFlow()\` → Converts the queue to an observable \`Flow\`
- It's **thread-safe** → multiple ViewModels can navigate concurrently

### Step 4: MainActivity observes the Channel

\`\`\`kotlin
// MainActivity.kt
ObserveAsEvent(flow = navigator.navigationActions) { action ->
    when (action) {
        is NavigationAction.Navigate -> navigationState.navigate(action.destination)
        is NavigationAction.Back -> navigationState.navigateBack()
    }
}
\`\`\`

**ObserveAsEvent:**
\`\`\`kotlin
@Composable
fun <T> ObserveAsEvent(flow: Flow<T>, onEvent: (T) -> Unit) {
    LaunchedEffect(flow) {
        flow.collect { event ->
            onEvent(event)  // ← Calls lambda for each event
        }
    }
}
\`\`\`

It's a helper that:
- Listens to a \`Flow\` in a \`LaunchedEffect\`
- For each emitted value, calls the \`onEvent\` lambda
- Automatically cancels when the Composable leaves the screen

### Step 5: NavigationStateHolder processes navigation

\`\`\`kotlin
// NavigationStateHolder.kt
fun navigate(destination: Destination) {
    // Is it a tab? (HomeScreen or SettingsScreen)
    val targetTab = findTabForDestination(destination)

    if (targetTab != null) {
        // It's tab navigation → switch tabs
        currentTab = targetTab
    } else {
        // It's in-tab navigation → add to backstack
        val currentStack = backStacks[currentTab]
        currentStack?.add(destination)  // ← HERE is the magic
    }
}

private fun findTabForDestination(destination: Destination): TabDestination? {
    return backStacks.keys.firstOrNull { tab ->
        tab.defaultRoute == destination
    }
}
\`\`\`

**What happens in this case?**

\`\`\`kotlin
destination = RingtoneDetailScreen("123")

// 1. findTabForDestination searches:
//    - Is RingtoneDetailScreen("123") == TabDestination.Home.defaultRoute? → NO
//    - Is RingtoneDetailScreen("123") == TabDestination.Settings.defaultRoute? → NO
//    targetTab = null

// 2. Since targetTab == null, it's in-tab navigation:
currentTab = Home
currentStack = backStacks[Home]
// Before: [HomeScreen]

currentStack.add(RingtoneDetailScreen("123"))
// After: [HomeScreen, RingtoneDetailScreen("123")]
\`\`\`

**NavigationStateHolder state:**
\`\`\`kotlin
backStacks = {
    Home → [HomeScreen, RingtoneDetailScreen("123")],  // ← CHANGED
    Settings → [SettingsScreen]
}
currentTab = Home
\`\`\`

### Step 6: MainActivity recomposition

Since \`backStacks[Home]\` changed (it's a \`SnapshotStateList\`), Compose detects the change and recomposes:

\`\`\`kotlin
// MainActivity.kt
val decoratedEntries = navigationState.toDecoratedEntries(entryProvider)
//                     ↑ THIS RECOMPOSES
\`\`\`

### Step 7: toDecoratedEntries converts backstacks to entries

\`\`\`kotlin
@Composable
fun toDecoratedEntries(
    entryProvider: (Destination) -> NavEntry<Destination>
): SnapshotStateList<NavEntry<Destination>> {

    // 1. For each tab, decorate its entries
    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider
        )
    }

    // 2. Return only entries from active tabs
    return getActiveTabRoutes()  // [Home]
        .flatMap { tab -> decoratedEntries[tab] ?: emptyList() }
        .toMutableStateList()
}
\`\`\`

**Detailed process:**

**7.1 - For the Home tab:**
\`\`\`kotlin
stack = [HomeScreen, RingtoneDetailScreen("123")]

// For each stack element, create NavEntry:
entryProvider(HomeScreen)
// → NavEntry(HomeScreen, content = { HomeScreen() })

entryProvider(RingtoneDetailScreen("123"))
// → NavEntry(RingtoneDetailScreen("123"), content = { RingtoneDetailScreen(...) })
\`\`\`

**7.2 - Apply decorators:**
\`\`\`kotlin
val saveableDecorator = rememberSaveableStateHolderNavEntryDecorator()
val viewModelDecorator = rememberViewModelStoreNavEntryDecorator()

// For each entry:
entry1Decorated = viewModelDecorator.decorate(
    saveableDecorator.decorate(entry1)
)
\`\`\`

**What does each decorator do?**

\`SaveableStateHolder\`:
- Creates a state "bucket" per entry
- Saves scroll position, TextField text, etc.
- When returning to that entry, restores the state

\`ViewModelStore\`:
- Creates a ViewModel scope per entry
- \`koinViewModel<RingtoneDetailViewModel>()\` always gets the same instance
- Cleaned up when the entry leaves the backstack

**7.3 - Final result:**
\`\`\`kotlin
[
  DecoratedNavEntry(HomeScreen, { HomeScreen() }),
  DecoratedNavEntry(RingtoneDetailScreen("123"), { RingtoneDetailScreen(...) })
]
\`\`\`

### Step 8: NavDisplay displays the screen

\`\`\`kotlin
// Inside NavDisplay (simplified):
@Composable
fun NavDisplay(entries: List<NavEntry<Destination>>, onBack: () -> Unit) {
    val currentEntry = entries.lastOrNull()  // ← The last entry

    currentEntry?.content?.invoke()  // ← Executes the @Composable

    BackHandler(enabled = true) {
        onBack()
    }
}
\`\`\`

**In this case:**
\`\`\`kotlin
entries.lastOrNull() = DecoratedNavEntry(RingtoneDetailScreen("123"), ...)

// Executes:
RingtoneDetailScreen(RingtoneDetailScreen("123"))
\`\`\`

**Result:** \`RingtoneDetailScreen\` is displayed with ringtone "123" 🎉

---

## Back Navigation

### What happens when you press BACK?

#### Step 1: Android system triggers back

\`\`\`kotlin
// NavDisplay detects system back
BackHandler(enabled = true) {
    onBack()  // ← Calls lambda
}
\`\`\`

#### Step 2: navigationState.navigateBack() is called

\`\`\`kotlin
// MainActivity.kt
NavigationHost(
    entries = decoratedEntries,
    onBack = { navigationState.navigateBack() }
)
\`\`\`

#### Step 3: NavigationStateHolder processes back

\`\`\`kotlin
fun navigateBack(): Boolean {
    val currentStack = backStacks[currentTab]
    val currentDestination = currentStack?.lastOrNull()

    // Case 1: At root of secondary tab → return to startTab
    if (currentDestination == currentTab.defaultRoute && currentTab != startTab) {
        currentTab = startTab
        return true
    }

    // Case 2: At root of startTab → exit app
    if (currentDestination == currentTab.defaultRoute && currentTab == startTab) {
        return false
    }

    // Case 3: More screens in stack → pop
    currentStack?.removeLastOrNull()
    return true
}
\`\`\`

**Example with our case:**
\`\`\`kotlin
currentStack = [HomeScreen, RingtoneDetailScreen("123")]
currentDestination = RingtoneDetailScreen("123")
currentTab.defaultRoute = HomeScreen

// RingtoneDetailScreen("123") != HomeScreen → NOT at root
// → Case 3: Pop from stack

currentStack.removeLastOrNull()
// Before: [HomeScreen, RingtoneDetailScreen("123")]
// After: [HomeScreen]

return true  // Back was handled
\`\`\`

#### Step 4: Recomposition

Since \`backStacks[Home]\` changed, it recomposes:
- \`toDecoratedEntries()\` generates new entries
- Now there's only \`[DecoratedNavEntry(HomeScreen, ...)]\`
- \`NavDisplay\` shows \`HomeScreen\`

**Result:** You're back at \`HomeScreen\` 🏠

---

## Multiple BackStacks (Tabs)

### Scenario: Navigation between tabs

**Initial state:**
\`\`\`kotlin
backStacks = {
    Home → [HomeScreen],
    Settings → [SettingsScreen]
}
currentTab = Home
\`\`\`

**User navigates: HomeScreen → RingtoneDetail("123")**
\`\`\`kotlin
backStacks = {
    Home → [HomeScreen, RingtoneDetailScreen("123")],  // ← Changes
    Settings → [SettingsScreen]
}
currentTab = Home
\`\`\`

**User presses Settings tab:**
\`\`\`kotlin
// TabBar.kt
NavigationBarItem(
    onClick = { onTabSelected(TabDestination.Settings) }
)

// NavigationStateHolder.kt
fun onTabSelected(tab: TabDestination) {
    currentTab = tab  // Home → Settings
}
\`\`\`

**State after:**
\`\`\`kotlin
backStacks = {
    Home → [HomeScreen, RingtoneDetailScreen("123")],  // ← NO change
    Settings → [SettingsScreen]
}
currentTab = Settings  // ← Changes
\`\`\`

### "Exit Through Home" Pattern

**Which entries are displayed?**

\`\`\`kotlin
getActiveTabRoutes()
// → [Home, Settings]  ← Home tab stays active in background

decoratedEntries[Home] = [
    DecoratedNavEntry(HomeScreen, ...),
    DecoratedNavEntry(RingtoneDetailScreen("123"), ...)
]

decoratedEntries[Settings] = [
    DecoratedNavEntry(SettingsScreen, ...)
]

// Final result (flatMap):
[
  DecoratedNavEntry(HomeScreen, ...),
  DecoratedNavEntry(RingtoneDetailScreen("123"), ...),
  DecoratedNavEntry(SettingsScreen, ...)  // ← This is displayed
]
\`\`\`

**NavDisplay shows the last one:** \`SettingsScreen\` ⚙️

**Key:** The Home tab **remains in memory** with its stack intact.

### Back from Settings

**User presses back:**
```kotlin
navigateBack()

// currentStack = backStacks[Settings] = [SettingsScreen]
// currentDestination = SettingsScreen
// currentTab = Settings
// startTab = Home

// Are we at root of a tab that's NOT the startTab?
if (currentDestination == currentTab.defaultRoute && currentTab != startTab) {
    currentTab = startTab  // Settings → Home
    return true
}
```

**State after back:**
```kotlin
backStacks = {
    Home → [HomeScreen, RingtoneDetailScreen("123")],  // ← Intact!
    Settings → [SettingsScreen]
}
currentTab = Home  // ← Back to Home
```

**Entries displayed:**
```kotlin
getActiveTabRoutes() → [Home]

decoratedEntries[Home] = [
    DecoratedNavEntry(HomeScreen, ...),
    DecoratedNavEntry(RingtoneDetailScreen("123"), ...)
]

// NavDisplay shows:
RingtoneDetailScreen("123")  // ← You're back where you were!
```

**✨ Magic:** The Home tab state was completely preserved.

---

## State Preservation

### SaveableStateHolder per Tab

Each tab has its own \`SaveableStateHolder\`:

```kotlin
// For Home tab:
val saveableDecorator1 = rememberSaveableStateHolderNavEntryDecorator()

// For Settings tab:
val saveableDecorator2 = rememberSaveableStateHolderNavEntryDecorator()
```

When decorating an entry:
```kotlin
val entry = NavEntry(HomeScreen, { HomeScreen() })
val decorated = saveableDecorator.decorate(entry)

// Internally:
decorated.content = {
    SaveableStateProvider(key = "HomeScreen") {
        entry.content()  // HomeScreen()
    }
}
```

\`SaveableStateProvider\`:
- Creates a state "bucket" associated with the key
- All \`rememberSaveable\` inside save their state in that bucket
- When the entry reappears, restores the state

**Example:**
```kotlin
@Composable
fun HomeScreen() {
    var text by rememberSaveable { mutableStateOf("") }

    TextField(value = text, onValueChange = { text = it })
}
```

**Lifecycle:**
1. User types "Hello" in the TextField
2. \`text\` is saved in HomeScreen's bucket
3. User switches to Settings tab
4. HomeScreen leaves the screen, but state remains
5. User returns to Home tab
6. \`text\` is restored from bucket → "Hello" is still there

### ViewModelStore per Entry

```kotlin
val viewModelDecorator = rememberViewModelStoreNavEntryDecorator()

Creates a `ViewModelStore` per entry:
```

```kotlin
decorated.content = {
    ViewModelStoreProvider(key = "RingtoneDetailScreen-123") {
        entry.content()  // RingtoneDetailScreen(...)
    }
}
```

When calling \`koinViewModel<RingtoneDetailViewModel>()\`:
- Looks in that entry's \`ViewModelStore\`
- If it exists, returns it
- If not, creates and stores it

**Lifecycle:**
- Entry enters backstack → ViewModel is created
- Entry stays in backstack → ViewModel is maintained
- Entry leaves backstack → \`ViewModel.onCleared()\` is called

---

## System Advantages

### 1. Testable

```kotlin
@Test
fun `when user clicks, navigates to detail`() = runTest {
    // Given
    val mockNavigator = mockk<Navigator>()
    coEvery { mockNavigator.navigate(any()) } just Runs

    val viewModel = HomeViewModel(mockNavigator)

    // When
    viewModel.onRingtoneClick("123")

    // Then
    coVerify {
        mockNavigator.navigate(
            Destination.RingtoneDetailScreen("123")
        )
    }
}
```

**No callbacks in Composables:**
```kotlin
// ❌ Hard to test:
HomeScreen(onNavigate = { /* ViewModel logic */ })

// ✅ Easy to test:
HomeScreen()  // ViewModel handles everything internally
```

### 2. Decoupled

```kotlin
// ViewModel only knows the Navigator interface
class HomeViewModel(private val navigator: Navigator)

// Does NOT know:
// - NavigationStateHolder
// - NavBackStack
// - Tabs
// - MainActivity
```

### 3. Independent multiple backstacks

```
backStacks = {
    Home → [HomeScreen, Detail("1"), Detail("2")],
    Settings → [SettingsScreen, ProfileScreen]
}

// Each tab maintains its independent history
// Switching tabs doesn't lose the other's state
```

### 4. "Exit through Home" pattern

```kotlin
// User: Home → Settings → presses back
// Result: Returns to Home (doesn't exit app)

// User: Home → presses back
// Result: Exits app
```

### 5. Complete state preservation

- UI state (TextField, scroll) → \`SaveableStateHolder\`
- ViewModel state → \`ViewModelStore\`
- Survives:
  - Tab changes
  - Screen rotations
  - Process death

---

## Usage Guide

### In a ViewModel

```kotlin
class MyViewModel(
    private val navigator: Navigator
) : BaseViewModel(navigator) {

    fun navigateToDetail(id: String) {
        launchCatching {
            navigator.navigate(Destination.RingtoneDetailScreen(id))
        }
    }

    fun goBack() {
        launchCatching {
            navigator.navigateUp()
        }
    }
}
```

### In a Screen

```kotlin
@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()

    Button(onClick = {
        viewModel.navigateToDetail("123")  // ✅ No callbacks
    }) {
        Text("View detail")
    }
}
```

### Adding a new screen

**1. Define the destination:**
```kotlin
// Destination.kt
sealed interface Destination: NavKey {
    // ... existing destinations

    @Serializable
    data class NewScreen(val param: String) : Destination
}
```

**2. Add the entry in MainActivity:**
```kotlin
val entryProvider = entryProvider {
    // ... existing entries

    entry<Destination.NewScreen> { newScreen ->
        NewScreen(newScreen.param)
    }
}
```

**3. Navigate from a ViewModel:**
```kotlin
fun onSomeAction() {
    launchCatching {
        navigator.navigate(Destination.NewScreen("value"))
    }
}
```

### Adding a new Tab

**1. Define the tab:**
```kotlin
// TabDestination.kt
sealed interface TabDestination {
    // ... existing tabs
    @Serializable
    data object NewTab : TabDestination {
        override val name: Int = R.string.bottom_bar_new
        override val selectedIcon: Int = R.drawable.ic_new_filled
        override val unselectedIcon: Int = R.drawable.ic_new_outlined
        override val defaultRoute: Destination = Destination.NewTabScreen
    }
}
```

**2. Create the backstack in rememberNavigationState:**
```kotlin
@Composable
fun rememberNavigationState(): NavigationStateHolder {
    val homeBackStack = rememberNavBackStack(TabDestination.Home.defaultRoute)
    val settingsBackStack = rememberNavBackStack(TabDestination.Settings.defaultRoute)
    val newBackStack = rememberNavBackStack(TabDestination.NewTab.defaultRoute)  // ← New

    val backStacks = remember {
        mapOf(
            TabDestination.Home to homeBackStack,
            TabDestination.Settings to settingsBackStack,
            TabDestination.NewTab to newBackStack  // ← New
        )
    }
    // ...
}
```

**3. Add the tab to TabBar:**
```kotlin
// TabBar.kt
private val TAB_DESTINATIONS = listOf(
    TabDestination.Home,
    TabDestination.Settings,
    TabDestination.NewTab  // ← New
)
```

---

## Complete Cycle Summary

1. **User clicks** → \`viewModel.onRingtoneClick()\`
2. **ViewModel** → \`navigator.navigate(destination)\`
3. **Navigator** → \`channel.send(NavigationAction)\`
4. **MainActivity** → observes channel → \`navigationState.navigate()\`
5. **NavigationStateHolder** → \`backStack.add(destination)\` (modifies state)
6. **Recomposition** → \`toDecoratedEntries()\` generates new entries
7. **NavDisplay** → displays the last entry
8. **User sees** → \`RingtoneDetailScreen\`

---

## System Components

| Component | Responsibility | Location |
|-----------|----------------|----------|
| **Destination** | Defines routes (NavKey) | \`core/navigation/destination/Destination.kt\` |
| **TabDestination** | Defines tabs | \`core/navigation/destination/TabDestination.kt\` |
| **NavBackStack** | Maintains history per tab | Provided by Navigation 3 |
| **NavigationStateHolder** | Manages backstacks | \`core/navigation/action/NavigationStateHolder.kt\` |
| **Navigator** | Interface for ViewModels | \`core/navigation/action/Navigator.kt\` |
| **NavigationAction** | Navigation actions | \`core/navigation/action/NavigationAction.kt\` |
| **entryProvider** | Maps Destination → Composable | Defined in \`MainActivity.kt\` |
| **Decorators** | Add SaveableState and ViewModels | Provided by Navigation 3 |
| **NavDisplay** | Displays current screen | Provided by Navigation 3 |
| **NavigationHost** | NavDisplay wrapper | \`core/navigation/NavigationHost.kt\` |
| **TabBar** | Bottom navigation bar | \`core/navigation/TabBar.kt\` |

---
