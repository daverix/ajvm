package net.daverix.ajvm.jvm;


import java.io.IOException;

public class StringBuilderObject implements VirtualObject {
    private StringBuilder builder;

    public StringBuilderObject() {

    }

    @Override
    public String getName() {
        return "java/lang/StringBuilder";
    }

    @Override
    public void setFieldValue(String fieldName, Object value) {

    }

    @Override
    public Object getFieldValue(String fieldName) {
        return null;
    }

    @Override
    public Object invokeMethod(String name, String descriptor, Object... args) throws IOException {
        if("<init>".equals(name))  {
            builder = new StringBuilder();
            return null;
        } else if("append".equals(name)) {
            builder.append(args[0]);
            return this;
        } else if("toString".equals(name)) {
            return builder.toString();
        }

        throw new UnsupportedOperationException("method " + name +" not implemented in StringBuilderObject");
    }
}
