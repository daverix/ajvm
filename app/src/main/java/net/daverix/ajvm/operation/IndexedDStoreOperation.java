package net.daverix.ajvm.operation;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class IndexedDStoreOperation implements ByteCodeOperation {
    private final int index;

    public IndexedDStoreOperation(int index) {
        this.index = index;
    }

    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        Object localVariable = currentFrame.pop();
        if(!(localVariable instanceof Double))
            throw new IllegalStateException("variable " + localVariable + " is not a Double");

        currentFrame.setLocalVariable(index, localVariable);
    }
}
