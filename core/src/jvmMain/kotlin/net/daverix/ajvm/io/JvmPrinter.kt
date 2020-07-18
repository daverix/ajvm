package net.daverix.ajvm.io

import java.io.PrintStream

class JvmPrinter(private val printStream: PrintStream): Printer {
    override fun println(text: String) {
        printStream.println(text)
    }

    override fun println(number: Int) {
        printStream.println(number)
    }
}