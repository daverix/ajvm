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
package net.daverix.ajvm


import net.daverix.ajvm.io.ClassInfo
import net.daverix.ajvm.io.ConstantPool
import net.daverix.ajvm.io.MethodInfo
import net.daverix.ajvm.operation.ByteCodeOperation
import java.io.IOException
import java.util.*

class RuntimeVirtualObject(private val classInfo: ClassInfo,
                           private val byteCodeOperations: Map<Opcodes, ByteCodeOperation>) : VirtualObject {

    private val fieldValues = HashMap<String, Any?>()
    private val constantPool: ConstantPool = classInfo.constantPool

    override val name: String
        get() = constantPool[classInfo.classIndex] as String

    override fun initialize(args: Array<Any>) {

    }

    override fun setFieldValue(fieldName: String, value: Any?) {
        fieldValues.put(fieldName, value)
    }

    override fun getFieldValue(fieldName: String): Any? {
        return fieldValues[fieldName]
    }

    @Throws(IOException::class)
    override fun invokeMethod(name: String, descriptor: String, args: Array<Any?>): Any? {
        val method = getMethodByNameAndDescriptor(name, descriptor)

        val (maxStack, maxLocals, code) = method.getCodeAttribute()
        val reader = ByteCodeReader(code)

        val currentFrame = Frame(
                maxLocals,
                maxStack)
        for (i in args.indices) {
            currentFrame.setLocalVariable(i, args[i])
        }

        while (reader.canReadByte()) {
            val indexOfBytecode = reader.index
            val byteCode = fromByteCode(reader.readUnsignedByte()) ?: throw IllegalStateException("byteCode is null")

            if (byteCode == Opcodes.RETURN) return null
            if (byteCode == Opcodes.IRETURN) return currentFrame.pop()

            byteCodeOperations[byteCode]?.execute(reader, indexOfBytecode, currentFrame) ?: throw IllegalStateException("Unknown bytecode: ${byteCode.name} (${byteCode.byteCode.toString(2)}) at position $indexOfBytecode")
        }

        return null
    }

    private fun getMethodByNameAndDescriptor(methodName: String, descriptor: String): MethodInfo {
        val methods = classInfo.methods
        for (method in methods) {
            val constantName = constantPool[method.nameIndex] as String?
            val constantDescriptor = constantPool[method.descriptorIndex] as String?

            if (methodName == constantName && descriptor == constantDescriptor) {
                return method
            }
        }

        throw NoSuchMethodError("Cannot find method $methodName in $name")
    }
}
