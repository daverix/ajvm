/*
    Java Virtual Machine for Android
    Copyright (C) 2017 David Laurell

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
 */
package net.daverix.ajvm.jvm;


import net.daverix.ajvm.Opcodes;
import net.daverix.ajvm.io.ByteCodeReader;
import net.daverix.ajvm.io.ClassInfo;
import net.daverix.ajvm.io.ClassReference;
import net.daverix.ajvm.io.CodeAttribute;
import net.daverix.ajvm.io.FieldReference;
import net.daverix.ajvm.io.MethodHandleReference;
import net.daverix.ajvm.io.MethodInfo;
import net.daverix.ajvm.io.MethodReference;
import net.daverix.ajvm.io.NameAndTypeDescriptorReference;
import net.daverix.ajvm.io.StringReference;
import net.daverix.ajvm.io.VirtualObjectLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.daverix.ajvm.io.CodeAttribute.fromMethod;

public class RuntimeVirtualObject implements VirtualObject {
    private static final Pattern METHOD_PARAMETER_COUNT_PATTERN = Pattern.compile("\\((B|C|D|F|I|J|L.+;|S|Z|\\[.+)*\\)B|C|D|F|I|J|L.+;|S|Z|\\[.+");
    private final Map<String, Object> fieldValues = new HashMap<>();
    private final Map<String, VirtualObject> staticClasses;
    private final VirtualObjectLoader loader;
    private final ClassInfo classInfo;
    private final Object[] constantPool;

    public RuntimeVirtualObject(Map<String, VirtualObject> staticClasses,
                                VirtualObjectLoader loader,
                                ClassInfo classInfo) {
        this.staticClasses = staticClasses;
        this.loader = loader;
        this.classInfo = classInfo;
        this.constantPool = classInfo.getConstantPool();
    }

    @Override
    public void initialize(Object[] args) {

    }

    @Override
    public String getName() {
        return (String) constantPool[classInfo.getClassIndex()];
    }

    @Override
    public void setFieldValue(String fieldName, Object value) {
        fieldValues.put(fieldName, value);
    }

    @Override
    public Object getFieldValue(String fieldName) {
        return fieldValues.get(fieldName);
    }

    @Override
    public Object invokeMethod(String name, String descriptor, Object[] args) throws IOException {
        MethodInfo method = getMethodByNameAndDescriptor(name, descriptor);
        if (method == null)
            throw new IllegalArgumentException("Cannot find method with name " + name + " and descriptor " + descriptor);

        CodeAttribute codeAttribute = fromMethod(method, constantPool);
        ByteCodeReader reader = new ByteCodeReader(codeAttribute.getCode());

        Stack<Frame> stackFrames = new Stack<>();
        Frame currentFrame = new Frame(
                codeAttribute.getMaxLocals(),
                codeAttribute.getMaxStack());
        for (int i = 0; i < args.length; i++) {
            currentFrame.setLocalVariable(i, args[i]);
        }

        stackFrames.push(currentFrame);

        while (reader.canReadByte()) {
            int indexOfBytecode = reader.getIndex();
            int byteCode = reader.readUnsignedByte();

            switch (byteCode) {
                case Opcodes.NEW:
                    int newObjectIndex = reader.readUnsignedShort();
                    ClassReference classRef = (ClassReference) constantPool[newObjectIndex];
                    String className = (String) constantPool[classRef.getNameIndex()];

                    VirtualObject virtualObject = loader.load(className);
                    currentFrame.push(virtualObject);
                    break;
                case Opcodes.DUP:
                    currentFrame.push(currentFrame.peek());
                    break;
                case Opcodes.LDC:
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
                    break;
                case Opcodes.GETSTATIC:
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
                    break;
                case Opcodes.INVOKEVIRTUAL:
                    invokeMethod(currentFrame, reader.readUnsignedShort());
                    break;
                case Opcodes.INVOKESPECIAL:
                    //TODO: http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.invokespecial
                    // need to call super methods etc properly
                    invokeMethod(currentFrame, reader.readUnsignedShort());
                    break;
                case Opcodes.INVOKESTATIC:
                    invokeStaticMethod(currentFrame, reader.readUnsignedShort());
                    break;
                case Opcodes.ICONST_M1:
                    currentFrame.push(-1);
                    break;
                case Opcodes.ICONST_0:
                    currentFrame.push(0);
                    break;
                case Opcodes.ICONST_1:
                    currentFrame.push(1);
                    break;
                case Opcodes.ICONST_2:
                    currentFrame.push(2);
                    break;
                case Opcodes.ICONST_3:
                    currentFrame.push(3);
                    break;
                case Opcodes.ICONST_4:
                    currentFrame.push(4);
                    break;
                case Opcodes.ICONST_5:
                    currentFrame.push(5);
                    break;
                case Opcodes.DCONST_0:
                    currentFrame.push(0d);
                    break;
                case Opcodes.DCONST_1:
                    currentFrame.push(1d);
                    break;
                case Opcodes.ISTORE_0:
                case Opcodes.LSTORE_0:
                case Opcodes.FSTORE_0:
                case Opcodes.DSTORE_0:
                case Opcodes.ASTORE_0:
                    currentFrame.setLocalVariable(0, currentFrame.pop());
                    break;
                case Opcodes.ISTORE_1:
                case Opcodes.LSTORE_1:
                case Opcodes.FSTORE_1:
                case Opcodes.DSTORE_1:
                case Opcodes.ASTORE_1:
                    currentFrame.setLocalVariable(1, currentFrame.pop());
                    break;
                case Opcodes.ISTORE_2:
                case Opcodes.LSTORE_2:
                case Opcodes.FSTORE_2:
                case Opcodes.DSTORE_2:
                case Opcodes.ASTORE_2:
                    currentFrame.setLocalVariable(2, currentFrame.pop());
                    break;
                case Opcodes.ISTORE_3:
                case Opcodes.LSTORE_3:
                case Opcodes.FSTORE_3:
                case Opcodes.DSTORE_3:
                case Opcodes.ASTORE_3:
                    currentFrame.setLocalVariable(3, currentFrame.pop());
                    break;
                case Opcodes.ISTORE:
                case Opcodes.LSTORE:
                case Opcodes.FSTORE:
                case Opcodes.DSTORE:
                case Opcodes.ASTORE:
                    currentFrame.setLocalVariable(reader.readUnsignedByte(), currentFrame.pop());
                    break;
                case Opcodes.ILOAD_0:
                case Opcodes.LLOAD_0:
                case Opcodes.FLOAD_0:
                case Opcodes.DLOAD_0:
                case Opcodes.ALOAD_0:
                    currentFrame.push(currentFrame.getLocalVariable(0));
                    break;
                case Opcodes.ILOAD_1:
                case Opcodes.LLOAD_1:
                case Opcodes.FLOAD_1:
                case Opcodes.DLOAD_1:
                case Opcodes.ALOAD_1:
                    currentFrame.push(currentFrame.getLocalVariable(1));
                    break;
                case Opcodes.ILOAD_2:
                case Opcodes.LLOAD_2:
                case Opcodes.FLOAD_2:
                case Opcodes.DLOAD_2:
                case Opcodes.ALOAD_2:
                    currentFrame.push(currentFrame.getLocalVariable(2));
                    break;
                case Opcodes.ILOAD_3:
                case Opcodes.LLOAD_3:
                case Opcodes.FLOAD_3:
                case Opcodes.DLOAD_3:
                case Opcodes.ALOAD_3:
                    currentFrame.push(currentFrame.getLocalVariable(3));
                    break;
                case Opcodes.ILOAD:
                case Opcodes.LLOAD:
                case Opcodes.FLOAD:
                case Opcodes.DLOAD:
                case Opcodes.ALOAD:
                    int loadIndex = reader.readUnsignedByte();
                    currentFrame.push(currentFrame.getLocalVariable(loadIndex));
                    break;
                case Opcodes.AALOAD:
                    int index = (int) currentFrame.pop();
                    Object[] array = (Object[]) currentFrame.pop();
                    currentFrame.push(array[index]);
                    break;
                case Opcodes.IADD:
                    iadd(currentFrame);
                    break;
                case Opcodes.ISUB:
                    isub(currentFrame);
                    break;
                case Opcodes.IMUL:
                    imul(currentFrame);
                    break;
                case Opcodes.IDIV:
                    idiv(currentFrame);
                    break;
                case Opcodes.IREM:
                    irem(currentFrame);
                    break;
                case Opcodes.TABLESWITCH:
                    reader.skip((indexOfBytecode + 1) % 4);
                    int defaultValue = reader.readInt();
                    int low = reader.readInt();
                    int high = reader.readInt();
                    if(low > high) {
                        throw new IllegalStateException(String.format(Locale.ENGLISH,
                                "low is higher than high: %d > %d", low, high));
                    }

                    int offsetWidth = high - low + 1;
                    int[] table = new int[offsetWidth];
                    for (int i = 0; i < offsetWidth; i++) {
                        table[i] = reader.readInt();
                    }
                    int tableIndex = (int) currentFrame.pop();
                    int targetAddress;
                    if ((tableIndex < low) || (tableIndex > high)) {
                        // TODO: why would Math.abs be needed to turn for example -120 into 120 here?
                        targetAddress = indexOfBytecode + Math.abs(defaultValue);
                    } else {
                        targetAddress = indexOfBytecode + table[tableIndex - low];
                    }
                    reader.jumpTo(targetAddress);
                    break;
                case Opcodes.IFEQ:
                    int ifEqOffset = reader.readUnsignedShort();
                    int value = (int) currentFrame.pop();
                    if (value == 0) {
                        reader.jumpTo(indexOfBytecode + ifEqOffset);
                    }
                    break;
                case Opcodes.GOTO:
                    int gotoOffset = reader.readUnsignedShort();
                    reader.jumpTo(indexOfBytecode + gotoOffset);
                    break;
                case Opcodes.RETURN:
                    currentFrame = stackFrames.pop();
                    break;
                case Opcodes.IRETURN:
                    return currentFrame.pop();
                case Opcodes.NOP:
                    // no operation
                    break;
                case Opcodes.L2I:
                    long longValue = (long) currentFrame.pop();
                    currentFrame.push((int) longValue);
                    break;
                default:
                    throw new IllegalStateException("Unknown bytecode: " + Integer.toHexString(byteCode));
            }
        }

        return null;
    }

    private void invokeStaticMethod(Frame currentFrame, int methodReferenceIndex) throws IOException {
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

    private void invokeMethod(Frame currentFrame, int methodReferenceIndex) throws IOException {
        MethodReference methodReference = (MethodReference) constantPool[methodReferenceIndex];
        NameAndTypeDescriptorReference nameAndType = (NameAndTypeDescriptorReference) constantPool[methodReference.getNameAndTypeIndex()];
        String methodName = (String) constantPool[nameAndType.getNameIndex()];
        String methodDescriptor = (String) constantPool[nameAndType.getDescriptorIndex()];
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

    private void irem(Frame currentFrame) {
        int iAdd2 = (int) currentFrame.pop();
        int iAdd1 = (int) currentFrame.pop();
        currentFrame.push(iAdd1 % iAdd2);
    }

    private static void iadd(Frame currentFrame) {
        int iAdd2 = (int) currentFrame.pop();
        int iAdd1 = (int) currentFrame.pop();
        currentFrame.push(iAdd1 + iAdd2);
    }

    private static void idiv(Frame currentFrame) {
        int iDiv2 = (int) currentFrame.pop();
        int iDiv1 = (int) currentFrame.pop();
        currentFrame.push(iDiv1 / iDiv2);
    }

    private static void isub(Frame currentFrame) {
        int iSub2 = (int) currentFrame.pop();
        int iSub1 = (int) currentFrame.pop();
        currentFrame.push(iSub1 - iSub2);
    }

    private static void imul(Frame currentFrame) {
        int iMul2 = (int) currentFrame.pop();
        int iMul1 = (int) currentFrame.pop();
        currentFrame.push(iMul1 * iMul2);
    }

    private static int getArgumentCount(String descriptor) {
        if (descriptor.startsWith("()"))
            return 0;

        Matcher matcher = METHOD_PARAMETER_COUNT_PATTERN.matcher(descriptor);
        return matcher.groupCount();
    }

    private MethodInfo getMethodByNameAndDescriptor(String methodName, String descriptor) {
        MethodInfo[] methods = classInfo.getMethods();
        for (MethodInfo method : methods) {
            String constantName = (String) constantPool[method.getNameIndex()];
            String constantDescriptor = (String) constantPool[method.getDescriptorIndex()];

            if (methodName.equals(constantName) && descriptor.equals(constantDescriptor)) {
                return method;
            }
        }

        throw new NoSuchMethodError("Cannot find method " + methodName + " in " + getName());
    }
}
