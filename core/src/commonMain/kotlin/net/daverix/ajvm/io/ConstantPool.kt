package net.daverix.ajvm.io

import net.daverix.ajvm.*

data class ClassReference(val nameIndex: Int)
data class StringReference(val index: Int)
data class FieldReference(val classIndex: Int, val nameAndTypeIndex: Int)
data class MethodReference(val classIndex: Int, val nameAndTypeIndex: Int)
data class InterfaceMethodReference(val classIndex: Int, val nameAndTypeIndex: Int)
data class NameAndTypeDescriptorReference(val nameIndex: Int, val descriptorIndex: Int)
data class MethodTypeReference(val descriptorIndex: Int)
data class MethodHandleReference(val referenceKind: Int, val referenceIndex: Int)
data class InvokeDynamicReference(val bootstrapMethodAttrIndex: Int, val nameAndTypeIndex: Int)

data class ConstantPool(private val data: Array<Any?>) {
    operator fun get(index: Int): Any? = data[index]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ConstantPool) return false

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    companion object {

        private const val CONSTANT_TAG_UTF8 = 1
        private const val CONSTANT_TAG_INTEGER = 3
        private const val CONSTANT_TAG_FLOAT = 4
        private const val CONSTANT_TAG_LONG = 5
        private const val CONSTANT_TAG_DOUBLE = 6
        private const val CONSTANT_TAG_CLASS = 7
        private const val CONSTANT_TAG_STRING = 8
        private const val CONSTANT_TAG_FIELD_REFERENCE = 9
        private const val CONSTANT_TAG_METHOD_REFERENCE = 10
        private const val CONSTANT_TAG_INTERFACE_METHOD_REFERENCE = 11
        private const val CONSTANT_TAG_NAME_AND_TYPE = 12
        private const val CONSTANT_TAG_METHOD_HANDLE = 15
        private const val CONSTANT_TAG_METHOD_TYPE = 16
        private const val CONSTANT_TAG_INVOKE_DYNAMIC = 18

        fun DataInputStream.readConstantPool(): ConstantPool {
            val constantPoolCount = readUnsignedShort()
            val constantPool = arrayOfNulls<Any?>(constantPoolCount)
            var i = 1
            while (i < constantPoolCount) {
                when (readUnsignedByte()) {
                    -1 -> error("EOF when reading type from constant pool")
                    CONSTANT_TAG_UTF8 -> constantPool[i++] = readUTF()
                    CONSTANT_TAG_INTEGER -> constantPool[i++] = readInt()
                    CONSTANT_TAG_FLOAT -> constantPool[i++] = readFloat()
                    CONSTANT_TAG_LONG -> {
                        val longBytes = ByteArray(8)
                        if (read(longBytes, 0, 4) != 4)
                            error("could not read high part of long")

                        if (read(longBytes, 4, 4) != 4)
                            error("could not read low part of long")

                        constantPool[i] = longBytes.wrapToLong()
                        // we advance index one additional time because we are sure that the second
                        // part comes directly after
                        i += 2
                    }
                    CONSTANT_TAG_DOUBLE -> {
                        val doubleBytes = ByteArray(8)
                        if (read(doubleBytes, 0, 4) != 4)
                            error("could not read high part of double")

                        if (read(doubleBytes, 4, 4) != 4)
                            error("could not read low part of double")

                        constantPool[i] = doubleBytes.wrapToDouble()
                        // we advance index one additional time because we are sure that the second
                        // part comes directly after
                        i += 2
                    }
                    CONSTANT_TAG_CLASS -> constantPool[i++] = ClassReference(
                            nameIndex = readUnsignedShort()
                    )
                    CONSTANT_TAG_STRING -> constantPool[i++] = StringReference(
                            index = readUnsignedShort()
                    )
                    CONSTANT_TAG_FIELD_REFERENCE -> constantPool[i++] = FieldReference(
                            classIndex = readUnsignedShort(),
                            nameAndTypeIndex = readUnsignedShort()
                    )
                    CONSTANT_TAG_METHOD_REFERENCE -> constantPool[i++] = MethodReference(
                            classIndex = readUnsignedShort(),
                            nameAndTypeIndex = readUnsignedShort()
                    )
                    CONSTANT_TAG_INTERFACE_METHOD_REFERENCE -> constantPool[i++] = InterfaceMethodReference(
                            classIndex = readUnsignedShort(),
                            nameAndTypeIndex = readUnsignedShort()
                    )
                    CONSTANT_TAG_NAME_AND_TYPE -> constantPool[i++] = NameAndTypeDescriptorReference(
                            nameIndex = readUnsignedShort(),
                            descriptorIndex = readUnsignedShort()
                    )
                    CONSTANT_TAG_METHOD_TYPE -> constantPool[i++] = MethodTypeReference(
                            descriptorIndex = readUnsignedShort()
                    )
                    CONSTANT_TAG_METHOD_HANDLE -> constantPool[i++] = MethodHandleReference(
                            referenceKind = readUnsignedByte(),
                            referenceIndex = readUnsignedShort()
                    )
                    CONSTANT_TAG_INVOKE_DYNAMIC -> constantPool[i++] = InvokeDynamicReference(
                            bootstrapMethodAttrIndex = readUnsignedShort(),
                            nameAndTypeIndex = readUnsignedShort()
                    )
                }
            }
            return ConstantPool(constantPool)
        }
    }
}