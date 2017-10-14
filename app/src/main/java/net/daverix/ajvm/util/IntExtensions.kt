package net.daverix.ajvm.util


infix fun Int.containsFlag(flag: Int): Boolean {
    return (this and flag) != 0
}
