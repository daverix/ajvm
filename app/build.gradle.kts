plugins {
    id("com.android.application") version "4.1.2"
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")
    defaultConfig {
        applicationId = "net.daverix.net.daverix.ajvm"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "$version"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        val release by getting {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":core"))
}