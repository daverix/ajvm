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

import net.daverix.ajvm.io.readClassInfo

class ApplicationObjectLoader(
        private val fileOpener: FileOpener,
        private val outStream: PrintStreamObject,
        private val errStream: PrintStreamObject,
        private val staticObjects: MutableMap<String, VirtualObject> = mutableMapOf()
) {
    fun load(qualifiedName: String): VirtualObject = when (qualifiedName) {
        "java/lang/System" -> SystemObject(outStream, errStream)
        "java/lang/StringBuilder" -> StringBuilderObject()
        "java/lang/Integer" -> IntegerObject()
        else -> RuntimeVirtualObject(
                classInfo = fileOpener.openFile(qualifiedName) {
                    readClassInfo()
                },
                loadObject = ::load,
                loadStaticObject = ::loadStatic
        )
    }

    private fun loadStatic(qualifiedName: String): VirtualObject {
        var staticObject: VirtualObject? = staticObjects[qualifiedName]
        if (staticObject == null) {
            staticObject = load(qualifiedName)
            staticObjects[qualifiedName] = staticObject
        }
        return staticObject
    }
}
