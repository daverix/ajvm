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
        project.fileTree(classesDir).filter { !it.isDirectory && it.extension == "class" }.forEach { file ->
            val packageName = "net.daverix.ajvm.testdata"
            val relativePath = packageName.replace('.', '/')
            val className = file.nameWithoutExtension

            val generatedFile = File(outputDir, "$relativePath/${className}TestData.kt")
            generatedFile.parentFile.mkdirs()
            println("generating $generatedFile")

            val joinedData = file.readBytes().joinToString(", ") {
                String.format("0x%02X.toByte()", it)
            }
            generatedFile.writeText("""
                    package $packageName
                    
                    val byteCodeOf${className} = byteArrayOf($joinedData)
                """.trimIndent())
        }
    }
}
