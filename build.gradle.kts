import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.ben.manes)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.di.hilt) apply false
    alias(libs.plugins.licensee) apply false
}

fun isStable(version: String): Boolean {
    val upperVersion = version.uppercase()
    val stableKeyword = listOf("RELEASE", "FINAL").any(upperVersion::contains)
    val regex = Regex("^[0-9,.v-]+(r)?$")
    return stableKeyword || regex.matches(version.lowercase())
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
    checkForGradleUpdate = true
    rejectVersionIf {
        !isStable(candidate.version) && isStable(currentVersion)
    }
}
