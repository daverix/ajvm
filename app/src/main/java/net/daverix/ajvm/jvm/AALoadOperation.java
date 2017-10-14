package net.daverix.ajvm.jvm;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class AALoadOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        int index = (int) currentFrame.pop();
        Object[] array = (Object[]) currentFrame.pop();
        currentFrame.push(array[index]);
    }
}
