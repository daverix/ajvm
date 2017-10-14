package net.daverix.ajvm.operation


import net.daverix.ajvm.ByteCodeReader
import net.daverix.ajvm.Frame
import java.io.IOException

class AALoadOperation : ByteCodeOperation {
    @Throws(IOException::class)
    override fun execute(reader: ByteCodeReader,
                         indexOfBytecode: Int,
                         currentFrame: Frame) {
        val index = currentFrame.pop() as Int
        val array = currentFrame.pop() as Array<Any?>
        currentFrame.push(array[index])
    }
}
