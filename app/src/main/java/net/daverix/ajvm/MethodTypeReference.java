package net.daverix.ajvm;


public class MethodTypeReference {
    private final int descriptorIndex;

    public MethodTypeReference(int descriptorIndex) {
        this.descriptorIndex = descriptorIndex;
    }

    public int getDescriptorIndex() {
        return descriptorIndex;
    }
}
