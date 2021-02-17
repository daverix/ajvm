package net.daverix.ajvm.io

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.DataView
import org.khronos.webgl.Int8Array

actual open class DataInputStream(private val dataView: DataView) {
    private var index: Int = 0

    actual fun readUnsignedByte(): Int {
        if (index >= dataView.buffer.byteLength)
            return -1

        return dataView.getUint8(index++).toInt()
    }

    actual fun readUnsignedShort(): Int {
        val value = dataView.getUint16(index, littleEndian = false).toInt()
        index += 2
        return value
    }

    actual fun readInt(): Int {
        val value = dataView.getInt32(index, littleEndian = false)
        index += 4
        return value
    }

    actual fun readUTF(): String {
        val utf8Length: Int = readUnsignedShort()
        val byteArray = ByteArray(utf8Length)
        readFully(byteArray)
        return byteArray.decodeToString(throwOnInvalidSequence = true)
    }

    actual fun readFloat(): Float {
        val value = dataView.getFloat32(index, littleEndian = false)
        index += 4
        return value
    }

    actual fun read(bytes: ByteArray, offset: Int, length: Int): Int {
        var bytesRead = 0
        while (bytesRead < length && bytesRead < bytes.size) {
            bytes[offset + bytesRead] = dataView.getUint8(index + bytesRead)
            bytesRead++
        }
        index += bytesRead
        return bytesRead
    }

    actual fun read(bytes: ByteArray): Int {
        return read(bytes, 0, bytes.size)
    }

    actual fun readFully(bytes: ByteArray) {
        val count: Int = read(bytes)
        if (count < 0) {
            error("end of stream")
        }
    }

    actual fun skipBytes(count: Int): Int {
        var newIndex = index + count
        if(newIndex >= dataView.buffer.byteLength)
            newIndex = dataView.buffer.byteLength-1

        val skipped = newIndex - index
        index = newIndex
        return skipped
    }
}

actual fun <T> ByteArray.useDataInputStream(func: (DataInputStream) -> T): T {
    val buffer = ArrayBuffer(size)
    val array = Int8Array(buffer)
    array.set(toTypedArray(), 0)

    val dataView = DataView(buffer, 0, size)
    return func(DataInputStream(dataView))
}