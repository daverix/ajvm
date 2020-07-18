package net.daverix.ajvm.io

import java.io.DataInputStream

class JvmStreamReader(private val inputStream: DataInputStream) : StreamReader {
    override fun readUnsignedByte(): Int = inputStream.readUnsignedByte()

    override fun readUnsignedShort(): Int = inputStream.readUnsignedShort()

    override fun readInt(): Int = inputStream.readInt()

    override fun readUTF(): String = inputStream.readUTF()

    override fun readFloat(): Float = inputStream.readFloat()

    override fun read(bytes: ByteArray, start: Int, length: Int): Int = inputStream.read(bytes, start, length)
    override fun read(bytes: ByteArray): Int = inputStream.read(bytes)

    override fun readFully(info: ByteArray) = inputStream.readFully(info)
}