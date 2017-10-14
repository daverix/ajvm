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
package net.daverix.ajvm.io


import net.daverix.ajvm.containsFlag
import java.io.DataInputStream
import java.io.IOException

class MethodInfo(accessFlags: Int,
                 val nameIndex: Int,
                 val descriptorIndex: Int,
                 val attributes: Array<AttributeInfo>,
                 private val constantPool: ConstantPool) {
    val isPrivate = accessFlags containsFlag ACC_PRIVATE
    val isProtected = accessFlags containsFlag ACC_PROTECTED
    val isPublic = accessFlags containsFlag ACC_PUBLIC
    val isStatic = accessFlags containsFlag ACC_STATIC
    val isFinal = accessFlags containsFlag ACC_FINAL

    @Throws(IOException::class)
    fun getCodeAttribute(): CodeAttribute {
        return CodeAttribute.read(attributes["Code"].info)
    }

    operator fun Array<AttributeInfo>.get(name: String): AttributeInfo {
        return first { constantPool[it.nameIndex] == name }
    }

    companion object {
        val ACC_PUBLIC = 0x0001
        val ACC_PRIVATE = 0x0002
        val ACC_PROTECTED = 0x0004
        val ACC_STATIC = 0x0008
        val ACC_FINAL = 0x0010
        val ACC_SYNCHRONIZED = 0x0020
        val ACC_BRIDGE = 0x0040
        val ACC_VARARGS = 0x0080
        val ACC_NATIVE = 0x0100
        val ACC_ABSTRACT = 0x0400
        val ACC_STRICT = 0x0800
        val ACC_SYNTHETIC = 0x1000

        @Throws(IOException::class)
        fun read(stream: DataInputStream,
                 constantPool: ConstantPool): MethodInfo {
            val accessFlags = stream.readUnsignedShort()
            val nameIndex = stream.readUnsignedShort()
            val descriptorIndex = stream.readUnsignedShort()
            val attributes = stream.readAttributes()

            return MethodInfo(accessFlags, nameIndex, descriptorIndex, attributes, constantPool)
        }
    }
}
