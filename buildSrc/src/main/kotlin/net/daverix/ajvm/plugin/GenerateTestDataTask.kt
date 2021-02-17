package net.daverix.ajvm.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.jar.JarEntry
import java.util.jar.JarFile
import javax.inject.Inject

abstract class GenerateTestDataTask : DefaultTask() {
    @get:InputFiles
    abstract val config: Property<Configuration>

    @get:OutputDirectory
    abstract val outputDir: Property<File>

    @TaskAction
    fun generateTestData() {
        val packageName = "net.daverix.ajvm.testdata"
        val relativePath = packageName.replace('.', '/')
        val generatedFile = File(outputDir.get(), "$relativePath/TestData.kt")
        generatedFile.parentFile.mkdirs()

        val rtPath = "${System.getProperty("java.home")}/lib/rt.jar"
        val paths = config.get().resolve().map { it.absolutePath } + rtPath

        generatedFile.writeText("""
package $packageName

val testClassPath = listOf(
${paths.joinToString(",\n") { "    \"$it\"" }}
)
        """.trimIndent())
    }
}
