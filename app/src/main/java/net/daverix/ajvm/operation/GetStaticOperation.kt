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


import net.daverix.ajvm.ByteCodeReader
import net.daverix.ajvm.Frame
import net.daverix.ajvm.VirtualObject
import net.daverix.ajvm.VirtualObjectLoader
import net.daverix.ajvm.io.ClassReference
import net.daverix.ajvm.io.ConstantPool
import net.daverix.ajvm.io.FieldReference
import net.daverix.ajvm.io.NameAndTypeDescriptorReference
import java.io.IOException

class GetStaticOperation(private val staticClasses: MutableMap<String, VirtualObject>,
                         private val loader: VirtualObjectLoader,
                         private val constantPool: ConstantPool) : ByteCodeOperation {

    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader, indexOfBytecode: Int, currentFrame: Frame) {
        val staticFieldIndex = reader.readUnsignedShort()

        val fieldReference = constantPool[staticFieldIndex] as FieldReference
        val fieldNameAndType = constantPool[fieldReference.nameAndTypeIndex] as NameAndTypeDescriptorReference
        val fieldName = constantPool[fieldNameAndType.nameIndex] as String

        val classReference = constantPool[fieldReference.classIndex] as ClassReference
        val fieldClassName = constantPool[classReference.nameIndex] as String

        var staticClass: VirtualObject? = staticClasses[fieldClassName]
        if (staticClass == null) {
            staticClass = loader.load(fieldClassName)
            staticClasses.put(fieldClassName, staticClass)
        }

        currentFrame.push(staticClass.getFieldValue(fieldName))
    }
}
