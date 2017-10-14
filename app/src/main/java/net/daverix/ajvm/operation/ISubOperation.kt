package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException

class ISubOperation : ByteCodeOperation {
    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val iSub2 = currentFrame.pop() as Int
        val iSub1 = currentFrame.pop() as Int
        currentFrame.push(iSub1 - iSub2)
    }
}
