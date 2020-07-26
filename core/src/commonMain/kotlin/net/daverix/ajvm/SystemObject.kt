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

class SystemObject(
        private val out: PrintStreamObject,
        private val err: PrintStreamObject
) : VirtualObject {
    override fun invokeMethod(name: String, descriptor: String, args: Array<Any?>): Any? {
        return null
    }

    override fun getFieldValue(fieldName: String): Any? {
        return when (fieldName) {
            "out" -> out
            "err" -> err
            else -> error("no field with name $fieldName exist")
        }
    }

    override fun setFieldValue(fieldName: String, value: Any?) {
        error("no fields with name $fieldName can be set")
    }
}
