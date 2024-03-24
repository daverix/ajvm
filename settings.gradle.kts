@file:Suppress("UnstableApiUsage")

rootProject.name = "ajvm"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
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
