package net.daverix.ajvm.io

interface StreamReader {
    fun readUnsignedByte(): Int
    fun readUnsignedShort(): Int
    fun readInt(): Int
    fun readUTF(): String
    fun readFloat(): Float
    fun read(bytes: ByteArray, start: Int, length: Int): Int
    fun read(bytes: ByteArray): Int
    fun readFully(info: ByteArray)
}
