package net.daverix.ajvm


interface ObjectLoader {
    suspend fun load(qualifiedName: String): VirtualObject

    suspend fun loadStatic(qualifiedName: String): VirtualObject
}
