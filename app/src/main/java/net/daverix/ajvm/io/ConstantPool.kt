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

import java.io.DataInputStream
import java.io.IOException
import java.nio.ByteBuffer

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

typealias ConstantPool = Array<Any?>

@Throws(IOException::class)
fun DataInputStream.readConstantPool(): Array<Any?> {
    val constantPoolCount = readUnsignedShort()
    val constantPool = arrayOfNulls<Any?>(constantPoolCount)
    var i = 1
    while (i < constantPoolCount) {
        val tag = readUnsignedByte()
        when (tag) {
            -1 -> throw IOException("EOF when reading type from constant pool")
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
                    throw IllegalStateException("could not read first part of long")

                if (readUnsignedShort() != CONSTANT_TAG_LONG)
                    throw IllegalStateException("expecting to read another constant pool for long value")
                if (read(longBytes, 4, 4) != 4)
                    throw IllegalStateException("could not read second part of long")

                constantPool[i] = ByteBuffer.wrap(longBytes).long
                // we advance index one additional time because we are sure that the second
                // part comes directly after
                i++
                i++
            }
            CONSTANT_TAG_DOUBLE -> {
                val doubleBytes = ByteArray(8)
                if (read(doubleBytes, 0, 4) != 4)
                    throw IllegalStateException("could not read first part of double")

                if (readUnsignedShort() != CONSTANT_TAG_LONG)
                    throw IllegalStateException("expecting to read another constant pool for double value")
                if (read(doubleBytes, 4, 4) != 4)
                    throw IllegalStateException("could not read second part of double")

                constantPool[i] = ByteBuffer.wrap(doubleBytes).double
                // we advance index one additional time because we are sure that the second
                // part comes directly after
                i++
                i++
            }
            CONSTANT_TAG_CLASS_REFERENCE -> {
                constantPool[i] = ClassReference(readUnsignedShort())
                i++
            }
            CONSTANT_TAG_STRING_REFERENCE -> {
                constantPool[i] = StringReference(readUnsignedShort())
                i++
            }
            CONSTANT_TAG_FIELD_REFERENCE -> {
                constantPool[i] = FieldReference(readUnsignedShort(), readUnsignedShort())
                i++
            }
            CONSTANT_TAG_METHOD_REFERENCE -> {
                constantPool[i] = MethodReference(readUnsignedShort(), readUnsignedShort())
                i++
            }
            CONSTANT_TAG_INTERFACE_METHOD_REFERENCE -> {
                constantPool[i] = InterfaceMethodReference(readUnsignedShort(), readUnsignedShort())
                i++
            }
            CONSTANT_TAG_NAME_AND_TYPE_DESCRIPTOR -> {
                constantPool[i] = NameAndTypeDescriptorReference(readUnsignedShort(), readUnsignedShort())
                i++
            }
            CONSTANT_TAG_NAME_METHOD_TYPE -> {
                constantPool[i] = MethodTypeReference(readUnsignedShort())
                i++
            }
            CONSTANT_TAG_METHOD_HANDLE -> {
                constantPool[i] = MethodHandleReference(readUnsignedByte(), readUnsignedShort())
                i++
            }
            CONSTANT_TAG_INVOKE_DYNAMIC -> constantPool[i] = {
                InvokeDynamicReference(readUnsignedShort(), readUnsignedShort())
                i++
            }
        }

    }
    return constantPool
}
