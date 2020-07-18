package net.daverix.ajvm.io

data class ConstantPool(private val data: Array<Any?>) {
    operator fun get(index: Int): Any? {
        return data[index]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ConstantPool

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}
