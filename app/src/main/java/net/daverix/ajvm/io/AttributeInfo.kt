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
import java.util.*

data class AttributeInfo(val nameIndex: Int,
                         val info: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AttributeInfo) return false

        if (nameIndex != other.nameIndex) return false
        if (!Arrays.equals(info, other.info)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nameIndex
        result = 31 * result + Arrays.hashCode(info)
        return result
    }
}

@Throws(IOException::class)
private fun DataInputStream.readAttribute(): AttributeInfo {
    val nameIndex = readUnsignedShort()
    val attributeLength = readInt()
    val info = ByteArray(attributeLength)
    readFully(info)
    return AttributeInfo(nameIndex, info)
}

@Throws(IOException::class)
fun DataInputStream.readAttributes(): Array<AttributeInfo> {
    return Array(readUnsignedShort()) { readAttribute() }
}
