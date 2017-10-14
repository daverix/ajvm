package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException

class IMulOperation : ByteCodeOperation {
    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader, indexOfBytecode: Int, currentFrame: Frame) {
        val iMul2 = currentFrame.pop() as Int
        val iMul1 = currentFrame.pop() as Int
        currentFrame.push(iMul1 * iMul2)
    }
}
