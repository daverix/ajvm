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
import net.daverix.ajvm.io.CodeAttribute;
import net.daverix.ajvm.io.ConstantPool;
import net.daverix.ajvm.io.MethodInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RuntimeVirtualObject implements VirtualObject {

    private final Map<String, Object> fieldValues = new HashMap<>();
    private final ClassInfo classInfo;
    private final ConstantPool constantPool;
    private final Map<Integer, ByteCodeOperation> byteCodeOperations;

    public RuntimeVirtualObject(ClassInfo classInfo, Map<Integer, ByteCodeOperation> byteCodeOperations) {
        this.classInfo = classInfo;
        this.constantPool = classInfo.getConstantPool();
        this.byteCodeOperations = byteCodeOperations;
    }

    @Override
    public void initialize(Object[] args) {

    }

    @Override
    public String getName() {
        return (String) constantPool.get(classInfo.getClassIndex());
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

        CodeAttribute codeAttribute = method.getCodeAttribute();
        ByteCodeReader reader = new ByteCodeReader(codeAttribute.getCode());

        Frame currentFrame = new Frame(
                codeAttribute.getMaxLocals(),
                codeAttribute.getMaxStack());
        for (int i = 0; i < args.length; i++) {
            currentFrame.setLocalVariable(i, args[i]);
        }

        while (reader.canReadByte()) {
            int indexOfBytecode = reader.getIndex();
            int byteCode = reader.readUnsignedByte();
            if(byteCode == Opcodes.RETURN)
                return null;

            if(byteCode == Opcodes.IRETURN)
                return currentFrame.pop();

            ByteCodeOperation operation = byteCodeOperations.get(byteCode);
            if(operation == null) {
                throw new IllegalStateException("Unknown bytecode: " + Integer.toHexString(byteCode) + " at position " + indexOfBytecode);
            }

            operation.execute(reader, indexOfBytecode, currentFrame);
        }

        return null;
    }

    private MethodInfo getMethodByNameAndDescriptor(String methodName, String descriptor) {
        MethodInfo[] methods = classInfo.getMethods();
        for (MethodInfo method : methods) {
            String constantName = (String) constantPool.get(method.getNameIndex());
            String constantDescriptor = (String) constantPool.get(method.getDescriptorIndex());

            if (methodName.equals(constantName) && descriptor.equals(constantDescriptor)) {
                return method;
            }
        }

        throw new NoSuchMethodError("Cannot find method " + methodName + " in " + getName());
    }
}
