package net.daverix.ajvm;

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
}
