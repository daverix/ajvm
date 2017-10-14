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


import net.daverix.ajvm.operation.*
import java.io.IOException
import java.util.*

class ApplicationObjectLoader(private val classInfoProvider: ClassInfoProvider,
                              private val staticClasses: MutableMap<String, VirtualObject>,
                              private val outStream: PrintStreamObject,
                              private val errStream: PrintStreamObject) : VirtualObjectLoader {
    @Throws(IOException::class)
    override fun load(qualifiedName: String): VirtualObject {
        when (qualifiedName) {
            "java/lang/System" -> return SystemObject(outStream, errStream)
            "java/lang/StringBuilder" -> return StringBuilderObject()
            "java/lang/Integer" -> return IntegerObject()
            else -> {
                val classInfo = classInfoProvider.getClassInfo(qualifiedName)
                val byteCodeOperations = HashMap<Int, ByteCodeOperation>()

                byteCodeOperations.put(Opcodes.NEW, NewOperation(this, classInfo.constantPool))
                byteCodeOperations.put(Opcodes.DUP, DupOperation())
                byteCodeOperations.put(Opcodes.LDC, LDCOperation(classInfo.constantPool))
                byteCodeOperations.put(Opcodes.GETSTATIC, GetStaticOperation(staticClasses, this, classInfo.constantPool))
                byteCodeOperations.put(Opcodes.INVOKEVIRTUAL, InvokeVirtualOperation(classInfo.constantPool))
                byteCodeOperations.put(Opcodes.INVOKESPECIAL, InvokeSpecialOperation(classInfo.constantPool))
                byteCodeOperations.put(Opcodes.INVOKESTATIC, InvokeStaticOperation(staticClasses, this, classInfo.constantPool))
                byteCodeOperations.put(Opcodes.ICONST_M1, PushConstOperation(-1))
                byteCodeOperations.put(Opcodes.ICONST_0, PushConstOperation(0))
                byteCodeOperations.put(Opcodes.LCONST_0, PushConstOperation(0L))
                byteCodeOperations.put(Opcodes.DCONST_0, PushConstOperation(0.0))
                byteCodeOperations.put(Opcodes.FCONST_0, PushConstOperation(0f))
                byteCodeOperations.put(Opcodes.ICONST_1, PushConstOperation(1))
                byteCodeOperations.put(Opcodes.LCONST_1, PushConstOperation(1L))
                byteCodeOperations.put(Opcodes.DCONST_1, PushConstOperation(1.0))
                byteCodeOperations.put(Opcodes.FCONST_1, PushConstOperation(1f))
                byteCodeOperations.put(Opcodes.ICONST_2, PushConstOperation(2))
                byteCodeOperations.put(Opcodes.FCONST_2, PushConstOperation(2f))
                byteCodeOperations.put(Opcodes.ICONST_3, PushConstOperation(3))
                byteCodeOperations.put(Opcodes.ICONST_4, PushConstOperation(4))
                byteCodeOperations.put(Opcodes.ICONST_5, PushConstOperation(5))
                byteCodeOperations.put(Opcodes.ASTORE_0, IndexedAStoreOperation(0))
                byteCodeOperations.put(Opcodes.ISTORE_0, IndexedIStoreOperation(0))
                byteCodeOperations.put(Opcodes.LSTORE_0, IndexedLStoreOperation(0))
                byteCodeOperations.put(Opcodes.DSTORE_0, IndexedDStoreOperation(0))
                byteCodeOperations.put(Opcodes.FSTORE_0, IndexedFStoreOperation(0))
                byteCodeOperations.put(Opcodes.ASTORE_1, IndexedAStoreOperation(1))
                byteCodeOperations.put(Opcodes.ISTORE_1, IndexedIStoreOperation(1))
                byteCodeOperations.put(Opcodes.LSTORE_1, IndexedLStoreOperation(1))
                byteCodeOperations.put(Opcodes.DSTORE_1, IndexedDStoreOperation(1))
                byteCodeOperations.put(Opcodes.FSTORE_1, IndexedFStoreOperation(1))
                byteCodeOperations.put(Opcodes.ASTORE_2, IndexedAStoreOperation(2))
                byteCodeOperations.put(Opcodes.ISTORE_2, IndexedIStoreOperation(2))
                byteCodeOperations.put(Opcodes.LSTORE_2, IndexedLStoreOperation(2))
                byteCodeOperations.put(Opcodes.DSTORE_2, IndexedDStoreOperation(2))
                byteCodeOperations.put(Opcodes.FSTORE_2, IndexedFStoreOperation(2))
                byteCodeOperations.put(Opcodes.ASTORE_3, IndexedAStoreOperation(3))
                byteCodeOperations.put(Opcodes.ISTORE_3, IndexedIStoreOperation(3))
                byteCodeOperations.put(Opcodes.LSTORE_3, IndexedLStoreOperation(3))
                byteCodeOperations.put(Opcodes.DSTORE_3, IndexedDStoreOperation(3))
                byteCodeOperations.put(Opcodes.FSTORE_3, IndexedFStoreOperation(3))
                byteCodeOperations.put(Opcodes.ASTORE, AStoreOperation())
                byteCodeOperations.put(Opcodes.ISTORE, IStoreOperation())
                byteCodeOperations.put(Opcodes.LSTORE, LStoreOperation())
                byteCodeOperations.put(Opcodes.DSTORE, DStoreOperation())
                byteCodeOperations.put(Opcodes.FSTORE, FStoreOperation())
                byteCodeOperations.put(Opcodes.ALOAD_0, IndexedALoadOperation(0))
                byteCodeOperations.put(Opcodes.ILOAD_0, IndexedILoadOperation(0))
                byteCodeOperations.put(Opcodes.LLOAD_0, IndexedLLoadOperation(0))
                byteCodeOperations.put(Opcodes.DLOAD_0, IndexedDLoadOperation(0))
                byteCodeOperations.put(Opcodes.FLOAD_0, IndexedFLoadOperation(0))
                byteCodeOperations.put(Opcodes.ALOAD_1, IndexedALoadOperation(1))
                byteCodeOperations.put(Opcodes.ILOAD_1, IndexedILoadOperation(1))
                byteCodeOperations.put(Opcodes.LLOAD_1, IndexedLLoadOperation(1))
                byteCodeOperations.put(Opcodes.DLOAD_1, IndexedDLoadOperation(1))
                byteCodeOperations.put(Opcodes.FLOAD_1, IndexedFLoadOperation(1))
                byteCodeOperations.put(Opcodes.ALOAD_2, IndexedALoadOperation(2))
                byteCodeOperations.put(Opcodes.ILOAD_2, IndexedILoadOperation(2))
                byteCodeOperations.put(Opcodes.LLOAD_2, IndexedLLoadOperation(2))
                byteCodeOperations.put(Opcodes.DLOAD_2, IndexedDLoadOperation(2))
                byteCodeOperations.put(Opcodes.FLOAD_2, IndexedFLoadOperation(2))
                byteCodeOperations.put(Opcodes.ALOAD_3, IndexedALoadOperation(3))
                byteCodeOperations.put(Opcodes.ILOAD_3, IndexedILoadOperation(3))
                byteCodeOperations.put(Opcodes.LLOAD_3, IndexedLLoadOperation(3))
                byteCodeOperations.put(Opcodes.DLOAD_3, IndexedDLoadOperation(3))
                byteCodeOperations.put(Opcodes.FLOAD_3, IndexedFLoadOperation(3))
                byteCodeOperations.put(Opcodes.ALOAD, ALoadOperation())
                byteCodeOperations.put(Opcodes.ILOAD, ILoadOperation())
                byteCodeOperations.put(Opcodes.LLOAD, LLoadOperation())
                byteCodeOperations.put(Opcodes.DLOAD, DLoadOperation())
                byteCodeOperations.put(Opcodes.FLOAD, FLoadOperation())
                byteCodeOperations.put(Opcodes.AALOAD, AALoadOperation())
                byteCodeOperations.put(Opcodes.IADD, IAddOperation())
                byteCodeOperations.put(Opcodes.ISUB, ISubOperation())
                byteCodeOperations.put(Opcodes.IMUL, IMulOperation())
                byteCodeOperations.put(Opcodes.IDIV, IDivOperation())
                byteCodeOperations.put(Opcodes.IREM, IRemOperation())
                byteCodeOperations.put(Opcodes.TABLESWITCH, TableSwitchOperation())
                byteCodeOperations.put(Opcodes.IFEQ, IfEqOperation())
                byteCodeOperations.put(Opcodes.GOTO, GotoOperation())
                byteCodeOperations.put(Opcodes.NOP, NoOperation())
                byteCodeOperations.put(Opcodes.L2I, L2IOperation())

                return RuntimeVirtualObject(classInfo, byteCodeOperations)
            }
        }

    }
}
