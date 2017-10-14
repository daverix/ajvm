package net.daverix.ajvm.jvm;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class IfEqOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader, int indexOfBytecode, Frame currentFrame) throws IOException {
        int ifEqOffset = reader.readUnsignedShort();
        int value = (int) currentFrame.pop();
        if (value == 0) {
            reader.jumpTo(indexOfBytecode + ifEqOffset);
        }
    }
}
