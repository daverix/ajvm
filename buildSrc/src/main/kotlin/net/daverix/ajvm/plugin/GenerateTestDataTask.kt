package net.daverix.ajvm.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class GenerateTestDataTask @Inject constructor(
        @InputDirectory val classesDir: File,
        @OutputDirectory val outputDir: File
) : DefaultTask() {

    @TaskAction
    fun generateTestData() {
        val packageName = "net.daverix.ajvm.testdata"
        val relativePath = packageName.replace('.', '/')
        val generatedFile = File(outputDir, "$relativePath/TestData.kt")
        generatedFile.parentFile.mkdirs()

        val lines = mutableListOf<String>()
        project.fileTree(classesDir).filter { !it.isDirectory && it.extension == "class" }.forEach { file ->
            val className = file.nameWithoutExtension.replace("$","\\$")
            println("parsing $className")

            val bytes = file.readBytes().joinToString(", ") {
                String.format("0x%02X", it)
            }
            lines += "    \"net/daverix/ajvm/test/$className\" to intArrayOf($bytes).map { it.toByte() }.toByteArray()"
        }
        generatedFile.writeText("""
package $packageName

val testData = mapOf(
${lines.joinToString(",\n")}
)
                """.trimIndent())
    }
}
