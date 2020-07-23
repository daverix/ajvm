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
package net.daverix.ajvm

class ByteCodeReader(private val code: ByteArray) {
    var index = 0
        private set

    fun canReadByte(): Boolean {
        return canRead(1)
    }

    private fun canRead(amount: Int): Boolean {
        return index + amount < code.size
    }

    fun readUnsignedByte(): Int {
        if (!canReadByte())
            error("Cannot read byte, reached end of array")

        return code[index++].toInt() and 0xFF
    }

    fun readUnsignedShort(): Int {
        if (!canRead(2))
            error("Cannot read short, reached end of array")

        val first = code[index++].toInt()
        val second = code[index++].toInt()

        return (first shl 8) + second
    }

    fun readInt(): Int {
        if (!canRead(4))
            error("Cannot read int, reached end of array")

        return code[index++].toInt() shl 24 or
                (code[index++].toInt() shl 16) or
                (code[index++].toInt() shl 8) or
                code[index++].toInt()
    }

    fun readLong(): Long {
        if (!canRead(8))
            error("Cannot read long, reached end of array")

        return ((code[index++].toInt() and 0xFF).toLong() shl 56) +
                ((code[index++].toInt() and 0xFF).toLong() shl 48) +
                ((code[index++].toInt() and 0xFF).toLong() shl 40) +
                ((code[index++].toInt() and 0xFF).toLong() shl 32) +
                ((code[index++].toInt() and 0xFF).toLong() shl 24) +
                (code[index++].toInt() shl 16).toLong() +
                (code[index++].toInt() shl 8).toLong() +
                code[index++].toLong()
    }

    fun readFloat(): Float {
        if (!canRead(4))
            throw IllegalStateException("Cannot read float, reached end of array")

        return Float.fromBits(readInt())
    }

    fun readDouble(): Double {
        if (!canRead(8))
            throw IllegalStateException("Cannot read double, reached end of array")

        return Double.fromBits(readLong())
    }

    fun skip(count: Int) {
        if (index + count < 0 || index + count >= code.size)
            error("count $count would put index out of range")

        index += count
    }

    fun jumpTo(index: Int) {
        if (index < 0 || index >= code.size)
            error("$index is outside range")

        this.index = index
    }
}
