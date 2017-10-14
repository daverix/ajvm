package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException

class DStoreOperation : ByteCodeOperation {
    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val index = reader.readUnsignedByte()
        val localVariable = currentFrame.pop()

        if (localVariable !is Double)
            throw IllegalStateException("variable $localVariable is not an Double")

        currentFrame.setLocalVariable(index, localVariable)
    }
}
