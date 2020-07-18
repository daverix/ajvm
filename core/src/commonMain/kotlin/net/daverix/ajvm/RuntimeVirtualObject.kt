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


import net.daverix.ajvm.io.AttributeInfo
import net.daverix.ajvm.io.ClassInfo
import net.daverix.ajvm.io.CodeAttribute
import net.daverix.ajvm.io.MethodInfo
import net.daverix.ajvm.operation.ByteCodeOperation

class RuntimeVirtualObject(
        private val classInfo: ClassInfo,
        private val byteCodeOperations: Map<Opcode, ByteCodeOperation>
) : VirtualObject {
    override val fields: Map<String, Any> = HashMap()
    override val name: String
            get() = classInfo.name

    override fun initialize(args: Array<Any>) {

    }

    override fun invokeMethod(name: String, descriptor: String, args: Array<Any?>): Any? {
        val method = getMethodByNameAndDescriptor(name, descriptor)
                ?: error("Cannot find method $name in ${classInfo.name}")

        val (maxStack, maxLocals, code) = CodeAttribute.read(method.attributes["Code"].info)
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
                error("Unknown bytecode: $byteCodeName ($hexCode) at position $byteCodeIndex")
            }

            operation.execute(reader, byteCodeIndex, stack, localVariables)
        }

        return null
    }

    private operator fun Array<AttributeInfo>.get(name: String): AttributeInfo {
        return first { classInfo.constantPool[it.nameIndex] == name }
    }

    private fun getMethodByNameAndDescriptor(methodName: String, descriptor: String): MethodInfo? {
        val methods = classInfo.methods
        val constantPool = classInfo.constantPool

        return methods.firstOrNull {
            constantPool[it.nameIndex] == methodName && constantPool[it.descriptorIndex] == descriptor
        }
    }
}
