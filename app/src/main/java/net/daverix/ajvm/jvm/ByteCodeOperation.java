package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;

public interface ByteCodeOperation {
    void execute(ByteCodeReader reader,
                 int indexOfBytecode,
                 Frame currentFrame) throws IOException;
}
