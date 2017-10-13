package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;

public class IndexedAStoreOperation implements ByteCodeOperation {
    private final int index;

    public IndexedAStoreOperation(int index) {
        this.index = index;
    }

    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        Object localVariable = currentFrame.pop();
        currentFrame.setLocalVariable(index, localVariable);
    }
}
