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


import java.io.Closeable
import java.io.DataInputStream
import java.io.IOException
import java.nio.ByteBuffer

class ClassInfoReader(private val stream: DataInputStream) : Closeable {
    @Throws(IOException::class)
    fun read(): ClassInfo {
        val magicNumber = stream.readInt()
        if (magicNumber != MAGIC_NUMBER) {
            throw IOException("Not a java class file, expected " + MAGIC_NUMBER + " but got " + Integer.toHexString(magicNumber))
        }
        val minorVersion = stream.readUnsignedShort()
        val majorVersion = stream.readUnsignedShort()
        val constantPool = readConstantPool()
        val accessFlags = stream.readUnsignedShort()
        val thisClass = stream.readUnsignedShort()
        val superClass = stream.readUnsignedShort()
        val interfaces = readInterfaces()
        val fields = readFields()
        val methods = readMethods(constantPool)
        val attributes = stream.readAttributes()

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

    @Throws(IOException::class)
    private fun readMethods(constantPool: ConstantPool): Array<MethodInfo> {
        return Array(stream.readUnsignedShort()) {
            MethodInfo.read(stream, constantPool)
        }
    }

    @Throws(IOException::class)
    private fun readFields(): Array<FieldInfo> {
        return Array(stream.readUnsignedShort()) {
            stream.readField()
        }
    }

    @Throws(IOException::class)
    private fun readInterfaces(): IntArray {
        return IntArray(stream.readUnsignedShort()) {
            stream.readUnsignedShort()
        }
    }

    @Throws(IOException::class)
    private fun readConstantPool(): ConstantPool {
        val constantPoolCount = stream.readUnsignedShort()
        val constantPool = arrayOfNulls<Any?>(constantPoolCount)
        var i = 1
        while (i < constantPoolCount) {
            val tag = stream.readUnsignedByte()
            when (tag) {
                -1 -> throw IOException("EOF when reading type from constant pool")
                CONSTANT_TAG_STRING -> {
                    constantPool[i] = stream.readUTF()
                    i++
                }
                CONSTANT_TAG_INTEGER -> {
                    constantPool[i] = stream.readInt()
                    i++
                }
                CONSTANT_TAG_FLOAT -> {
                    constantPool[i] = stream.readFloat()
                    i++
                }
                CONSTANT_TAG_LONG -> {
                    val longBytes = ByteArray(8)
                    if (stream.read(longBytes, 0, 4) != 4)
                        throw IllegalStateException("could not read first part of long")

                    if (stream.readUnsignedShort() != CONSTANT_TAG_LONG)
                        throw IllegalStateException("expecting to read another constant pool for long value")
                    if (stream.read(longBytes, 4, 4) != 4)
                        throw IllegalStateException("could not read second part of long")

                    constantPool[i] = ByteBuffer.wrap(longBytes).long
                    // we advance index one additional time because we are sure that the second
                    // part comes directly after
                    i++
                    i++
                }
                CONSTANT_TAG_DOUBLE -> {
                    val doubleBytes = ByteArray(8)
                    if (stream.read(doubleBytes, 0, 4) != 4)
                        throw IllegalStateException("could not read first part of double")

                    if (stream.readUnsignedShort() != CONSTANT_TAG_LONG)
                        throw IllegalStateException("expecting to read another constant pool for double value")
                    if (stream.read(doubleBytes, 4, 4) != 4)
                        throw IllegalStateException("could not read second part of double")

                    constantPool[i] = ByteBuffer.wrap(doubleBytes).double
                    // we advance index one additional time because we are sure that the second
                    // part comes directly after
                    i++
                    i++
                }
                CONSTANT_TAG_CLASS_REFERENCE -> {
                    constantPool[i] = ClassReference(stream.readUnsignedShort())
                    i++
                }
                CONSTANT_TAG_STRING_REFERENCE -> {
                    constantPool[i] = StringReference(stream.readUnsignedShort())
                    i++
                }
                CONSTANT_TAG_FIELD_REFERENCE -> {
                    constantPool[i] = FieldReference(stream.readUnsignedShort(), stream.readUnsignedShort())
                    i++
                }
                CONSTANT_TAG_METHOD_REFERENCE -> {
                    constantPool[i] = MethodReference(stream.readUnsignedShort(), stream.readUnsignedShort())
                    i++
                }
                CONSTANT_TAG_INTERFACE_METHOD_REFERENCE -> {
                    constantPool[i] = InterfaceMethodReference(stream.readUnsignedShort(), stream.readUnsignedShort())
                    i++
                }
                CONSTANT_TAG_NAME_AND_TYPE_DESCRIPTOR -> {
                    constantPool[i] = NameAndTypeDescriptorReference(stream.readUnsignedShort(), stream.readUnsignedShort())
                    i++
                }
                CONSTANT_TAG_NAME_METHOD_TYPE -> {
                    constantPool[i] = MethodTypeReference(stream.readUnsignedShort())
                    i++
                }
                CONSTANT_TAG_METHOD_HANDLE -> {
                    constantPool[i] = MethodHandleReference(stream.readUnsignedByte(), stream.readUnsignedShort())
                    i++
                }
                CONSTANT_TAG_INVOKE_DYNAMIC -> constantPool[i] = {
                    InvokeDynamicReference(stream.readUnsignedShort(), stream.readUnsignedShort())
                    i++
                }
            }

        }
        return constantPool
    }

    @Throws(IOException::class)
    override fun close() {
        stream.close()
    }

    companion object {
        private val MAGIC_NUMBER = -0x35014542
        private val CONSTANT_TAG_STRING = 1
        private val CONSTANT_TAG_INTEGER = 3
        private val CONSTANT_TAG_FLOAT = 4
        private val CONSTANT_TAG_LONG = 5
        private val CONSTANT_TAG_DOUBLE = 6
        private val CONSTANT_TAG_CLASS_REFERENCE = 7
        private val CONSTANT_TAG_STRING_REFERENCE = 8
        private val CONSTANT_TAG_FIELD_REFERENCE = 9
        private val CONSTANT_TAG_METHOD_REFERENCE = 10
        private val CONSTANT_TAG_INTERFACE_METHOD_REFERENCE = 11
        private val CONSTANT_TAG_NAME_AND_TYPE_DESCRIPTOR = 12
        private val CONSTANT_TAG_METHOD_HANDLE = 15
        private val CONSTANT_TAG_NAME_METHOD_TYPE = 16
        private val CONSTANT_TAG_INVOKE_DYNAMIC = 18
    }
}
