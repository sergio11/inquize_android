import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    kotlin("kapt")
    alias(libs.plugins.daggerHilt)
}

android {
    namespace = "com.dreamsoftware.inquize"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.dreamsoftware.inquize"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        // load the api key from local properties file and make it
        // available as a build config field
        val properties = Properties().apply {
            load(project.rootProject.file("local.properties").inputStream())
        }
        // google-gemini api key
        val geminiApiKey = properties.getProperty("GOOGLE_GEMINI_API_KEY")
        buildConfigField(
            type = "String",
            name = "GOOGLE_GEMINI_API_KEY",
            value = "\"$geminiApiKey\""
        )
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidComposeCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    coreLibraryDesugaring(libs.core.library.desugaring)

    // compose
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.bundles.compose)
    androidTestImplementation(libs.bundles.composeTest)

    // Brownie UI
    implementation(libs.brownie.ui)

    // CameraX
    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.androidx.camera.view)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // sdk for google's gemini models
    implementation(libs.generativeai)

    // junit
    testImplementation(libs.junit)

    // coroutines-test
    testImplementation(libs.kotlinx.coroutines.test)

    // datastore
    implementation(libs.androidx.preferences.datastore)
}
kapt {
    correctErrorTypes = true
}