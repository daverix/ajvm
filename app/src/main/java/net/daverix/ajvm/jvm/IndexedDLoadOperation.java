package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;
import java.util.Stack;

public class IndexedDLoadOperation implements ByteCodeOperation {
    private final int index;

    public IndexedDLoadOperation(int index) {
        this.index = index;
    }

    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        Object variable = currentFrame.getLocalVariable(index);
        if (!(variable instanceof Double))
            throw new IllegalStateException("variable " + variable + " is not a Double");
        
        currentFrame.push(variable);
    }
}
