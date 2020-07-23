package net.daverix.ajvm

import net.daverix.ajvm.io.ClassInfo
import net.daverix.ajvm.io.useDataInputStream

class ByteCodeClassInfoProvider(private val byteCode: ByteArray) : ClassInfoProvider {
    override fun getClassInfo(className: String): ClassInfo = byteCode.useDataInputStream { ClassInfo.read(it) }
}
