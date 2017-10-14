package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException

class IndexedLLoadOperation(private val index: Int) : ByteCodeOperation {

    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val variable = currentFrame.getLocalVariable(index)
        if (variable !is Long)
            throw IllegalStateException("variable $variable is not a Long")

        currentFrame.push(variable)
    }
}
