package net.daverix.ajvm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static net.daverix.ajvm.ByteReader.readBytes;

public class Attribute {
    private final int nameIndex;
    private final byte[] info;
    private final Object[] constantPool;

    public Attribute(int nameIndex, byte[] info, Object[] constantPool) {
        this.nameIndex = nameIndex;
        this.info = info;
        this.constantPool = constantPool;
    }

    public String getName() {
        return (String) constantPool[nameIndex];
    }

    public byte[] getInfo() {
        return info;
    }

    public CodeAttribute asCodeAttribute() throws IOException {
        CodeAttribute attribute;
        try (DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(info))) {
            attribute = CodeAttribute.read(dataInputStream, constantPool);
        }
        return attribute;
    }

    public static Attribute read(DataInputStream stream, Object[] constantPool) throws IOException {
        int nameIndex = stream.readUnsignedShort();
        int attributeLength = stream.readInt();
        byte[] info = readBytes(stream, attributeLength);
        return new Attribute(nameIndex, info, constantPool);
    }

    public static Attribute[] readArray(DataInputStream stream, Object[] constantPool) throws IOException {
        int count = stream.readUnsignedShort();
        Attribute[] attributes = new Attribute[count];
        for (int i = 0; i < count; i++) {
            attributes[i] = Attribute.read(stream, constantPool);
        }
        return attributes;
    }
}
