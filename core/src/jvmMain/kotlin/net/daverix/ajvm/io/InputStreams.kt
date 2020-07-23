package net.daverix.ajvm.io

actual typealias DataInputStream = java.io.DataInputStream

actual fun <T> ByteArray.useDataInputStream(func: (DataInputStream) -> T): T = inputStream().use {
    func(DataInputStream(it))
}