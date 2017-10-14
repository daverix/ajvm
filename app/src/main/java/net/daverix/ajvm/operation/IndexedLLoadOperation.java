package net.daverix.ajvm.operation;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class IndexedLLoadOperation implements ByteCodeOperation {
    private final int index;

    public IndexedLLoadOperation(int index) {
        this.index = index;
    }

    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        Object variable = currentFrame.getLocalVariable(index);
        if (!(variable instanceof Long))
            throw new IllegalStateException("variable " + variable + " is not a Long");
        
        currentFrame.push(variable);
    }
}
