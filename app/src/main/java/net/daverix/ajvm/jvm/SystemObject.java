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


import net.daverix.ajvm.VirtualObject;

import java.io.IOException;

public class SystemObject implements VirtualObject {
    private final PrintStreamObject out;
    private final PrintStreamObject err;

    public SystemObject(PrintStreamObject out,
                        PrintStreamObject err) {
        this.out = out;
        this.err = err;
    }

    @Override
    public void initialize(Object[] args) {

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
        if("out".equals(fieldName))
            return out;

        if("err".equals(fieldName))
            return err;

        return null;
    }

    @Override
    public Object invokeMethod(String name, String descriptor, Object... args) throws IOException {
        return null;
    }
}
