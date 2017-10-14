package net.daverix.ajvm.jvm;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class IndexedALoadOperation implements ByteCodeOperation {
    private final int index;

    public IndexedALoadOperation(int index) {
        this.index = index;
    }

    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        Object variable = currentFrame.getLocalVariable(index);
        currentFrame.push(variable);
    }
}
