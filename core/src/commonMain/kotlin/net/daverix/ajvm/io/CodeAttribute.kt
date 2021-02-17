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
package net.daverix.ajvm.io

import net.daverix.ajvm.*

data class CodeAttribute(
        val maxStack: Int,
        val maxLocals: Int,
        val code: List<Operation>,
        val byteCodeTable: Map<Int, Int>,
        val exceptionTable: List<Exception>,
        val attributes: List<AttributeInfo>
)

fun DataInputStream.readCodeAttribute(attributeLength: Int): CodeAttribute {
    val maxStack = readUnsignedShort()
    val maxLocals = readUnsignedShort()
    val codeLength = readInt()

    val code = mutableListOf<Operation>()
    val bytecodeTable = mutableMapOf<Int, Int>()
    var index = 0
    var operationIndex = 0
    while (index < codeLength) {
        val byteCodeIndex = index
        val byteCode = readUnsignedByte()
        index++
        bytecodeTable[byteCodeIndex] = operationIndex
        operationIndex++

        code += when (fromByteCode(byteCode)) {
            Opcode.RETURN -> Operation.Return
            Opcode.IRETURN -> Operation.IReturn
            Opcode.LRETURN -> Operation.LReturn
            Opcode.FRETURN -> Operation.FReturn
            Opcode.DRETURN -> Operation.DReturn
            Opcode.ARETURN -> Operation.AReturn
            Opcode.NOP -> Operation.NOP
            Opcode.ACONST_NULL -> Operation.AConstNull
            Opcode.ICONST_M1 -> Operation.IConst(-1)
            Opcode.ICONST_0 -> Operation.IConst(0)
            Opcode.ICONST_1 -> Operation.IConst(1)
            Opcode.ICONST_2 -> Operation.IConst(2)
            Opcode.ICONST_3 -> Operation.IConst(3)
            Opcode.ICONST_4 -> Operation.IConst(4)
            Opcode.ICONST_5 -> Operation.IConst(5)
            Opcode.LCONST_0 -> Operation.LConst(0L)
            Opcode.LCONST_1 -> Operation.LConst(1L)
            Opcode.FCONST_0 -> Operation.FConst(0f)
            Opcode.FCONST_1 -> Operation.FConst(1f)
            Opcode.FCONST_2 -> Operation.FConst(2f)
            Opcode.DCONST_0 -> Operation.DConst(0.0)
            Opcode.DCONST_1 -> Operation.DConst(1.0)
            Opcode.BI_PUSH -> {
                val value = readUnsignedByte()
                index++
                Operation.IConst(value)
            }
            Opcode.SI_PUSH -> {
                val value = readUnsignedShort()
                index += 2
                Operation.IConst(value)
            }
            Opcode.LDC -> {
                val value = readUnsignedByte()
                index++
                Operation.Ldc(value)
            }
            Opcode.LDC_W -> {
                val value = readUnsignedShort()
                index += 2
                Operation.Ldc(value)
            }
            Opcode.LDC2_W -> {
                val value = readUnsignedShort()
                index += 2
                Operation.Ldc2(value)
            }
            Opcode.ILOAD -> {
                val value = readUnsignedByte()
                index++
                Operation.ILoad(value)
            }
            Opcode.LLOAD -> {
                val value = readUnsignedByte()
                index++
                Operation.LLoad(value)
            }
            Opcode.FLOAD -> {
                val value = readUnsignedByte()
                index++
                Operation.FLoad(value)
            }
            Opcode.DLOAD -> {
                val value = readUnsignedByte()
                index++
                Operation.DLoad(value)
            }
            Opcode.ALOAD -> {
                val value = readUnsignedByte()
                index++
                Operation.ALoad(value)
            }
            Opcode.ILOAD_0 -> Operation.ILoad(0)
            Opcode.ILOAD_1 -> Operation.ILoad(1)
            Opcode.ILOAD_2 -> Operation.ILoad(2)
            Opcode.ILOAD_3 -> Operation.ILoad(3)
            Opcode.LLOAD_0 -> Operation.LLoad(0)
            Opcode.LLOAD_1 -> Operation.LLoad(1)
            Opcode.LLOAD_2 -> Operation.LLoad(2)
            Opcode.LLOAD_3 -> Operation.LLoad(3)
            Opcode.FLOAD_0 -> Operation.FLoad(0)
            Opcode.FLOAD_1 -> Operation.FLoad(1)
            Opcode.FLOAD_2 -> Operation.FLoad(2)
            Opcode.FLOAD_3 -> Operation.FLoad(3)
            Opcode.DLOAD_0 -> Operation.DLoad(0)
            Opcode.DLOAD_1 -> Operation.DLoad(1)
            Opcode.DLOAD_2 -> Operation.DLoad(2)
            Opcode.DLOAD_3 -> Operation.DLoad(3)
            Opcode.ALOAD_0 -> Operation.ALoad(0)
            Opcode.ALOAD_1 -> Operation.ALoad(1)
            Opcode.ALOAD_2 -> Operation.ALoad(2)
            Opcode.ALOAD_3 -> Operation.ALoad(3)
            Opcode.IALOAD -> Operation.IALoad
            Opcode.LALOAD -> Operation.LALoad
            Opcode.FALOAD -> Operation.FALoad
            Opcode.DALOAD -> Operation.DALoad
            Opcode.AALOAD -> Operation.AALoad
            Opcode.BALOAD -> Operation.BALoad
            Opcode.CALOAD -> Operation.CALoad
            Opcode.SALOAD -> Operation.SALoad
            Opcode.ISTORE -> {
                val value = readUnsignedByte()
                index++
                Operation.IStore(value)
            }
            Opcode.LSTORE -> {
                val value = readUnsignedByte()
                index++
                Operation.LStore(value)
            }
            Opcode.FSTORE -> {
                val value = readUnsignedByte()
                index++
                Operation.FStore(value)
            }
            Opcode.DSTORE -> {
                val value = readUnsignedByte()
                index++
                Operation.DStore(value)
            }
            Opcode.ASTORE -> {
                val value = readUnsignedByte()
                index++
                Operation.AStore(value)
            }
            Opcode.ISTORE_0 -> Operation.IStore(0)
            Opcode.ISTORE_1 -> Operation.IStore(1)
            Opcode.ISTORE_2 -> Operation.IStore(2)
            Opcode.ISTORE_3 -> Operation.IStore(3)
            Opcode.LSTORE_0 -> Operation.LStore(0)
            Opcode.LSTORE_1 -> Operation.LStore(1)
            Opcode.LSTORE_2 -> Operation.LStore(2)
            Opcode.LSTORE_3 -> Operation.LStore(3)
            Opcode.FSTORE_0 -> Operation.FStore(0)
            Opcode.FSTORE_1 -> Operation.FStore(1)
            Opcode.FSTORE_2 -> Operation.FStore(2)
            Opcode.FSTORE_3 -> Operation.FStore(3)
            Opcode.DSTORE_0 -> Operation.DStore(0)
            Opcode.DSTORE_1 -> Operation.DStore(1)
            Opcode.DSTORE_2 -> Operation.DStore(2)
            Opcode.DSTORE_3 -> Operation.DStore(3)
            Opcode.ASTORE_0 -> Operation.AStore(0)
            Opcode.ASTORE_1 -> Operation.AStore(1)
            Opcode.ASTORE_2 -> Operation.AStore(2)
            Opcode.ASTORE_3 -> Operation.AStore(3)
            Opcode.IASTORE -> Operation.IAStore
            Opcode.LASTORE -> Operation.LAStore
            Opcode.FASTORE -> Operation.FAStore
            Opcode.DASTORE -> Operation.DAStore
            Opcode.AASTORE -> Operation.AAStore
            Opcode.BASTORE -> Operation.BAStore
            Opcode.CASTORE -> Operation.CAStore
            Opcode.SASTORE -> Operation.SAStore
            Opcode.POP -> Operation.Pop
            Opcode.POP2 -> Operation.Pop2
            Opcode.DUP -> Operation.Dup
            Opcode.DUP_X1 -> Operation.DupX1
            Opcode.DUP_X2 -> Operation.DupX2
            Opcode.DUP2 -> Operation.Dup2
            Opcode.DUP2_X1 -> Operation.Dup2X1
            Opcode.DUP2_X2 -> Operation.Dup2X2
            Opcode.SWAP -> Operation.Swap
            Opcode.IADD -> Operation.IAdd
            Opcode.LADD -> Operation.LAdd
            Opcode.FADD -> Operation.FAdd
            Opcode.DADD -> Operation.DAdd
            Opcode.ISUB -> Operation.ISub
            Opcode.LSUB -> Operation.LSub
            Opcode.FSUB -> Operation.FSub
            Opcode.DSUB -> Operation.DSub
            Opcode.IMUL -> Operation.IMul
            Opcode.LMUL -> Operation.LMul
            Opcode.FMUL -> Operation.FMul
            Opcode.DMUL -> Operation.DMul
            Opcode.IDIV -> Operation.IDiv
            Opcode.LDIV -> Operation.LDiv
            Opcode.FDIV -> Operation.FDiv
            Opcode.DDIV -> Operation.DDiv
            Opcode.IREM -> Operation.IRem
            Opcode.LREM -> Operation.LRem
            Opcode.FREM -> Operation.FRem
            Opcode.DREM -> Operation.DRem
            Opcode.INEG -> Operation.INeg
            Opcode.LNEG -> Operation.LNeg
            Opcode.FNEG -> Operation.FNeg
            Opcode.DNEG -> Operation.DNeg
            Opcode.ISHL -> Operation.IShl
            Opcode.LSHL -> Operation.LShl
            Opcode.ISHR -> Operation.IShr
            Opcode.LSHR -> Operation.LShr
            Opcode.IUSHR -> Operation.IUshr
            Opcode.LUSHR -> Operation.LUshr
            Opcode.IAND -> Operation.IAnd
            Opcode.LAND -> Operation.LAnd
            Opcode.IOR -> Operation.IOr
            Opcode.LOR -> Operation.LOr
            Opcode.IXOR -> Operation.IXor
            Opcode.LXOR -> Operation.LXor
            Opcode.IINC -> {
                val localVariableIndex = readUnsignedByte()
                index++
                val incAmount = readUnsignedByte()
                index++
                Operation.IInc(localVariableIndex, incAmount)
            }
            Opcode.I2L -> Operation.IntToLong
            Opcode.I2F -> Operation.IntToFloat
            Opcode.I2D -> Operation.IntToDouble
            Opcode.I2B -> Operation.IntToByte
            Opcode.I2C -> Operation.IntToChar
            Opcode.I2S -> Operation.IntToShort
            Opcode.L2I -> Operation.LongToInt
            Opcode.L2F -> Operation.LongToFloat
            Opcode.L2D -> Operation.LongToDouble
            Opcode.F2I -> Operation.FloatToInt
            Opcode.F2L -> Operation.FloatToLong
            Opcode.F2D -> Operation.FloatToDouble
            Opcode.D2I -> Operation.DoubleToInt
            Opcode.D2L -> Operation.DoubleToLong
            Opcode.D2F -> Operation.DoubleToFloat
            Opcode.LCMP -> Operation.LCmp
            Opcode.FCMPL -> Operation.FCmpL
            Opcode.FCMPG -> Operation.FCmpG
            Opcode.DCMPL -> Operation.DCmpL
            Opcode.DCMPG -> Operation.DCmpG
            Opcode.IFEQ -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IIfEqual(byteCodeIndex, offset)
            }
            Opcode.IFNE -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IIfNotEqual(byteCodeIndex, offset)
            }
            Opcode.IFLT -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IIfLessThan(byteCodeIndex, offset)
            }
            Opcode.IFGE -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IIfGreaterThanOrEqual(byteCodeIndex, offset)
            }
            Opcode.IFGT -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IIfGreaterThan(byteCodeIndex, offset)
            }
            Opcode.IFLE -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IIfLessThanOrEqual(byteCodeIndex, offset)
            }
            Opcode.IF_ICMPEQ -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IfCompareIntEqual(byteCodeIndex, offset)
            }
            Opcode.IF_ICMPNE -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IfCompareIntNotEqual(byteCodeIndex, offset)
            }
            Opcode.IF_ICMPLT -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IfCompareIntLessThan(byteCodeIndex, offset)
            }
            Opcode.IF_ICMPGE -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IfCompareIntGreaterThanOrEqual(byteCodeIndex, offset)
            }
            Opcode.IF_ICMPGT -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IfCompareIntGreaterThanOrEqual(byteCodeIndex, offset)
            }
            Opcode.IF_ICMPLE -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IfCompareIntLessThanOrEqual(byteCodeIndex, offset)
            }
            Opcode.IF_ACMPEQ -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IfCompareReferenceEqual(byteCodeIndex, offset)
            }
            Opcode.IF_ACMPNE -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.IfCompareReferenceNotEqual(byteCodeIndex, offset)
            }
            Opcode.GOTO -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.Goto(byteCodeIndex, offset)
            }
            Opcode.JSR -> {
                val offset = readUnsignedShort()
                index += 2
                Operation.JumpToSubroutine(byteCodeIndex, offset)
            }
            Opcode.JSR_W -> {
                val offset = readInt()
                index += 4
                Operation.JumpToSubroutine(byteCodeIndex, offset)
            }
            Opcode.RET -> {
                val retIndex = readUnsignedByte()
                index++
                Operation.Ret(retIndex)
            }
            Opcode.TABLESWITCH -> {
                val skip = (byteCodeIndex + 1) % 4
                skipBytes(skip)
                index += skip

                val defaultValue = readInt()
                index += 4

                val low = readInt()
                index += 4

                val high = readInt()
                index += 4

                if (low > high) {
                    error("low is higher than high: $low > $high")
                }

                val offsetWidth = high - low + 1
                val table = List(offsetWidth) { readInt() }
                index += 4 * offsetWidth

                Operation.TableSwitch(
                        byteCodeIndex = byteCodeIndex,
                        defaultValue = defaultValue,
                        low = low,
                        high = high,
                        table = table
                )
            }
            Opcode.LOOKUPSWITCH -> {
                val skip = (byteCodeIndex + 1) % 4
                skipBytes(skip)
                index += skip

                val defaultValue = readInt()
                index += 4
                val npairs = readInt()
                index += 4

                if(npairs < 0) error("npairs must be >= 0")

                val pairs = mutableMapOf<Int,Int>()
                repeat(npairs) {
                    val key = readInt()
                    index += 4
                    val value = readInt()
                    index += 4
                    pairs += key to value
                }

                Operation.LookupSwitch(
                        byteCodeIndex = byteCodeIndex,
                        defaultValue = defaultValue,
                        pairs = pairs
                )
            }
            Opcode.GETSTATIC -> {
                val field = readUnsignedShort()
                index += 2
                Operation.GetStatic(field)
            }
            Opcode.PUTSTATIC -> {
                val field = readUnsignedShort()
                index += 2
                Operation.PutStatic(field)
            }
            Opcode.GETFIELD -> {
                val field = readUnsignedShort()
                index += 2
                Operation.GetField(field)
            }
            Opcode.PUTFIELD -> {
                val field = readUnsignedShort()
                index += 2
                Operation.PutField(field)
            }
            Opcode.INVOKEVIRTUAL -> {
                val methodReferenceIndex = readUnsignedShort()
                index += 2
                Operation.InvokeVirtual(methodReferenceIndex)
            }
            Opcode.INVOKESPECIAL -> {
                val methodReferenceIndex = readUnsignedShort()
                index += 2
                Operation.InvokeSpecial(methodReferenceIndex)
            }
            Opcode.INVOKESTATIC -> {
                val methodReferenceIndex = readUnsignedShort()
                index += 2
                Operation.InvokeStatic(methodReferenceIndex)
            }
            Opcode.INVOKEINTERFACE -> {
                val methodReferenceIndex = readUnsignedShort()
                index += 2
                Operation.InvokeInterface(methodReferenceIndex)
            }
            Opcode.INVOKEDYNAMIC -> {
                val methodReferenceIndex = readUnsignedShort()
                index += 2
                Operation.InvokeDynamic(methodReferenceIndex)
            }
            Opcode.NEW -> {
                val classReferenceIndex = readUnsignedShort()
                index += 2
                Operation.New(classReferenceIndex)
            }
            Opcode.NEWARRAY -> {
                val type = readUnsignedByte()
                index++
                Operation.NewArray(type)
            }
            Opcode.ANEWARRAY -> {
                val classReferenceIndex = readUnsignedShort()
                index += 2
                Operation.ANewArray(classReferenceIndex)
            }
            Opcode.ARRAYLENGTH -> Operation.ArrayLength
            Opcode.ATHROW -> Operation.AThrow
            Opcode.CHECKCAST -> {
                val classReferenceIndex = readUnsignedShort()
                index += 2
                Operation.CheckCast(classReferenceIndex)
            }
            Opcode.INSTANCEOF -> {
                val classReferenceIndex = readUnsignedShort()
                index += 2
                Operation.InstanceOf(classReferenceIndex)
            }
            Opcode.MONITORENTER -> Operation.MonitorEnter
            Opcode.MONITOREXIT -> Operation.MonitorExit
            Opcode.WIDE -> {
                val wideCode = readUnsignedByte()
                index++

                when(fromByteCode(wideCode)) {
                    Opcode.ILOAD -> {
                        val value = readUnsignedShort()
                        index += 2
                        Operation.ILoad(value)
                    }
                    Opcode.FLOAD -> {
                        val value = readUnsignedShort()
                        index += 2
                        Operation.FLoad(value)
                    }
                    Opcode.ALOAD -> {
                        val value = readUnsignedShort()
                        index += 2
                        Operation.ALoad(value)
                    }
                    Opcode.LLOAD -> {
                        val value = readUnsignedShort()
                        index += 2
                        Operation.LLoad(value)
                    }
                    Opcode.DLOAD -> {
                        val value = readUnsignedShort()
                        index += 2
                        Operation.DLoad(value)
                    }
                    Opcode.ISTORE -> {
                        val value = readUnsignedShort()
                        index += 2
                        Operation.IStore(value)
                    }
                    Opcode.FSTORE -> {
                        val value = readUnsignedShort()
                        index += 2
                        Operation.FStore(value)
                    }
                    Opcode.ASTORE -> {
                        val value = readUnsignedShort()
                        index += 2
                        Operation.AStore(value)
                    }
                    Opcode.DSTORE -> {
                        val value = readUnsignedShort()
                        index += 2
                        Operation.DStore(value)
                    }
                    Opcode.RET -> {
                        val value = readUnsignedShort()
                        index += 2
                        Operation.Ret(value)
                    }
                    Opcode.IINC -> {
                        val localVariableIndex = readUnsignedShort()
                        index += 2
                        val amount = readUnsignedShort()
                        index += 2

                        Operation.IInc(localVariableIndex, amount)
                    }
                    else -> error("invalid wide instruction opCode: $wideCode")
                }
            }
            Opcode.MULTIANEWARRAY -> {
                val classIndex = readUnsignedShort()
                index += 2
                val dimensions = readUnsignedByte()
                index++
                Operation.MultiANewArray(classIndex, dimensions)
            }
            Opcode.IFNULL -> {
                val offset = readUnsignedShort()
                index+=2
                Operation.IfNull(byteCodeIndex, offset)
            }
            Opcode.IFNONNULL -> {
                val offset = readUnsignedShort()
                index+=2
                Operation.IfNonNull(byteCodeIndex, offset)
            }
            Opcode.GOTO_W -> {
                val offset = readInt()
                index += 4
                Operation.Goto(byteCodeIndex, offset)
            }
            Opcode.BREAKPOINT -> error("opcode 0xca (BREAKPOINT) should not be used and is an error")
            Opcode.IMPDEP1 -> error("opcode 0xfe (IMPDEP1) should not be used and is an error")
            Opcode.IMPDEP2 -> error("opcode 0xfe (IMPDEP2) should not be used and is an error")
        }
    }

    val exceptionCount = readUnsignedShort()
    val exceptionTable = List(exceptionCount) {
        Exception()
    }
    val attributes = readAttributes()

    return CodeAttribute(maxStack, maxLocals, code, bytecodeTable, exceptionTable, attributes)
}
