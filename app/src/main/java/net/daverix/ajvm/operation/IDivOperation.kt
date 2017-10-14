package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException

class IDivOperation : ByteCodeOperation {
    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val iDiv2 = currentFrame.pop() as Int
        val iDiv1 = currentFrame.pop() as Int
        currentFrame.push(iDiv1 / iDiv2)
    }
}
