package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;
import java.util.Stack;

public class ISubOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        int iSub2 = (int) currentFrame.pop();
        int iSub1 = (int) currentFrame.pop();
        currentFrame.push(iSub1 - iSub2);
    }
}
