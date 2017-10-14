package net.daverix.ajvm.operation;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class IndexedLStoreOperation implements ByteCodeOperation {
    private final int index;

    public IndexedLStoreOperation(int index) {
        this.index = index;
    }

    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        Object localVariable = currentFrame.pop();
        if(!(localVariable instanceof Long))
            throw new IllegalStateException("variable " + localVariable + " is not a Long");

        currentFrame.setLocalVariable(index, localVariable);
    }
}
