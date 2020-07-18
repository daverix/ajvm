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

data class FieldInfo(
        val accessFlags: Int,
        val nameIndex: Int,
        val descriptorIndex: Int,
        val attributes: Array<AttributeInfo>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FieldInfo

        if (accessFlags != other.accessFlags) return false
        if (nameIndex != other.nameIndex) return false
        if (descriptorIndex != other.descriptorIndex) return false
        if (!attributes.contentEquals(other.attributes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = accessFlags
        result = 31 * result + nameIndex
        result = 31 * result + descriptorIndex
        result = 31 * result + attributes.contentHashCode()
        return result
    }

    companion object {
        val ACC_PUBLIC = 0x0001
        val ACC_PRIVATE = 0x0002
        val ACC_PROTECTED = 0x0004
        val ACC_STATIC = 0x0008
        val ACC_FINAL = 0x0010
        val ACC_VOLATILE = 0x0040
        val ACC_TRANSIENT = 0x0080
        val ACC_SYNTHETIC = 0x1000
        val ACC_ENUM = 0x4000
    }
}
