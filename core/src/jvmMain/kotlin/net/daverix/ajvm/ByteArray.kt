package net.daverix.ajvm

import java.nio.ByteBuffer

actual fun ByteArray.wrapToLong(): Long = ByteBuffer.wrap(this).long
actual fun ByteArray.wrapToDouble(): Double = ByteBuffer.wrap(this).double
