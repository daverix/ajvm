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

import net.daverix.ajvm.io.Printer

class PrintStreamObject(private val printer: Printer) : VirtualObject {

    override fun invokeMethod(name: String, descriptor: String, args: Array<Any?>): Any? {
        if ("println" == name && "(Ljava/lang/String;)V" == descriptor) {
            printer.println(args[0] as String)
            return null
        } else if ("println" == name && "(I)V" == descriptor) {
            printer.println(args[0] as Int)
            return null
        }

        error("\"$name$descriptor\" not implemented")
    }

    override fun getFieldValue(fieldName: String): Any? {
        throw UnsupportedOperationException("not supported in PrintStreamObject?")
    }

    override fun setFieldValue(fieldName: String, value: Any?) {
        error("no fields with name $fieldName can be set")
    }
}
