package net.daverix.ajvm;


public class InvokeDynamicReference {
    private final int bootstrapMethodAttrIndex;
    private final int nameAndTypeIndex;

    public InvokeDynamicReference(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public int getBootstrapMethodAttrIndex() {
        return bootstrapMethodAttrIndex;
    }

    public int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }
}
