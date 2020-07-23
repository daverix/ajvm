package net.daverix.ajvm

import net.daverix.ajvm.io.Printer

class FakePrinter : Printer {
    private var outputBuilder = StringBuilder()

    val output: String
        get() = outputBuilder.toString()

    override fun println(text: String) {
        outputBuilder.append("$text\n")
    }

    override fun println(number: Int) {
        outputBuilder.append("$number\n")
    }
}