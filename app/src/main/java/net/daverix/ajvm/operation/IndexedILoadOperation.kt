package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException

class IndexedILoadOperation(private val index: Int) : ByteCodeOperation {

    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val variable = currentFrame.getLocalVariable(index)
        if (variable !is Int)
            throw IllegalStateException("variable $variable is not an Integer")

        currentFrame.push(variable)
    }
}
