package net.daverix.ajvm.operation;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

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
