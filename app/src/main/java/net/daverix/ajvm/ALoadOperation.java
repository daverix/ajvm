package net.daverix.ajvm;


import net.daverix.ajvm.io.ByteCodeReader;
import net.daverix.ajvm.jvm.ByteCodeOperation;
import net.daverix.ajvm.jvm.Frame;

import java.io.IOException;

class ALoadOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader, int indexOfBytecode, Frame currentFrame) throws IOException {
        int index = reader.readUnsignedByte();
        Object variable = currentFrame.getLocalVariable(index);
        currentFrame.push(variable);
    }
}
