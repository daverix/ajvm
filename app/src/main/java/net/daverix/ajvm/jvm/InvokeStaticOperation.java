package net.daverix.ajvm.jvm;


import net.daverix.ajvm.ByteCodeReader;
import net.daverix.ajvm.Frame;
import net.daverix.ajvm.VirtualObject;
import net.daverix.ajvm.VirtualObjectLoader;
import net.daverix.ajvm.io.ClassReference;
import net.daverix.ajvm.io.ConstantPool;
import net.daverix.ajvm.io.MethodReference;
import net.daverix.ajvm.io.NameAndTypeDescriptorReference;

import java.io.IOException;
import java.util.Map;

import static net.daverix.ajvm.MethodUtilsKt.getArgumentCount;

public class InvokeStaticOperation implements ByteCodeOperation {
    private final Map<String, VirtualObject> staticClasses;
    private final VirtualObjectLoader loader;
    private final ConstantPool constantPool;

    public InvokeStaticOperation(Map<String, VirtualObject> staticClasses,
                                 VirtualObjectLoader loader,
                                 ConstantPool constantPool) {
        this.staticClasses = staticClasses;
        this.loader = loader;
        this.constantPool = constantPool;
    }

    @Override
    public void execute(ByteCodeReader reader,
                        int indexOfBytecode,
                        Frame currentFrame) throws IOException {
        int methodReferenceIndex = reader.readUnsignedShort();
        MethodReference methodReference = (MethodReference) constantPool.get(methodReferenceIndex);
        NameAndTypeDescriptorReference nameAndType = (NameAndTypeDescriptorReference) constantPool.get(methodReference.getNameAndTypeIndex());
        String methodName = (String) constantPool.get(nameAndType.getNameIndex());
        String methodDescriptor = (String) constantPool.get(nameAndType.getDescriptorIndex());
        int argumentCount = getArgumentCount(methodDescriptor);

        Object[] methodArgs = new Object[argumentCount];
        for (int i = argumentCount - 1; i >= 0; i--) {
            methodArgs[i] = currentFrame.pop();
        }

        ClassReference classReference = (ClassReference) constantPool.get(methodReference.getClassIndex());
        String className = (String) constantPool.get(classReference.getNameIndex());
        VirtualObject staticClass = staticClasses.get(className);
        if (staticClass == null) {
            staticClass = loader.load(className);
            staticClasses.put(className, staticClass);
        }

        Object result = staticClass.invokeMethod(methodName, methodDescriptor, methodArgs);
        if (!methodDescriptor.endsWith("V")) {
            currentFrame.push(result);
        }
    }
}
