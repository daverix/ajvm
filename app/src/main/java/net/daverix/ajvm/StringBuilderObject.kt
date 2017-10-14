package net.daverix.ajvm


import java.io.IOException

class StringBuilderObject : VirtualObject {
    private var builder: StringBuilder? = null

    override val name: String
        get() = "java/lang/StringBuilder"

    override fun initialize(args: Array<Any>) {

    }

    override fun setFieldValue(fieldName: String, value: Any?) {

    }

    override fun getFieldValue(fieldName: String): Any? {
        return null
    }

    @Throws(IOException::class)
    override fun invokeMethod(name: String, descriptor: String, args: Array<Any?>): Any? {
        return when (name) {
            "<init>" -> {
                builder = StringBuilder()
                null
            }
            "append" -> {
                builder!!.append(args[0])
                this
            }
            "toString" -> builder!!.toString()
            else -> throw UnsupportedOperationException("method $name not implemented in StringBuilderObject")
        }
    }
}
