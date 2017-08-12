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


import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class FieldInfo {
    public static final int ACC_PUBLIC = 0x0001;
    public static final int ACC_PRIVATE = 0x0002;
    public static final int ACC_PROTECTED = 0x0004;
    public static final int ACC_STATIC = 0x0008;
    public static final int ACC_FINAL = 0x0010;
    public static final int ACC_VOLATILE = 0x0040;
    public static final int ACC_TRANSIENT = 0x0080;
    public static final int ACC_SYNTHETIC = 0x1000;
    public static final int ACC_ENUM = 0x4000;

    private final int accessFlags;
    private final int nameIndex;
    private final int descriptorIndex;
    private final AttributeInfo[] attributes;

    private FieldInfo(int accessFlags,
                      int nameIndex,
                      int descriptorIndex,
                      AttributeInfo[] attributes) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public Object[] getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "Field{" +
                "accessFlags=" + accessFlags +
                ", nameIndex=" + nameIndex +
                ", descriptorIndex=" + descriptorIndex +
                ", attributes=" + Arrays.toString(attributes) +
                '}';
    }

    public static FieldInfo readField(DataInputStream stream) throws IOException {
        int accessFlags = stream.readUnsignedShort();
        int nameIndex = stream.readUnsignedShort();
        int descriptorIndex = stream.readUnsignedShort();
        AttributeInfo[] attributes = AttributeInfo.readArray(stream);

        return new FieldInfo(accessFlags,
                nameIndex,
                descriptorIndex,
                attributes);
    }
}
