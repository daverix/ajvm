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
        val methodReference = constantPool[methodReferenceIndex] as MethodReference
        val nameAndType = constantPool[methodReference.nameAndTypeIndex] as NameAndTypeDescriptorReference
        val methodName = constantPool[nameAndType.nameIndex] as String
        val methodDescriptor = constantPool[nameAndType.descriptorIndex] as MethodDescriptor
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
