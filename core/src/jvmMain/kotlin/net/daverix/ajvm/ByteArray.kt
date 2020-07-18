package net.daverix.ajvm

import net.daverix.ajvm.io.JvmStreamReader
import net.daverix.ajvm.io.StreamReader
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.nio.ByteBuffer

actual fun <T> ByteArray.useReader(func: (StreamReader) -> T): T =
        DataInputStream(ByteArrayInputStream(this)).use {
            func(JvmStreamReader(it))
        }

actual fun ByteArray.wrapToLong(): Long = ByteBuffer.wrap(this).long
actual fun ByteArray.wrapToDouble(): Double = ByteBuffer.wrap(this).double