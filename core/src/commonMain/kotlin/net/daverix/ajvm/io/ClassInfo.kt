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
        val accessFlags: Int,
        val name: String,
        val superClassName: String?,
        val interfaces: List<Int>,
        val fields: List<FieldInfo>,
        val methods: List<MethodInfo>,
        val attributes: List<AttributeInfo>
) {
    fun getMethodByNameAndDescriptor(name: String, descriptor: String): MethodInfo? =
            methods.firstOrNull { it.name == name && it.descriptor == descriptor }
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
    val interfaces = List(numberOfInterfaces) {
        readUnsignedShort()
    }

    val classReference = constantPool[thisClass] as ClassReference
    val name = constantPool[classReference.nameIndex] as String
    val superClassName = if(superClass == 0) null else {
        val superClassReference = constantPool[superClass] as ClassReference
        constantPool[superClassReference.nameIndex] as String
    }

    val fields = readFields(constantPool)
    val methods = readMethods(constantPool)
    val attributes = readAttributes(constantPool)

    return ClassInfo(
            majorVersion,
            minorVersion,
            accessFlags,
            name,
            superClassName,
            interfaces,
            fields,
            methods,
            attributes
    )
}

private fun DataInputStream.readMethods(constantPool: ConstantPool): List<MethodInfo> =
        List(readUnsignedShort()) {
            val accessFlags = readUnsignedShort()
            val nameIndex = readUnsignedShort()
            val descriptorIndex = readUnsignedShort()
            val otherAttributes = mutableListOf<AttributeInfo>()
            var codeAttribute: CodeAttribute? = null
            val attributeCount = readUnsignedShort()
            repeat(attributeCount) {
                val attributeNameIndex = readUnsignedShort()
                val attributeLength = readInt()

                val attributeName = constantPool[attributeNameIndex] as String
                if (attributeName == "Code") {
                    try {
                        codeAttribute = readCodeAttribute(constantPool)
                    } catch (ex: Throwable) {
                        throw IllegalStateException("cannot read code attribute from method ${constantPool[nameIndex]} with length $attributeLength", ex)
                    }
                } else {
                    val info = ByteArray(attributeLength)
                    readFully(info)
                    otherAttributes += AttributeInfo(attributeName, info)
                }
            }

            val name = constantPool[nameIndex] as String
            val descriptor = constantPool[descriptorIndex] as String

            MethodInfo(accessFlags, name, descriptor, codeAttribute, otherAttributes)
        }
