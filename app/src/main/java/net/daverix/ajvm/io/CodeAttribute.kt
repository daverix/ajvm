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


import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.IOException
import java.util.*

data class CodeAttribute(val maxStack: Int,
                         val maxLocals: Int,
                         val code: ByteArray,
                         val exceptionTable: Array<Exception>,
                         val attributes: Array<AttributeInfo>) {
    companion object {
        @Throws(IOException::class)
        fun read(info: ByteArray): CodeAttribute {
            DataInputStream(ByteArrayInputStream(info)).use {
                return CodeAttribute.read(it)
            }
        }

        @Throws(IOException::class)
        private fun read(stream: DataInputStream): CodeAttribute {
            val maxStack = stream.readUnsignedShort()
            val maxLocals = stream.readUnsignedShort()
            val codeLength = stream.readInt()

            val code = ByteArray(codeLength)
            if (stream.read(code) != codeLength)
                throw IOException("could not read all bytes for code")

            val exceptionCount = stream.readUnsignedShort()
            val exceptionTable = Array(exceptionCount) {
                stream.readException()
            }
            val attributes = stream.readAttributes()

            return CodeAttribute(maxStack, maxLocals, code, exceptionTable, attributes)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CodeAttribute) return false

        if (maxStack != other.maxStack) return false
        if (maxLocals != other.maxLocals) return false
        if (!Arrays.equals(code, other.code)) return false
        if (!Arrays.equals(exceptionTable, other.exceptionTable)) return false
        if (!Arrays.equals(attributes, other.attributes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = maxStack
        result = 31 * result + maxLocals
        result = 31 * result + Arrays.hashCode(code)
        result = 31 * result + Arrays.hashCode(exceptionTable)
        result = 31 * result + Arrays.hashCode(attributes)
        return result
    }

    override fun toString(): String {
        return "CodeAttribute(maxStack=$maxStack, " +
                "maxLocals=$maxLocals, " +
                "code=${Arrays.toString(code)}, " +
                "exceptionTable=${Arrays.toString(exceptionTable)}, " +
                "attributes=${Arrays.toString(attributes)})"
    }
}
