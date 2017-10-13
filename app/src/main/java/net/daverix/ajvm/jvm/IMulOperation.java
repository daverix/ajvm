package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;
import java.util.Stack;

public class IMulOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader, int indexOfBytecode, Frame currentFrame) throws IOException {
        int iMul2 = (int) currentFrame.pop();
        int iMul1 = (int) currentFrame.pop();
        currentFrame.push(iMul1 * iMul2);
    }
}
