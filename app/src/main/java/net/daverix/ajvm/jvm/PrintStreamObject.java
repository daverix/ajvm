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
import java.io.PrintStream;

public class PrintStreamObject implements VirtualObject {
    private final PrintStream printStream;

    public PrintStreamObject(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public String getName() {
        return "java/io/PrintStream";
    }

    @Override
    public void setFieldValue(String fieldName, Object value) {

    }

    @Override
    public Object getFieldValue(String fieldName) {
        return null;
    }

    @Override
    public Object invokeMethod(String name, String descriptor, Object... args) throws IOException {
        if(name.equals("println") && "(Ljava/lang/String;)V".equals(descriptor)) {
            printStream.println((String) args[0]);
            return null;
        }

        throw new UnsupportedOperationException(name + " with descriptor " + descriptor + " not implemented");
    }
}
