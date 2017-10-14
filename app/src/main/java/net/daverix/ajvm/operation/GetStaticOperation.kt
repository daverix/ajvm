package net.daverix.ajvm.operation


import net.daverix.ajvm.Frame
import net.daverix.ajvm.VirtualObject
import net.daverix.ajvm.ByteCodeReader
import net.daverix.ajvm.io.ClassReference
import net.daverix.ajvm.io.ConstantPool
import net.daverix.ajvm.io.FieldReference
import net.daverix.ajvm.io.NameAndTypeDescriptorReference
import net.daverix.ajvm.VirtualObjectLoader

import java.io.IOException

class GetStaticOperation(private val staticClasses: MutableMap<String, VirtualObject>,
                         private val loader: VirtualObjectLoader,
                         private val constantPool: ConstantPool) : ByteCodeOperation {

    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader, indexOfBytecode: Int, currentFrame: Frame) {
        val staticFieldIndex = reader.readUnsignedShort()

        val fieldReference = constantPool[staticFieldIndex] as FieldReference?
        val fieldNameAndType = constantPool[fieldReference!!.nameAndTypeIndex] as NameAndTypeDescriptorReference?
        val fieldName = constantPool[fieldNameAndType!!.nameIndex] as String?

        val classReference = constantPool[fieldReference.classIndex] as ClassReference?
        val fieldClassName = constantPool[classReference!!.nameIndex] as String?

        var staticClass: VirtualObject? = staticClasses[fieldClassName]
        if (staticClass == null) {
            staticClass = loader.load(fieldClassName!!)
            staticClasses.put(fieldClassName, staticClass)
        }

        currentFrame.push(staticClass.getFieldValue(fieldName!!)!!)
    }
}
