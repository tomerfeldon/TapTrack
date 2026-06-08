plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.taptrack.shared"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Exposed with `api` so the app modules can extend AppCompatActivity
    // (the base class of BaseTrackerActivity) transitively.
    api(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
}
