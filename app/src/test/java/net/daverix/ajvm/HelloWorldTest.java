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


import net.daverix.ajvm.io.VirtualObjectLoader;
import net.daverix.ajvm.jvm.VirtualObject;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class HelloWorldTest {
    private VirtualObject sut;

    @Before
    public void setUp() throws IOException {
        //System.out.println(new File("./").getAbsolutePath());
        File dir = new File("app/build/intermediates/classes/test/debug/");

        HashMap<String, VirtualObject> staticClasses = new HashMap<>();
        VirtualObjectLoader testClassLoader = new TestObjectLoader(staticClasses, dir);
        sut = testClassLoader.load("net/daverix/ajvm/HelloWorld");
    }

    @Test
    public void run() throws IOException {
        sut.invokeMethod("main", "([Ljava/lang/String;)V");
    }
}
