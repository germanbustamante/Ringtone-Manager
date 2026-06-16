# Ringtone Manager — Plan de mejora portfolio

Estado del plan completo de upgrades para el proyecto.  
Convención de commits: `[RM-1.0.0] Type(scope): Summary`

---

## FASE 0 — Higiene de arquitectura ✅

| # | Commit | Estado |
|---|--------|--------|
| 0.1 | `Fix(data): Remove popularity side effect from getRingtoneDetail` | ✅ |
| 0.2 | `Refactor(data): Return Either from FirebaseAuthManager.executeVoid` | ✅ |
| 0.3 | `Test(domain): Add unit tests for all auth use cases` | ✅ |
| 0.4 | `Test(remote): Add unit tests for Firebase data sources and managers` | ✅ |

---

## FASE 1 — Narrativa ✅

| # | Commit | Estado |
|---|--------|--------|
| 1.1 | `Docs(readme): Add project README with architecture overview` | ✅ |
| 1.2 | `Docs(adr): Add architecture decision records for key technical choices` | ✅ |
| 1.3 | `Docs(arch): Add module graph and data flow diagrams` | ✅ |

---

## FASE 2 — Calidad, build y CI ✅

| # | Commit | Estado |
|---|--------|--------|
| 2.1 | `Feat(build): Enable R8 minification and resource shrinking for release` | ✅ |
| 2.2 | `Feat(build): Add release signing config from environment-backed keystore properties` | ✅ |
| 2.3 | `Feat(ci): Build and verify minified release in CI` | ✅ |
| 2.4 | `Feat(build): Add Kover coverage aggregation with verification gate in CI` | ✅ |
| 2.5 | `Feat(build): Add dependency-guard baselines for all modules` | ✅ |
| 2.6 | `Feat(build): Enforce module dependency rules with module graph assertions` | ✅ |
| 2.7 | `Feat(analytics): Add Crashlytics and Performance Monitoring` | ✅ |
| 2.8 | `Feat(firebase): Version Firestore security rules and indexes in the repo` | ✅ |
| 2.9 | `Fix(a11y): Add content descriptions and semantics across all interactive elements` | ✅ |
| 2.10 | `Feat(security): Enable Play Integrity App Check for release builds` | ✅ |

---

## FASE 3 — Modularización por feature ⏭️ SKIPPED

> Decisión del usuario: priorizar Fases 4–7 sobre la modularización por feature.  
> La única tarea ejecutada fue la que era prerrequisito técnico para otras fases:

| # | Commit | Estado |
|---|--------|--------|
| 3.1 | `Feat(build): Add Compose library convention plugin` | ✅ |
| 3.2 | `Refactor(modularization): Extract :core:designsystem module` | ⏭️ |
| 3.3 | `Refactor(modularization): Extract :core:ui module` | ⏭️ |
| 3.4 | `Refactor(modularization): Extract :core:navigation module` | ⏭️ |
| 3.5 | `Refactor(modularization): Extract :core:player module` | ⏭️ |
| 3.6 | `Refactor(modularization): Extract :feature:settings module` | ⏭️ |
| 3.7 | `Refactor(modularization): Extract :feature:auth module` | ⏭️ |
| 3.8 | `Refactor(modularization): Extract :feature:ringtone module` | ⏭️ |
| 3.9 | `Refactor(modularization): Extract :feature:home module` | ⏭️ |
| 3.10 | `Refactor(modularization): Slim :app to shell with splash and navigation host` | ⏭️ |

---

## FASE 4 — Offline-first y datos ✅

| # | Commit | Estado |
|---|--------|--------|
| 4.1 | `Feat(data): Add :data:local module with DataStore preferences` | ✅ |
| 4.2 | `Feat(data): Add Room database for ringtone caching` | ✅ — KSP 2.3.0 (kapt incompatible con Kotlin 2.3) |
| 4.3 | `Refactor(data): Make ringtone repositories offline-first with Room as SSOT` | ✅ |
| 4.4 | `Feat(data): Paginate popular ringtones with Firestore query cursors` | ✅ |

---

## FASE 5 — Audio de producción ✅ parcial

| # | Commit | Estado |
|---|--------|--------|
| 5.1 | `Feat(player): Handle audio focus and becoming-noisy events in playback` | ✅ |
| 5.2 | `Refactor(player): Replace Handler polling with coroutine-based position updates` | ✅ |
| 5.3 | `Feat(player): Add MediaSession for system playback integration` | ⏭️ — requiere foreground service, scope elevado |

---

## FASE 6 — Features de producto

| # | Commit | Estado |
|---|--------|--------|
| 6.1a | `Feat(ringtone): Set audio as device ringtone via MediaStore` | ✅ — incluye descarga + insert MediaStore + WRITE_SETTINGS |
| 6.1b | _(incluido en 6.1a)_ | ✅ |
| 6.2 | `Feat(ringtone): Implement share via Android share sheet` | ✅ |
| 6.3a | `Feat(favorites): Add favorites domain model and Firestore persistence` | ✅ |
| 6.3b | `Feat(favorites): Wire favorites into Home and ringtone detail` | ✅ — incluido en el commit de 6.3a |
| 6.4 | `Feat(settings): Implement in-app language selection with per-app locales` | ✅ |
| 6.5 | `Feat(search): Add ringtone search with Firestore prefix queries` | ❌ **PENDIENTE** |
| 6.6 | `Feat(profile): Add profile management with password change` | ✅ |
| 6.7 | Upload de ringtones a Storage _(stretch, opcional)_ | ⏭️ |

---

## FASE 7 — Performance y cierre

| # | Commit | Estado |
|---|--------|--------|
| 7.1 | `Feat(performance): Add baseline profile module with startup profile generation` | ❌ **PENDIENTE** |
| 7.2 | `Test(benchmark): Add startup macrobenchmark with CI comparison` | ❌ **PENDIENTE** |
| 7.3 | `Feat(build): Add Compose compiler metrics and stability configuration` | ❌ **PENDIENTE** |
| 7.4 | `Docs(readme): Update README and diagrams with final architecture` | ✅ — actualizado tras cada fase |

---

## Resumen

| Estado | Tareas |
|--------|--------|
| ✅ Completado | 33 |
| ❌ Pendiente | 4 |
| ⏭️ Skipped (decisión) | 12 |
| **Total plan** | **49** |

### Pendientes por orden de impacto/esfuerzo

| Tarea | Impacto | Esfuerzo estimado |
|-------|---------|-------------------|
| **7.3** Compose compiler metrics + stability config | Alto — señal senior muy reconocida | ~1h |
| **6.5** Search con Firestore prefix queries | Medio — completa la funcionalidad + demuestra conocimiento de limitaciones de Firestore sin full-text | ~2h |
| **7.1** Baseline profiles | Alto — patrón NIA, diferenciador en entrevistas | ~3h |
| **7.2** Macrobenchmark de startup | Alto si se citan números concretos en README | ~3h |
