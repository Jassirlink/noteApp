plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // Hilt plugin - needed for code generation
    alias(libs.plugins.hilt)
    // KSP - Kotlin Symbol Processing (used by Hilt and Room for code generation)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.devquest.noteapp"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.devquest.noteapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // ===== COMPOSE =====
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ===== NAVIGATION =====
    implementation(libs.navigation.compose)

    // ===== VIEWMODEL =====
    implementation(libs.lifecycle.viewmodel.compose)

    // ===== DAGGER HILT =====
    // hilt-android: the runtime library (annotations, component wiring)
    implementation(libs.hilt.android)
    // hilt-compiler: generates code at build time via KSP
    ksp(libs.hilt.compiler)
    // hilt-navigation-compose: lets us use hiltViewModel() in composables
    implementation(libs.hilt.navigation.compose)

    // ===== ROOM =====
    // room-runtime: core Room library
    implementation(libs.room.runtime)
    // room-ktx: adds Kotlin coroutine support (suspend functions, Flow)
    implementation(libs.room.ktx)
    // room-compiler: generates DAO implementation code via KSP
    ksp(libs.room.compiler)

    // ===== TESTS =====
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
