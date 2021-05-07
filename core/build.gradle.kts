import net.daverix.ajvm.plugin.GenerateTestDataTask

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.5.0"
}

val testDataDir = file("$buildDir/testdata")

kotlin {
    jvm()
    js(IR) {
        browser()
    }

    sourceSets {
        sourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(kotlin("stdlib-common"))
                }
            }
            val commonTest by getting {
                kotlin.srcDir("$buildDir/testdata")
                dependencies {
                    implementation(kotlin("test-common"))
                    implementation(kotlin("test-annotations-common"))
                }
            }

            val jsMain by getting {
                dependencies {
                    implementation(kotlin("stdlib-js"))
                }
            }
            val jsTest by getting {
                dependencies {
                    implementation(kotlin("test-js"))
                }
            }

            val jvmMain by getting {
                dependencies {
                    implementation(kotlin("stdlib-jdk8"))
                }
            }
            val jvmTest by getting {
                dependencies {
                    implementation(kotlin("test-junit"))
                }
            }
        }
    }
}

tasks {
    val generateTask = register(
            "generateTestDataFiles",
            GenerateTestDataTask::class,
            file("$rootDir/testdata/build/classes/java/main"),
            testDataDir
    )
    generateTask.configure {
        dependsOn(":testdata:classes")
    }

    named("compileTestKotlinJs") {
        dependsOn(generateTask)
    }

    named("compileTestKotlinJvm") {
        dependsOn(generateTask)
    }
}
