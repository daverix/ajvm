package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader

import java.io.IOException
import java.util.Locale

class LLoadOperation : ByteCodeOperation {
    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val index = reader.readUnsignedByte()
        val variable = currentFrame.getLocalVariable(index)
        if (variable !is Long)
            throw IllegalStateException(String.format(Locale.ENGLISH,
                    "variable %s at index %d is not a Long",
                    variable, index))

        currentFrame.push(variable)
    }
}
