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

import java.io.IOException

class StringBuilderObject : VirtualObject {
    private var builder: StringBuilder? = null
    override val fields: Map<String, Any> = mapOf()
    override val name: String
        get() = "java/lang/StringBuilder"

    override fun initialize(args: Array<Any>) {

    }

    @Throws(IOException::class)
    override fun invokeMethod(name: String, descriptor: String, args: Array<Any?>): Any? {
        return when (name) {
            "<init>" -> {
                builder = StringBuilder()
                null
            }
            "append" -> {
                builder!!.append(args[0])
                this
            }
            "toString" -> builder!!.toString()
            else -> throw UnsupportedOperationException("method $name not implemented in StringBuilderObject")
        }
    }
}
