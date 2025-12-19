

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization") version libs.versions.kotlin.get() // Asegúrate de que
    //  id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.fp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.fp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    configurations.all {
        // Excluir la versión antigua de las anotaciones
        exclude(group = "com.intellij", module = "annotations")
        // Forzar la versión definida en libs.versions.toml
        resolutionStrategy {
            force(libs.jetbrains.annotations)
        }
    }

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.foundation)
    //Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    // Compilador Room con KSP - Kotlin Symbol Processing
    // (tecnología de procesamiento de anotaciones)
    ksp(libs.room.compiler.ksp)
    //Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.core) // Accede a 'retrofit-core'
    implementation(libs.kotlinx.serialization.json) // Accede a 'kotlinx-serialization-json'
    implementation(libs.retrofit.converter.kotlinx) // Accede a 'retrofit-converter-kotlinx'
    implementation(libs.okhttp.core) // Accede a 'okhttp-core
    implementation(libs.coil.compose)
    implementation(libs.androidx.compose.material.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}