package net.daverix.ajvm;


public class FieldReference {
    private final int classIndex;
    private final int nameAndTypeIndex;

    public FieldReference(int classIndex, int nameAndTypeIndex) {
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }
}
