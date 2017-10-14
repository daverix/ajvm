package net.daverix.ajvm.operation


import net.daverix.ajvm.ByteCodeReader
import net.daverix.ajvm.Frame
import java.io.IOException
import java.util.*

class TableSwitchOperation : ByteCodeOperation {
    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        reader.skip((indexOfBytecode + 1) % 4)
        val defaultValue = reader.readInt()
        val low = reader.readInt()
        val high = reader.readInt()
        if (low > high) {
            throw IllegalStateException(String.format(Locale.ENGLISH,
                    "low is higher than high: %d > %d", low, high))
        }

        val offsetWidth = high - low + 1
        val table = IntArray(offsetWidth)
        for (i in 0 until offsetWidth) {
            table[i] = reader.readInt()
        }
        val tableIndex = currentFrame.pop() as Int
        val targetAddress: Int
        if (tableIndex < low || tableIndex > high) {
            // TODO: why would Math.abs be needed to turn for example -120 into 120 here?
            targetAddress = indexOfBytecode + Math.abs(defaultValue)
        } else {
            targetAddress = indexOfBytecode + table[tableIndex - low]
        }
        reader.jumpTo(targetAddress)
    }
}
