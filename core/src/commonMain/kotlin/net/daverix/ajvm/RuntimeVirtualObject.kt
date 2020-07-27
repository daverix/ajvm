/*
    Java Virtual Machine for Android
    Copyright (C) 2017 David Laurell

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
 */
package net.daverix.ajvm


import net.daverix.ajvm.io.*

class RuntimeVirtualObject(
        private val classInfo: ClassInfo,
        private val classLoader: VirtualObjectLoader,
        private val staticLoader: VirtualObjectLoader
) : VirtualObject {
    private val fields: MutableMap<String, Any?> = mutableMapOf()

    override fun invokeMethod(name: String, descriptor: String, args: Array<Any?>): Any? {
        val method = getMethodByNameAndDescriptor(name, descriptor)
                ?: error("Cannot find method $name in class ${classInfo.name}")

        val (maxStack, maxLocals, code) = method.attributes["Code"].info.useDataInputStream { CodeAttribute.read(it) }
        val stack = OperandStack(maxStack)

        val localVariables: Array<Any?> = arrayOfNulls(maxLocals)
        for (i in args.indices) {
            localVariables[i] = args[i]
        }

        val reader = ByteCodeReader(code)
        while (reader.canReadByte()) {
            val byteCodeIndex = reader.index
            val byteCode = reader.readUnsignedByte()
            when (val opcode = fromByteCode(byteCode)) {
                Opcode.RETURN -> return null
                Opcode.IRETURN -> return stack.pop() as Int
                Opcode.LRETURN -> return stack.pop() as Long
                Opcode.FRETURN -> return stack.pop() as Float
                Opcode.DRETURN -> return stack.pop() as Double
                Opcode.ARETURN -> return stack.pop()
                Opcode.NOP -> {
                    // no operation!
                }
                Opcode.ACONST_NULL -> stack.push(null)
                Opcode.ICONST_M1 -> stack.push(-1)
                Opcode.ICONST_0 -> stack.push(0)
                Opcode.ICONST_1 -> stack.push(1)
                Opcode.ICONST_2 -> stack.push(2)
                Opcode.ICONST_3 -> stack.push(3)
                Opcode.ICONST_4 -> stack.push(4)
                Opcode.ICONST_5 -> stack.push(5)
                Opcode.LCONST_0 -> stack.push(0L)
                Opcode.LCONST_1 -> stack.push(1L)
                Opcode.FCONST_0 -> stack.push(0f)
                Opcode.FCONST_1 -> stack.push(1f)
                Opcode.FCONST_2 -> stack.push(2f)
                Opcode.DCONST_0 -> stack.push(0.0)
                Opcode.DCONST_1 -> stack.push(1.0)
                Opcode.BI_PUSH -> stack.push(reader.readUnsignedByte())
                Opcode.SI_PUSH -> stack.push(reader.readUnsignedShort())
                Opcode.LDC -> stack.push(ldc(reader.readUnsignedByte()))
                Opcode.LDC_W -> stack.push(ldc(reader.readUnsignedShort()))
                Opcode.LDC2_W -> {
                    val constant = classInfo.constantPool[reader.readUnsignedShort()]
                    if (constant !is Long && constant !is Double)
                        error("expected $constant to be double or long")

                    stack.push(constant)
                }
                Opcode.ILOAD -> stack.push(localVariables[reader.readUnsignedByte()] as Int)
                Opcode.LLOAD -> stack.push(localVariables[reader.readUnsignedByte()] as Long)
                Opcode.FLOAD -> stack.push(localVariables[reader.readUnsignedByte()] as Float)
                Opcode.DLOAD -> stack.push(localVariables[reader.readUnsignedByte()] as Double)
                Opcode.ALOAD -> stack.push(localVariables[reader.readUnsignedByte()])
                Opcode.ILOAD_0 -> stack.push(localVariables[0] as Int)
                Opcode.ILOAD_1 -> stack.push(localVariables[1] as Int)
                Opcode.ILOAD_2 -> stack.push(localVariables[2] as Int)
                Opcode.ILOAD_3 -> stack.push(localVariables[3] as Int)
                Opcode.LLOAD_0 -> stack.push(localVariables[0] as Long)
                Opcode.LLOAD_1 -> stack.push(localVariables[1] as Long)
                Opcode.LLOAD_2 -> stack.push(localVariables[2] as Long)
                Opcode.LLOAD_3 -> stack.push(localVariables[3] as Long)
                Opcode.FLOAD_0 -> stack.push(localVariables[0] as Float)
                Opcode.FLOAD_1 -> stack.push(localVariables[1] as Float)
                Opcode.FLOAD_2 -> stack.push(localVariables[2] as Float)
                Opcode.FLOAD_3 -> stack.push(localVariables[3] as Float)
                Opcode.DLOAD_0 -> stack.push(localVariables[0] as Double)
                Opcode.DLOAD_1 -> stack.push(localVariables[1] as Double)
                Opcode.DLOAD_2 -> stack.push(localVariables[2] as Double)
                Opcode.DLOAD_3 -> stack.push(localVariables[3] as Double)
                Opcode.ALOAD_0 -> stack.push(localVariables[0])
                Opcode.ALOAD_1 -> stack.push(localVariables[1])
                Opcode.ALOAD_2 -> stack.push(localVariables[2])
                Opcode.ALOAD_3 -> stack.push(localVariables[3])
                Opcode.IALOAD -> {
                    val index = stack.pop() as Int
                    val array = stack.pop() as Array<*>
                    stack.push(array[index] as Int)
                }
                Opcode.LALOAD -> {
                    val index = stack.pop() as Int
                    val array = stack.pop() as Array<*>
                    stack.push(array[index] as Long)
                }
                Opcode.FALOAD -> {
                    val index = stack.pop() as Int
                    val array = stack.pop() as Array<*>
                    stack.push(array[index] as Float)
                }
                Opcode.DALOAD -> {
                    val index = stack.pop() as Int
                    val array = stack.pop() as Array<*>
                    stack.push(array[index] as Double)
                }
                Opcode.AALOAD -> {
                    val index = stack.pop() as Int
                    val array = stack.pop() as Array<*>
                    stack.push(array[index])
                }
                Opcode.BALOAD -> {
                    val index = stack.pop() as Int
                    val array = stack.pop() as Array<*>
                    val value = array[index]
                    if (value !is Byte && value !is Boolean)
                        error("expected $value to be byte or boolean")

                    stack.push(value)
                }
                Opcode.CALOAD -> {
                    val index = stack.pop() as Int
                    val array = stack.pop() as Array<*>
                    stack.push(array[index] as Char)
                }
                Opcode.SALOAD -> {
                    val index = stack.pop() as Int
                    val array = stack.pop() as Array<*>
                    stack.push(array[index] as Short)
                }
                Opcode.ISTORE -> localVariables[reader.readUnsignedByte()] = stack.pop() as Int
                Opcode.LSTORE -> localVariables[reader.readUnsignedByte()] = stack.pop() as Long
                Opcode.FSTORE -> localVariables[reader.readUnsignedByte()] = stack.pop() as Float
                Opcode.DSTORE -> localVariables[reader.readUnsignedByte()] = stack.pop() as Double
                Opcode.ASTORE -> localVariables[reader.readUnsignedByte()] = stack.pop()
                Opcode.ISTORE_0 -> localVariables[0] = stack.pop() as Int
                Opcode.ISTORE_1 -> localVariables[1] = stack.pop() as Int
                Opcode.ISTORE_2 -> localVariables[2] = stack.pop() as Int
                Opcode.ISTORE_3 -> localVariables[3] = stack.pop() as Int
                Opcode.LSTORE_0 -> localVariables[0] = stack.pop() as Long
                Opcode.LSTORE_1 -> localVariables[1] = stack.pop() as Long
                Opcode.LSTORE_2 -> localVariables[2] = stack.pop() as Long
                Opcode.LSTORE_3 -> localVariables[3] = stack.pop() as Long
                Opcode.FSTORE_0 -> localVariables[0] = stack.pop() as Float
                Opcode.FSTORE_1 -> localVariables[1] = stack.pop() as Float
                Opcode.FSTORE_2 -> localVariables[2] = stack.pop() as Float
                Opcode.FSTORE_3 -> localVariables[3] = stack.pop() as Float
                Opcode.DSTORE_0 -> localVariables[0] = stack.pop() as Double
                Opcode.DSTORE_1 -> localVariables[1] = stack.pop() as Double
                Opcode.DSTORE_2 -> localVariables[2] = stack.pop() as Double
                Opcode.DSTORE_3 -> localVariables[3] = stack.pop() as Double
                Opcode.ASTORE_0 -> localVariables[0] = stack.pop()
                Opcode.ASTORE_1 -> localVariables[1] = stack.pop()
                Opcode.ASTORE_2 -> localVariables[2] = stack.pop()
                Opcode.ASTORE_3 -> localVariables[3] = stack.pop()
                Opcode.IASTORE -> {
                    val value = stack.pop() as Int
                    val index = stack.pop() as Int
                    @Suppress("UNCHECKED_CAST") val array = stack.pop() as Array<Int>
                    array[index] = value
                }
                Opcode.LASTORE -> {
                    val value = stack.pop() as Long
                    val index = stack.pop() as Int
                    @Suppress("UNCHECKED_CAST") val array = stack.pop() as Array<Long>
                    array[index] = value
                }
                Opcode.FASTORE -> {
                    val value = stack.pop() as Float
                    val index = stack.pop() as Int
                    @Suppress("UNCHECKED_CAST") val array = stack.pop() as Array<Float>
                    array[index] = value
                }
                Opcode.DASTORE -> {
                    val value = stack.pop() as Double
                    val index = stack.pop() as Int
                    @Suppress("UNCHECKED_CAST") val array = stack.pop() as Array<Double>
                    array[index] = value
                }
                Opcode.AASTORE -> {
                    val value = stack.pop()
                    val index = stack.pop() as Int
                    @Suppress("UNCHECKED_CAST") val array = stack.pop() as Array<Any?>
                    array[index] = value
                }
                Opcode.BASTORE -> {
                    val value = stack.pop()
                    val index = stack.pop() as Int
                    val array = stack.pop()
                    @Suppress("UNCHECKED_CAST")
                    when (value) {
                        is Boolean -> (array as Array<Boolean>)[index] = value
                        is Byte -> (array as Array<Byte>)[index] = value
                        else -> error("$value is not a boolean or a byte")
                    }
                }
                Opcode.CASTORE -> {
                    val value = stack.pop() as Char
                    val index = stack.pop() as Int
                    @Suppress("UNCHECKED_CAST") val array = stack.pop() as Array<Char>
                    array[index] = value
                }
                Opcode.SASTORE -> {
                    val value = stack.pop() as Short
                    val index = stack.pop() as Int
                    @Suppress("UNCHECKED_CAST") val array = stack.pop() as Array<Short>
                    array[index] = value
                }
                Opcode.POP -> stack.pop()
                Opcode.POP2 -> {
                    val value = stack.pop()
                    if (value !is Long && value !is Double)
                        stack.pop()
                }
                Opcode.DUP -> stack.push(stack.peek())
                Opcode.DUP_X1 -> {
                    val val1 = stack.pop()
                    val val2 = stack.pop()
                    val val1Category = val1.getComputationalTypeCategory()
                    val val2Category = val2.getComputationalTypeCategory()
                    if (val1Category != 1 || val2Category != 1)
                        error("values must be of computational type category 1")

                    stack.push(val1)
                    stack.push(val2)
                    stack.push(val1)
                }
                Opcode.DUP_X2 -> {
                    val val1 = stack.pop()
                    val val2 = stack.pop()
                    val val1Category = val1.getComputationalTypeCategory()
                    val val2Category = val2.getComputationalTypeCategory()
                    if (val1Category == 1 && val2Category == 1) {
                        stack.push(val1)
                        stack.push(val2)
                        stack.push(val1)
                    } else {
                        val val3 = stack.pop()
                        val val3Category = val3.getComputationalTypeCategory()
                        if (val1Category == 1 && val2Category == 1 && val3Category == 1) {
                            stack.push(val1)
                            stack.push(val3)
                            stack.push(val2)
                            stack.push(val1)
                        } else {
                            error("$val1, $val2, $val3 must be of computational type category 1")
                        }
                    }
                }
                Opcode.DUP2 -> {
                    val val1 = stack.pop()
                    val val1Category = val1.getComputationalTypeCategory()
                    if (val1Category == 2) {
                        stack.push(val1)
                        stack.push(val1)
                    } else {
                        val val2 = stack.pop()
                        val val2Category = val2.getComputationalTypeCategory()
                        if (val1Category == 1 && val2Category == 1) {
                            stack.push(val2)
                            stack.push(val1)
                            stack.push(val2)
                            stack.push(val1)
                        } else {
                            error("$val1, $val2 must be of computational type category 1")
                        }
                    }
                }
                Opcode.DUP2_X1 -> {
                    val val1 = stack.pop()
                    val val2 = stack.pop()
                    val val1Category = val1.getComputationalTypeCategory()
                    val val2Category = val2.getComputationalTypeCategory()
                    if (val1Category == 2 && val2Category == 1) {
                        stack.push(val1)
                        stack.push(val2)
                        stack.push(val1)
                    } else {
                        val val3 = stack.pop()
                        val val3Category = val2.getComputationalTypeCategory()
                        if (val1Category == 1 && val2Category == 1 && val3Category == 1) {
                            stack.push(val2)
                            stack.push(val1)
                            stack.push(val3)
                            stack.push(val2)
                            stack.push(val1)
                        } else {
                            error("$val1, $val2, $val3 must be of computational type category 1")
                        }
                    }
                }
                Opcode.DUP2_X2 -> {
                    val val1 = stack.pop()
                    val val2 = stack.pop()
                    val val1Category = val1.getComputationalTypeCategory()
                    val val2Category = val2.getComputationalTypeCategory()
                    if (val1Category == 2 && val2Category == 2) {
                        stack.push(val1)
                        stack.push(val2)
                        stack.push(val1)
                    } else {
                        val val3 = stack.pop()
                        val val3Category = val3.getComputationalTypeCategory()
                        if (val1Category == 1 && val2Category == 1 && val3Category == 2) {
                            stack.push(val2)
                            stack.push(val1)
                            stack.push(val3)
                            stack.push(val2)
                            stack.push(val1)
                        } else if (val1Category == 1 && val2Category == 1 && val3Category == 1) {
                            stack.push(val1)
                            stack.push(val3)
                            stack.push(val2)
                            stack.push(val1)
                        } else {
                            val val4 = stack.pop()
                            val val4Category = val4.getComputationalTypeCategory()
                            if (val1Category == 1 && val2Category == 1 && val3Category == 1 && val4Category == 1) {
                                stack.push(val2)
                                stack.push(val1)
                                stack.push(val4)
                                stack.push(val3)
                                stack.push(val2)
                                stack.push(val1)
                            } else {
                                error("$val1, $val2, $val3, $val4 must be of computational type category 1")
                            }
                        }
                    }
                }
                Opcode.SWAP -> {
                    val val1 = stack.pop()
                    val val2 = stack.pop()
                    val val1Category = val1.getComputationalTypeCategory()
                    val val2Category = val2.getComputationalTypeCategory()
                    if (val1Category == 1 && val2Category == 1) {
                        stack.push(val1)
                        stack.push(val2)
                    } else {
                        error("$val1, $val2 must be of computational type category 1")
                    }
                }
                Opcode.IADD -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 + value2)
                }
                Opcode.LADD -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 + value2)
                }
                Opcode.FADD -> {
                    val value2 = stack.pop() as Float
                    val value1 = stack.pop() as Float
                    stack.push(value1 + value2)
                }
                Opcode.DADD -> {
                    val value2 = stack.pop() as Double
                    val value1 = stack.pop() as Double
                    stack.push(value1 + value2)
                }
                Opcode.ISUB -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 - value2)
                }
                Opcode.LSUB -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 - value2)
                }
                Opcode.FSUB -> {
                    val value2 = stack.pop() as Float
                    val value1 = stack.pop() as Float
                    stack.push(value1 - value2)
                }
                Opcode.DSUB -> {
                    val value2 = stack.pop() as Double
                    val value1 = stack.pop() as Double
                    stack.push(value1 - value2)
                }
                Opcode.IMUL -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 * value2)
                }
                Opcode.LMUL -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 * value2)
                }
                Opcode.FMUL -> {
                    val value2 = stack.pop() as Float
                    val value1 = stack.pop() as Float
                    stack.push(value1 * value2)
                }
                Opcode.DMUL -> {
                    val value2 = stack.pop() as Double
                    val value1 = stack.pop() as Double
                    stack.push(value1 * value2)
                }
                Opcode.IDIV -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 / value2)
                }
                Opcode.LDIV -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 / value2)
                }
                Opcode.FDIV -> {
                    val value2 = stack.pop() as Float
                    val value1 = stack.pop() as Float
                    stack.push(value1 / value2)
                }
                Opcode.DDIV -> {
                    val value2 = stack.pop() as Double
                    val value1 = stack.pop() as Double
                    stack.push(value1 / value2)
                }
                Opcode.IREM -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 % value2)
                }
                Opcode.LREM -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 % value2)
                }
                Opcode.FREM -> {
                    val value2 = stack.pop() as Float
                    val value1 = stack.pop() as Float
                    stack.push(value1 % value2)
                }
                Opcode.DREM -> {
                    val value2 = stack.pop() as Double
                    val value1 = stack.pop() as Double
                    stack.push(value1 % value2)
                }
                Opcode.INEG -> {
                    val value = stack.pop() as Int
                    stack.push(-value)
                }
                Opcode.LNEG -> {
                    val value = stack.pop() as Long
                    stack.push(-value)
                }
                Opcode.FNEG -> {
                    val value = stack.pop() as Float
                    stack.push(-value)
                }
                Opcode.DNEG -> {
                    val value = stack.pop() as Double
                    stack.push(-value)
                }
                Opcode.ISHL -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 shl value2)
                }
                Opcode.LSHL -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Long
                    stack.push(value1 shl value2)
                }
                Opcode.ISHR -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 shr value2)
                }
                Opcode.LSHR -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Long
                    stack.push(value1 shr value2)
                }
                Opcode.IUSHR -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 ushr value2)
                }
                Opcode.LUSHR -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Long
                    stack.push(value1 ushr value2)
                }
                Opcode.IAND -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 and value2)
                }
                Opcode.LAND -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 and value2)
                }
                Opcode.IOR -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 or value2)
                }
                Opcode.LOR -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 or value2)
                }
                Opcode.IXOR -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 xor value2)
                }
                Opcode.LXOR -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 xor value2)
                }
                Opcode.IINC -> {
                    val index = reader.readUnsignedByte()
                    val const = reader.readUnsignedByte()
                    localVariables[index] = (localVariables[index] as Int) + const
                }
                Opcode.I2L -> stack.push((stack.pop() as Int).toLong())
                Opcode.I2F -> stack.push((stack.pop() as Int).toFloat())
                Opcode.I2D -> stack.push((stack.pop() as Int).toDouble())
                Opcode.I2B -> stack.push((stack.pop() as Int).toByte())
                Opcode.I2C -> stack.push((stack.pop() as Int).toChar())
                Opcode.I2S -> stack.push((stack.pop() as Int).toShort())
                Opcode.L2I -> stack.push((stack.pop() as Long).toInt())
                Opcode.L2F -> stack.push((stack.pop() as Long).toFloat())
                Opcode.L2D -> stack.push((stack.pop() as Long).toDouble())
                Opcode.F2I -> stack.push((stack.pop() as Float).toInt())
                Opcode.F2L -> stack.push((stack.pop() as Float).toLong())
                Opcode.F2D -> stack.push((stack.pop() as Float).toDouble())
                Opcode.D2I -> stack.push((stack.pop() as Double).toInt())
                Opcode.D2L -> stack.push((stack.pop() as Double).toLong())
                Opcode.D2F -> stack.push((stack.pop() as Double).toFloat())
                Opcode.LCMP -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(when {
                        value1 == value2 -> 0
                        value1 > value2 -> 1
                        else -> -1
                    })
                }
                Opcode.FCMPL,
                Opcode.FCMPG -> {
                    val value2 = stack.pop() as Float
                    val value1 = stack.pop() as Float
                    stack.push(when {
                        value1.isNaN() || value2.isNaN() -> if (opcode == Opcode.FCMPG) 1 else -1
                        value1 == value2 -> 0
                        value1 > value2 -> 1
                        value1 < value2 -> -1
                        else -> if (opcode == Opcode.FCMPG) 1 else -1
                    })
                }
                Opcode.DCMPL,
                Opcode.DCMPG -> {
                    val value2 = stack.pop() as Double
                    val value1 = stack.pop() as Double
                    stack.push(when {
                        value1.isNaN() || value2.isNaN() -> if (opcode == Opcode.DCMPG) 1 else -1
                        value1 == value2 -> 0
                        value1 > value2 -> 1
                        value1 < value2 -> -1
                        else -> if (opcode == Opcode.DCMPG) 1 else -1
                    })
                }
                Opcode.IFEQ,
                Opcode.IFNE,
                Opcode.IFLT,
                Opcode.IFGE,
                Opcode.IFGT,
                Opcode.IFLE -> {
                    val offset = reader.readUnsignedShort()
                    val value = stack.pop() as Int
                    if (opcode == Opcode.IFEQ && value == 0 ||
                            opcode == Opcode.IFNE && value != 0 ||
                            opcode == Opcode.IFLT && value < 0 ||
                            opcode == Opcode.IFLE && value <= 0 ||
                            opcode == Opcode.IFGT && value > 0 ||
                            opcode == Opcode.IFGE && value >= 0) {
                        reader.jumpTo(byteCodeIndex + offset)
                    }
                }
                Opcode.IF_ICMPEQ,
                Opcode.IF_ICMPNE,
                Opcode.IF_ICMPLT,
                Opcode.IF_ICMPGE,
                Opcode.IF_ICMPGT,
                Opcode.IF_ICMPLE -> {
                    val offset = reader.readUnsignedShort()
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    if (opcode == Opcode.IF_ICMPEQ && value1 == value2 ||
                            opcode == Opcode.IF_ICMPNE && value1 != value2 ||
                            opcode == Opcode.IF_ICMPLT && value1 < value2 ||
                            opcode == Opcode.IF_ICMPLE && value1 <= value2 ||
                            opcode == Opcode.IF_ICMPGT && value1 > value2 ||
                            opcode == Opcode.IF_ICMPGE && value1 >= value2) {
                        reader.jumpTo(byteCodeIndex + offset)
                    }
                }
                Opcode.IF_ACMPEQ,
                Opcode.IF_ACMPNE -> {
                    val offset = reader.readUnsignedShort()
                    val value2 = stack.pop()
                    val value1 = stack.pop()
                    if (opcode == Opcode.IF_ACMPEQ && value1 === value2 ||
                            opcode == Opcode.IF_ACMPNE && value1 !== value2) {
                        reader.jumpTo(byteCodeIndex + offset)
                    }
                }
                Opcode.GOTO -> {
                    val gotoOffset = reader.readUnsignedShort()
                    reader.jumpTo(byteCodeIndex + gotoOffset)
                }
                Opcode.JSR, Opcode.JSR_W -> {
                    val offset = if (opcode == Opcode.JSR_W) reader.readInt() else reader.readUnsignedShort()
                    stack.push(byteCodeIndex)
                    reader.jumpTo(byteCodeIndex + offset)
                }
                Opcode.RET -> {
                    val index = reader.readUnsignedByte()
                    val address = localVariables[index] as Int
                    reader.jumpTo(address)
                }
                Opcode.TABLESWITCH -> tableSwitch(reader, byteCodeIndex, stack)
                Opcode.LOOKUPSWITCH -> lookupSwitch(reader, byteCodeIndex, stack)
                Opcode.GETSTATIC -> {
                    val index = reader.readUnsignedShort()
                    val fieldReference = classInfo.constantPool[index] as FieldReference
                    val staticClass = getStaticClassByClassIndex(fieldReference.classIndex)

                    val value = getInstanceFieldValue(staticClass, fieldReference)
                    stack.push(value)
                }
                Opcode.PUTSTATIC -> {
                    val index = reader.readUnsignedShort()
                    val value = stack.pop()

                    val fieldReference = classInfo.constantPool[index] as FieldReference
                    val staticClass = getStaticClassByClassIndex(fieldReference.classIndex)

                    setInstanceFieldValue(staticClass, fieldReference, value)
                }
                Opcode.GETFIELD -> {
                    val index = reader.readUnsignedShort()
                    val instance = stack.pop() as VirtualObject

                    val fieldReference = classInfo.constantPool[index] as FieldReference
                    val value = getInstanceFieldValue(instance, fieldReference)
                    stack.push(value)
                }
                Opcode.PUTFIELD -> {
                    val index = reader.readUnsignedShort()
                    val value = stack.pop()
                    val instance = stack.pop() as VirtualObject

                    val fieldReference = classInfo.constantPool[index] as FieldReference
                    setInstanceFieldValue(instance, fieldReference, value)
                }
                Opcode.INVOKEVIRTUAL -> {
                    val methodReferenceIndex = reader.readUnsignedShort()
                    val otherMethod = getMethod(classInfo.constantPool, methodReferenceIndex)
                    val methodArgs = stack.popMultiple(otherMethod.descriptor.parameters.size)
                    val instance = stack.pop()
                    val returnValue = invokeMethodOnInstance(instance, otherMethod, methodArgs)
                    if (returnValue is ReturnValue.Value) {
                        stack.push(returnValue.value)
                    }
                }
                Opcode.INVOKESPECIAL -> {
                    //TODO: create another method here that calls invokeMethod and does additional checks as described in...
                    //http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.invokespecial
                    val methodReferenceIndex = reader.readUnsignedShort()
                    val otherMethod = getMethod(classInfo.constantPool, methodReferenceIndex)
                    val methodArgs = stack.popMultiple(otherMethod.descriptor.parameters.size)
                    val instance = stack.pop()
                    val returnValue = invokeMethodOnInstance(instance, otherMethod, methodArgs)
                    if (returnValue is ReturnValue.Value) {
                        stack.push(returnValue.value)
                    }
                }
                Opcode.INVOKESTATIC -> {
                    val methodReferenceIndex = reader.readUnsignedShort()
                    val otherMethod = getMethod(classInfo.constantPool, methodReferenceIndex)
                    val methodArgs = stack.popMultiple(otherMethod.descriptor.parameters.size)
                    val staticClass = staticLoader.load(otherMethod.className)
                    val returnValue = invokeMethodOnInstance(staticClass, otherMethod, methodArgs)
                    if (returnValue is ReturnValue.Value) {
                        stack.push(returnValue.value)
                    }
                }
                Opcode.INVOKEINTERFACE -> TODO()
                Opcode.INVOKEDYNAMIC -> TODO()
                Opcode.NEW -> {
                    val newObjectIndex = reader.readUnsignedShort()
                    stack.push(new(newObjectIndex))
                }
                Opcode.NEWARRAY -> TODO()
                Opcode.ANEWARRAY -> TODO()
                Opcode.ARRAYLENGTH -> TODO()
                Opcode.ATHROW -> TODO()
                Opcode.CHECKCAST -> TODO()
                Opcode.INSTANCEOF -> TODO()
                Opcode.MONITORENTER -> TODO()
                Opcode.MONITOREXIT -> TODO()
                Opcode.WIDE -> TODO()
                Opcode.MULTIANEWARRAY -> TODO()
                Opcode.IFNULL -> TODO()
                Opcode.IFNONNULL -> TODO()
                Opcode.GOTO_W -> TODO()
                Opcode.BREAKPOINT -> TODO()
                Opcode.IMPDEP1 -> TODO()
                Opcode.IMPDEP2 -> TODO()
                else -> {
                    val byteCodeName = opcode?.name
                    val hexCode = byteCode.toString(16)
                    error("Unknown bytecode: $byteCodeName ($hexCode) at position $byteCodeIndex")
                }
            }

        }

        return null
    }

    private fun getInstanceFieldValue(instance: VirtualObject, fieldReference: FieldReference): Any? {
        val fieldNameAndType = classInfo.constantPool[fieldReference.nameAndTypeIndex] as NameAndTypeDescriptorReference
        val fieldName = classInfo.constantPool[fieldNameAndType.nameIndex] as String
        val value = instance.getFieldValue(fieldName)
        return value
    }

    private fun setInstanceFieldValue(instance: VirtualObject, fieldReference: FieldReference, value: Any?) {
        val fieldNameAndType = classInfo.constantPool[fieldReference.nameAndTypeIndex] as NameAndTypeDescriptorReference
        val fieldName = classInfo.constantPool[fieldNameAndType.nameIndex] as String

        instance.setFieldValue(fieldName, value)
    }

    override fun getFieldValue(fieldName: String): Any? {
        return fields[fieldName]
    }

    override fun setFieldValue(fieldName: String, value: Any?) {
        fields[fieldName] = value
    }

    private fun Any?.getComputationalTypeCategory(): Int = when (this) {
        this is Long || this is Double -> 2
        else -> 1
    }

    private fun tableSwitch(reader: ByteCodeReader, byteCodeIndex: Int, stack: OperandStack) {
        reader.skip((byteCodeIndex + 1) % 4)
        val defaultValue = reader.readInt()
        val low = reader.readInt()
        val high = reader.readInt()
        if (low > high) {
            error("low is higher than high: $low > $high")
        }

        val offsetWidth = high - low + 1
        val table = IntArray(offsetWidth) { reader.readInt() }
        val index = stack.pop() as Int
        val targetAddress = if (index < low || index > high) {
            byteCodeIndex + defaultValue
        } else {
            byteCodeIndex + table[index - low]
        }
        reader.jumpTo(targetAddress)
    }

    private fun lookupSwitch(reader: ByteCodeReader, byteCodeIndex: Int, stack: OperandStack) {
        reader.skip((byteCodeIndex + 1) % 4)
        val defaultValue = reader.readInt()
        val npairs = reader.readInt()
        if(npairs < 0) error("npairs be >= 0")
        val pairs = mutableMapOf<Int,Int>()
        repeat(npairs) {
            val key = reader.readInt()
            val value = reader.readInt()
            pairs += key to value
        }
        val key = stack.pop() as Int
        reader.jumpTo(byteCodeIndex + (pairs[key] ?: defaultValue))
    }

    private fun invokeMethodOnInstance(instance: Any?, method: Method, methodArgs: Array<Any?>): ReturnValue {
        return when {
            instance is VirtualObject -> {
                val value = instance.invokeMethod(method.name, method.rawDescriptor, methodArgs)
                if (method.descriptor.returnType == "V") ReturnValue.Void else ReturnValue.Value(value)
            }
            instance is String && method.name == "hashCode" -> ReturnValue.Value(instance.hashCode())
            instance is String && method.name == "equals" -> ReturnValue.Value(if (instance == methodArgs[0]) 1 else 0)
            instance is Int && method.name == "equals" -> ReturnValue.Value(if (instance === methodArgs[0]) 1 else 0)
            instance == null && method.name == "equals" -> ReturnValue.Value(if (instance == methodArgs[0]) 1 else 0)
            else -> throw UnsupportedOperationException("don't know how to handle popped value \"$instance\" for method \"${method.name} ${method.rawDescriptor}\" with args [${methodArgs.joinToString()}]")
        }
    }

    private sealed class ReturnValue {
        object Void : ReturnValue()
        class Value(val value: Any?) : ReturnValue()
    }

    private fun ldc(ldcIndex: Int): Any? =
            when (val constant = classInfo.constantPool[ldcIndex]) {
                is Int, is Float, is String, is MethodHandleReference -> constant
                is StringReference -> classInfo.constantPool[constant.index]!!
                is ClassReference -> TODO("how to get reference to class by name in constantPool?")
                else -> error("Fix returning value for $constant")
            }

    private fun new(newObjectIndex: Int): VirtualObject {
        val classRef = classInfo.constantPool[newObjectIndex] as ClassReference
        val className = classInfo.constantPool[classRef.nameIndex] as String

        return classLoader.load(className)
    }

    private fun getStaticClassByClassIndex(classIndex: Int): VirtualObject {
        val classReference = classInfo.constantPool[classIndex] as ClassReference
        val className = classInfo.constantPool[classReference.nameIndex] as String

        return staticLoader.load(className)
    }

    private operator fun Array<AttributeInfo>.get(name: String): AttributeInfo {
        return first { classInfo.constantPool[it.nameIndex] == name }
    }

    private fun getMethodByNameAndDescriptor(methodName: String, descriptor: String): MethodInfo? {
        val methods = classInfo.methods
        val constantPool = classInfo.constantPool

        return methods.firstOrNull {
            constantPool[it.nameIndex] == methodName && constantPool[it.descriptorIndex] == descriptor
        }
    }
}
