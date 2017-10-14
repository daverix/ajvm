package net.daverix.ajvm

import net.daverix.ajvm.io.ClassInfo
import net.daverix.ajvm.io.ClassInfoReader
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class FileSystemClassInfoProvider(private val dir: String) : ClassInfoProvider {
    override fun getClassInfo(className: String): ClassInfo {
        val file = File("$dir/$className.class")
        if (!file.exists())
            throw IOException("Cannot find " + file)

        return ClassInfoReader(DataInputStream(FileInputStream(file))).use { it.read() }
    }
}