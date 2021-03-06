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

class IntegerObject : VirtualObject {
    private var integer: Int? = null

    override fun invokeMethod(name: String, descriptor: String, args: Array<Any?>): Any? {
        return when {
            name == "<init>" -> {
                integer = args[0] as Int?
            }
            name == "parseInt" && descriptor == "(Ljava/lang/String;)I" && args.size == 1 -> {
                args[0].toString().toInt()
            }
            else -> null
        }
    }

    override fun getFieldValue(fieldName: String): Any? {
        error("no field with name $fieldName exist")
    }

    override fun setFieldValue(fieldName: String, value: Any?) {
        error("no field with name $fieldName exist")
    }
}
