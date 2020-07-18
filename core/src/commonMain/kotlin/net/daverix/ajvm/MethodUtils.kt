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

private val METHOD_DESCRIPTOR_REGEX = Regex("\\((.*)\\)(B|C|D|F|I|J|L.+;|S|Z|\\[.+|V)")
private val METHOD_PARAMETERS_REGEX = Regex("(B|C|D|F|I|J|L[^;]+;|S|Z|\\[[^\\[]+)+")

data class ParsedMethodDescriptor(val parameters: List<String>, val returnDescriptor: String)

fun parseMethodDescriptor(descriptor: String): ParsedMethodDescriptor {
    val methodResult = METHOD_DESCRIPTOR_REGEX.matchEntire(descriptor)
            ?: error("Cannot parse method descriptor $descriptor")

    val returnDescriptor = methodResult.groups[2]?.value ?: ""
    val parameters = methodResult.groups[1]?.value ?: ""
    val parametersResult = METHOD_PARAMETERS_REGEX.matchEntire(parameters)
    val parameterList = mutableListOf<String>()
    if(parametersResult != null) {
        for (i in 1 until parametersResult.groupValues.size) {
            parameterList += parametersResult.groupValues[i]
        }
    }

    return ParsedMethodDescriptor(parameterList, returnDescriptor)
}
