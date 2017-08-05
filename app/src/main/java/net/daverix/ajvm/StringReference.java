package net.daverix.ajvm;


public class StringReference {
    private final int index;
    private final Object[] constantPool;

    public StringReference(int index, Object[] constantPool) {
        this.index = index;
        this.constantPool = constantPool;
    }

    @Override
    public String toString() {
        return (String) constantPool[index];
    }
}
