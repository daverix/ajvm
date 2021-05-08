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

data class CodeAttribute(
        val maxStack: Int,
        val maxLocals: Int,
        val code: ByteArray,
        val exceptionTable: Array<Exception>,
        val attributes: Array<AttributeInfo>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CodeAttribute

        if (maxStack != other.maxStack) return false
        if (maxLocals != other.maxLocals) return false
        if (!code.contentEquals(other.code)) return false
        if (!exceptionTable.contentEquals(other.exceptionTable)) return false
        if (!attributes.contentEquals(other.attributes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = maxStack
        result = 31 * result + maxLocals
        result = 31 * result + code.contentHashCode()
        result = 31 * result + exceptionTable.contentHashCode()
        result = 31 * result + attributes.contentHashCode()
        return result
    }
}

fun DataInputStream.readCodeAttribute(): CodeAttribute {
    val maxStack = readUnsignedShort()
    val maxLocals = readUnsignedShort()
    val codeLength = readInt()

    val code = ByteArray(codeLength)
    if (read(code) != codeLength)
        error("could not read all bytes for code")

    val exceptionCount = readUnsignedShort()
    val exceptionTable = Array(exceptionCount) {
        Exception()
    }
    val attributes = readAttributes()

    return CodeAttribute(maxStack, maxLocals, code, exceptionTable, attributes)
}