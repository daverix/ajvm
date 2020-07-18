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
import net.daverix.ajvm.io.JvmPrinter
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.PrintStream
import java.util.*

class CalculatorTest {
    private lateinit var outputStream: ByteArrayOutputStream
    private lateinit var errStream: ByteArrayOutputStream
    private lateinit var sut: VirtualObject

    @Before
    @Throws(IOException::class)
    fun setUp() {
        val staticClasses = HashMap<String, VirtualObject>()
        outputStream = ByteArrayOutputStream()
        errStream = ByteArrayOutputStream()
        val classInfoProvider = FileSystemClassInfoProvider(getTestDataDirectory())
        val testClassLoader = ApplicationObjectLoader(classInfoProvider,
                staticClasses,
                PrintStreamObject(JvmPrinter(PrintStream(outputStream))),
                PrintStreamObject(JvmPrinter(PrintStream(errStream))))
        sut = testClassLoader.load("net/daverix/ajvm/test/Calculator")
    }

    @Test
    @Throws(IOException::class)
    fun add() {
        invokeCalculator("1 + 2")

        //Note! We call println so the string ends with \n
        assertThat(String(outputStream.toByteArray())).isEqualTo("3\n")
    }

    @Test
    @Throws(IOException::class)
    fun subtract() {
        invokeCalculator("3 - 2")

        //Note! We call println so the string ends with \n
        assertThat(String(outputStream.toByteArray())).isEqualTo("1\n")
    }

    @Test
    @Throws(IOException::class)
    fun divide() {
        invokeCalculator("6 / 2")

        //Note! We call println so the string ends with \n
        assertThat(String(outputStream.toByteArray())).isEqualTo("3\n")
    }

    @Test
    @Throws(IOException::class)
    fun multiply() {
        invokeCalculator("2 * 3")

        //Note! We call println so the string ends with \n
        assertThat(String(outputStream.toByteArray())).isEqualTo("6\n")
    }

    @Test
    @Throws(IOException::class)
    fun modulus() {
        invokeCalculator("5 % 2")

        //Note! We call println so the string ends with \n
        assertThat(String(outputStream.toByteArray())).isEqualTo("1\n")
    }

    @Test
    @Throws(IOException::class)
    fun testError() {
        invokeCalculator("5 ? 2")

        //Note! We call println so the string ends with \n
        assertThat(String(errStream.toByteArray())).isEqualTo("Unknown operator ?\n")
    }

    @Throws(IOException::class)
    private fun invokeCalculator(args: String) {
        sut.invokeMethod("main", "([Ljava/lang/String;)V",
                arrayOf(args.split(" ").toTypedArray()))
    }
}
