import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.di.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.room)
    alias(libs.plugins.licensee)
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
    allow("BSD-3-Clause") // TODO Add screen in settings with https://github.com/google/play-services-plugins/tree/main/oss-licenses-plugin
    allowUrl("https://developer.android.com/studio/terms.html")
}

android {
    val currentTime = System.currentTimeMillis().milliseconds

    compileSdk = libs.versions.sdk.compile.asProvider().get().toInt()
    // TODO wait support for compileSdk 35
    //  compileSdkExtension = libs.versions.sdk.compile.extension.get().toInt() wait for sdk 35
    buildToolsVersion = libs.versions.build.tools.version.get()

    defaultConfig {
        applicationId = "ua.frist008.action.record"
        namespace = applicationId
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        versionCode = (currentTime - (365 * 54).days).inWholeMinutes.toInt()
        versionName = libs.versions.version.name.get()

        // https://developer.android.com/guide/topics/resources/app-languages#gradle-config
        resourceConfigurations += listOf("en", "ru", "uk")

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        getByName(libs.versions.build.type.debug.get()) {
            storeFile = file("../keystore/${libs.versions.build.type.debug.get()}.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create(libs.versions.build.type.release.get()) {
            storeFile = file("../../keystore/${libs.versions.build.type.release.get()}.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = "-${libs.versions.build.type.debug.get()}"
            signingConfig = signingConfigs.getByName(libs.versions.build.type.debug.get())
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true

            resValue("bool", "firebase_performance_enabled", "true")
            resValue("bool", "firebase_analytics_enabled", "false")
            resValue("bool", "firebase_crashlytics_enabled", "false")

        }
        release {
            signingConfig = signingConfigs.getByName(libs.versions.build.type.release.get())
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            resValue("bool", "firebase_performance_enabled", "false")
            resValue("bool", "firebase_analytics_enabled", "true")
            resValue("bool", "firebase_crashlytics_enabled", "true")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        targetCompatibility(libs.versions.jvm.target.asProvider().get().toInt())
        sourceCompatibility(libs.versions.jvm.target.asProvider().get().toInt())
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.kotlin.get()
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "-opt-in=androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi",
            "-opt-in=kotlinx.coroutines.ObsoleteCoroutinesApi",
        )
        freeCompilerArgs += "-Xcontext-receivers"
    }

    buildFeatures {
        buildConfig = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Kotlin
    implementation(platform(libs.kotlin.bom))
    implementation(libs.bundles.kotlin.bom)
    implementation(libs.bundles.kotlin.common)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.tooling)

    // UI
    implementation(libs.bundles.common)
    implementation(libs.bundles.ui)
    implementation(libs.bundles.navigation)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase.bom)

    // Framework
    implementation(libs.bundles.di)
    ksp(libs.bundles.database.compiler)
    implementation(libs.bundles.database)
    ksp(libs.bundles.di.compiler)
    implementation(libs.ads)
    implementation(platform(libs.kotlin.coroutines.bom))
    implementation(libs.bundles.coroutines.bom)
    implementation(libs.data.store)

    // Util
    coreLibraryDesugaring(libs.jdk.desugar)
    implementation(libs.leakcanary)
    debugImplementation(libs.leakcanary.debug)
    implementation(libs.timber)
}
