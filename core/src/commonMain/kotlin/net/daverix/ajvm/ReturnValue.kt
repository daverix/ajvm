package net.daverix.ajvm

sealed class ReturnValue {
    object Void : ReturnValue()
    class Value(val value: Any?) : ReturnValue()
}
