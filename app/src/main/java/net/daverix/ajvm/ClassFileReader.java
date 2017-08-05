package net.daverix.ajvm;


import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ClassFileReader implements Closeable {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;
    private static final int CONSTANT_TAG_STRING = 1;
    private static final int CONSTANT_TAG_INTEGER = 3;
    private static final int CONSTANT_TAG_FLOAT = 4;
    private static final int CONSTANT_TAG_LONG = 5;
    private static final int CONSTANT_TAG_DOUBLE = 6;
    private static final int CONSTANT_TAG_CLASS_REFERENCE = 7;
    private static final int CONSTANT_TAG_STRING_REFERENCE = 8;
    private static final int CONSTANT_TAG_FIELD_REFERENCE = 9;
    private static final int CONSTANT_TAG_METHOD_REFERENCE = 10;
    private static final int CONSTANT_TAG_INTERFACE_METHOD_REFERENCE = 11;
    private static final int CONSTANT_TAG_NAME_AND_TYPE_DESCRIPTOR = 12;
    private static final int CONSTANT_TAG_METHOD_HANDLE = 15;
    private static final int CONSTANT_TAG_NAME_METHOD_TYPE = 16;
    private static final int CONSTANT_TAG_INVOKE_DYNAMIC = 18;
    private final DataInputStream stream;

    public ClassFileReader(InputStream stream) {
        if(stream == null)
            throw new IllegalArgumentException("stream is null");

        this.stream = new DataInputStream(stream);
    }

    public ClassFile read() throws IOException {
        int magicNumber = stream.readInt();
        if(magicNumber != MAGIC_NUMBER) {
            throw new IOException("Not a java class file, expected " + MAGIC_NUMBER + " but got " + magicNumber);
        }
        int minorVersion = stream.readUnsignedShort();
        int majorVersion = stream.readUnsignedShort();
        Object[] constantPool = readConstantPool();
        int accessFlags = stream.readUnsignedShort();
        int thisClass = stream.readUnsignedShort();
        int superClass = stream.readUnsignedShort();
        int[] interfaces = readInterfaces();
        Field[] fields = readFields(constantPool);
        Method[] methods = readMethods(constantPool);
        Attribute[] attributes = readAttributes(constantPool);

        return new ClassFile(majorVersion,
                minorVersion,
                constantPool,
                accessFlags,
                thisClass,
                superClass,
                interfaces,
                fields,
                methods,
                attributes);
    }

    private Method[] readMethods(Object[] constantPool) throws IOException {
        int count = stream.readUnsignedShort();
        Method[] methods = new Method[count];
        for (int i = 0; i < count; i++) {
            methods[i] = readMethod(constantPool);
        }
        return methods;
    }

    private Method readMethod(Object[] constantPool) throws IOException {
        int accessFlags = stream.readUnsignedShort();
        int nameIndex = stream.readUnsignedShort();
        int descriptorIndex = stream.readUnsignedShort();
        Attribute[] attributes = readAttributes(constantPool);

        return new Method(accessFlags, nameIndex, descriptorIndex, attributes, constantPool);
    }

    private Field[] readFields(Object[] constantPool) throws IOException {
        int count = stream.readUnsignedShort();
        Field[] fields = new Field[count];
        for (int i = 0; i < count; i++) {
            fields[i] = readField(constantPool);
        }
        return fields;
    }

    private Field readField(Object[] constantPool) throws IOException {
        int accessFlags = stream.readUnsignedShort();
        int nameIndex = stream.readUnsignedShort();
        int descriptorIndex = stream.readUnsignedShort();
        Attribute[] attributes = readAttributes(constantPool);

        return new Field(accessFlags,
                nameIndex,
                descriptorIndex,
                attributes,
                constantPool);
    }

    private Attribute[] readAttributes(Object[] constantPool) throws IOException {
        int count = stream.readUnsignedShort();
        Attribute[] attributes = new Attribute[count];
        for (int i = 0; i < count; i++) {
            attributes[i] = readAttribute(constantPool);
        }
        return attributes;
    }

    private Attribute readAttribute(Object[] constantPool) throws IOException {
        int nameIndex = stream.readUnsignedShort();
        int attributeLength = stream.readInt();
        byte[] info = readBytes(attributeLength);
        return new Attribute(nameIndex, info, constantPool);
    }

    private int[] readInterfaces() throws IOException {
        int count = stream.readUnsignedShort();
        int[] interfaces = new int[count];
        for (int i = 0; i < count; i++) {
            interfaces[i] = stream.readUnsignedShort();
        }
        return interfaces;
    }

    private Object[] readConstantPool() throws IOException {
        int constantPoolCount = stream.readUnsignedShort();
        Object[] constantPool = new Object[constantPoolCount];
        for (int i = 1; i < constantPoolCount; i++) {
            int tag = stream.readUnsignedByte();
            switch (tag) {
                case -1:
                    throw new IOException("EOF when reading type from constant pool");
                case CONSTANT_TAG_STRING:
                    constantPool[i] = stream.readUTF();
                    continue;
                case CONSTANT_TAG_INTEGER:
                    constantPool[i] = stream.readInt();
                    continue;
                case CONSTANT_TAG_FLOAT:
                    constantPool[i] = stream.readFloat();
                    continue;
                case CONSTANT_TAG_LONG:
                    byte[] longBytes = new byte[8];
                    if(stream.read(longBytes, 0, 4) != 4)
                        throw new IllegalStateException("could not read first part of long");

                    if(stream.readUnsignedShort() != CONSTANT_TAG_LONG)
                        throw new IllegalStateException("expecting to read another constant pool for long value");
                    if(stream.read(longBytes, 4, 4) != 4)
                        throw new IllegalStateException("could not read second part of long");

                    constantPool[i] = ByteBuffer.wrap(longBytes).getLong();
                    i++; // we advance index by two
                    continue;
                case CONSTANT_TAG_DOUBLE:
                    byte[] doubleBytes = new byte[8];
                    if(stream.read(doubleBytes, 0, 4) != 4)
                        throw new IllegalStateException("could not read first part of double");

                    if(stream.readUnsignedShort() != CONSTANT_TAG_LONG)
                        throw new IllegalStateException("expecting to read another constant pool for double value");
                    if(stream.read(doubleBytes, 4, 4) != 4)
                        throw new IllegalStateException("could not read second part of double");

                    constantPool[i] = ByteBuffer.wrap(doubleBytes).getDouble();
                    i++; // we advance index by two
                    constantPool[i] = stream.readDouble();
                    continue;
                case CONSTANT_TAG_CLASS_REFERENCE:
                    constantPool[i] = new ClassReference(stream.readUnsignedShort(), constantPool);
                    continue;
                case CONSTANT_TAG_STRING_REFERENCE:
                    constantPool[i] = new StringReference(stream.readUnsignedShort(), constantPool);
                    continue;
                case CONSTANT_TAG_FIELD_REFERENCE:
                    constantPool[i] = new FieldReference(stream.readUnsignedShort(), stream.readUnsignedShort());
                    continue;
                case CONSTANT_TAG_METHOD_REFERENCE:
                    constantPool[i] = new MethodReference(stream.readUnsignedShort(), stream.readUnsignedShort());
                    continue;
                case CONSTANT_TAG_INTERFACE_METHOD_REFERENCE:
                    constantPool[i] = new InterfaceMethodReference(stream.readUnsignedShort(), stream.readUnsignedShort());
                    continue;
                case CONSTANT_TAG_NAME_AND_TYPE_DESCRIPTOR:
                    constantPool[i] = new NameAndTypeDescriptorReference(stream.readUnsignedShort(), stream.readUnsignedShort());
                    continue;
                case CONSTANT_TAG_NAME_METHOD_TYPE:
                    constantPool[i] = new MethodTypeReference(stream.readUnsignedShort());
                    continue;
                case CONSTANT_TAG_METHOD_HANDLE:
                    constantPool[i] = new MethodHandleReference(stream.readUnsignedByte(), stream.readUnsignedShort());
                    continue;
                case CONSTANT_TAG_INVOKE_DYNAMIC:
                    constantPool[i] = new InvokeDynamicReference(stream.readUnsignedShort(), stream.readUnsignedShort());
            }
        }
        return constantPool;
    }

    private byte[] readBytes(int size) throws IOException {
        byte[] data = new byte[size];
        int read = stream.read(data);
        if(read != size) {
            throw new IOException("incorrect number of bytes read: " + read + ", expected " + size);
        }
        return data;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
