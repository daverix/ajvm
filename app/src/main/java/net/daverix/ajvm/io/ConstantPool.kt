package net.daverix.ajvm.io


class ConstantPool {
    private val pool = HashMap<Int,Any>()

    operator fun set(index: Int, value: Any) {
        pool[index] = value
    }

    operator fun get(index: Int): Any? {
        if(!pool.containsKey(index))
            return null

        return pool[index]
    }
}
