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

    public RuntimeVirtualObject(Map<String, VirtualObject> staticClasses,
                                VirtualObjectLoader loader,
                                ClassInfo classInfo) {
        this.staticClasses = staticClasses;
        this.loader = loader;
        this.classInfo = classInfo;
    }

    @Override
    public String getName() {
        return (String) classInfo.getConstantPool()[classInfo.getClassIndex()];
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
        if(method == null)
            throw new IllegalArgumentException("Cannot find method with name " + name + " and descriptor " + descriptor);

        CodeAttribute codeAttribute = fromMethod(method, classInfo.getConstantPool());
        ByteCodeReader reader = new ByteCodeReader(codeAttribute.getCode());

        Stack<Frame> stackFrames = new Stack<>();
        Frame currentFrame = new Frame(classInfo.getConstantPool(),
                codeAttribute.getMaxLocals(),
                codeAttribute.getMaxStack());
        stackFrames.push(currentFrame);

        while (reader.canReadByte()) {
            int byteCode = reader.readUnsignedByte();

            switch (byteCode) {
                case Opcodes.LDC:
                    int ldcIndex = reader.readUnsignedByte();

                    Object constant = currentFrame.getConstant(ldcIndex);
                    if (constant instanceof Integer ||
                            constant instanceof Float ||
                            constant instanceof String ||
                            constant instanceof MethodHandleReference) {
                        currentFrame.push(constant);
                    } else if (constant instanceof StringReference) {
                        currentFrame.push(classInfo.getConstantPool()[((StringReference) constant).getIndex()]);
                    }
                    break;
                case Opcodes.GETSTATIC:
                    int staticFieldIndex = reader.readUnsignedShort();

                    FieldReference fieldReference = (FieldReference) currentFrame.getConstant(staticFieldIndex);
                    NameAndTypeDescriptorReference fieldNameAndType = (NameAndTypeDescriptorReference) currentFrame.getConstant(fieldReference.getNameAndTypeIndex());
                    String fieldName = (String) currentFrame.getConstant(fieldNameAndType.getNameIndex());

                    ClassReference classReference = (ClassReference) currentFrame.getConstant(fieldReference.getClassIndex());
                    String fieldClassName = (String) currentFrame.getConstant(classReference.getNameIndex());

                    VirtualObject staticClass = staticClasses.get(fieldClassName);
                    if(staticClass == null) {
                        staticClass = loader.load(fieldClassName);
                        staticClasses.put(fieldClassName, staticClass);
                    }

                    currentFrame.push(staticClass.getFieldValue(fieldName));
                    break;
                case Opcodes.INVOKEVIRTUAL:
                    int invokeVirtualIndex = reader.readUnsignedShort();

                    MethodReference methodReference = (MethodReference) currentFrame.getConstant(invokeVirtualIndex);
                    NameAndTypeDescriptorReference nameAndType = (NameAndTypeDescriptorReference) currentFrame.getConstant(methodReference.getNameAndTypeIndex());
                    String methodName = (String) currentFrame.getConstant(nameAndType.getNameIndex());
                    String methodDescriptor = (String) currentFrame.getConstant(nameAndType.getDescriptorIndex());
                    int argumentCount = getArgumentCount(methodDescriptor);

                    Object[] methodArgs = new Object[argumentCount];
                    for (int i = 0; i < argumentCount; i++) {
                        methodArgs[i] = currentFrame.pop();
                    }

                    Object instance = currentFrame.pop();
                    if(instance instanceof VirtualObject) {
                        ((VirtualObject) instance).invokeMethod(methodName, methodDescriptor, methodArgs);
                    } else {
                        throw new UnsupportedOperationException("don't know how to handle " + instance);
                    }

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
                case Opcodes.ILOAD_0:
                case Opcodes.LLOAD_0:
                case Opcodes.FLOAD_0:
                case Opcodes.DLOAD_0:
                    currentFrame.push(currentFrame.getLocalVariable(0));
                    break;
                case Opcodes.ILOAD_1:
                case Opcodes.LLOAD_1:
                case Opcodes.FLOAD_1:
                case Opcodes.DLOAD_1:
                    currentFrame.push(currentFrame.getLocalVariable(1));
                    break;
                case Opcodes.ILOAD_2:
                case Opcodes.LLOAD_2:
                case Opcodes.FLOAD_2:
                case Opcodes.DLOAD_2:
                    currentFrame.push(currentFrame.getLocalVariable(2));
                    break;
                case Opcodes.ILOAD_3:
                case Opcodes.LLOAD_3:
                case Opcodes.FLOAD_3:
                case Opcodes.DLOAD_3:
                    currentFrame.push(currentFrame.getLocalVariable(3));
                    break;
                case Opcodes.ILOAD:
                case Opcodes.LLOAD:
                case Opcodes.FLOAD:
                case Opcodes.DLOAD:
                    int loadIndex = reader.readUnsignedByte();
                    currentFrame.push(currentFrame.getLocalVariable(loadIndex));
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
                case Opcodes.RETURN:
                    currentFrame = stackFrames.pop();
                    break;
                case Opcodes.IRETURN:
                    //return currentFrame.pop();
                    break;
                default:
                    throw new IllegalStateException("Unknown bytecode: " + Integer.toHexString(byteCode));
            }
        }

        return null;
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
        Matcher matcher = METHOD_PARAMETER_COUNT_PATTERN.matcher(descriptor);
        return matcher.groupCount();
    }

    private MethodInfo getMethodByNameAndDescriptor(String methodName, String descriptor) {
        Object[] constantPool = classInfo.getConstantPool();

        MethodInfo[] methods = classInfo.getMethods();
        for (MethodInfo method : methods) {
            String constantName = (String) constantPool[method.getNameIndex()];
            String constantDescriptor = (String) constantPool[method.getDescriptorIndex()];

            if(methodName.equals(constantName) && descriptor.equals(constantDescriptor)) {
                return method;
            }
        }

        return null;
    }
}
