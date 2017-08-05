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
package net.daverix.ajvm;

import java.util.Arrays;

public class ClassFile {
    private final int majorVersion;
    private final int minorVersion;
    private final Object[] constantPool;
    private final int accessFlags;
    private final int thisClass;
    private final int superClass;
    private final int[] interfaces;
    private final Field[] fields;
    private final Method[] methods;
    private final Attribute[] attributes;

    public ClassFile(int majorVersion,
                     int minorVersion,
                     Object[] constantPool,
                     int accessFlags,
                     int thisClass,
                     int superClass,
                     int[] interfaces,
                     Field[] fields,
                     Method[] methods,
                     Attribute[] attributes) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.constantPool = constantPool;
        this.accessFlags = accessFlags;
        this.thisClass = thisClass;
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.fields = fields;
        this.methods = methods;
        this.attributes = attributes;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public int[] getInterfaces() {
        return interfaces;
    }

    public Field[] getFields() {
        return fields;
    }

    public Method[] getMethods() {
        return methods;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public String getClassName() {
        ClassReference classReference = (ClassReference) constantPool[thisClass];
        return classReference.getName();
    }

    public String getSuperClassName() {
        ClassReference classReference = (ClassReference) constantPool[superClass];
        return classReference.getName();
    }

    public Method getMethodByName(String methodName) {
        for (Method method : methods) {
            if(methodName.equals(method.getName())) {
                return method;
            }
        }

        return null;
    }

    public Field getFieldByName(String fieldName) {
        for (Field field : fields) {
            if(fieldName.equals(field.getName())) {
                return field;
            }
        }

        return null;
    }

    public Attribute getAttributeByName(String name) {
        for (Attribute attribute : attributes) {
            if(name.equals(attribute.getName())) {
                return attribute;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "ClassFile{" +
                "majorVersion=" + majorVersion +
                ", minorVersion=" + minorVersion +
                ", constantPool=" + Arrays.toString(constantPool) +
                ", accessFlags=" + accessFlags +
                ", thisClass=" + thisClass +
                ", superClass=" + superClass +
                ", interfaces=" + Arrays.toString(interfaces) +
                ", fields=" + Arrays.toString(fields) +
                ", methods=" + Arrays.toString(methods) +
                ", attributes=" + Arrays.toString(attributes) +
                '}';
    }
}
