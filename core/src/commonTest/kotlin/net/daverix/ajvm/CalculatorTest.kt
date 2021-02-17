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

class CalculatorTest {
    private lateinit var stdOut: FakePrinter
    private lateinit var stdErr: FakePrinter
    private lateinit var sut: VirtualMachine

    @BeforeTest
    fun setUp() {
        stdOut = FakePrinter()
        stdErr = FakePrinter()
        sut = VirtualMachine(testClassInfoProvider)
    }

    @Test
    fun add() = runBlockingTest {
        invokeCalculator("1 + 2")

        //Note! We call println so the string ends with \n
        assertEquals("3\n", stdOut.output)
    }

    @Test
    fun subtract() = runBlockingTest {
        invokeCalculator("3 - 2")

        //Note! We call println so the string ends with \n
        assertEquals("1\n", stdOut.output)
    }

    @Test
    fun divide() = runBlockingTest {
        invokeCalculator("6 / 2")

        //Note! We call println so the string ends with \n
        assertEquals("3\n", stdOut.output)
    }

    @Test
    fun multiply() = runBlockingTest {
        invokeCalculator("2 * 3")

        //Note! We call println so the string ends with \n
        assertEquals("6\n", stdOut.output)
    }

    @Test
    fun modulus() = runBlockingTest {
        invokeCalculator("5 % 2")

        //Note! We call println so the string ends with \n
        assertEquals("1\n", stdOut.output)
    }

    @Test
    fun testError() = runBlockingTest {
        invokeCalculator("5 ? 2")

        //Note! We call println so the string ends with \n
        assertEquals("Unknown operator ?\n", stdErr.output)
    }

    private suspend fun invokeCalculator(args: String) {
        sut.run(
                className = "net/daverix/ajvm/test/Calculator",
                args = args.split(" ").toTypedArray()
        )
    }
}
