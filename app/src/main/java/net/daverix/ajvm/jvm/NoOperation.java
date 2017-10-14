package net.daverix.ajvm.jvm;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public class NoOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader, int indexOfBytecode, Frame currentFrame) throws IOException {
        //no operation
    }
}
