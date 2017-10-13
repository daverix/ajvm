package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;
import net.daverix.ajvm.io.ClassReference;
import net.daverix.ajvm.io.MethodReference;
import net.daverix.ajvm.io.NameAndTypeDescriptorReference;
import net.daverix.ajvm.io.VirtualObjectLoader;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

import static net.daverix.ajvm.jvm.MethodUtils.getArgumentCount;

public class InvokeStaticOperation implements ByteCodeOperation {
    private final Map<String, VirtualObject> staticClasses;
    private final VirtualObjectLoader loader;
    private final Object[] constantPool;

    public InvokeStaticOperation(Map<String, VirtualObject> staticClasses,
                                 VirtualObjectLoader loader,
                                 Object[] constantPool) {
        this.staticClasses = staticClasses;
        this.loader = loader;
        this.constantPool = constantPool;
    }

    @Override
    public void execute(ByteCodeReader reader, int indexOfBytecode, Frame currentFrame) throws IOException {
        int methodReferenceIndex = reader.readUnsignedShort();
        MethodReference methodReference = (MethodReference) constantPool[methodReferenceIndex];
        NameAndTypeDescriptorReference nameAndType = (NameAndTypeDescriptorReference) constantPool[methodReference.getNameAndTypeIndex()];
        String methodName = (String) constantPool[nameAndType.getNameIndex()];
        String methodDescriptor = (String) constantPool[nameAndType.getDescriptorIndex()];
        int argumentCount = getArgumentCount(methodDescriptor);

        Object[] methodArgs = new Object[argumentCount];
        for (int i = argumentCount - 1; i >= 0; i--) {
            methodArgs[i] = currentFrame.pop();
        }

        ClassReference classReference = (ClassReference) constantPool[methodReference.getClassIndex()];
        String className = (String) constantPool[classReference.getNameIndex()];
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
