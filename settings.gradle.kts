@file:Suppress("UnstableApiUsage")

rootProject.name = "ajm"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":testdata")
include(":core")
include(":app")
