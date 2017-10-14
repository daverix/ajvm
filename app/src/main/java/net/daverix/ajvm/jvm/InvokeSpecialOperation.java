package net.daverix.ajvm.jvm;


import net.daverix.ajvm.io.ByteCodeReader;
import net.daverix.ajvm.io.ConstantPool;
import net.daverix.ajvm.io.MethodReference;
import net.daverix.ajvm.io.NameAndTypeDescriptorReference;

import java.io.IOException;

import static net.daverix.ajvm.jvm.MethodUtils.getArgumentCount;

public class InvokeSpecialOperation implements ByteCodeOperation {
    private final ConstantPool constantPool;

    public InvokeSpecialOperation(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    @Override
    public void execute(ByteCodeReader reader, int indexOfBytecode, Frame currentFrame) throws IOException {
        //TODO: http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.invokespecial
        // need to call super methods etc properly
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

        Object instance = currentFrame.pop();
        if (instance instanceof VirtualObject) {
            Object result = ((VirtualObject) instance).invokeMethod(methodName, methodDescriptor, methodArgs);
            if (!methodDescriptor.endsWith("V")) {
                currentFrame.push(result);
            }
        } else if (instance instanceof String && methodName.equals("hashCode")) {
            currentFrame.push(instance.hashCode());
        } else if (instance instanceof String && methodName.equals("equals")) {
            currentFrame.push(instance.equals(methodArgs[0]) ? 1 : 0);
        } else if (instance instanceof Integer) {
            currentFrame.push(instance == methodArgs[0] ? 1 : 0);
        } else {
            throw new UnsupportedOperationException("don't know how to handle " + instance);
        }
    }
}
