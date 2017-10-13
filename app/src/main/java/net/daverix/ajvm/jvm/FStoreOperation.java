package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;
import java.util.Stack;

public class FStoreOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        int index = reader.readUnsignedByte();
        Object localVariable = currentFrame.pop();

        if(!(localVariable instanceof Float))
            throw new IllegalStateException("variable " + localVariable + " is not an Float");

        currentFrame.setLocalVariable(index, localVariable);
    }
}
