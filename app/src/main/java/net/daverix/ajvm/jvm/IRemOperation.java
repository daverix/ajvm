package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;
import java.util.Stack;

public class IRemOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        int iAdd2 = (int) currentFrame.pop();
        int iAdd1 = (int) currentFrame.pop();
        currentFrame.push(iAdd1 % iAdd2);
    }
}
