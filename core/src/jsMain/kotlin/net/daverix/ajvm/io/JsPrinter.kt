package net.daverix.ajvm.io

class JsPrinter : Printer {
    override fun println(text: String) {
        console.log(text)
    }

    override fun println(number: Int) {
        console.log(number)
    }
}
