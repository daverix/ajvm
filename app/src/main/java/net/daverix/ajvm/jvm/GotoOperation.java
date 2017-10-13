package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;
import java.util.Stack;

public class GotoOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        int gotoOffset = reader.readUnsignedShort();
        reader.jumpTo(indexOfBytecode + gotoOffset);
    }
}
