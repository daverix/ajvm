package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException

class IStoreOperation : ByteCodeOperation {
    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val index = reader.readUnsignedByte()
        val localVariable = currentFrame.pop()

        if (localVariable !is Int)
            throw IllegalStateException("variable $localVariable is not an Long")

        currentFrame.setLocalVariable(index, localVariable)
    }
}
