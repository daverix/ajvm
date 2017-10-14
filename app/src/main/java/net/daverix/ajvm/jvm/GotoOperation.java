package net.daverix.ajvm.jvm;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class GotoOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        int gotoOffset = reader.readUnsignedShort();
        reader.jumpTo(indexOfBytecode + gotoOffset);
    }
}
