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

import net.daverix.ajvm.io.ConstantPool.Companion.readConstantPool

data class ClassInfo(
        val majorVersion: Int,
        val minorVersion: Int,
        val constantPool: ConstantPool,
        val accessFlags: Int,
        val classIndex: Int,
        val superClassIndex: Int,
        val interfaces: IntArray,
        val fields: Array<FieldInfo>,
        val methods: Array<MethodInfo>,
        val attributes: Array<AttributeInfo>
) {
    val name: String
        get() {
            val classReference = constantPool[classIndex] as ClassReference
            return constantPool[classReference.nameIndex] as String
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ClassInfo

        if (majorVersion != other.majorVersion) return false
        if (minorVersion != other.minorVersion) return false
        if (constantPool != other.constantPool) return false
        if (accessFlags != other.accessFlags) return false
        if (classIndex != other.classIndex) return false
        if (superClassIndex != other.superClassIndex) return false
        if (!interfaces.contentEquals(other.interfaces)) return false
        if (!fields.contentEquals(other.fields)) return false
        if (!methods.contentEquals(other.methods)) return false
        if (!attributes.contentEquals(other.attributes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = majorVersion
        result = 31 * result + minorVersion
        result = 31 * result + constantPool.hashCode()
        result = 31 * result + accessFlags
        result = 31 * result + classIndex
        result = 31 * result + superClassIndex
        result = 31 * result + interfaces.contentHashCode()
        result = 31 * result + fields.contentHashCode()
        result = 31 * result + methods.contentHashCode()
        result = 31 * result + attributes.contentHashCode()
        return result
    }
}

private const val MAGIC_NUMBER = -889275714 // 0xCAFEBABEu

fun DataInputStream.readClassInfo(): ClassInfo {
    val magicNumber = readInt()
    if (magicNumber != MAGIC_NUMBER) {
        error("Not a java class file, expected $MAGIC_NUMBER but got $magicNumber")
    }
    val minorVersion = readUnsignedShort()
    val majorVersion = readUnsignedShort()
    val constantPool = readConstantPool()
    val accessFlags = readUnsignedShort()
    val thisClass = readUnsignedShort()
    val superClass = readUnsignedShort()
    val numberOfInterfaces = readUnsignedShort()
    val interfaces = IntArray(numberOfInterfaces) {
        readUnsignedShort()
    }
    val fields = readFields()
    val methods = readMethods()
    val attributes = readAttributes(this)

    return ClassInfo(majorVersion,
            minorVersion,
            constantPool,
            accessFlags,
            thisClass,
            superClass,
            interfaces,
            fields,
            methods,
            attributes)
}

private fun DataInputStream.readMethods(): Array<MethodInfo> {
    return Array(readUnsignedShort()) {
        val accessFlags = readUnsignedShort()
        val nameIndex = readUnsignedShort()
        val descriptorIndex = readUnsignedShort()
        val attributes = readAttributes(this)

        MethodInfo(accessFlags, nameIndex, descriptorIndex, attributes)
    }
}

private fun DataInputStream.readFields(): Array<FieldInfo> {
    return Array(readUnsignedShort()) {
        val accessFlags = readUnsignedShort()
        val nameIndex = readUnsignedShort()
        val descriptorIndex = readUnsignedShort()
        val attributes = readAttributes(this)

        FieldInfo(accessFlags, nameIndex, descriptorIndex, attributes)
    }
}