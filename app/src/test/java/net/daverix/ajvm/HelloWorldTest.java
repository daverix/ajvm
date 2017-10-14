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


import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import static com.google.common.truth.Truth.assertThat;

public class HelloWorldTest {
    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errStream;
    private VirtualObject sut;

    @Before
    public void setUp() throws IOException {
        //System.out.println(new File("./").getAbsolutePath());

        HashMap<String, VirtualObject> staticClasses = new HashMap<>();
        outputStream = new ByteArrayOutputStream();
        errStream = new ByteArrayOutputStream();

        ClassInfoProvider classInfoProvider = new FileSystemClassInfoProvider("app/build/intermediates/classes/test/debug/");
        VirtualObjectLoader testClassLoader = new ApplicationObjectLoader(classInfoProvider,
                staticClasses,
                new PrintStreamObject(new PrintStream(outputStream)),
                new PrintStreamObject(new PrintStream(errStream)));
        sut = testClassLoader.load("net/daverix/ajvm/HelloWorld");
    }

    @Test
    public void run() throws IOException {
        sut.invokeMethod("main", "([Ljava/lang/String;)V", new Object[]{new String[]{"World"}});

        //Note! We call println so the string ends with \n
        assertThat(new String(outputStream.toByteArray())).isEqualTo("Hello World!\n");
    }
}
