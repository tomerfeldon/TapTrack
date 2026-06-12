plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.taptrack.shared"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Exposed with `api` so the app modules can extend AppCompatActivity
    // (the base class of BaseTrackerActivity) transitively, and so the shared
    // Material 3 components + Theme.TapTrack are visible to both apps.
    api(libs.androidx.appcompat)
    api(libs.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx) // enableEdgeToEdge()

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
