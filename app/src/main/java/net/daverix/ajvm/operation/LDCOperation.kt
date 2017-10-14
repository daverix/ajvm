package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.ByteCodeReader
import net.daverix.ajvm.io.ConstantPool
import net.daverix.ajvm.io.MethodHandleReference
import net.daverix.ajvm.io.StringReference

import java.io.IOException

class LDCOperation(private val constantPool: ConstantPool) : ByteCodeOperation {

    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader, indexOfBytecode: Int, currentFrame: Frame) {
        val ldcIndex = reader.readUnsignedByte()

        val constant = constantPool[ldcIndex]
        if (constant is Int ||
                constant is Float ||
                constant is String ||
                constant is MethodHandleReference) {
            currentFrame.push(constant)
        } else if (constant is StringReference) {
            currentFrame.push(constantPool[constant.index]!!)
        }
    }
}
