package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;
import java.util.Stack;

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
