plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    namespace = AppConfig.namespace
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"

        freeCompilerArgs =  freeCompilerArgs +
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi" +
                "-opt-in=kotlin.time.ExperimentalTime"
    }

    buildFeatures {
        viewBinding = true

        // Disable unused AGP features
        buildConfig = false
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(Dependencies.AndroidX.implementation)
    implementation(Dependencies.Coil.implementation)
    implementation(Dependencies.Hilt.implementation)
    implementation(Dependencies.Insetter.implementation)
    implementation(Dependencies.Kotlin.implementation)
    implementation(Dependencies.Lifecycle.implementation)
    implementation(Dependencies.Material.implementation)
    implementation(Dependencies.Navigation.implementation)
    implementation(Dependencies.Other.implementation)
    implementation(Dependencies.Paging3.implementation)
    implementation(Dependencies.Retrofit.implementation)

    kapt(Dependencies.Hilt.kapt)

    debugImplementation(Dependencies.Chucker.debugImplementation)
    releaseImplementation(Dependencies.Chucker.releaseImplementation)
}