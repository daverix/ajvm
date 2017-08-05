package net.daverix.ajvm;


public class NameAndTypeDescriptorReference {
    private final int nameIndex;
    private final int descriptorIndex;

    public NameAndTypeDescriptorReference(int nameIndex, int descriptorIndex) {
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public int getDescriptorIndex() {
        return descriptorIndex;
    }
}
