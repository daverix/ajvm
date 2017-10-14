package net.daverix.ajvm.jvm;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.ByteCodeReader;
import net.daverix.ajvm.io.ConstantPool;
import net.daverix.ajvm.io.MethodHandleReference;
import net.daverix.ajvm.io.StringReference;

import java.io.IOException;

public class LDCOperation implements ByteCodeOperation {
    private final ConstantPool constantPool;

    public LDCOperation(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    @Override
    public void execute(ByteCodeReader reader, int indexOfBytecode, Frame currentFrame) throws IOException {
        int ldcIndex = reader.readUnsignedByte();

        Object constant = constantPool.get(ldcIndex);
        if (constant instanceof Integer ||
                constant instanceof Float ||
                constant instanceof String ||
                constant instanceof MethodHandleReference) {
            currentFrame.push(constant);
        } else if (constant instanceof StringReference) {
            currentFrame.push(constantPool.get(((StringReference) constant).getIndex()));
        }
    }
}
