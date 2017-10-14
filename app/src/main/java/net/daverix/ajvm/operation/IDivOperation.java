package net.daverix.ajvm.operation;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class IDivOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        int iDiv2 = (int) currentFrame.pop();
        int iDiv1 = (int) currentFrame.pop();
        currentFrame.push(iDiv1 / iDiv2);
    }
}
