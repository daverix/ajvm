package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;
import java.util.Stack;

public class IndexedFStoreOperation implements ByteCodeOperation {
    private final int index;

    public IndexedFStoreOperation(int index) {
        this.index = index;
    }

    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        Object localVariable = currentFrame.pop();
        if(!(localVariable instanceof Float))
            throw new IllegalStateException("variable " + localVariable + " is not a Float");

        currentFrame.setLocalVariable(index, localVariable);
    }
}
