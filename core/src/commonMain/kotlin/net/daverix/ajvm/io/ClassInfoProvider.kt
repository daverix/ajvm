package net.daverix.ajvm.io


interface ClassInfoProvider {
    suspend fun getClassInfo(qualifiedName: String): ClassInfo
}
