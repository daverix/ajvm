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

import net.daverix.ajvm.io.ClassInfo
import net.daverix.ajvm.io.ClassInfoReader
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class FileSystemClassInfoProvider(private val dir: String) : ClassInfoProvider {
    override fun getClassInfo(className: String): ClassInfo {
        val file = File("$dir/$className.class")
        if (!file.exists())
            throw IOException("Cannot find " + file)

        return ClassInfoReader(DataInputStream(FileInputStream(file))).use { it.read() }
    }
}