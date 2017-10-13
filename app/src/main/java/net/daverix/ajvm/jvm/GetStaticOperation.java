package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;
import net.daverix.ajvm.io.ClassReference;
import net.daverix.ajvm.io.FieldReference;
import net.daverix.ajvm.io.NameAndTypeDescriptorReference;
import net.daverix.ajvm.io.VirtualObjectLoader;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

public class GetStaticOperation implements ByteCodeOperation {
    private final Map<String, VirtualObject> staticClasses;
    private final VirtualObjectLoader loader;
    private final Object[] constantPool;

    public GetStaticOperation(Map<String, VirtualObject> staticClasses,
                              VirtualObjectLoader loader,
                              Object[] constantPool) {
        this.staticClasses = staticClasses;
        this.loader = loader;
        this.constantPool = constantPool;
    }

    @Override
    public void execute(ByteCodeReader reader, int indexOfBytecode, Frame currentFrame) throws IOException {
        int staticFieldIndex = reader.readUnsignedShort();

        FieldReference fieldReference = (FieldReference) constantPool[staticFieldIndex];
        NameAndTypeDescriptorReference fieldNameAndType = (NameAndTypeDescriptorReference) constantPool[fieldReference.getNameAndTypeIndex()];
        String fieldName = (String) constantPool[fieldNameAndType.getNameIndex()];

        ClassReference classReference = (ClassReference) constantPool[fieldReference.getClassIndex()];
        String fieldClassName = (String) constantPool[classReference.getNameIndex()];

        VirtualObject staticClass = staticClasses.get(fieldClassName);
        if (staticClass == null) {
            staticClass = loader.load(fieldClassName);
            staticClasses.put(fieldClassName, staticClass);
        }

        currentFrame.push(staticClass.getFieldValue(fieldName));
    }
}
