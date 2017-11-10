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
                val byteCodeOperations = mapOf(
                    Opcode.NEW to NewOperation(this@ApplicationObjectLoader, classInfo.constantPool),
                    Opcode.DUP to DupOperation(),
                    Opcode.LDC to LDCOperation(classInfo.constantPool),
                    Opcode.GETSTATIC to GetStaticOperation(staticClasses, this@ApplicationObjectLoader, classInfo.constantPool),
                    Opcode.INVOKEVIRTUAL to InvokeVirtualOperation(classInfo.constantPool),
                    Opcode.INVOKESPECIAL to InvokeSpecialOperation(classInfo.constantPool),
                    Opcode.INVOKESTATIC to InvokeStaticOperation(staticClasses, this@ApplicationObjectLoader, classInfo.constantPool),
                    Opcode.ICONST_M1 to PushConstOperation(-1),
                    Opcode.ICONST_0 to PushConstOperation(0),
                    Opcode.LCONST_0 to PushConstOperation(0L),
                    Opcode.DCONST_0 to PushConstOperation(0.0),
                    Opcode.FCONST_0 to PushConstOperation(0f),
                    Opcode.ICONST_1 to PushConstOperation(1),
                    Opcode.LCONST_1 to PushConstOperation(1L),
                    Opcode.DCONST_1 to PushConstOperation(1.0),
                    Opcode.FCONST_1 to PushConstOperation(1f),
                    Opcode.ICONST_2 to PushConstOperation(2),
                    Opcode.FCONST_2 to PushConstOperation(2f),
                    Opcode.ICONST_3 to PushConstOperation(3),
                    Opcode.ICONST_4 to PushConstOperation(4),
                    Opcode.ICONST_5 to PushConstOperation(5),
                    Opcode.ASTORE_0 to IndexedAStoreOperation(0),
                    Opcode.ISTORE_0 to IndexedIStoreOperation(0),
                    Opcode.LSTORE_0 to IndexedLStoreOperation(0),
                    Opcode.DSTORE_0 to IndexedDStoreOperation(0),
                    Opcode.FSTORE_0 to IndexedFStoreOperation(0),
                    Opcode.ASTORE_1 to IndexedAStoreOperation(1),
                    Opcode.ISTORE_1 to IndexedIStoreOperation(1),
                    Opcode.LSTORE_1 to IndexedLStoreOperation(1),
                    Opcode.DSTORE_1 to IndexedDStoreOperation(1),
                    Opcode.FSTORE_1 to IndexedFStoreOperation(1),
                    Opcode.ASTORE_2 to IndexedAStoreOperation(2),
                    Opcode.ISTORE_2 to IndexedIStoreOperation(2),
                    Opcode.LSTORE_2 to IndexedLStoreOperation(2),
                    Opcode.DSTORE_2 to IndexedDStoreOperation(2),
                    Opcode.FSTORE_2 to IndexedFStoreOperation(2),
                    Opcode.ASTORE_3 to IndexedAStoreOperation(3),
                    Opcode.ISTORE_3 to IndexedIStoreOperation(3),
                    Opcode.LSTORE_3 to IndexedLStoreOperation(3),
                    Opcode.DSTORE_3 to IndexedDStoreOperation(3),
                    Opcode.FSTORE_3 to IndexedFStoreOperation(3),
                    Opcode.ASTORE to AStoreOperation(),
                    Opcode.ISTORE to IStoreOperation(),
                    Opcode.LSTORE to LStoreOperation(),
                    Opcode.DSTORE to DStoreOperation(),
                    Opcode.FSTORE to FStoreOperation(),
                    Opcode.ALOAD_0 to IndexedALoadOperation(0),
                    Opcode.ILOAD_0 to IndexedILoadOperation(0),
                    Opcode.LLOAD_0 to IndexedLLoadOperation(0),
                    Opcode.DLOAD_0 to IndexedDLoadOperation(0),
                    Opcode.FLOAD_0 to IndexedFLoadOperation(0),
                    Opcode.ALOAD_1 to IndexedALoadOperation(1),
                    Opcode.ILOAD_1 to IndexedILoadOperation(1),
                    Opcode.LLOAD_1 to IndexedLLoadOperation(1),
                    Opcode.DLOAD_1 to IndexedDLoadOperation(1),
                    Opcode.FLOAD_1 to IndexedFLoadOperation(1),
                    Opcode.ALOAD_2 to IndexedALoadOperation(2),
                    Opcode.ILOAD_2 to IndexedILoadOperation(2),
                    Opcode.LLOAD_2 to IndexedLLoadOperation(2),
                    Opcode.DLOAD_2 to IndexedDLoadOperation(2),
                    Opcode.FLOAD_2 to IndexedFLoadOperation(2),
                    Opcode.ALOAD_3 to IndexedALoadOperation(3),
                    Opcode.ILOAD_3 to IndexedILoadOperation(3),
                    Opcode.LLOAD_3 to IndexedLLoadOperation(3),
                    Opcode.DLOAD_3 to IndexedDLoadOperation(3),
                    Opcode.FLOAD_3 to IndexedFLoadOperation(3),
                    Opcode.ALOAD to ALoadOperation(),
                    Opcode.ILOAD to ILoadOperation(),
                    Opcode.LLOAD to LLoadOperation(),
                    Opcode.DLOAD to DLoadOperation(),
                    Opcode.FLOAD to FLoadOperation(),
                    Opcode.AALOAD to AALoadOperation(),
                    Opcode.IADD to IAddOperation(),
                    Opcode.ISUB to ISubOperation(),
                    Opcode.IMUL to IMulOperation(),
                    Opcode.IDIV to IDivOperation(),
                    Opcode.IREM to IRemOperation(),
                    Opcode.TABLESWITCH to TableSwitchOperation(),
                    Opcode.IFEQ to IfEqOperation(),
                    Opcode.GOTO to GotoOperation(),
                    Opcode.NOP to NoOperation(),
                    Opcode.L2I to L2IOperation()
                )

                return RuntimeVirtualObject(classInfo, byteCodeOperations)
            }
        }

    }
}
