package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;

public class IStoreOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        int index = reader.readUnsignedByte();
        Object localVariable = currentFrame.pop();

        if(!(localVariable instanceof Integer))
            throw new IllegalStateException("variable " + localVariable + " is not an Long");

        currentFrame.setLocalVariable(index, localVariable);
    }
}
