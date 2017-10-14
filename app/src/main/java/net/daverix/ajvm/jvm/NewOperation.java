package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;
import net.daverix.ajvm.io.ClassReference;
import net.daverix.ajvm.io.ConstantPool;
import net.daverix.ajvm.io.VirtualObjectLoader;

import java.io.IOException;

public class NewOperation implements ByteCodeOperation {
    private final VirtualObjectLoader loader;
    private final ConstantPool constantPool;

    public NewOperation(VirtualObjectLoader loader,
                        ConstantPool constantPool) {
        this.loader = loader;
        this.constantPool = constantPool;
    }

    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        int newObjectIndex = reader.readUnsignedShort();
        ClassReference classRef = (ClassReference) constantPool.get(newObjectIndex);
        String className = (String) constantPool.get(classRef.getNameIndex());

        VirtualObject virtualObject = loader.load(className);
        currentFrame.push(virtualObject);
    }
}
