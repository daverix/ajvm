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
package net.daverix.ajvm.io;

import java.util.Arrays;

public class ClassInfo {
    private final int majorVersion;
    private final int minorVersion;
    private final Object[] constantPool;
    private final int accessFlags;
    private final int thisClass;
    private final int superClass;
    private final int[] interfaces;
    private final FieldInfo[] fields;
    private final MethodInfo[] methods;
    private final AttributeInfo[] attributes;

    public ClassInfo(int majorVersion,
                     int minorVersion,
                     Object[] constantPool,
                     int accessFlags,
                     int thisClass,
                     int superClass,
                     int[] interfaces,
                     FieldInfo[] fields,
                     MethodInfo[] methods,
                     AttributeInfo[] attributes) {
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

    public Object[] getConstantPool() {
        return constantPool;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public int[] getInterfaces() {
        return interfaces;
    }

    public FieldInfo[] getFields() {
        return fields;
    }

    public MethodInfo[] getMethods() {
        return methods;
    }

    public AttributeInfo[] getAttributes() {
        return attributes;
    }

    public int getClassIndex() {
        return thisClass;
    }

    public int getSuperClassIndex() {
        return superClass;
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
