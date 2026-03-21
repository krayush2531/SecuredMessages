# Copilot Instructions for SecuredMessages

## Build, test, and lint commands

- Make wrapper executable first when needed: `chmod +x gradlew`
- Build debug APK (used in CI): `./gradlew assembleDebug`
- Full build: `./gradlew build`
- Lint module: `./gradlew :app:lintDebug` (or all variants: `./gradlew lint`)
- Run unit tests: `./gradlew :app:testDebugUnitTest`
- Run a single unit test class/method:
  - `./gradlew :app:testDebugUnitTest --tests "com.mdtauhid.securedmessages.ExampleTest"`
  - `./gradlew :app:testDebugUnitTest --tests "com.mdtauhid.securedmessages.ExampleTest.testMethod"`
- Run instrumentation tests on device/emulator: `./gradlew :app:connectedDebugAndroidTest`
- Run a single instrumentation test:
  - `./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mdtauhid.securedmessages.ExampleInstrumentedTest`

Notes:
- This repo currently has no checked-in files under `app/src/test` or `app/src/androidTest`.
- CI workflow (`.github/workflows/manual_build.yml`) uses JDK 17 and `assembleDebug`.

## High-level architecture

- Single-module Android app (`:app`) using Kotlin, ViewBinding, Room, and KSP.
- Main UI shell is `MainActivity`, which hosts a `ViewPager2` + `TabLayout` via `MessagesPagerAdapter`.
- Each tab is a `MessageFragment` with optional `SmsCategory` filter (`All` tab uses `null` category).
- Data pipeline is:
  - `SmsReceiver` receives `android.provider.Telephony.SMS_RECEIVED`
  - parses sender with `SmsCategoryParser`
  - writes `Message` rows into Room via `MessageDao`
  - UI observes Room-backed flows through `MessageRepository` -> `MessageViewModel` -> `LiveData`.
- Room stack:
  - `Message` entity
  - `MessageDao` query methods return `Flow<List<Message>>`
  - `MessageDatabase` + `DatabaseProvider` singleton
  - `Converters` stores `SmsCategory` as enum name in DB.

## Key repository conventions

- Dependency wiring is manual (no DI framework): construct `MessageRepository` and `MessageViewModelFactory` at call sites (Activity/Fragment).
- Fragments use `activityViewModels` so all tabs share the same `MessageViewModel` instance.
- Category tab/filter contract:
  - fragment arg key is `"category"` with enum `name` string
  - `null` means no filter (show all messages).
- SMS category parsing is sender-based: parser expects dash-separated sender IDs and uses the last segment as category code (`P/S/G/T/I/U` -> enum).
- Broadcast receiver work pattern uses `goAsync()` + coroutine on `Dispatchers.IO`; always finish with `pendingResult.finish()` in `finally`.
- Runtime SMS permissions (`RECEIVE_SMS`, `READ_SMS`) are requested in `MainActivity`; keep permission-sensitive flows aligned with this behavior.
