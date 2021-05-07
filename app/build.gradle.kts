plugins {
    id("com.android.application") version "4.1.2"
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.2")
    defaultConfig {
        applicationId = "net.daverix.net.daverix.ajvm"
        minSdkVersion(21)
        targetSdkVersion(30)
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