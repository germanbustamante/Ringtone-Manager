pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Ringtone Manager"

includeBuild("build-logic")
include(":app")
include(":core:model")
include(":core:domain")
include(":data:repository")
include(":data:remote")
include(":data:local")
include(":analytics")
include(":bridgeDi")
