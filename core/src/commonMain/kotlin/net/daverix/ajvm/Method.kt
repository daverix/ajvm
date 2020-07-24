package net.daverix.ajvm

import net.daverix.ajvm.io.ClassReference
import net.daverix.ajvm.io.ConstantPool
import net.daverix.ajvm.io.MethodReference
import net.daverix.ajvm.io.NameAndTypeDescriptorReference

data class MethodDescriptor(val parameters: List<String>, val returnType: String)
data class Method(val name: String, val descriptor: MethodDescriptor, val rawDescriptor: String, val className: String)

private val METHOD_DESCRIPTOR_REGEX = Regex("\\((.*)\\)(B|C|D|F|I|J|L.+;|S|Z|\\[.+|V)")
private val METHOD_PARAMETERS_REGEX = Regex("(B|C|D|F|I|J|L[^;]+;|S|Z|\\[[^\\[]+)+")

private fun parseMethodDescriptor(descriptor: String): MethodDescriptor {
    val methodResult = METHOD_DESCRIPTOR_REGEX.matchEntire(descriptor)
            ?: error("Cannot parse method descriptor $descriptor")

    val returnDescriptor = methodResult.groups[2]?.value ?: ""
    val parameters = methodResult.groups[1]?.value ?: ""
    val parametersResult = METHOD_PARAMETERS_REGEX.matchEntire(parameters)
    val parameterList = mutableListOf<String>()
    if(parametersResult != null) {
        for (i in 1 until parametersResult.groupValues.size) {
            parameterList += parametersResult.groupValues[i]
        }
    }

    return MethodDescriptor(parameterList, returnDescriptor)
}

fun getMethod(constantPool: ConstantPool, methodReferenceIndex: Int): Method {
    val methodReference = constantPool[methodReferenceIndex] as MethodReference
    val classReference = constantPool[methodReference.classIndex] as ClassReference
    val className = constantPool[classReference.nameIndex] as String
    val nameAndType = constantPool[methodReference.nameAndTypeIndex] as NameAndTypeDescriptorReference
    val name = constantPool[nameAndType.nameIndex] as String
    val descriptor = constantPool[nameAndType.descriptorIndex] as String

    return Method(
            name = name,
            className = className,
            rawDescriptor = descriptor,
            descriptor = parseMethodDescriptor(descriptor)
    )
}