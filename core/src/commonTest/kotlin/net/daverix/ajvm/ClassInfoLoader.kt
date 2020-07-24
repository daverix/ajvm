package net.daverix.ajvm

import net.daverix.ajvm.io.DataInputStream
import net.daverix.ajvm.io.useDataInputStream
import net.daverix.ajvm.testdata.testData

object TestDataFileOpener : FileOpener {
    override fun <T> openFile(className: String, reader: DataInputStream.()->T): T =
            testData[className]?.useDataInputStream(reader)
            ?: error("cannot find $className")
}
