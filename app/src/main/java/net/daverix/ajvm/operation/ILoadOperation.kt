package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException
import java.util.Locale

class ILoadOperation : ByteCodeOperation {
    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val index = reader.readUnsignedByte()
        val variable = currentFrame.getLocalVariable(index)
        if (variable !is Int)
            throw IllegalStateException(String.format(Locale.ENGLISH,
                    "variable %s at index %d is not an Integer",
                    variable, index))

        currentFrame.push(variable)
    }
}