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
import net.daverix.ajvm.jvm.PrintStreamObject;
import net.daverix.ajvm.jvm.VirtualObject;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import static com.google.common.truth.Truth.assertThat;

public class CalculatorTest {
    private ByteArrayOutputStream outputStream;
    private VirtualObject sut;

    @Before
    public void setUp() throws IOException {
        //System.out.println(new File("./").getAbsolutePath());
        File dir = new File("app/build/intermediates/classes/test/debug/");

        HashMap<String, VirtualObject> staticClasses = new HashMap<>();
        outputStream = new ByteArrayOutputStream();
        VirtualObjectLoader testClassLoader = new TestObjectLoader(staticClasses,
                dir, new PrintStreamObject(new PrintStream(outputStream)));
        sut = testClassLoader.load("net/daverix/ajvm/Calculator");
    }

    @Test
    public void add() throws IOException {
        invokeCalculator("1 + 2");

        //Note! We call println so the string ends with \n
        assertThat(new String(outputStream.toByteArray())).isEqualTo("3\n");
    }

    @Test
    public void subtract() throws IOException {
        invokeCalculator("3 - 2");

        //Note! We call println so the string ends with \n
        assertThat(new String(outputStream.toByteArray())).isEqualTo("1\n");
    }

    @Test
    public void divide() throws IOException {
        invokeCalculator("6 / 2");

        //Note! We call println so the string ends with \n
        assertThat(new String(outputStream.toByteArray())).isEqualTo("3\n");
    }

    @Test
    public void multiply() throws IOException {
        invokeCalculator("2 * 3");

        //Note! We call println so the string ends with \n
        assertThat(new String(outputStream.toByteArray())).isEqualTo("6\n");
    }

    @Test
    public void modulus() throws IOException {
        invokeCalculator("5 % 2");

        //Note! We call println so the string ends with \n
        assertThat(new String(outputStream.toByteArray())).isEqualTo("1\n");
    }

    private void invokeCalculator(String args) throws IOException {
        sut.invokeMethod("main", "([Ljava/lang/String;)V",
                new Object[]{args.split(" ")});
    }
}