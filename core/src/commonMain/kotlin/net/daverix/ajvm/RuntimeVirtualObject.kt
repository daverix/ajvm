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
        private val loadObject: suspend (String) -> VirtualObject,
        private val loadStaticObject: suspend (String) -> VirtualObject
) : VirtualObject {
    private val fields: MutableMap<String, Any?> = mutableMapOf()

    override suspend fun invokeMethod(name: String, descriptor: String, args: Array<Any?>): Any? {
        val method = getMethodByNameAndDescriptor(name, descriptor)
                ?: error("Cannot find method $name in class ${classInfo.name}")

        val (maxStack, maxLocals, code, byteCodeTable) = method.codeAttribute
                ?: error("code attribute not found")
        val stack = OperandStack(maxStack)

        val localVariables: Array<Any?> = arrayOfNulls(maxLocals)
        for (i in args.indices) {
            localVariables[i] = args[i]
        }

        var index = 0
        while (index < code.size) {
            when (val operation = code[index]) {
                Operation.Return -> return null
                Operation.IReturn -> return stack.pop() as Int
                Operation.LReturn -> return stack.pop() as Long
                Operation.FReturn -> return stack.pop() as Float
                Operation.DReturn -> return stack.pop() as Double
                Operation.AReturn -> return stack.pop()
                Operation.NOP -> {
                    // no operation!
                }
                Operation.AConstNull -> stack.push(null)
                is Operation.IConst -> stack.push(operation.value)
                is Operation.LConst -> stack.push(operation.value)
                is Operation.FConst -> stack.push(operation.value)
                is Operation.DConst -> stack.push(operation.value)
                is Operation.Ldc -> stack.push(ldc(operation.index))
                is Operation.Ldc2 -> {
                    val constant = classInfo.constantPool[operation.index]
                    if (constant !is Long && constant !is Double)
                        error("expected $constant to be double or long")

                    stack.push(constant)
                }
                is Operation.ILoad -> stack.push(localVariables[operation.index] as Int)
                is Operation.LLoad -> stack.push(localVariables[operation.index] as Long)
                is Operation.FLoad -> stack.push(localVariables[operation.index] as Float)
                is Operation.DLoad -> stack.push(localVariables[operation.index] as Double)
                is Operation.ALoad -> stack.push(localVariables[operation.index])
                Operation.IALoad -> {
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as IntArray
                    stack.push(array[arrayIndex])
                }
                Operation.LALoad -> {
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as LongArray
                    stack.push(array[arrayIndex])
                }
                Operation.FALoad -> {
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as FloatArray
                    stack.push(array[arrayIndex])
                }
                Operation.DALoad -> {
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as DoubleArray
                    stack.push(array[arrayIndex])
                }
                Operation.AALoad -> {
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as Array<*>
                    stack.push(array[arrayIndex])
                }
                Operation.BALoad -> {
                    val arrayIndex = stack.pop() as Int
                    when (val array = stack.pop()) {
                        is ByteArray -> stack.push(array[arrayIndex].toInt())
                        is BooleanArray -> stack.push(if (array[arrayIndex]) 1 else 0)
                        else -> error("expected array to be of type byte or boolean")
                    }
                }
                Operation.CALoad -> {
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as CharArray
                    stack.push(array[arrayIndex])
                }
                Operation.SALoad -> {
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as ShortArray
                    stack.push(array[arrayIndex])
                }
                is Operation.IStore -> localVariables[operation.index] = stack.pop() as Int
                is Operation.LStore -> localVariables[operation.index] = stack.pop() as Long
                is Operation.FStore -> localVariables[operation.index] = stack.pop() as Float
                is Operation.DStore -> localVariables[operation.index] = stack.pop() as Double
                is Operation.AStore -> localVariables[operation.index] = stack.pop()
                Operation.IAStore -> {
                    val value = stack.pop() as Int
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as IntArray
                    array[arrayIndex] = value
                }
                Operation.LAStore -> {
                    val value = stack.pop() as Long
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as LongArray
                    array[arrayIndex] = value
                }
                Operation.FAStore -> {
                    val value = stack.pop() as Float
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as FloatArray
                    array[arrayIndex] = value
                }
                Operation.DAStore -> {
                    val value = stack.pop() as Double
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as DoubleArray
                    array[arrayIndex] = value
                }
                Operation.AAStore -> {
                    val value = stack.pop()
                    val arrayIndex = stack.pop() as Int
                    @Suppress("UNCHECKED_CAST") val array = stack.pop() as Array<Any?>
                    array[arrayIndex] = value
                }
                Operation.BAStore -> {
                    val value = stack.pop()
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop()
                    when (value) {
                        is Byte -> (array as ByteArray)[arrayIndex] = value
                        is Boolean -> (array as BooleanArray)[arrayIndex] = value
                        else -> error("expected value $value to be byte or boolean")
                    }
                }
                Operation.CAStore -> {
                    val value = stack.pop() as Char
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as CharArray
                    array[arrayIndex] = value
                }
                Operation.SAStore -> {
                    val value = stack.pop() as Short
                    val arrayIndex = stack.pop() as Int
                    val array = stack.pop() as ShortArray
                    array[arrayIndex] = value
                }
                Operation.Pop -> stack.pop()
                Operation.Pop2 -> {
                    val value = stack.pop()
                    if (value !is Long && value !is Double)
                        stack.pop()
                }
                Operation.Dup -> stack.push(stack.peek())
                Operation.DupX1 -> {
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
                Operation.DupX2 -> {
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
                Operation.Dup2 -> {
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
                Operation.Dup2X1 -> {
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
                Operation.Dup2X2 -> {
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
                Operation.Swap -> {
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
                Operation.IAdd -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 + value2)
                }
                Operation.LAdd -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 + value2)
                }
                Operation.FAdd -> {
                    val value2 = stack.pop() as Float
                    val value1 = stack.pop() as Float
                    stack.push(value1 + value2)
                }
                Operation.DAdd -> {
                    val value2 = stack.pop() as Double
                    val value1 = stack.pop() as Double
                    stack.push(value1 + value2)
                }
                Operation.ISub -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 - value2)
                }
                Operation.LSub -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 - value2)
                }
                Operation.FSub -> {
                    val value2 = stack.pop() as Float
                    val value1 = stack.pop() as Float
                    stack.push(value1 - value2)
                }
                Operation.DSub -> {
                    val value2 = stack.pop() as Double
                    val value1 = stack.pop() as Double
                    stack.push(value1 - value2)
                }
                Operation.IMul -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 * value2)
                }
                Operation.LMul -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 * value2)
                }
                Operation.FMul -> {
                    val value2 = stack.pop() as Float
                    val value1 = stack.pop() as Float
                    stack.push(value1 * value2)
                }
                Operation.DMul -> {
                    val value2 = stack.pop() as Double
                    val value1 = stack.pop() as Double
                    stack.push(value1 * value2)
                }
                Operation.IDiv -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 / value2)
                }
                Operation.LDiv -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 / value2)
                }
                Operation.FDiv -> {
                    val value2 = stack.pop() as Float
                    val value1 = stack.pop() as Float
                    stack.push(value1 / value2)
                }
                Operation.DDiv -> {
                    val value2 = stack.pop() as Double
                    val value1 = stack.pop() as Double
                    stack.push(value1 / value2)
                }
                Operation.IRem -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 % value2)
                }
                Operation.LRem -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 % value2)
                }
                Operation.FRem -> {
                    val value2 = stack.pop() as Float
                    val value1 = stack.pop() as Float
                    stack.push(value1 % value2)
                }
                Operation.DRem -> {
                    val value2 = stack.pop() as Double
                    val value1 = stack.pop() as Double
                    stack.push(value1 % value2)
                }
                Operation.INeg -> {
                    val value = stack.pop() as Int
                    stack.push(-value)
                }
                Operation.LNeg -> {
                    val value = stack.pop() as Long
                    stack.push(-value)
                }
                Operation.FNeg -> {
                    val value = stack.pop() as Float
                    stack.push(-value)
                }
                Operation.DNeg -> {
                    val value = stack.pop() as Double
                    stack.push(-value)
                }
                Operation.IShl -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 shl value2)
                }
                Operation.IShr -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 shr value2)
                }
                Operation.LShl -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Long
                    stack.push(value1 shl value2)
                }
                Operation.LShr -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Long
                    stack.push(value1 shr value2)
                }
                Operation.IUshr -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 ushr value2)
                }
                Operation.LUshr -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Long
                    stack.push(value1 ushr value2)
                }
                Operation.IAnd -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 and value2)
                }
                Operation.LAnd -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 and value2)
                }
                Operation.IOr -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 or value2)
                }
                Operation.LOr -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 or value2)
                }
                Operation.IXor -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    stack.push(value1 xor value2)
                }
                Operation.LXor -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(value1 xor value2)
                }
                is Operation.IInc -> {
                    val current = localVariables[operation.index] as Int
                    localVariables[operation.index] = current + operation.const
                }
                Operation.IntToLong -> stack.push((stack.pop() as Int).toLong())
                Operation.IntToFloat -> stack.push((stack.pop() as Int).toFloat())
                Operation.IntToDouble -> stack.push((stack.pop() as Int).toDouble())
                Operation.IntToByte -> stack.push((stack.pop() as Int).toByte())
                Operation.IntToChar -> stack.push((stack.pop() as Int).toChar())
                Operation.IntToShort -> stack.push((stack.pop() as Int).toShort())
                Operation.LongToInt -> stack.push((stack.pop() as Long).toInt())
                Operation.LongToFloat -> stack.push((stack.pop() as Long).toFloat())
                Operation.LongToDouble -> stack.push((stack.pop() as Long).toDouble())
                Operation.FloatToInt -> stack.push((stack.pop() as Float).toInt())
                Operation.FloatToLong -> stack.push((stack.pop() as Long).toInt())
                Operation.FloatToDouble -> stack.push((stack.pop() as Double).toInt())
                Operation.DoubleToInt -> stack.push((stack.pop() as Double).toInt())
                Operation.DoubleToLong -> stack.push((stack.pop() as Double).toLong())
                Operation.DoubleToFloat -> stack.push((stack.pop() as Double).toFloat())
                Operation.LCmp -> {
                    val value2 = stack.pop() as Long
                    val value1 = stack.pop() as Long
                    stack.push(when {
                        value1 == value2 -> 0
                        value1 > value2 -> 1
                        else -> -1
                    })
                }
                Operation.FCmpL,
                Operation.FCmpG -> {
                    val value2 = stack.pop() as Float
                    val value1 = stack.pop() as Float
                    stack.push(when {
                        value1.isNaN() || value2.isNaN() -> if (operation == Operation.FCmpG) 1 else -1
                        value1 == value2 -> 0
                        value1 > value2 -> 1
                        value1 < value2 -> -1
                        else -> if (operation == Operation.FCmpG) 1 else -1
                    })
                }
                Operation.DCmpL,
                Operation.DCmpG -> {
                    val value2 = stack.pop() as Double
                    val value1 = stack.pop() as Double
                    stack.push(when {
                        value1.isNaN() || value2.isNaN() -> if (operation == Operation.DCmpG) 1 else -1
                        value1 == value2 -> 0
                        value1 > value2 -> 1
                        value1 < value2 -> -1
                        else -> if (operation == Operation.DCmpG) 1 else -1
                    })
                }
                is Operation.IIfEqual -> {
                    val value = stack.pop() as Int
                    if(value == 0) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IIfNotEqual -> {
                    val value = stack.pop() as Int
                    if(value != 0) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IIfLessThan -> {
                    val value = stack.pop() as Int
                    if (value < 0) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IIfGreaterThan -> {
                    val value = stack.pop() as Int
                    if (value > 0) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IIfLessThanOrEqual -> {
                    val value = stack.pop() as Int
                    if (value <= 0) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IIfGreaterThanOrEqual -> {
                    val value = stack.pop() as Int
                    if (value >= 0) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IfCompareIntEqual -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    if (value1 == value2) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IfCompareIntNotEqual -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    if (value1 != value2) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IfCompareIntLessThan -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    if (value1 < value2) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IfCompareIntGreaterThanOrEqual -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    if (value1 >= value2) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IfCompareIntLessThanOrEqual -> {
                    val value2 = stack.pop() as Int
                    val value1 = stack.pop() as Int
                    if (value1 <= value2) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IfCompareReferenceEqual -> {
                    val value2 = stack.pop()
                    val value1 = stack.pop()
                    if (value1 === value2) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IfCompareReferenceNotEqual -> {
                    val value2 = stack.pop()
                    val value1 = stack.pop()
                    if (value1 !== value2) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.Goto -> {
                    index = getJumpPosition(byteCodeTable, operation)
                }
                is Operation.JumpToSubroutine -> {
                    stack.push(operation.byteCodeIndex)
                    index = getJumpPosition(byteCodeTable, operation)
                }
                is Operation.Ret -> {
                    val address = localVariables[operation.index] as Int
                    index = byteCodeTable[address] ?: error("cannot find index to jump to")
                }
                is Operation.TableSwitch -> {
                    val tableIndex = stack.pop() as Int
                    val targetAddress = if (tableIndex < operation.low || tableIndex > operation.high) {
                        operation.byteCodeIndex + operation.defaultValue
                    } else {
                        operation.byteCodeIndex + operation.table[tableIndex - operation.low]
                    }
                    index = byteCodeTable[targetAddress] ?: error("cannot find index to jump to")
                }
                is Operation.LookupSwitch -> {
                    val key = stack.pop() as Int
                    val targetAddress = operation.byteCodeIndex + (operation.pairs[key] ?: operation.defaultValue)
                    index = byteCodeTable[targetAddress] ?: error("cannot find index to jump to")
                }
                is Operation.GetStatic -> {
                    val fieldReference = classInfo.constantPool[operation.field] as FieldReference
                    val staticClass = getStaticClassByClassIndex(fieldReference.classIndex)

                    val value = getInstanceFieldValue(staticClass, fieldReference)
                    stack.push(value)
                }
                is Operation.PutStatic -> {
                    val value = stack.pop()
                    val fieldReference = classInfo.constantPool[operation.field] as FieldReference
                    val staticClass = getStaticClassByClassIndex(fieldReference.classIndex)

                    setInstanceFieldValue(staticClass, fieldReference, value)
                }
                is Operation.GetField -> {
                    val instance = stack.pop() as VirtualObject
                    val fieldReference = classInfo.constantPool[operation.field] as FieldReference

                    val value = getInstanceFieldValue(instance, fieldReference)
                    stack.push(value)
                }
                is Operation.PutField -> {
                    val value = stack.pop()
                    val instance = stack.pop() as VirtualObject

                    val fieldReference = classInfo.constantPool[operation.field] as FieldReference
                    setInstanceFieldValue(instance, fieldReference, value)
                }
                is Operation.InvokeVirtual -> {
                    val otherMethod = getMethod(classInfo.constantPool, operation.methodReferenceIndex)
                    val methodArgs = stack.popMultiple(otherMethod.descriptor.parameters.size)
                    val instance = stack.pop()
                    val returnValue = invokeMethodOnInstance(instance, otherMethod, methodArgs)
                    if (returnValue is ReturnValue.Value) {
                        stack.push(returnValue.value)
                    }
                }
                is Operation.InvokeSpecial -> {
                    //TODO: create another method here that calls invokeMethod and does additional checks as described in...
                    //http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.invokespecial
                    val otherMethod = getMethod(classInfo.constantPool, operation.methodReferenceIndex)
                    val methodArgs = stack.popMultiple(otherMethod.descriptor.parameters.size)
                    val instance = stack.pop()
                    val returnValue = invokeMethodOnInstance(instance, otherMethod, methodArgs)
                    if (returnValue is ReturnValue.Value) {
                        stack.push(returnValue.value)
                    }
                }
                is Operation.InvokeStatic -> {
                    val otherMethod = getMethod(classInfo.constantPool, operation.methodReferenceIndex)
                    val methodArgs = stack.popMultiple(otherMethod.descriptor.parameters.size)
                    val staticClass = loadStaticObject(otherMethod.className)
                    val returnValue = invokeMethodOnInstance(staticClass, otherMethod, methodArgs)
                    if (returnValue is ReturnValue.Value) {
                        stack.push(returnValue.value)
                    }
                }
                is Operation.InvokeInterface -> TODO()
                is Operation.InvokeDynamic -> TODO()
                is Operation.New -> {
                    val classRef = classInfo.constantPool[operation.classReferenceIndex] as ClassReference
                    val className = classInfo.constantPool[classRef.nameIndex] as String

                    stack.push(loadObject(className))
                }
                is Operation.NewArray -> TODO()
                is Operation.ANewArray -> TODO()
                Operation.ArrayLength -> TODO()
                Operation.AThrow -> TODO()
                is Operation.CheckCast -> TODO()
                is Operation.InstanceOf -> TODO()
                Operation.MonitorEnter -> TODO()
                Operation.MonitorExit -> TODO()
                is Operation.MultiANewArray -> TODO()
                is Operation.IfNull -> {
                    val value = stack.pop()
                    if (value == null) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
                is Operation.IfNonNull -> {
                    val value = stack.pop()
                    if(value != null) {
                        index = getJumpPosition(byteCodeTable, operation)
                    }
                }
            }
            index++
        }

        return null
    }

    private fun getJumpPosition(byteCodeTable: Map<Int, Int>, operation: OffsetOperation) =
            byteCodeTable[operation.byteCodeIndex + operation.offset]
                    ?: error("cannot find index to jump to")

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

    private suspend fun invokeMethodOnInstance(instance: Any?, method: Method, methodArgs: Array<Any?>): ReturnValue {
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

    private fun ldc(ldcIndex: Int): Any =
            when (val constant = classInfo.constantPool[ldcIndex]) {
                is Int, is Float, is String, is MethodHandleReference -> constant
                is StringReference -> classInfo.constantPool[constant.index]!!
                is ClassReference -> TODO("how to get reference to class by name in constantPool?")
                else -> error("Fix returning value for $constant")
            }

    private suspend fun getStaticClassByClassIndex(classIndex: Int): VirtualObject {
        val classReference = classInfo.constantPool[classIndex] as ClassReference
        val className = classInfo.constantPool[classReference.nameIndex] as String

        return loadStaticObject(className)
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
