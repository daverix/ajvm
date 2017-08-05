package net.daverix.ajvm;


public class MethodHandleReference {


    private final int referenceKind;
    private final int referenceIndex;

    public MethodHandleReference(int referenceKind, int referenceIndex) {
        this.referenceKind = referenceKind;
        this.referenceIndex = referenceIndex;
    }

    public int getReferenceKind() {
        return referenceKind;
    }

    public int getReferenceIndex() {
        return referenceIndex;
    }
}
