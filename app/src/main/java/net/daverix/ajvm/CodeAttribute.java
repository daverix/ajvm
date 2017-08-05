package net.daverix.ajvm;


import java.io.DataInputStream;
import java.io.IOException;

public class CodeAttribute {
    private final int maxStack;
    private final int maxLocals;
    private final byte[] code;
    private final Exception[] exceptionTable;
    private final Attribute[] attributes;

    public CodeAttribute(int maxStack,
                         int maxLocals,
                         byte[] code,
                         Exception[] exceptionTable,
                         Attribute[] attributes) {
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.code = code;
        this.exceptionTable = exceptionTable;
        this.attributes = attributes;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public byte[] getCode() {
        return code;
    }

    public Exception[] getExceptionTable() {
        return exceptionTable;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public static CodeAttribute read(DataInputStream stream, Object[] constantPool) throws IOException {
        int maxStack = stream.readUnsignedShort();
        int maxLocals = stream.readUnsignedShort();
        int codeLength = stream.readInt();
        byte[] code = new byte[codeLength];
        if(stream.read(code) != codeLength)
            throw new IOException("could not read all bytes for code");

        int exceptionCount = stream.readUnsignedShort();
        Exception[] exceptionTable = new Exception[exceptionCount];
        for (int i = 0; i < exceptionCount; i++) {
            exceptionTable[i] = Exception.read(stream, constantPool);
        }
        int attributeCount = stream.readUnsignedShort();
        Attribute[] attributes = new Attribute[attributeCount];
        for (int i = 0; i <attributeCount; i++) {
            attributes[i] = Attribute.read(stream, constantPool);
        }

        return new CodeAttribute(maxStack, maxLocals, code, exceptionTable, attributes);
    }
}
