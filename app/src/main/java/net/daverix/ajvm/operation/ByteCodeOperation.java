package net.daverix.ajvm.operation;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;

import java.io.IOException;

public interface ByteCodeOperation {
    void execute(ByteCodeReader reader,
                 int indexOfBytecode,
                 Frame currentFrame) throws IOException;
}
