package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException

interface ByteCodeOperation {
    @Throws(IOException::class)
    fun execute(reader: ByteCodeReader,
                indexOfBytecode: Int,
                currentFrame: Frame)
}
