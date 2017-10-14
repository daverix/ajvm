package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException

class IRemOperation : ByteCodeOperation {
    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val iAdd2 = currentFrame.pop() as Int
        val iAdd1 = currentFrame.pop() as Int
        currentFrame.push(iAdd1 % iAdd2)
    }
}
