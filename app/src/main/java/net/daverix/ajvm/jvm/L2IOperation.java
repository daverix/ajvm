package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;

public class L2IOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        long longValue = (long) currentFrame.pop();
        currentFrame.push((int) longValue);
    }
}