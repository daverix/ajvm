package net.daverix.ajvm

import net.daverix.ajvm.io.ClassInfo
import net.daverix.ajvm.io.useDataInputStream
import net.daverix.ajvm.testdata.testData

object TestDataClassInfoProvider : ClassInfoProvider {
    override fun getClassInfo(className: String): ClassInfo = testData[className]
            ?.useDataInputStream { ClassInfo.read(it) }
            ?: error("cannot find $className")
}
