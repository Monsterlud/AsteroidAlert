import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.isIncrementalKapt

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.navigation.safe.args)
}

android {
    namespace = "com.monsalud.asteroidalert"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.monsalud.asteroidalert"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    kapt {
        useBuildCache = false
    }
}

dependencies {

    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiprefreshlayout)

    // Material
    implementation(libs.material)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // ViewModel & LiveData
    implementation(libs.lifecycle)
    implementation(libs.lifecycle.runtime)

    // Retrofit
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.moshi)
    implementation(libs.retrofit2.converter.scalars)
    implementation(libs.retrofit.coroutines.adapter)

    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    // Room Database
    implementation(libs.room)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // Glide & Picasso
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.picasso)

    // WorkManager
    implementation(libs.workmanager)

    // Testing
    testImplementation(libs.junit)

    // Instrumented Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}