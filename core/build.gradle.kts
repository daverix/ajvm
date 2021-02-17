import net.daverix.ajvm.plugin.GenerateTestDataTask

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.4.30"
}

val testDataDir = file("$buildDir/testdata")
val testConfig by configurations.creating

dependencies {
    attributesSchema {
        attribute(Usage.USAGE_ATTRIBUTE)
    }

    testConfig(project(":testdata")) {
        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        }
    }
}

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
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
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
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2")
                }
            }
        }
    }
}

tasks {
    val generateTestDataFiles by registering(GenerateTestDataTask::class) {
        config.set(testConfig)
        outputDir.set(testDataDir)
    }

    named("compileTestKotlinJs") {
        dependsOn(generateTestDataFiles)
    }

    named("compileTestKotlinJvm") {
        dependsOn(generateTestDataFiles)
    }
}
