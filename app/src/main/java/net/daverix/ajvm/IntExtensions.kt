package net.daverix.ajvm


infix fun Int.containsFlag(flag: Int): Boolean {
    return (this and flag) != 0
}
