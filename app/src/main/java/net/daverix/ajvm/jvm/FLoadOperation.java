package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;

import java.io.IOException;
import java.util.Locale;
import java.util.Stack;

public class FLoadOperation implements ByteCodeOperation {
    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        int index = reader.readUnsignedByte();
        Object variable = currentFrame.getLocalVariable(index);
        if (!(variable instanceof Float))
            throw new IllegalStateException(String.format(Locale.ENGLISH,
                    "variable %s at index %d is not a Float",
                    variable, index));

        currentFrame.push(variable);
    }
}
