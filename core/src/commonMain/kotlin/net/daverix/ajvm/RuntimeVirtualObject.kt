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
import kotlin.math.abs

class RuntimeVirtualObject(
        private val classInfo: ClassInfo,
        private val classLoader: VirtualObjectLoader,
        private val staticLoader: VirtualObjectLoader
) : VirtualObject {
    override val fields: Map<String, Any> = HashMap()
    override val name: String
            get() = classInfo.name

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
            when (val byteCode = fromByteCode(reader.readUnsignedByte())) {
                Opcode.RETURN -> return null
                Opcode.IRETURN -> return stack.pop()
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
                Opcode.BI_PUSH -> TODO()
                Opcode.SI_PUSH -> TODO()
                Opcode.LDC -> {
                    val ldcIndex = reader.readUnsignedByte()
                    ldc(ldcIndex, stack)
                }
                Opcode.LDC_W -> TODO()
                Opcode.LDC2_W -> TODO()
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
                Opcode.IALOAD -> TODO()
                Opcode.LALOAD -> TODO()
                Opcode.FALOAD -> TODO()
                Opcode.DALOAD -> TODO()
                Opcode.AALOAD -> {
                    val index = stack.pop() as Int
                    val array = stack.pop() as Array<*>
                    stack.push(array[index])
                }
                Opcode.BALOAD -> TODO()
                Opcode.CALOAD -> TODO()
                Opcode.SALOAD -> TODO()
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
                Opcode.IASTORE -> TODO()
                Opcode.LASTORE -> TODO()
                Opcode.FASTORE -> TODO()
                Opcode.DASTORE -> TODO()
                Opcode.AASTORE -> TODO()
                Opcode.BASTORE -> TODO()
                Opcode.CASTORE -> TODO()
                Opcode.SASTORE -> TODO()
                Opcode.POP -> TODO()
                Opcode.POP2 -> TODO()
                Opcode.DUP -> stack.push(stack.peek())
                Opcode.DUP_X1 -> TODO()
                Opcode.DUP_X2 -> TODO()
                Opcode.DUP2 -> TODO()
                Opcode.DUP2_X1 -> TODO()
                Opcode.DUP2_X2 -> TODO()
                Opcode.SWAP -> TODO()
                Opcode.IADD -> {
                    val iAdd2 = stack.pop() as Int
                    val iAdd1 = stack.pop() as Int
                    stack.push(iAdd1 + iAdd2)
                }
                Opcode.LADD -> TODO()
                Opcode.FADD -> TODO()
                Opcode.DADD -> TODO()
                Opcode.ISUB -> {
                    val iSub2 = stack.pop() as Int
                    val iSub1 = stack.pop() as Int
                    stack.push(iSub1 - iSub2)
                }
                Opcode.LSUB -> TODO()
                Opcode.FSUB -> TODO()
                Opcode.DSUB -> TODO()
                Opcode.IMUL -> {
                    val iMul2 = stack.pop() as Int
                    val iMul1 = stack.pop() as Int
                    stack.push(iMul1 * iMul2)
                }
                Opcode.LMUL -> TODO()
                Opcode.FMUL -> TODO()
                Opcode.DMUL -> TODO()
                Opcode.IDIV -> {
                    val iDiv2 = stack.pop() as Int
                    val iDiv1 = stack.pop() as Int
                    stack.push(iDiv1 / iDiv2)
                }
                Opcode.LDIV -> TODO()
                Opcode.FDIV -> TODO()
                Opcode.DDIV -> TODO()
                Opcode.IREM -> {
                    val iAdd2 = stack.pop() as Int
                    val iAdd1 = stack.pop() as Int
                    stack.push(iAdd1 % iAdd2)
                }
                Opcode.LREM -> TODO()
                Opcode.FREM -> TODO()
                Opcode.DREM -> TODO()
                Opcode.INEG -> TODO()
                Opcode.LNEG -> TODO()
                Opcode.FNEG -> TODO()
                Opcode.DNEG -> TODO()
                Opcode.ISHL -> TODO()
                Opcode.LSHL -> TODO()
                Opcode.ISHR -> TODO()
                Opcode.LSHR -> TODO()
                Opcode.IUSHR -> TODO()
                Opcode.LUSHR -> TODO()
                Opcode.IAND -> TODO()
                Opcode.LAND -> TODO()
                Opcode.IOR -> TODO()
                Opcode.LOR -> TODO()
                Opcode.IXOR -> TODO()
                Opcode.LXOR -> TODO()
                Opcode.IINC -> TODO()
                Opcode.I2L -> TODO()
                Opcode.I2F -> TODO()
                Opcode.I2D -> TODO()
                Opcode.L2I -> stack.push((stack.pop() as Long).toInt())
                Opcode.L2F -> stack.push((stack.pop() as Long).toFloat())
                Opcode.L2D -> stack.push((stack.pop() as Long).toDouble())
                Opcode.F2I -> stack.push((stack.pop() as Float).toInt())
                Opcode.F2L -> stack.push((stack.pop() as Float).toLong())
                Opcode.F2D -> stack.push((stack.pop() as Float).toDouble())
                Opcode.D2I -> stack.push((stack.pop() as Double).toInt())
                Opcode.D2L -> stack.push((stack.pop() as Double).toLong())
                Opcode.D2F -> stack.push((stack.pop() as Double).toFloat())
                Opcode.I2B -> stack.push((stack.pop() as Int).toByte())
                Opcode.I2C -> stack.push((stack.pop() as Int).toChar())
                Opcode.I2S -> stack.push((stack.pop() as Int).toShort())
                Opcode.LCMP -> TODO()
                Opcode.FCMPL -> TODO()
                Opcode.FCMPG -> TODO()
                Opcode.DCMPL -> TODO()
                Opcode.DCMPG -> TODO()
                Opcode.IFEQ -> {
                    val ifEqOffset = reader.readUnsignedShort()
                    val value = stack.pop() as Int
                    if (value == 0) {
                        reader.jumpTo(byteCodeIndex + ifEqOffset)
                    }
                }
                Opcode.IFNE -> TODO()
                Opcode.IFLT -> TODO()
                Opcode.IFGE -> TODO()
                Opcode.IFGT -> TODO()
                Opcode.IFLE -> TODO()
                Opcode.IF_ICMPEQ -> TODO()
                Opcode.IF_ICMPNE -> TODO()
                Opcode.IF_ICMPLT -> TODO()
                Opcode.IF_ICMPGE -> TODO()
                Opcode.IF_ICMPGT -> TODO()
                Opcode.IF_ICMPLE -> TODO()
                Opcode.IF_ACMPEQ -> TODO()
                Opcode.IF_ACMPNE -> TODO()
                Opcode.GOTO -> {
                    val gotoOffset = reader.readUnsignedShort()
                    reader.jumpTo(byteCodeIndex + gotoOffset)
                }
                Opcode.JSR -> TODO()
                Opcode.RET -> TODO()
                Opcode.TABLESWITCH -> tableSwitch(reader, byteCodeIndex, stack)
                Opcode.LOOKUPSWITCH -> TODO()
                Opcode.LRETURN -> TODO()
                Opcode.FRETURN -> TODO()
                Opcode.DRETURN -> TODO()
                Opcode.ARETURN -> TODO()
                Opcode.GETSTATIC -> {
                    val staticFieldIndex = reader.readUnsignedShort()
                    stack.push(getStaticFieldValue(staticFieldIndex))
                }
                Opcode.PUTSTATIC -> TODO()
                Opcode.GETFIELD -> TODO()
                Opcode.PUTFIELD -> TODO()
                Opcode.INVOKEVIRTUAL -> {
                    val methodReferenceIndex = reader.readUnsignedShort()
                    val otherMethod = getMethod(classInfo.constantPool, methodReferenceIndex)
                    val methodArgs = stack.popMultiple(otherMethod.descriptor.parameters.size)
                    val instance = stack.pop()
                    val returnValue = invokeMethodOnInstance(instance, otherMethod, methodArgs)
                    if(returnValue is ReturnValue.Value) {
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
                    if(returnValue is ReturnValue.Value) {
                        stack.push(returnValue.value)
                    }
                }
                Opcode.INVOKESTATIC -> {
                    val methodReferenceIndex = reader.readUnsignedShort()
                    val otherMethod = getMethod(classInfo.constantPool, methodReferenceIndex)
                    val methodArgs = stack.popMultiple(otherMethod.descriptor.parameters.size)
                    val staticClass = staticLoader.load(otherMethod.className)
                    val returnValue = invokeMethodOnInstance(staticClass, otherMethod, methodArgs)
                    if(returnValue is ReturnValue.Value) {
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
                Opcode.JSR_W -> TODO()
                Opcode.BREAKPOINT -> TODO()
                Opcode.IMPDEP1 -> TODO()
                Opcode.IMPDEP2 -> TODO()
                else -> {
                    val byteCodeName = byteCode.name
                    val hexCode = byteCode.byteCode.toString(16)
                    error("Unknown bytecode: $byteCodeName ($hexCode) at position $byteCodeIndex")
                }
            }

        }

        return null
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
        val tableIndex = stack.pop() as Int
        val targetAddress = if (tableIndex < low || tableIndex > high) {
            // TODO: why would Math.abs be needed to turn for example -120 into 120 here?
            byteCodeIndex + abs(defaultValue)
        } else {
            byteCodeIndex + table[tableIndex - low]
        }
        reader.jumpTo(targetAddress)
    }

    private fun invokeMethodOnInstance(instance: Any?, method: Method, methodArgs: Array<Any?>): ReturnValue {
        return when {
            instance is VirtualObject -> {
                val value = instance.invokeMethod(method.name, method.rawDescriptor, methodArgs)
                if(method.descriptor.returnType == "V") ReturnValue.Void else ReturnValue.Value(value)
            }
            instance is String && method.name == "hashCode" -> ReturnValue.Value(instance.hashCode())
            instance is String && method.name == "equals" -> ReturnValue.Value(if (instance == methodArgs[0]) 1 else 0)
            instance is Int && method.name == "equals" -> ReturnValue.Value(if (instance === methodArgs[0]) 1 else 0)
            else -> throw UnsupportedOperationException("don't know how to handle popped value \"$instance\" for method \"$method\" with args [${methodArgs.joinToString()}]")
        }
    }

    private sealed class ReturnValue {
        object Void : ReturnValue()
        class Value(val value: Any?): ReturnValue()
    }

    private fun ldc(ldcIndex: Int, stack: OperandStack) {
        when (val constant = classInfo.constantPool[ldcIndex]) {
            is Int, is Float, is String, is MethodHandleReference -> stack.push(constant)
            is StringReference -> stack.push(classInfo.constantPool[constant.index]!!)
        }
    }

    private fun new(newObjectIndex: Int): VirtualObject {
        val classRef = classInfo.constantPool[newObjectIndex] as ClassReference
        val className = classInfo.constantPool[classRef.nameIndex] as String

        return classLoader.load(className)
    }

    private fun getStaticFieldValue(staticFieldIndex: Int): Any? {
        val fieldReference = classInfo.constantPool[staticFieldIndex] as FieldReference
        val fieldNameAndType = classInfo.constantPool[fieldReference.nameAndTypeIndex] as NameAndTypeDescriptorReference
        val fieldName = classInfo.constantPool[fieldNameAndType.nameIndex] as String

        val classReference = classInfo.constantPool[fieldReference.classIndex] as ClassReference
        val fieldClassName = classInfo.constantPool[classReference.nameIndex] as String

        val staticClass = staticLoader.load(fieldClassName)
        return staticClass.fields[fieldName]
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
