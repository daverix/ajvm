package net.daverix.ajvm.io

expect open class DataInputStream {
    fun readFully(bytes: ByteArray)
    fun readUnsignedByte(): Int
    fun readUnsignedShort(): Int
    fun readInt(): Int
    fun readUTF(): String
    fun readFloat(): Float
    fun read(bytes: ByteArray, offset: Int, length: Int): Int
    fun read(bytes: ByteArray): Int
    fun skipBytes(count: Int): Int
}

expect fun <T> ByteArray.useDataInputStream(func: (DataInputStream)->T): T
