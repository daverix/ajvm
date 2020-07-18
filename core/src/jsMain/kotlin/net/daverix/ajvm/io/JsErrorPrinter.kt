package net.daverix.ajvm.io

class JsErrorPrinter : Printer {
    override fun println(text: String) {
        console.error(text)
    }

    override fun println(number: Int) {
        console.error(number)
    }
}
