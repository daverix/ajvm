package net.daverix.ajvm

class StaticClassLoader(private val classLoader: VirtualObjectLoader) : VirtualObjectLoader {
    private val staticClasses: MutableMap<String, VirtualObject> = mutableMapOf()

    override fun load(qualifiedName: String): VirtualObject {
        var staticClass: VirtualObject? = staticClasses[qualifiedName]
        if (staticClass == null) {
            staticClass = classLoader.load(qualifiedName)
            staticClasses[qualifiedName] = staticClass
        }
        return staticClass
    }
}
