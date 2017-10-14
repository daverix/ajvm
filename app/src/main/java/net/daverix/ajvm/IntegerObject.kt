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

class IntegerObject : VirtualObject {
    private var integer: Int? = null

    override val name: String
        get() = "java/lang/Integer"

    override fun initialize(args: Array<Any>) {
        integer = args[0] as Int
    }

    override fun setFieldValue(fieldName: String, value: Any?) {

    }

    override fun getFieldValue(fieldName: String): Any? {
        return null
    }

    @Throws(IOException::class)
    override fun invokeMethod(name: String, descriptor: String, args: Array<Any?>): Any? {
        return if ("parseInt" == name && descriptor == "(Ljava/lang/String;)I") {
            Integer.parseInt(args[0] as String)
        } else null

    }
}
