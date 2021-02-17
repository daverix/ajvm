package net.daverix.ajvm

import net.daverix.ajvm.io.ClassInfoProvider


class VirtualMachine(
        private val classInfoProvider: ClassInfoProvider
) {
    private val staticObjects = mutableMapOf<String, VirtualObject>()

    suspend fun run(className: String?=null, vararg args: Any?) {
        val realName = className ?: error("finding className in manifest of jar file is not yet supported")
        val instance = load(realName)
        instance.invokeMain(args)
    }

    private suspend fun load(qualifiedName: String): VirtualObject {
        val classInfo = classInfoProvider.getClassInfo(qualifiedName)
        return RuntimeVirtualObject(
                classInfo = classInfo,
                loadObject = ::load,
                loadStaticObject = ::loadStatic
        )
    }

    private suspend fun loadStatic(qualifiedName: String): VirtualObject {
        var staticObject: VirtualObject? = staticObjects[qualifiedName]
        if (staticObject == null) {
            staticObject = load(qualifiedName)
            staticObjects[qualifiedName] = staticObject
        }
        return staticObject
    }
}