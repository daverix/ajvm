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
package net.daverix.ajvm.io

import java.util.*

data class ClassInfo(val majorVersion: Int,
                     val minorVersion: Int,
                     val constantPool: ConstantPool,
                     val accessFlags: Int,
                     val classIndex: Int,
                     val superClassIndex: Int,
                     val interfaces: IntArray,
                     val fields: Array<FieldInfo>,
                     val methods: Array<MethodInfo>,
                     val attributes: Array<AttributeInfo>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClassInfo) return false

        if (majorVersion != other.majorVersion) return false
        if (minorVersion != other.minorVersion) return false
        if (constantPool != other.constantPool) return false
        if (accessFlags != other.accessFlags) return false
        if (classIndex != other.classIndex) return false
        if (superClassIndex != other.superClassIndex) return false
        if (!Arrays.equals(interfaces, other.interfaces)) return false
        if (!Arrays.equals(fields, other.fields)) return false
        if (!Arrays.equals(methods, other.methods)) return false
        if (!Arrays.equals(attributes, other.attributes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = majorVersion
        result = 31 * result + minorVersion
        result = 31 * result + constantPool.hashCode()
        result = 31 * result + accessFlags
        result = 31 * result + classIndex
        result = 31 * result + superClassIndex
        result = 31 * result + Arrays.hashCode(interfaces)
        result = 31 * result + Arrays.hashCode(fields)
        result = 31 * result + Arrays.hashCode(methods)
        result = 31 * result + Arrays.hashCode(attributes)
        return result
    }

    override fun toString(): String {
        return "ClassInfo(majorVersion=$majorVersion, " +
                "minorVersion=$minorVersion, " +
                "constantPool=$constantPool, " +
                "accessFlags=$accessFlags, " +
                "classIndex=$classIndex, " +
                "superClassIndex=$superClassIndex, " +
                "interfaces=${Arrays.toString(interfaces)}, " +
                "fields=${Arrays.toString(fields)}, " +
                "methods=${Arrays.toString(methods)}, " +
                "attributes=${Arrays.toString(attributes)})"
    }

}
