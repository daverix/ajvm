package net.daverix.ajvm

import net.daverix.ajvm.io.ClassInfo
import net.daverix.ajvm.io.ClassInfoProvider
import net.daverix.ajvm.io.DataInputStream
import net.daverix.ajvm.io.readClassInfo
import net.daverix.ajvm.testdata.testClassPath
import java.io.File
import java.util.jar.JarFile

actual val testClassInfoProvider: ClassInfoProvider = object : ClassInfoProvider {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getClassInfo(qualifiedName: String): ClassInfo {
        for(path in testClassPath) {
            val file = JarFile(File(path))
            val entry = file.getJarEntry("$qualifiedName.class")
            if(entry != null) {
                try {
                    return DataInputStream(file.getInputStream(entry)).use {
                        it.readClassInfo()
                    }
                } catch (ex: Exception) {
                    throw IllegalStateException("error reading $qualifiedName", ex)
                }
            }
        }
        error("cannot find $qualifiedName.class")
    }
}