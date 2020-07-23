package net.daverix.ajvm.io

import net.daverix.ajvm.wrapToDouble
import net.daverix.ajvm.wrapToLong

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

    companion object {
        private const val CONSTANT_TAG_STRING = 1
        private const val CONSTANT_TAG_INTEGER = 3
        private const val CONSTANT_TAG_FLOAT = 4
        private const val CONSTANT_TAG_LONG = 5
        private const val CONSTANT_TAG_DOUBLE = 6
        private const val CONSTANT_TAG_CLASS_REFERENCE = 7
        private const val CONSTANT_TAG_STRING_REFERENCE = 8
        private const val CONSTANT_TAG_FIELD_REFERENCE = 9
        private const val CONSTANT_TAG_METHOD_REFERENCE = 10
        private const val CONSTANT_TAG_INTERFACE_METHOD_REFERENCE = 11
        private const val CONSTANT_TAG_NAME_AND_TYPE_DESCRIPTOR = 12
        private const val CONSTANT_TAG_METHOD_HANDLE = 15
        private const val CONSTANT_TAG_NAME_METHOD_TYPE = 16
        private const val CONSTANT_TAG_INVOKE_DYNAMIC = 18

        fun DataInputStream.readConstantPool(): ConstantPool {
            val constantPoolCount = readUnsignedShort()
            val constantPool = arrayOfNulls<Any?>(constantPoolCount)
            var i = 1
            while (i < constantPoolCount) {
                when (readUnsignedByte()) {
                    -1 -> error("EOF when reading type from constant pool")
                    CONSTANT_TAG_STRING -> {
                        constantPool[i] = readUTF()
                        i++
                    }
                    CONSTANT_TAG_INTEGER -> {
                        constantPool[i] = readInt()
                        i++
                    }
                    CONSTANT_TAG_FLOAT -> {
                        constantPool[i] = readFloat()
                        i++
                    }
                    CONSTANT_TAG_LONG -> {
                        val longBytes = ByteArray(8)
                        if (read(longBytes, 0, 4) != 4)
                            error("could not read first part of long")

                        val constantType = readUnsignedShort()
                        if (constantType != CONSTANT_TAG_LONG)
                            error("expecting to read another constant pool for long value (got $constantType)")
                        if (read(longBytes, 4, 4) != 4)
                            error("could not read second part of long")

                        constantPool[i] = longBytes.wrapToLong()
                        // we advance index one additional time because we are sure that the second
                        // part comes directly after
                        i+=2
                    }
                    CONSTANT_TAG_DOUBLE -> {
                        val doubleBytes = ByteArray(8)
                        if (read(doubleBytes, 0, 4) != 4)
                            error("could not read first part of double")

                        val constantType = readUnsignedShort()
                        if (constantType != CONSTANT_TAG_LONG)
                            error("expecting to read another constant pool for double value (got $constantType)")
                        if (read(doubleBytes, 4, 4) != 4)
                            error("could not read second part of double")

                        constantPool[i] = doubleBytes.wrapToDouble()
                        // we advance index one additional time because we are sure that the second
                        // part comes directly after
                        i+=2
                    }
                    CONSTANT_TAG_CLASS_REFERENCE -> {
                        constantPool[i] = ClassReference(nameIndex = readUnsignedShort())
                        i++
                    }
                    CONSTANT_TAG_STRING_REFERENCE -> {
                        constantPool[i] = StringReference(index = readUnsignedShort())
                        i++
                    }
                    CONSTANT_TAG_FIELD_REFERENCE -> {
                        constantPool[i] = FieldReference(
                                classIndex = readUnsignedShort(),
                                nameAndTypeIndex = readUnsignedShort()
                        )
                        i++
                    }
                    CONSTANT_TAG_METHOD_REFERENCE -> {
                        constantPool[i] = MethodReference(
                                classIndex = readUnsignedShort(),
                                nameAndTypeIndex = readUnsignedShort()
                        )
                        i++
                    }
                    CONSTANT_TAG_INTERFACE_METHOD_REFERENCE -> {
                        constantPool[i] = InterfaceMethodReference(
                                classIndex = readUnsignedShort(),
                                nameAndTypeIndex = readUnsignedShort()
                        )
                        i++
                    }
                    CONSTANT_TAG_NAME_AND_TYPE_DESCRIPTOR -> {
                        constantPool[i] = NameAndTypeDescriptorReference(
                                nameIndex = readUnsignedShort(),
                                descriptorIndex = readUnsignedShort()
                        )
                        i++
                    }
                    CONSTANT_TAG_NAME_METHOD_TYPE -> {
                        constantPool[i] = MethodTypeReference(descriptorIndex = readUnsignedShort())
                        i++
                    }
                    CONSTANT_TAG_METHOD_HANDLE -> {
                        constantPool[i] = MethodHandleReference(
                                referenceKind = readUnsignedByte(),
                                referenceIndex = readUnsignedShort()
                        )
                        i++
                    }
                    CONSTANT_TAG_INVOKE_DYNAMIC -> constantPool[i] = {
                        constantPool[i] = InvokeDynamicReference(
                                bootstrapMethodAttrIndex = readUnsignedShort(),
                                nameAndTypeIndex = readUnsignedShort()
                        )
                        i++
                    }
                }
            }
            return ConstantPool(constantPool)
        }
    }
}
