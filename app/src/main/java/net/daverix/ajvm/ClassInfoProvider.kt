package net.daverix.ajvm

import net.daverix.ajvm.io.ClassInfo


interface ClassInfoProvider {
    fun getClassInfo(className: String): ClassInfo
}