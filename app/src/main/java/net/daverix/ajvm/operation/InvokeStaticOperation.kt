package net.daverix.ajvm.operation


import net.daverix.ajvm.*
import net.daverix.ajvm.io.ClassReference
import net.daverix.ajvm.io.ConstantPool
import net.daverix.ajvm.io.MethodReference
import net.daverix.ajvm.io.NameAndTypeDescriptorReference
import java.io.IOException

class InvokeStaticOperation(private val staticClasses: MutableMap<String, VirtualObject>,
                            private val loader: VirtualObjectLoader,
                            private val constantPool: ConstantPool) : ByteCodeOperation {

    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val methodReferenceIndex = reader.readUnsignedShort()
        val methodReference = constantPool[methodReferenceIndex] as MethodReference
        val nameAndType = constantPool[methodReference.nameAndTypeIndex] as NameAndTypeDescriptorReference
        val methodName = constantPool[nameAndType.nameIndex] as String
        val methodDescriptor = constantPool[nameAndType.descriptorIndex] as String
        val argumentCount = methodDescriptor.getArgumentCount()

        val methodArgs = arrayOfNulls<Any>(argumentCount)
        for (i in argumentCount - 1 downTo 0) {
            methodArgs[i] = currentFrame.pop()
        }

        val classReference = constantPool[methodReference.classIndex] as ClassReference
        val className = constantPool[classReference.nameIndex] as String
        var staticClass: VirtualObject? = staticClasses[className]
        if (staticClass == null) {
            staticClass = loader.load(className)
            staticClasses.put(className, staticClass)
        }

        val result = staticClass.invokeMethod(methodName, methodDescriptor, methodArgs)
        if (!methodDescriptor.endsWith("V")) {
            currentFrame.push(result!!)
        }
    }
}
