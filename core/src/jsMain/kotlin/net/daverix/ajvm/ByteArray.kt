package net.daverix.ajvm

import org.khronos.webgl.*

actual fun ByteArray.wrapToLong(): Long {
    val buffer = ArrayBuffer(8)
    val byteArray = Int8Array(buffer)
    val int32Array = Int32Array(buffer)
    for(i in 0 until 8) {
        byteArray[i] = this[i]
    }
    return (int32Array[1].toLong() shl 32) or int32Array[0].toLong()
}

actual fun ByteArray.wrapToDouble(): Double {
    val buffer = ArrayBuffer(8)
    val byteArray = Int8Array(buffer)
    val float64Array = Float64Array(buffer)
    for(i in 0 until 8) {
        byteArray[i] = this[i]
    }
    return float64Array[0]
}
