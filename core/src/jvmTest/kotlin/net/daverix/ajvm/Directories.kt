package net.daverix.ajvm

import java.io.File

fun getTestDataDirectory(): File {
    val userDir = System.getProperty("user.dir")
    return File(File(userDir).parentFile, "testdata/build/classes/java/main")
}