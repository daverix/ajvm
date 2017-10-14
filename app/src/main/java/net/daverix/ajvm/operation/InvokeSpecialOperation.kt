package net.daverix.ajvm.operation


import net.daverix.ajvm.ByteCodeReader
import net.daverix.ajvm.Frame
import net.daverix.ajvm.VirtualObject
import net.daverix.ajvm.getArgumentCount
import net.daverix.ajvm.io.ConstantPool
import net.daverix.ajvm.io.MethodReference
import net.daverix.ajvm.io.NameAndTypeDescriptorReference
import java.io.IOException


class InvokeSpecialOperation(private val constantPool: ConstantPool) : ByteCodeOperation {

    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader, indexOfBytecode: Int, currentFrame: Frame) {
        //TODO: http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.invokespecial
        // need to call super methods etc properly
        val methodReferenceIndex = reader.readUnsignedShort()
        val methodReference = constantPool[methodReferenceIndex] as MethodReference?
        val nameAndType = constantPool[methodReference!!.nameAndTypeIndex] as NameAndTypeDescriptorReference
        val methodName = constantPool[nameAndType.nameIndex] as String
        val methodDescriptor = constantPool[nameAndType.descriptorIndex] as String
        val argumentCount = methodDescriptor.getArgumentCount()

        val methodArgs = arrayOfNulls<Any>(argumentCount)
        for (i in argumentCount - 1 downTo 0) {
            methodArgs[i] = currentFrame.pop()
        }

        val instance = currentFrame.pop()
        when {
            instance is VirtualObject -> {
                val result = instance.invokeMethod(methodName, methodDescriptor, methodArgs)
                if (!methodDescriptor.endsWith("V")) {
                    currentFrame.push(result!!)
                }
            }
            instance is String && methodName == "hashCode" -> currentFrame.push(instance.hashCode())
            instance is String && methodName == "equals" -> currentFrame.push(if (instance == methodArgs[0]) 1 else 0)
            instance is Int -> currentFrame.push(if (instance === methodArgs[0]) 1 else 0)
            else -> throw UnsupportedOperationException("don't know how to handle " + instance)
        }
    }
}
