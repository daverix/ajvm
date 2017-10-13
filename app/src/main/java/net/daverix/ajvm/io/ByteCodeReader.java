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

public class ByteCodeReader {
    private int index = 0;
    private byte[] code;

    public ByteCodeReader(byte[] code) {
        this.code = code;
    }

    public boolean canReadByte() {
        return canRead(1);
    }

    private boolean canRead(int amount) {
        return index < (code.length - amount);
    }

    public int readUnsignedByte() {
        if (!canReadByte())
            throw new IllegalStateException("Cannot read byte, reached end of array");

        return code[index++] & 0xFF;
    }

    public int readUnsignedShort() {
        if (!canRead(2))
            throw new IllegalStateException("Cannot read short, reached end of array");

        int first = code[index++];
        int second = code[index++];

        return (first << 8) + second;
    }

    public int readInt() {
        if (!canRead(4))
            throw new IllegalStateException("Cannot read int, reached end of array");

        return (code[index++] << 24) |
                (code[index++] << 16) |
                (code[index++] << 8) |
                code[index++];
    }

    public long readLong() {
        if (!canRead(8))
            throw new IllegalStateException("Cannot read long, reached end of array");

        return ((long) (code[index++] & 0xFF) << 56) +
                ((long) (code[index++] & 0xFF) << 48) +
                ((long) (code[index++] & 0xFF) << 40) +
                ((long) (code[index++] & 0xFF) << 32) +
                ((long) (code[index++] & 0xFF) << 24) +
                (code[index++] << 16) +
                (code[index++] << 8) +
                code[index++];
    }

    public float readFloat() {
        if (!canRead(4))
            throw new IllegalStateException("Cannot read float, reached end of array");

        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        if (!canRead(8))
            throw new IllegalStateException("Cannot read double, reached end of array");

        return Double.longBitsToDouble(readLong());
    }

    public void skip(int count) {
        if ((index + count) < 0 || (index + count) >= code.length)
            throw new IllegalArgumentException("count " + count + " would put index out of range");

        index += count;
    }

    public int getIndex() {
        return index;
    }

    public void jumpTo(int index) {
        if (index < 0 || index >= code.length)
            throw new IllegalArgumentException(index + " is outside range");

        this.index = index;
    }
}
