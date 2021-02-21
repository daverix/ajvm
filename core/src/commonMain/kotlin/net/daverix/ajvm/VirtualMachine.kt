package net.daverix.ajvm

import net.daverix.ajvm.io.*


class VirtualMachine(
        private val classInfoProvider: ClassInfoProvider
) {
    private val staticObjects = mutableMapOf<String, VirtualObject>()

    suspend fun run(
            className: String? = null,
            vararg args: String,
    ) {
        val realName = className
                ?: error("finding className in manifest of jar file is not yet supported")

        val system = objectLoader.loadStatic("java/lang/System")
        system.invokeStaticMethod("initializeSystemClass", "()V", emptyArray())

        val objectArgs = args.map { arg ->
            objectLoader.load("java/lang/String").apply {
                invokeSpecialMethod(
                        "java/lang/String",
                        "<init>",
                        "([C)V",
                        arrayOf(arg.toCharArray())
                )
            }
        }.toTypedArray()

        val instance = objectLoader.loadStatic(realName)
        instance.invokeStaticMethod(methodName = "main",
                descriptor = "([Ljava/lang/String;)V",
                args = arrayOf(objectArgs)
        )
    }

    private val objectLoader = object : ObjectLoader {
        override suspend fun load(qualifiedName: String): VirtualObject =
                loadObject(qualifiedName, static = false)

        override suspend fun loadStatic(qualifiedName: String): VirtualObject {
            //TODO: thread safety??
            var staticObject: VirtualObject? = staticObjects[qualifiedName]
            if (staticObject == null) {
                staticObject = loadObject(qualifiedName, static = true)
                staticObjects[qualifiedName] = staticObject
                staticObject.apply {
                    val clinit = classInfo.getMethodByNameAndDescriptor("<clinit>", "()V")
                    if (clinit != null) {
                        invokeStaticMethod("<clinit>", "()V", emptyArray())
                    }
                }
            }
            return staticObject
        }

        private suspend fun loadObject(qualifiedName: String, static: Boolean): VirtualObject {
            val classInfo = classInfoProvider.getClassInfo(qualifiedName)
            val fields = getFields(classInfo, static)
            return VirtualObject(
                    classInfo = classInfo,
                    fields = fields,
                    objectLoader = this
            )
        }
    }

    private fun getFields(classInfo: ClassInfo, static: Boolean): MutableMap<String, Any?> =
            classInfo.fields
                    .filter { (it.accessFlags and FieldInfo.ACC_STATIC != 0) == static }
                    .map { field ->
                        field.name to when {
                            field.descriptor == "B" -> 0
                            field.descriptor == "S" -> 0
                            field.descriptor == "I" -> 0
                            field.descriptor == "J" -> 0L
                            field.descriptor == "F" -> 0f
                            field.descriptor == "D" -> 0.0
                            field.descriptor == "C" -> '\u0000'
                            field.descriptor == "Z" -> false
                            field.descriptor.startsWith("[") -> null
                            field.descriptor.matches("L.+;".toRegex()) -> null
                            else -> error("unknown descriptor ${field.descriptor}")
                        }
                    }
                    .toMap()
                    .toMutableMap()
}
