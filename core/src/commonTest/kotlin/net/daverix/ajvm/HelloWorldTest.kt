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

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HelloWorldTest {
    private lateinit var stdOut: FakePrinter
    private lateinit var stdErr: FakePrinter
    private lateinit var sut: VirtualObject

    @BeforeTest
    fun setUp() {
        stdOut = FakePrinter()
        stdErr = FakePrinter()
        val testClassLoader = ApplicationObjectLoader(
                TestDataClassInfoProvider,
                PrintStreamObject(stdOut),
                PrintStreamObject(stdErr)
        )
        sut = testClassLoader.load("net/daverix/ajvm/test/HelloWorld")
    }

    @Test
    fun run() {
        sut.invokeMain(arrayOf("World"))

        //Note! We call println so the string ends with \n
        assertEquals("Hello World!\n", stdOut.output)
    }
}
