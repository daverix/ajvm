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


data class MethodInfo(
        val accessFlags: Int,
        val name: String,
        val descriptor: String,
        val codeAttribute: CodeAttribute?,
        val otherAttributes: List<AttributeInfo>
) {
    val isPrivate = accessFlags and ACC_PRIVATE != 0
    val isProtected = accessFlags and ACC_PROTECTED != 0
    val isPublic = accessFlags and ACC_PUBLIC != 0
    val isStatic = accessFlags and ACC_STATIC != 0
    val isFinal = accessFlags and ACC_FINAL != 0
    val isNative = accessFlags and ACC_NATIVE != 0
    val isSuper = accessFlags and ACC_NATIVE != 0

    companion object {
        const val ACC_PUBLIC = 0x0001
        const val ACC_PRIVATE = 0x0002
        const val ACC_PROTECTED = 0x0004
        const val ACC_STATIC = 0x0008
        const val ACC_FINAL = 0x0010
        const val ACC_SUPER = 0x0020
        const val ACC_BRIDGE = 0x0040
        const val ACC_NATIVE = 0x0100
        const val ACC_INTERFACE = 0x0200
        const val ACC_STRICT = 0x0800
        const val ACC_SYNTHETIC = 0x1000
        const val ACC_ANNOTATION = 0x2000
        const val ACC_ENUM = 0x4000
    }
}
