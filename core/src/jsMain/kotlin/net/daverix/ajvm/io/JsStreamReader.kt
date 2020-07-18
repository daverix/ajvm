package net.daverix.ajvm.io

import org.khronos.webgl.*

class JsStreamReader(private val buffer: ArrayBuffer): StreamReader {
    private var index: Int = 0

    override fun readUnsignedByte(): Int {
        val array = Uint8Array(buffer)
        val value = array[index]
        index++
        return value.toInt()
    }

    override fun readUnsignedShort(): Int {
        val array = Uint16Array(buffer)
        val value = array[index]
        index++
        return value.toInt()
    }

    override fun readInt(): Int {
        val array = Uint16Array(buffer)
        val value = array[index]
        index++
        return value.toInt()
    }

    override fun readUTF(): String {
        TODO("Not yet implemented")
    }

    override fun readFloat(): Float {
        val array = Float32Array(buffer)
        val value = array[index]
        index++
        return value
    }

    override fun read(bytes: ByteArray, start: Int, length: Int): Int {
        val array = Int8Array(buffer)
        var readIndex = 0
        for(i in start until length) {
            if(readIndex >= array.length) break

            bytes[readIndex] = array[i]
            readIndex++
        }
        return readIndex
    }

    override fun read(bytes: ByteArray): Int {
        val array = Int8Array(buffer)
        var bytesRead = 0
        for(i in 0 until array.length) {
            if(i >= bytes.size) break

            bytes[i] = array[i]
            bytesRead++
        }
        return bytesRead
    }

    override fun readFully(bytes: ByteArray) {
        val array = Int8Array(buffer)
        for(i in 0 until array.length) {
            if(i >= bytes.size) break

            bytes[i] = array[i]
        }
    }
}