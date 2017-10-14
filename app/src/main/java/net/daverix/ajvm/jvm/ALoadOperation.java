package net.daverix.ajvm.jvm;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class ALoadOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader, int indexOfBytecode, Frame currentFrame) throws IOException {
        int index = reader.readUnsignedByte();
        Object variable = currentFrame.getLocalVariable(index);
        currentFrame.push(variable);
    }
}
