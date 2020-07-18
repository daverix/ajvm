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

import net.daverix.ajvm.wrapToDouble
import net.daverix.ajvm.wrapToLong

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

    companion object {
        private const val MAGIC_NUMBER = -0x35014542
        private const val CONSTANT_TAG_STRING = 1
        private const val CONSTANT_TAG_INTEGER = 3
        private const val CONSTANT_TAG_FLOAT = 4
        private const val CONSTANT_TAG_LONG = 5
        private const val CONSTANT_TAG_DOUBLE = 6
        private const val CONSTANT_TAG_CLASS_REFERENCE = 7
        private const val CONSTANT_TAG_STRING_REFERENCE = 8
        private const val CONSTANT_TAG_FIELD_REFERENCE = 9
        private const val CONSTANT_TAG_METHOD_REFERENCE = 10
        private const val CONSTANT_TAG_INTERFACE_METHOD_REFERENCE = 11
        private const val CONSTANT_TAG_NAME_AND_TYPE_DESCRIPTOR = 12
        private const val CONSTANT_TAG_METHOD_HANDLE = 15
        private const val CONSTANT_TAG_NAME_METHOD_TYPE = 16
        private const val CONSTANT_TAG_INVOKE_DYNAMIC = 18

        fun read(reader: StreamReader): ClassInfo {
            val magicNumber = reader.readInt()
            if (magicNumber != MAGIC_NUMBER) {
                error("Not a java class file, expected $MAGIC_NUMBER but got ${magicNumber.toString(16)}")
            }
            val minorVersion = reader.readUnsignedShort()
            val majorVersion = reader.readUnsignedShort()
            val constantPool = reader.readConstantPool()
            val accessFlags = reader.readUnsignedShort()
            val thisClass = reader.readUnsignedShort()
            val superClass = reader.readUnsignedShort()
            val interfaces = IntArray(reader.readUnsignedShort()) {
                reader.readUnsignedShort()
            }
            val fields = reader.readFields()
            val methods = reader.readMethods()
            val attributes = readAttributes(reader)

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

        private fun StreamReader.readConstantPool(): ConstantPool {
            val constantPoolCount = readUnsignedShort()
            val constantPool = arrayOfNulls<Any?>(constantPoolCount)
            var i = 1
            while (i < constantPoolCount) {
                when (readUnsignedByte()) {
                    -1 -> error("EOF when reading type from constant pool")
                    CONSTANT_TAG_STRING -> {
                        constantPool[i] = readUTF()
                        i++
                    }
                    CONSTANT_TAG_INTEGER -> {
                        constantPool[i] = readInt()
                        i++
                    }
                    CONSTANT_TAG_FLOAT -> {
                        constantPool[i] = readFloat()
                        i++
                    }
                    CONSTANT_TAG_LONG -> {
                        val longBytes = ByteArray(8)
                        if (read(longBytes, 0, 4) != 4)
                            error("could not read first part of long")

                        if (readUnsignedShort() != CONSTANT_TAG_LONG)
                            error("expecting to read another constant pool for long value")
                        if (read(longBytes, 4, 4) != 4)
                            error("could not read second part of long")

                        constantPool[i] = longBytes.wrapToLong()
                        // we advance index one additional time because we are sure that the second
                        // part comes directly after
                        i++
                        i++
                    }
                    CONSTANT_TAG_DOUBLE -> {
                        val doubleBytes = ByteArray(8)
                        if (read(doubleBytes, 0, 4) != 4)
                            error("could not read first part of double")

                        if (readUnsignedShort() != CONSTANT_TAG_LONG)
                            error("expecting to read another constant pool for double value")
                        if (read(doubleBytes, 4, 4) != 4)
                            error("could not read second part of double")

                        constantPool[i] = doubleBytes.wrapToDouble()
                        // we advance index one additional time because we are sure that the second
                        // part comes directly after
                        i++
                        i++
                    }
                    CONSTANT_TAG_CLASS_REFERENCE -> {
                        constantPool[i] = ClassReference(nameIndex = readUnsignedShort())
                        i++
                    }
                    CONSTANT_TAG_STRING_REFERENCE -> {
                        constantPool[i] = StringReference(index = readUnsignedShort())
                        i++
                    }
                    CONSTANT_TAG_FIELD_REFERENCE -> {
                        constantPool[i] = FieldReference(
                                classIndex = readUnsignedShort(),
                                nameAndTypeIndex = readUnsignedShort()
                        )
                        i++
                    }
                    CONSTANT_TAG_METHOD_REFERENCE -> {
                        constantPool[i] = MethodReference(
                                classIndex = readUnsignedShort(),
                                nameAndTypeIndex = readUnsignedShort()
                        )
                        i++
                    }
                    CONSTANT_TAG_INTERFACE_METHOD_REFERENCE -> {
                        constantPool[i] = InterfaceMethodReference(
                                classIndex = readUnsignedShort(),
                                nameAndTypeIndex = readUnsignedShort()
                        )
                        i++
                    }
                    CONSTANT_TAG_NAME_AND_TYPE_DESCRIPTOR -> {
                        constantPool[i] = NameAndTypeDescriptorReference(
                                nameIndex = readUnsignedShort(),
                                descriptorIndex = readUnsignedShort()
                        )
                        i++
                    }
                    CONSTANT_TAG_NAME_METHOD_TYPE -> {
                        constantPool[i] = MethodTypeReference(descriptorIndex = readUnsignedShort())
                        i++
                    }
                    CONSTANT_TAG_METHOD_HANDLE -> {
                        constantPool[i] = MethodHandleReference(
                                referenceKind = readUnsignedByte(),
                                referenceIndex = readUnsignedShort()
                        )
                        i++
                    }
                    CONSTANT_TAG_INVOKE_DYNAMIC -> constantPool[i] = {
                        InvokeDynamicReference(
                                bootstrapMethodAttrIndex = readUnsignedShort(),
                                nameAndTypeIndex = readUnsignedShort()
                        )
                        i++
                    }
                }

            }
            return ConstantPool(constantPool)
        }

        private fun StreamReader.readMethods(): Array<MethodInfo> {
            return Array(readUnsignedShort()) {
                val accessFlags = readUnsignedShort()
                val nameIndex = readUnsignedShort()
                val descriptorIndex = readUnsignedShort()
                val attributes = readAttributes(this)

                MethodInfo(accessFlags, nameIndex, descriptorIndex, attributes)
            }
        }

        private fun StreamReader.readFields(): Array<FieldInfo> {
            return Array(readUnsignedShort()) {
                val accessFlags = readUnsignedShort()
                val nameIndex = readUnsignedShort()
                val descriptorIndex = readUnsignedShort()
                val attributes = readAttributes(this)

                FieldInfo(accessFlags, nameIndex, descriptorIndex, attributes)
            }
        }
    }
}
