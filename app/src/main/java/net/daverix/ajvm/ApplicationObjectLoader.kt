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


import android.annotation.SuppressLint
import net.daverix.ajvm.operation.*
import java.io.IOException

class ApplicationObjectLoader(private val classInfoProvider: ClassInfoProvider,
                              private val staticClasses: MutableMap<String, VirtualObject>,
                              private val outStream: PrintStreamObject,
                              private val errStream: PrintStreamObject) : VirtualObjectLoader {
    @SuppressLint("UseSparseArrays")
    @Throws(IOException::class)
    override fun load(qualifiedName: String): VirtualObject {
        when (qualifiedName) {
            "java/lang/System" -> return SystemObject(outStream, errStream)
            "java/lang/StringBuilder" -> return StringBuilderObject()
            "java/lang/Integer" -> return IntegerObject()
            else -> {
                val classInfo = classInfoProvider.getClassInfo(qualifiedName)
                val byteCodeOperations = mapOf(
                    Opcodes.NEW to NewOperation(this@ApplicationObjectLoader, classInfo.constantPool),
                    Opcodes.DUP to DupOperation(),
                    Opcodes.LDC to LDCOperation(classInfo.constantPool),
                    Opcodes.GETSTATIC to GetStaticOperation(staticClasses, this@ApplicationObjectLoader, classInfo.constantPool),
                    Opcodes.INVOKEVIRTUAL to InvokeVirtualOperation(classInfo.constantPool),
                    Opcodes.INVOKESPECIAL to InvokeSpecialOperation(classInfo.constantPool),
                    Opcodes.INVOKESTATIC to InvokeStaticOperation(staticClasses, this@ApplicationObjectLoader, classInfo.constantPool),
                    Opcodes.ICONST_M1 to PushConstOperation(-1),
                    Opcodes.ICONST_0 to PushConstOperation(0),
                    Opcodes.LCONST_0 to PushConstOperation(0L),
                    Opcodes.DCONST_0 to PushConstOperation(0.0),
                    Opcodes.FCONST_0 to PushConstOperation(0f),
                    Opcodes.ICONST_1 to PushConstOperation(1),
                    Opcodes.LCONST_1 to PushConstOperation(1L),
                    Opcodes.DCONST_1 to PushConstOperation(1.0),
                    Opcodes.FCONST_1 to PushConstOperation(1f),
                    Opcodes.ICONST_2 to PushConstOperation(2),
                    Opcodes.FCONST_2 to PushConstOperation(2f),
                    Opcodes.ICONST_3 to PushConstOperation(3),
                    Opcodes.ICONST_4 to PushConstOperation(4),
                    Opcodes.ICONST_5 to PushConstOperation(5),
                    Opcodes.ASTORE_0 to IndexedAStoreOperation(0),
                    Opcodes.ISTORE_0 to IndexedIStoreOperation(0),
                    Opcodes.LSTORE_0 to IndexedLStoreOperation(0),
                    Opcodes.DSTORE_0 to IndexedDStoreOperation(0),
                    Opcodes.FSTORE_0 to IndexedFStoreOperation(0),
                    Opcodes.ASTORE_1 to IndexedAStoreOperation(1),
                    Opcodes.ISTORE_1 to IndexedIStoreOperation(1),
                    Opcodes.LSTORE_1 to IndexedLStoreOperation(1),
                    Opcodes.DSTORE_1 to IndexedDStoreOperation(1),
                    Opcodes.FSTORE_1 to IndexedFStoreOperation(1),
                    Opcodes.ASTORE_2 to IndexedAStoreOperation(2),
                    Opcodes.ISTORE_2 to IndexedIStoreOperation(2),
                    Opcodes.LSTORE_2 to IndexedLStoreOperation(2),
                    Opcodes.DSTORE_2 to IndexedDStoreOperation(2),
                    Opcodes.FSTORE_2 to IndexedFStoreOperation(2),
                    Opcodes.ASTORE_3 to IndexedAStoreOperation(3),
                    Opcodes.ISTORE_3 to IndexedIStoreOperation(3),
                    Opcodes.LSTORE_3 to IndexedLStoreOperation(3),
                    Opcodes.DSTORE_3 to IndexedDStoreOperation(3),
                    Opcodes.FSTORE_3 to IndexedFStoreOperation(3),
                    Opcodes.ASTORE to AStoreOperation(),
                    Opcodes.ISTORE to IStoreOperation(),
                    Opcodes.LSTORE to LStoreOperation(),
                    Opcodes.DSTORE to DStoreOperation(),
                    Opcodes.FSTORE to FStoreOperation(),
                    Opcodes.ALOAD_0 to IndexedALoadOperation(0),
                    Opcodes.ILOAD_0 to IndexedILoadOperation(0),
                    Opcodes.LLOAD_0 to IndexedLLoadOperation(0),
                    Opcodes.DLOAD_0 to IndexedDLoadOperation(0),
                    Opcodes.FLOAD_0 to IndexedFLoadOperation(0),
                    Opcodes.ALOAD_1 to IndexedALoadOperation(1),
                    Opcodes.ILOAD_1 to IndexedILoadOperation(1),
                    Opcodes.LLOAD_1 to IndexedLLoadOperation(1),
                    Opcodes.DLOAD_1 to IndexedDLoadOperation(1),
                    Opcodes.FLOAD_1 to IndexedFLoadOperation(1),
                    Opcodes.ALOAD_2 to IndexedALoadOperation(2),
                    Opcodes.ILOAD_2 to IndexedILoadOperation(2),
                    Opcodes.LLOAD_2 to IndexedLLoadOperation(2),
                    Opcodes.DLOAD_2 to IndexedDLoadOperation(2),
                    Opcodes.FLOAD_2 to IndexedFLoadOperation(2),
                    Opcodes.ALOAD_3 to IndexedALoadOperation(3),
                    Opcodes.ILOAD_3 to IndexedILoadOperation(3),
                    Opcodes.LLOAD_3 to IndexedLLoadOperation(3),
                    Opcodes.DLOAD_3 to IndexedDLoadOperation(3),
                    Opcodes.FLOAD_3 to IndexedFLoadOperation(3),
                    Opcodes.ALOAD to ALoadOperation(),
                    Opcodes.ILOAD to ILoadOperation(),
                    Opcodes.LLOAD to LLoadOperation(),
                    Opcodes.DLOAD to DLoadOperation(),
                    Opcodes.FLOAD to FLoadOperation(),
                    Opcodes.AALOAD to AALoadOperation(),
                    Opcodes.IADD to IAddOperation(),
                    Opcodes.ISUB to ISubOperation(),
                    Opcodes.IMUL to IMulOperation(),
                    Opcodes.IDIV to IDivOperation(),
                    Opcodes.IREM to IRemOperation(),
                    Opcodes.TABLESWITCH to TableSwitchOperation(),
                    Opcodes.IFEQ to IfEqOperation(),
                    Opcodes.GOTO to GotoOperation(),
                    Opcodes.NOP to NoOperation(),
                    Opcodes.L2I to L2IOperation()
                )

                return RuntimeVirtualObject(classInfo, byteCodeOperations)
            }
        }

    }
}
