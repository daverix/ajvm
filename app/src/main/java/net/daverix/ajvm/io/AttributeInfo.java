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

public class AttributeInfo {
    private final int nameIndex;
    private final byte[] info;

    private AttributeInfo(int nameIndex, byte[] info) {
        this.nameIndex = nameIndex;
        this.info = info;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public byte[] getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "AttributeInfo{" +
                "nameIndex=" + nameIndex +
                ", info=" + Arrays.toString(info) +
                '}';
    }

    public static AttributeInfo getAttributeByName(AttributeInfo[] attributes,
                                                   Object[] constantPool,
                                                   String name) {
        for (AttributeInfo attribute : attributes){
            if(name.equals(constantPool[attribute.getNameIndex()])) {
                return attribute;
            }
        }

        return null;
    }

    public static AttributeInfo read(DataInputStream stream) throws IOException {
        int nameIndex = stream.readUnsignedShort();
        int attributeLength = stream.readInt();
        byte[] info = new byte[attributeLength];
        stream.readFully(info);
        return new AttributeInfo(nameIndex, info);
    }

    public static AttributeInfo[] readArray(DataInputStream stream) throws IOException {
        int count = stream.readUnsignedShort();
        AttributeInfo[] attributes = new AttributeInfo[count];
        for (int i = 0; i < count; i++) {
            attributes[i] = AttributeInfo.read(stream);
        }
        return attributes;
    }
}
