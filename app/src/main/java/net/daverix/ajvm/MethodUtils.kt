package net.daverix.ajvm


import java.util.regex.Pattern

private val METHOD_PARAMETER_COUNT_PATTERN = Pattern.compile("\\((B|C|D|F|I|J|L.+;|S|Z|\\[.+)*\\)B|C|D|F|I|J|L.+;|S|Z|\\[.+")

fun String.getArgumentCount(): Int {
    if (startsWith("()"))
        return 0

    return METHOD_PARAMETER_COUNT_PATTERN.matcher(this).groupCount()
}
