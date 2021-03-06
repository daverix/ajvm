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

class OperandStack(private val maxStackDepth: Int) {
    private val operandStack = mutableListOf<Any?>()

    fun push(item: Any?) {
        if (operandStack.size == maxStackDepth)
            error("Stackoverflow: can't push object onto stack, max stack depth $maxStackDepth reached")

        operandStack.add(item)
    }

    fun pop(): Any? {
        val size = operandStack.size
        return if (size > 0) {
            operandStack.removeAt(size - 1)
        } else null
    }

    fun popMultiple(count: Int): Array<Any?> {
        val array = arrayOfNulls<Any?>(count)
        for(i in count-1 downTo 0) {
            array[i] = pop()
        }
        return array
    }

    fun peek() = operandStack.lastOrNull()
}
