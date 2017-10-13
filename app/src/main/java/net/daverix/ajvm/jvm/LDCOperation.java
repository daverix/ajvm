package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;
import net.daverix.ajvm.io.MethodHandleReference;
import net.daverix.ajvm.io.StringReference;

import java.io.IOException;
import java.util.Stack;

public class LDCOperation implements ByteCodeOperation {
    private final Object[] constantPool;

    public LDCOperation(Object[] constantPool) {
        this.constantPool = constantPool;
    }

    @Override
    public void execute(ByteCodeReader reader, int indexOfBytecode, Frame currentFrame) throws IOException {
        int ldcIndex = reader.readUnsignedByte();

        Object constant = constantPool[ldcIndex];
        if (constant instanceof Integer ||
                constant instanceof Float ||
                constant instanceof String ||
                constant instanceof MethodHandleReference) {
            currentFrame.push(constant);
        } else if (constant instanceof StringReference) {
            currentFrame.push(constantPool[((StringReference) constant).getIndex()]);
        }
    }
}
