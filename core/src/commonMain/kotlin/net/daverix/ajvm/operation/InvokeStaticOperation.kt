/*
    Java Virtual Machine for Android
    Copyright (C) 2017 David Laurell

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
 */
package net.daverix.ajvm.operation


import net.daverix.ajvm.*
import net.daverix.ajvm.io.ClassReference
import net.daverix.ajvm.io.ConstantPool
import net.daverix.ajvm.io.MethodReference
import net.daverix.ajvm.io.NameAndTypeDescriptorReference

class InvokeStaticOperation(private val staticClasses: MutableMap<String, VirtualObject>,
                            private val loader: VirtualObjectLoader,
                            private val constantPool: ConstantPool) : ByteCodeOperation {

    override fun execute(
            reader: ByteCodeReader,
            indexOfBytecode: Int,
            stack: OperandStack,
            localVariables: Array<Any?>
    ) {
        val methodReferenceIndex = reader.readUnsignedShort()
        val methodReference = constantPool[methodReferenceIndex] as MethodReference
        val nameAndType = constantPool[methodReference.nameAndTypeIndex] as NameAndTypeDescriptorReference
        val methodName = constantPool[nameAndType.nameIndex] as String
        val methodDescriptor = constantPool[nameAndType.descriptorIndex] as String
        val parsedMethodDescriptor = parseMethodDescriptor(methodDescriptor)
        val argumentCount = parsedMethodDescriptor.parameters.size

        val methodArgs = arrayOfNulls<Any>(argumentCount)
        for (i in argumentCount - 1 downTo 0) {
            methodArgs[i] = stack.pop()
        }

        val classReference = constantPool[methodReference.classIndex] as ClassReference
        val className = constantPool[classReference.nameIndex] as String
        var staticClass: VirtualObject? = staticClasses[className]
        if (staticClass == null) {
            staticClass = loader.load(className)
            staticClasses[className] = staticClass
        }

        val result = staticClass.invokeMethod(methodName, methodDescriptor, methodArgs)
        if (parsedMethodDescriptor.returnDescriptor != "V") {
            stack.push(result)
        }
    }
}