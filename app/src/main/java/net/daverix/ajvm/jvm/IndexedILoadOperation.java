package net.daverix.ajvm.jvm;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class IndexedILoadOperation implements ByteCodeOperation {
    private final int index;

    public IndexedILoadOperation(int index) {
        this.index = index;
    }

    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        Object variable = currentFrame.getLocalVariable(index);
        if (!(variable instanceof Integer))
            throw new IllegalStateException("variable " + variable + " is not an Integer");

        currentFrame.push(variable);
    }
}
