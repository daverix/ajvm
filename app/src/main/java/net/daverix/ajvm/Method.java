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


import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class Method {
    public static final int ACC_PUBLIC = 0x0001;
    public static final int ACC_PRIVATE = 0x0002;
    public static final int ACC_PROTECTED = 0x0004;
    public static final int ACC_STATIC = 0x0008;
    public static final int ACC_FINAL = 0x0010;
    public static final int ACC_SYNCHRONIZED = 0x0020;
    public static final int ACC_BRIDGE = 0x0040;
    public static final int ACC_VARARGS = 0x0080;
    public static final int ACC_NATIVE = 0x0100;
    public static final int ACC_ABSTRACT = 0x0400;
    public static final int ACC_STRICT = 0x0800;
    public static final int ACC_SYNTHETIC = 0x1000;

    private final int accessFlags;
    private final int nameIndex;
    private final int descriptorIndex;
    private final Attribute[] attributes;
    private final Object[] constantPool;

    public Method(int accessFlags,
                  int nameIndex,
                  int descriptorIndex,
                  Attribute[] attributes,
                  Object[] constantPool) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
        this.constantPool = constantPool;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    @Override
    public String toString() {
        return "Method{" +
                "accessFlags=" + accessFlags +
                ", nameIndex=" + nameIndex +
                ", descriptorIndex=" + descriptorIndex +
                ", attributes=" + Arrays.toString(attributes) +
                '}';
    }

    public String getName() {
        return (String) constantPool[nameIndex];
    }

    public String getDescriptor() {
        return (String) constantPool[descriptorIndex];
    }

    public Attribute getAttributeByName(String name) {
        for (Attribute attribute : attributes) {
            if(name.equals(attribute.getName())) {
                return attribute;
            }
        }

        return null;
    }

    public static Method read(DataInputStream stream, Object[] constantPool) throws IOException {
        int accessFlags = stream.readUnsignedShort();
        int nameIndex = stream.readUnsignedShort();
        int descriptorIndex = stream.readUnsignedShort();
        Attribute[] attributes = Attribute.readArray(stream, constantPool);

        return new Method(accessFlags, nameIndex, descriptorIndex, attributes, constantPool);
    }
}
