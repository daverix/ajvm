package net.daverix.ajvm;


public class ClassReference {
    private final int nameIndex;
    private final Object[] constantPool;

    public ClassReference(int nameIndex, Object[] constantPool) {
        this.nameIndex = nameIndex;
        this.constantPool = constantPool;
    }

    public String getName() {
        return (String) constantPool[nameIndex];
    }
}
