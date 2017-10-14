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
package net.daverix.ajvm


import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.PrintStream
import java.util.*

class HelloWorldTest {
    private var outputStream: ByteArrayOutputStream? = null
    private var errStream: ByteArrayOutputStream? = null
    private var sut: VirtualObject? = null

    @Before
    @Throws(IOException::class)
    fun setUp() {
        //System.out.println(new File("./").getAbsolutePath());

        val staticClasses = HashMap<String, VirtualObject>()
        outputStream = ByteArrayOutputStream()
        errStream = ByteArrayOutputStream()

        val classInfoProvider = FileSystemClassInfoProvider("app/build/intermediates/classes/test/debug/")
        val testClassLoader = ApplicationObjectLoader(classInfoProvider,
                staticClasses,
                PrintStreamObject(PrintStream(outputStream!!)),
                PrintStreamObject(PrintStream(errStream!!)))
        sut = testClassLoader.load("net/daverix/ajvm/HelloWorld")
    }

    @Test
    @Throws(IOException::class)
    fun run() {
        sut!!.invokeMethod("main", "([Ljava/lang/String;)V", arrayOf(arrayOf("World")))

        //Note! We call println so the string ends with \n
        assertThat(String(outputStream!!.toByteArray())).isEqualTo("Hello World!\n")
    }
}
