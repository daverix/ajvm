package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException

class IfEqOperation : ByteCodeOperation {
    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader, indexOfBytecode: Int, currentFrame: Frame) {
        val ifEqOffset = reader.readUnsignedShort()
        val value = currentFrame.pop() as Int
        if (value == 0) {
            reader.jumpTo(indexOfBytecode + ifEqOffset)
        }
    }
}
