// Top-level build file. Plugins are declared here (apply false) and applied
// in each module so the version catalog drives a single source of truth.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}
