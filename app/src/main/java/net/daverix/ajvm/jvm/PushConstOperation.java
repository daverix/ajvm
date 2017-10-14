package net.daverix.ajvm.jvm;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class PushConstOperation<T> implements ByteCodeOperation {
    private final T value;

    public PushConstOperation(T value) {
        this.value = value;
    }

    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        currentFrame.push(value);
    }
}
