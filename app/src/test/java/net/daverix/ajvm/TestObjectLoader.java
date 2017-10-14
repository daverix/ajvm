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
package net.daverix.ajvm;


import net.daverix.ajvm.io.ClassInfo;
import net.daverix.ajvm.io.ClassInfoReader;
import net.daverix.ajvm.io.ConstantPool;
import net.daverix.ajvm.jvm.AALoadOperation;
import net.daverix.ajvm.jvm.ALoadOperation;
import net.daverix.ajvm.jvm.AStoreOperation;
import net.daverix.ajvm.jvm.ByteCodeOperation;
import net.daverix.ajvm.jvm.DLoadOperation;
import net.daverix.ajvm.jvm.DStoreOperation;
import net.daverix.ajvm.jvm.DupOperation;
import net.daverix.ajvm.jvm.FLoadOperation;
import net.daverix.ajvm.jvm.FStoreOperation;
import net.daverix.ajvm.jvm.GetStaticOperation;
import net.daverix.ajvm.jvm.GotoOperation;
import net.daverix.ajvm.jvm.IAddOperation;
import net.daverix.ajvm.jvm.IDivOperation;
import net.daverix.ajvm.jvm.ILoadOperation;
import net.daverix.ajvm.jvm.IMulOperation;
import net.daverix.ajvm.jvm.IRemOperation;
import net.daverix.ajvm.jvm.IStoreOperation;
import net.daverix.ajvm.jvm.ISubOperation;
import net.daverix.ajvm.jvm.IfEqOperation;
import net.daverix.ajvm.jvm.IndexedALoadOperation;
import net.daverix.ajvm.jvm.IndexedAStoreOperation;
import net.daverix.ajvm.jvm.IndexedDLoadOperation;
import net.daverix.ajvm.jvm.IndexedDStoreOperation;
import net.daverix.ajvm.jvm.IndexedFLoadOperation;
import net.daverix.ajvm.jvm.IndexedFStoreOperation;
import net.daverix.ajvm.jvm.IndexedILoadOperation;
import net.daverix.ajvm.jvm.IndexedIStoreOperation;
import net.daverix.ajvm.jvm.IndexedLLoadOperation;
import net.daverix.ajvm.jvm.IndexedLStoreOperation;
import net.daverix.ajvm.jvm.IntegerObject;
import net.daverix.ajvm.jvm.InvokeSpecialOperation;
import net.daverix.ajvm.jvm.InvokeStaticOperation;
import net.daverix.ajvm.jvm.InvokeVirtualOperation;
import net.daverix.ajvm.jvm.L2IOperation;
import net.daverix.ajvm.jvm.LDCOperation;
import net.daverix.ajvm.jvm.LLoadOperation;
import net.daverix.ajvm.jvm.LStoreOperation;
import net.daverix.ajvm.jvm.NewOperation;
import net.daverix.ajvm.jvm.NoOperation;
import net.daverix.ajvm.jvm.PrintStreamObject;
import net.daverix.ajvm.jvm.PushConstOperation;
import net.daverix.ajvm.jvm.RuntimeVirtualObject;
import net.daverix.ajvm.jvm.StringBuilderObject;
import net.daverix.ajvm.jvm.SystemObject;
import net.daverix.ajvm.jvm.TableSwitchOperation;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestObjectLoader implements VirtualObjectLoader {
    private final Map<String, VirtualObject> staticClasses;
    private final File dir;
    private final PrintStreamObject outStream;
    private final PrintStreamObject errStream;

    public TestObjectLoader(Map<String, VirtualObject> staticClasses,
                            File dir,
                            PrintStreamObject outStream,
                            PrintStreamObject errStream) {
        this.staticClasses = staticClasses;
        this.dir = dir;
        this.outStream = outStream;
        this.errStream = errStream;
    }

    @Override
    public VirtualObject load(String className) throws IOException {
        if("java/lang/System".equals(className)) {
            return new SystemObject(outStream, errStream);
        } else if("java/lang/StringBuilder".equals(className)) {
            return new StringBuilderObject();
        } else if("java/lang/Integer".equals(className)) {
            return new IntegerObject();
        }

        File file = new File(dir + "/" + className + ".class");
        if(!file.exists())
            throw new IOException("Cannot find " + file);

        ClassInfo classInfo;
        try(ClassInfoReader reader = new ClassInfoReader(new DataInputStream(new FileInputStream(file)))) {
            classInfo = reader.read();
        }
        ConstantPool constantPool = classInfo.getConstantPool();

        Map<Integer, ByteCodeOperation> byteCodeOperations = new HashMap<>();
        byteCodeOperations.put(Opcodes.INSTANCE.getNEW(), new NewOperation(this, constantPool));
        byteCodeOperations.put(Opcodes.INSTANCE.getDUP(), new DupOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getLDC(), new LDCOperation(constantPool));
        byteCodeOperations.put(Opcodes.INSTANCE.getGETSTATIC(), new GetStaticOperation(staticClasses, this, constantPool));
        byteCodeOperations.put(Opcodes.INSTANCE.getINVOKEVIRTUAL(), new InvokeVirtualOperation(constantPool));
        byteCodeOperations.put(Opcodes.INSTANCE.getINVOKESPECIAL(), new InvokeSpecialOperation(constantPool));
        byteCodeOperations.put(Opcodes.INSTANCE.getINVOKESTATIC(), new InvokeStaticOperation(staticClasses, this, constantPool));
        byteCodeOperations.put(Opcodes.INSTANCE.getICONST_M1(), new PushConstOperation<>(-1));
        byteCodeOperations.put(Opcodes.INSTANCE.getICONST_0(), new PushConstOperation<>(0));
        byteCodeOperations.put(Opcodes.INSTANCE.getLCONST_0(), new PushConstOperation<>(0L));
        byteCodeOperations.put(Opcodes.INSTANCE.getDCONST_0(), new PushConstOperation<>(0d));
        byteCodeOperations.put(Opcodes.INSTANCE.getFCONST_0(), new PushConstOperation<>(0f));
        byteCodeOperations.put(Opcodes.INSTANCE.getICONST_1(), new PushConstOperation<>(1));
        byteCodeOperations.put(Opcodes.INSTANCE.getLCONST_1(), new PushConstOperation<>(1L));
        byteCodeOperations.put(Opcodes.INSTANCE.getDCONST_1(), new PushConstOperation<>(1d));
        byteCodeOperations.put(Opcodes.INSTANCE.getFCONST_1(), new PushConstOperation<>(1f));
        byteCodeOperations.put(Opcodes.INSTANCE.getICONST_2(), new PushConstOperation<>(2));
        byteCodeOperations.put(Opcodes.INSTANCE.getFCONST_2(), new PushConstOperation<>(2f));
        byteCodeOperations.put(Opcodes.INSTANCE.getICONST_3(), new PushConstOperation<>(3));
        byteCodeOperations.put(Opcodes.INSTANCE.getICONST_4(), new PushConstOperation<>(4));
        byteCodeOperations.put(Opcodes.INSTANCE.getICONST_5(), new PushConstOperation<>(5));
        byteCodeOperations.put(Opcodes.INSTANCE.getASTORE_0(), new IndexedAStoreOperation(0));
        byteCodeOperations.put(Opcodes.INSTANCE.getISTORE_0(), new IndexedIStoreOperation(0));
        byteCodeOperations.put(Opcodes.INSTANCE.getLSTORE_0(), new IndexedLStoreOperation(0));
        byteCodeOperations.put(Opcodes.INSTANCE.getDSTORE_0(), new IndexedDStoreOperation(0));
        byteCodeOperations.put(Opcodes.INSTANCE.getFSTORE_0(), new IndexedFStoreOperation(0));
        byteCodeOperations.put(Opcodes.INSTANCE.getASTORE_1(), new IndexedAStoreOperation(1));
        byteCodeOperations.put(Opcodes.INSTANCE.getISTORE_1(), new IndexedIStoreOperation(1));
        byteCodeOperations.put(Opcodes.INSTANCE.getLSTORE_1(), new IndexedLStoreOperation(1));
        byteCodeOperations.put(Opcodes.INSTANCE.getDSTORE_1(), new IndexedDStoreOperation(1));
        byteCodeOperations.put(Opcodes.INSTANCE.getFSTORE_1(), new IndexedFStoreOperation(1));
        byteCodeOperations.put(Opcodes.INSTANCE.getASTORE_2(), new IndexedAStoreOperation(2));
        byteCodeOperations.put(Opcodes.INSTANCE.getISTORE_2(), new IndexedIStoreOperation(2));
        byteCodeOperations.put(Opcodes.INSTANCE.getLSTORE_2(), new IndexedLStoreOperation(2));
        byteCodeOperations.put(Opcodes.INSTANCE.getDSTORE_2(), new IndexedDStoreOperation(2));
        byteCodeOperations.put(Opcodes.INSTANCE.getFSTORE_2(), new IndexedFStoreOperation(2));
        byteCodeOperations.put(Opcodes.INSTANCE.getASTORE_3(), new IndexedAStoreOperation(3));
        byteCodeOperations.put(Opcodes.INSTANCE.getISTORE_3(), new IndexedIStoreOperation(3));
        byteCodeOperations.put(Opcodes.INSTANCE.getLSTORE_3(), new IndexedLStoreOperation(3));
        byteCodeOperations.put(Opcodes.INSTANCE.getDSTORE_3(), new IndexedDStoreOperation(3));
        byteCodeOperations.put(Opcodes.INSTANCE.getFSTORE_3(), new IndexedFStoreOperation(3));
        byteCodeOperations.put(Opcodes.INSTANCE.getASTORE(), new AStoreOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getISTORE(), new IStoreOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getLSTORE(), new LStoreOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getDSTORE(), new DStoreOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getFSTORE(), new FStoreOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getALOAD_0(), new IndexedALoadOperation(0));
        byteCodeOperations.put(Opcodes.INSTANCE.getILOAD_0(), new IndexedILoadOperation(0));
        byteCodeOperations.put(Opcodes.INSTANCE.getLLOAD_0(), new IndexedLLoadOperation(0));
        byteCodeOperations.put(Opcodes.INSTANCE.getDLOAD_0(), new IndexedDLoadOperation(0));
        byteCodeOperations.put(Opcodes.INSTANCE.getFLOAD_0(), new IndexedFLoadOperation(0));
        byteCodeOperations.put(Opcodes.INSTANCE.getALOAD_1(), new IndexedALoadOperation(1));
        byteCodeOperations.put(Opcodes.INSTANCE.getILOAD_1(), new IndexedILoadOperation(1));
        byteCodeOperations.put(Opcodes.INSTANCE.getLLOAD_1(), new IndexedLLoadOperation(1));
        byteCodeOperations.put(Opcodes.INSTANCE.getDLOAD_1(), new IndexedDLoadOperation(1));
        byteCodeOperations.put(Opcodes.INSTANCE.getFLOAD_1(), new IndexedFLoadOperation(1));
        byteCodeOperations.put(Opcodes.INSTANCE.getALOAD_2(), new IndexedALoadOperation(2));
        byteCodeOperations.put(Opcodes.INSTANCE.getILOAD_2(), new IndexedILoadOperation(2));
        byteCodeOperations.put(Opcodes.INSTANCE.getLLOAD_2(), new IndexedLLoadOperation(2));
        byteCodeOperations.put(Opcodes.INSTANCE.getDLOAD_2(), new IndexedDLoadOperation(2));
        byteCodeOperations.put(Opcodes.INSTANCE.getFLOAD_2(), new IndexedFLoadOperation(2));
        byteCodeOperations.put(Opcodes.INSTANCE.getALOAD_3(), new IndexedALoadOperation(3));
        byteCodeOperations.put(Opcodes.INSTANCE.getILOAD_3(), new IndexedILoadOperation(3));
        byteCodeOperations.put(Opcodes.INSTANCE.getLLOAD_3(), new IndexedLLoadOperation(3));
        byteCodeOperations.put(Opcodes.INSTANCE.getDLOAD_3(), new IndexedDLoadOperation(3));
        byteCodeOperations.put(Opcodes.INSTANCE.getFLOAD_3(), new IndexedFLoadOperation(3));
        byteCodeOperations.put(Opcodes.INSTANCE.getALOAD(), new ALoadOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getILOAD(), new ILoadOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getLLOAD(), new LLoadOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getDLOAD(), new DLoadOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getFLOAD(), new FLoadOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getAALOAD(), new AALoadOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getIADD(), new IAddOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getISUB(), new ISubOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getIMUL(), new IMulOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getIDIV(), new IDivOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getIREM(), new IRemOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getTABLESWITCH(), new TableSwitchOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getIFEQ(), new IfEqOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getGOTO(), new GotoOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getNOP(), new NoOperation());
        byteCodeOperations.put(Opcodes.INSTANCE.getL2I(), new L2IOperation());

        return new RuntimeVirtualObject(classInfo, byteCodeOperations);
    }
}
