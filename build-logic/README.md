# Convention Plugins

The `build-logic` folder defines project-specific convention plugins, used to keep a single
source of truth for common module configurations.

This approach is heavily based on
[Google - Now in Android](https://github.com/android/nowinandroid/tree/main/build-logic)

By setting up convention plugins in `build-logic`, we can avoid duplicated build script setup,
messy `subproject` configurations, without the pitfalls of the `buildSrc` directory.

`build-logic` is an included build, as configured in the root
[`settings.gradle.kts`](../settings.gradle.kts).

Inside `build-logic` is a `convention` module, which defines a set of plugins that all normal
modules can use to configure themselves.

## Architecture
This build-logic module is a composite build that contains a :convention sub-project. All plugins are defined as programmatic, class-based plugins written in Kotlin for maximum type safety and testability.

### Core Components
- Extension Functions: Located in the extensions package, these are Kotlin extension functions that simplify interacting with the Gradle API and the version catalog (libs).

- Convention Plugins: Located in the plugins package, each class implements Plugin<Project> and encapsulates a specific piece of build logic (e.g., DetektConventionPlugin).

- Modules.kt: A simple object that provides type-safe constants for referencing other project modules in dependency blocks.

### How to Use a Convention Plugin
To apply a convention plugin to a module (e.g., :app or :analytics), you must first create one in convention folder, register it and then apply it by its ID.

#### Step 1: Register the Plugin
All plugins must be registered in the build.gradle.kts file of the :convention module. This gives each plugin a unique ID that can be referenced by other modules.

_File: build-logic/convention/build.gradle.kts_

```kotlin
gradlePlugin {
    plugins {
        register("detektConventionPlugin") {
            id = "detekt.convention.plugin"
            implementationClass = "plugins.analysis.DetektConventionPlugin"
        }
        // ... register all other plugins here
    }
}
```

#### Step 2: Apply the Plugin
In the build.gradle.kts file of any module that needs the configuration, simply apply the plugin by its ID in the plugins block.

_File: app/build.gradle.kts_

```kotlin
plugins {
    // ...
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    id("detekt.convention.plugin")
}
```

### Available Convention Plugins

#### DetektConventionPlugin
- ID: detekt.convention.plugin
- Purpose: Applies and configures the Detekt static analysis tool with our project's shared configuration. It also adds the necessary detektPlugins dependency for formatting.

#### UnitTestConventionPlugin
- ID: unit.test.convention.plugin
- Purpose: Configures the module for JUnit 5 unit testing. It enables the JUnit 5 platform and adds all standard testing dependencies (junit-bom, coroutines-test, mockk).
