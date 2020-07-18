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


import net.daverix.ajvm.OperandStack
import net.daverix.ajvm.VirtualObjectLoader
import net.daverix.ajvm.io.ClassReference
import net.daverix.ajvm.io.ConstantPool

class NewOperation(private val loader: VirtualObjectLoader,
                   private val constantPool: ConstantPool) : ByteCodeOperation {

    override fun execute(reader: net.daverix.ajvm.ByteCodeReader,
                         indexOfBytecode: Int,
                         stack: OperandStack,
                         localVariables: Array<Any?>) {
        val newObjectIndex = reader.readUnsignedShort()
        val classRef = constantPool[newObjectIndex] as ClassReference
        val className = constantPool[classRef.nameIndex] as String

        val virtualObject = loader.load(className)
        stack.push(virtualObject)
    }
}
