package net.daverix.ajvm.jvm;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class IAddOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader, int indexOfBytecode, Frame currentFrame) throws IOException {
        int iAdd2 = (int) currentFrame.pop();
        int iAdd1 = (int) currentFrame.pop();
        currentFrame.push(iAdd1 + iAdd2);
    }
}
