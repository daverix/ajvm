package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.VirtualObject
import net.daverix.ajvm.ByteCodeReader
import net.daverix.ajvm.io.ClassReference
import net.daverix.ajvm.io.ConstantPool
import net.daverix.ajvm.VirtualObjectLoader

import java.io.IOException

class NewOperation(private val loader: VirtualObjectLoader,
                   private val constantPool: ConstantPool) : ByteCodeOperation {

    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val newObjectIndex = reader.readUnsignedShort()
        val classRef = constantPool[newObjectIndex] as ClassReference?
        val className = constantPool[classRef!!.nameIndex] as String?

        val virtualObject = loader.load(className!!)
        currentFrame.push(virtualObject)
    }
}
