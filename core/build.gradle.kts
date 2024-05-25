import net.daverix.ajvm.plugin.GenerateTestDataTask

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "2.0.0"
}

val testDataDir: Provider<Directory> = layout.buildDirectory.dir("testdata")

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
                kotlin.srcDir(testDataDir)
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
    val generateTestDataFiles by registering(GenerateTestDataTask::class) {
        classesDir.set(rootProject.project("testdata").layout.buildDirectory.dir("classes/java/main"))
        outputDir.set(testDataDir)

        dependsOn(":testdata:classes")
    }

    named("compileTestKotlinJs") {
        dependsOn(generateTestDataFiles)
    }

    named("compileTestKotlinJvm") {
        dependsOn(generateTestDataFiles)
    }
}
