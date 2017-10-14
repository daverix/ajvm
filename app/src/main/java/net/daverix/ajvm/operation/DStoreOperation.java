package net.daverix.ajvm.operation;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class DStoreOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        int index = reader.readUnsignedByte();
        Object localVariable = currentFrame.pop();

        if(!(localVariable instanceof Double))
            throw new IllegalStateException("variable " + localVariable + " is not an Double");

        currentFrame.setLocalVariable(index, localVariable);
    }
}
