package extensions

import org.gradle.api.artifacts.VersionCatalog

internal fun VersionCatalog.get(libraryAlias: String) =
    findLibrary(libraryAlias).get()

internal fun VersionCatalog.getVersion(key: String): String =
    findVersion(key).get().toString()