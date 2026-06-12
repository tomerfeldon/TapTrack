# TapTrack


Two separate Android apps that share **one** library module. The shared module
holds an **abstract Activity**; each app provides a concrete Activity that
extends it. This demonstrates the **Template Method** design pattern: the base
class defines the full algorithm (UI + counting + logging) and each app only
fills in the small parts that differ.

## The two apps

| App          | Title                | Counts           | Goal | Accent | Emoji |
|--------------|----------------------|------------------|------|--------|-------|
| **WaterTap** | Water Intake Tracker | glasses of water | 8    | blue   | 💧    |
| **StretchTap** | Stretching Tracker | stretch sets     | 5    | green  | 🧘    |

They are functionally identical - that is the whole point. The only difference
is the handful of values each app overrides.

## Features

All implemented once in `BaseTrackerActivity` and shared by both apps:

- **Add / Undo / Reset** the count.
- **Daily goal + progress bar** (`LinearProgressIndicator`) with a "🎉 Goal
  reached!" state.
- **Per-entry timestamp** in the scrollable log (`glass of water #3 - 14:32`).
- **Per-app accent color + emoji**, applied via abstract overrides.
- **Material 3** look & feel (cards, buttons, theme).

## Module structure

```
TapTrack/
├─ shared/        Android library - abstract activity + shared layout/resources
├─ watertap/      Application - WaterActivity (LAUNCHER)
└─ stretchtap/    Application - StretchActivity (LAUNCHER)
```

- `:watertap` and `:stretchtap` both `implementation(project(":shared"))`.
- `:shared` is **not runnable on its own** (it is a library).

### Packages

| Module        | Package / namespace      |
|---------------|--------------------------|
| `:shared`     | `com.taptrack.shared`    |
| `:watertap`   | `com.taptrack.watertap`  |
| `:stretchtap` | `com.taptrack.stretchtap`|

## How the Template Method pattern is applied

`BaseTrackerActivity` (in `:shared`) contains **all** the behavior:

```kotlin
abstract class BaseTrackerActivity : AppCompatActivity() {
    abstract val screenTitle: String   // provided by each app
    abstract val unitLabel: String     // provided by each app
    abstract val dailyGoal: Int        // progress-bar target
    abstract val accentColor: Int      // theming
    abstract val emoji: String         // header icon

    // count, entries list, onCreate wiring, add/undo/reset, timestamps,
    // progress and refresh() all live here - the apps add no logic of their own.
}
```

Each concrete activity is just the overrides — no logic:

```kotlin
class WaterActivity : BaseTrackerActivity() {
    override val screenTitle = "Water Intake Tracker"
    override val unitLabel = "glass of water"
    override val dailyGoal = 8
    override val accentColor = 0xFF2196F3.toInt()
    override val emoji = "💧"
}
```

```kotlin
class StretchActivity : BaseTrackerActivity() {
    override val screenTitle = "Stretching Tracker"
    override val unitLabel = "stretch set"
    override val dailyGoal = 5
    override val accentColor = 0xFF4CAF50.toInt()
    override val emoji = "🧘"
}
```

If counting or logging logic ever appears inside an app activity, that defeats
the purpose - it belongs in the base.

## Building & running

Requires the Android SDK (use Android Studio, or set `ANDROID_HOME`).

```bash
./gradlew :watertap:assembleDebug
./gradlew :stretchtap:assembleDebug
```

Each app installs and launches independently, showing its own title and an
"Add <unit>" button. Tapping **Add** increments the count and appends
`"<unit> #<n>"` to the scrollable log.

## Tech

- Kotlin, Gradle Kotlin DSL + version catalog (`gradle/libs.versions.toml`)
- AGP 8.7.3, Kotlin 2.0.21, compileSdk/targetSdk 34, minSdk 24
- Dependencies: AndroidX `appcompat` + `com.google.android.material` (Material 3)

## Not included yet

Persistence (Room), `RecyclerView`, and any storage are intentionally left for
a later step.
