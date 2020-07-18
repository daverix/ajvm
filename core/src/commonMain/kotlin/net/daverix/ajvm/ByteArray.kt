package net.daverix.ajvm

import net.daverix.ajvm.io.StreamReader

expect fun <T> ByteArray.useReader(func: (StreamReader)->T): T

expect fun ByteArray.wrapToLong(): Long
expect fun ByteArray.wrapToDouble(): Double
