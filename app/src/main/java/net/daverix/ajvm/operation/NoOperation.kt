package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException

class NoOperation : ByteCodeOperation {
    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader, indexOfBytecode: Int, currentFrame: Frame) {
        //no operation
    }
}