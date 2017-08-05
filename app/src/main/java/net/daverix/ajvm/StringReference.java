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


public class StringReference {
    private final int index;
    private final Object[] constantPool;

    public StringReference(int index, Object[] constantPool) {
        this.index = index;
        this.constantPool = constantPool;
    }

    @Override
    public String toString() {
        return (String) constantPool[index];
    }
}
