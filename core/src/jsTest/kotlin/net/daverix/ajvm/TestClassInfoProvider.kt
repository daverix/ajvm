package net.daverix.ajvm

import net.daverix.ajvm.io.ClassInfo
import net.daverix.ajvm.io.ClassInfoProvider

actual val testClassInfoProvider: ClassInfoProvider = object : ClassInfoProvider {
    override suspend fun getClassInfo(qualifiedName: String): ClassInfo {
        error("not implemented yet...")
    }
}