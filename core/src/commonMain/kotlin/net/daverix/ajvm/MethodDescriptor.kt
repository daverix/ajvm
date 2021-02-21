package net.daverix.ajvm

data class MethodDescriptor(val parameters: List<String>, val returnType: String)

private val METHOD_DESCRIPTOR_REGEX = Regex("\\((.*)\\)(B|C|D|F|I|J|L.+;|S|Z|\\[.+|V)")
private val METHOD_PARAMETERS_REGEX = Regex("(B|C|D|F|I|J|L[^;]+;|S|Z|\\[\\1)")

fun parseMethodDescriptor(descriptor: String): MethodDescriptor {
    val methodResult = METHOD_DESCRIPTOR_REGEX.matchEntire(descriptor)
            ?: error("Cannot parse method descriptor $descriptor")

    val returnDescriptor = methodResult.groups[2]?.value ?: ""
    val parameters = methodResult.groups[1]?.value ?: ""
    val parameterList = METHOD_PARAMETERS_REGEX.findAll(parameters).map { it.value }.toList()

    return MethodDescriptor(parameterList, returnDescriptor)
}
