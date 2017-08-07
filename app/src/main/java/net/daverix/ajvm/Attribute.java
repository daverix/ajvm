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

import static net.daverix.ajvm.ByteReader.readBytes;

public class Attribute {
    private final int nameIndex;
    private final byte[] info;
    private final Object[] constantPool;

    public Attribute(int nameIndex, byte[] info, Object[] constantPool) {
        this.nameIndex = nameIndex;
        this.info = info;
        this.constantPool = constantPool;
    }

    public String getName() {
        return (String) constantPool[nameIndex];
    }

    public byte[] getInfo() {
        return info;
    }

    public static Attribute read(DataInputStream stream, Object[] constantPool) throws IOException {
        int nameIndex = stream.readUnsignedShort();
        int attributeLength = stream.readInt();
        byte[] info = readBytes(stream, attributeLength);
        return new Attribute(nameIndex, info, constantPool);
    }

    public static Attribute[] readArray(DataInputStream stream, Object[] constantPool) throws IOException {
        int count = stream.readUnsignedShort();
        Attribute[] attributes = new Attribute[count];
        for (int i = 0; i < count; i++) {
            attributes[i] = Attribute.read(stream, constantPool);
        }
        return attributes;
    }
}
