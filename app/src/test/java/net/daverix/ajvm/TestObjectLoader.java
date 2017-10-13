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


import net.daverix.ajvm.io.ClassInfo;
import net.daverix.ajvm.io.ClassInfoReader;
import net.daverix.ajvm.io.VirtualObjectLoader;
import net.daverix.ajvm.jvm.IntegerObject;
import net.daverix.ajvm.jvm.PrintStreamObject;
import net.daverix.ajvm.jvm.RuntimeVirtualObject;
import net.daverix.ajvm.jvm.StringBuilderObject;
import net.daverix.ajvm.jvm.SystemObject;
import net.daverix.ajvm.jvm.VirtualObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class TestObjectLoader implements VirtualObjectLoader {
    private final Map<String, VirtualObject> staticClasses;
    private final File dir;
    private final PrintStreamObject outStream;

    public TestObjectLoader(Map<String,VirtualObject> staticClasses,
                            File dir,
                            PrintStreamObject outStream) {
        this.staticClasses = staticClasses;
        this.dir = dir;
        this.outStream = outStream;
    }

    @Override
    public VirtualObject load(String className) throws IOException {
        if("java/lang/System".equals(className)) {
            return new SystemObject(outStream);
        } else if("java/lang/StringBuilder".equals(className)) {
            return new StringBuilderObject();
        } else if("java/lang/Integer".equals(className)) {
            return new IntegerObject();
        }

        File file = new File(dir + "/" + className + ".class");
        if(!file.exists())
            throw new IOException("Cannot find " + file);

        ClassInfo classInfo;
        try(ClassInfoReader reader = new ClassInfoReader(new DataInputStream(new FileInputStream(file)))) {
            classInfo = reader.read();
        }

        return new RuntimeVirtualObject(staticClasses, this, classInfo);
    }
}