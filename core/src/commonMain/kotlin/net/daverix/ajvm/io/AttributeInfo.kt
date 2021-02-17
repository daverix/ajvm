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

data class AttributeInfo(val nameIndex: Int, val info: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AttributeInfo

        if (nameIndex != other.nameIndex) return false
        if (!info.contentEquals(other.info)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nameIndex
        result = 31 * result + info.contentHashCode()
        return result
    }
}

fun DataInputStream.readAttributes(): List<AttributeInfo> {
    return List(readUnsignedShort()) {
        val nameIndex = readUnsignedShort()
        val info = ByteArray(readInt())
        readFully(info)
        AttributeInfo(nameIndex, info)
    }
}