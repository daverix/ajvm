plugins {
    id("com.android.application") version "8.3.1"
}

android {
    namespace = "net.daverix.ajvm"
    compileSdk = 34
    defaultConfig {
        applicationId = "net.daverix.net.daverix.ajvm"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "$version"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":core"))
}