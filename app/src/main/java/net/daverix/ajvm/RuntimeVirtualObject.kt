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
import net.daverix.ajvm.io.MethodInfo
import net.daverix.ajvm.operation.ByteCodeOperation
import java.io.IOException

class RuntimeVirtualObject(private val classInfo: ClassInfo,
                           private val byteCodeOperations: Map<Opcode, ByteCodeOperation>) : VirtualObject {
    override val fields: Map<String, Any> = HashMap()
    override val name: String
        get() = classInfo.name

    override fun initialize(args: Array<Any>) {

    }

    @Throws(IOException::class)
    override fun invokeMethod(name: String, descriptor: String, args: Array<Any?>): Any? {
        val method = getMethodByNameAndDescriptor(name, descriptor)

        val (maxStack, maxLocals, code) = method.getCodeAttribute()
        val reader = ByteCodeReader(code)

        val localVariables: Array<Any?> = arrayOfNulls(maxLocals)
        for (i in args.indices) {
            localVariables[i] = args[i]
        }

        val stack = OperandStack(maxStack)

        while (reader.canReadByte()) {
            val byteCodeIndex = reader.index
            val byteCode = fromByteCode(reader.readUnsignedByte())

            if (byteCode == Opcode.RETURN) return null
            if (byteCode == Opcode.IRETURN) return stack.pop()

            val operation = byteCodeOperations[byteCode]
            if (operation == null) {
                val byteCodeName = byteCode.name
                val hexCode = byteCode.byteCode.toString(2)
                throw IllegalStateException("Unknown bytecode: $byteCodeName ($hexCode) " +
                        "at position $byteCodeIndex")
            }

            operation.execute(reader, byteCodeIndex, stack, localVariables)
        }

        return null
    }

    private fun getMethodByNameAndDescriptor(methodName: String, descriptor: String): MethodInfo {
        val methods = classInfo.methods
        val constantPool = classInfo.constantPool

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
