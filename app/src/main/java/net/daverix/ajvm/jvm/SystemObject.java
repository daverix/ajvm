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


import java.io.IOException;

public class SystemObject implements VirtualObject {
    private final PrintStreamObject out;

    public SystemObject(PrintStreamObject out) {
        this.out = out;
    }

    @Override
    public String getName() {
        return "java/lang/System";
    }

    @Override
    public void setFieldValue(String fieldName, Object value) {

    }

    @Override
    public Object getFieldValue(String fieldName) {
        if(fieldName.equals("out"))
            return out;

        return null;
    }

    @Override
    public Object invokeMethod(String name, String descriptor, Object... args) throws IOException {
        return null;
    }
}
