// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.services) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
    alias(libs.plugins.module.graph.assert)
    alias(libs.plugins.paparazzi) apply false
}

// Enforce the Clean Architecture dependency rules as a CI check instead of a
// convention: presentation and the pure core must never reach across the graph
// into the data layer or the composition root. Run with `./gradlew assertModuleGraph`.
moduleGraphAssert {
    configurations = setOf("implementation", "api")
    restricted = arrayOf(
        ":app -X> .*:data:.*",        // presentation must go through :bridgeDi, not data directly
        ".*:core:.* -X> .*:data:.*",  // the pure core must not depend on the data layer
        ".*:core:.* -X> :app",        // the core must not depend on presentation
        ".*:core:.* -X> :bridgeDi",   // the core must not depend on the composition root
    )
}

// Aggregate coverage of the business-logic layers (domain + data) into a single
// merged report and gate. Presentation (:app) is dominated by Compose UI that is
// covered by Paparazzi screenshot tests rather than line coverage, and the DI
// wiring (:bridgeDi) is verified by the Koin graph test — neither is a meaningful
// line-coverage target, so they are kept out of the gate.
dependencies {
    kover(project(":core:model"))
    kover(project(":core:domain"))
    kover(project(":data:repository"))
    kover(project(":data:remote"))
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "*.BuildConfig",
                    "*ComposableSingletons*",
                    "*\$\$serializer",
                )
            }
        }
        verify {
            rule("Minimum line coverage of domain and data layers") {
                minBound(80)
            }
        }
    }
}
