package net.daverix.ajvm


import java.io.IOException

class IntegerObject : VirtualObject {
    private var integer: Int? = null

    override val name: String
        get() = "java/lang/Integer"

    override fun initialize(args: Array<Any>) {
        integer = args[0] as Int
    }

    override fun setFieldValue(fieldName: String, value: Any?) {

    }

    override fun getFieldValue(fieldName: String): Any? {
        return null
    }

    @Throws(IOException::class)
    override fun invokeMethod(name: String, descriptor: String, args: Array<Any?>): Any? {
        return if ("parseInt" == name && descriptor == "(Ljava/lang/String;)I") {
            Integer.parseInt(args[0] as String)
        } else null

    }
}
