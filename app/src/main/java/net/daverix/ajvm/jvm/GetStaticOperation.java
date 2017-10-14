package net.daverix.ajvm.jvm;


import net.daverix.ajvm.Frame;
import net.daverix.ajvm.VirtualObject;
import net.daverix.ajvm.ByteCodeReader;
import net.daverix.ajvm.io.ClassReference;
import net.daverix.ajvm.io.ConstantPool;
import net.daverix.ajvm.io.FieldReference;
import net.daverix.ajvm.io.NameAndTypeDescriptorReference;
import net.daverix.ajvm.VirtualObjectLoader;

import java.io.IOException;
import java.util.Map;

public class GetStaticOperation implements ByteCodeOperation {
    private final Map<String, VirtualObject> staticClasses;
    private final VirtualObjectLoader loader;
    private final ConstantPool constantPool;

    public GetStaticOperation(Map<String, VirtualObject> staticClasses,
                              VirtualObjectLoader loader,
                              ConstantPool constantPool) {
        this.staticClasses = staticClasses;
        this.loader = loader;
        this.constantPool = constantPool;
    }

    @Override
    public void execute(ByteCodeReader reader, int indexOfBytecode, Frame currentFrame) throws IOException {
        int staticFieldIndex = reader.readUnsignedShort();

        FieldReference fieldReference = (FieldReference) constantPool.get(staticFieldIndex);
        NameAndTypeDescriptorReference fieldNameAndType = (NameAndTypeDescriptorReference) constantPool.get(fieldReference.getNameAndTypeIndex());
        String fieldName = (String) constantPool.get(fieldNameAndType.getNameIndex());

        ClassReference classReference = (ClassReference) constantPool.get(fieldReference.getClassIndex());
        String fieldClassName = (String) constantPool.get(classReference.getNameIndex());

        VirtualObject staticClass = staticClasses.get(fieldClassName);
        if (staticClass == null) {
            staticClass = loader.load(fieldClassName);
            staticClasses.put(fieldClassName, staticClass);
        }

        currentFrame.push(staticClass.getFieldValue(fieldName));
    }
}
