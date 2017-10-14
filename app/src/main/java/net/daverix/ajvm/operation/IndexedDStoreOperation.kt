package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException

class IndexedDStoreOperation(private val index: Int) : ByteCodeOperation {

    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val localVariable = currentFrame.pop()
        if (localVariable !is Double)
            throw IllegalStateException("variable $localVariable is not a Double")

        currentFrame.setLocalVariable(index, localVariable)
    }
}