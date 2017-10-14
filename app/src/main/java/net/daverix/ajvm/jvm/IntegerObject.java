package net.daverix.ajvm.jvm;


import net.daverix.ajvm.VirtualObject;

import java.io.IOException;

public class IntegerObject implements VirtualObject {
    private Integer integer;

    public IntegerObject() {
    }

    @Override
    public void initialize(Object[] args) {
        integer = (Integer) args[0];
    }

    @Override
    public String getName() {
        return "java/lang/Integer";
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
        if("parseInt".equals(name) && descriptor.equals("(Ljava/lang/String;)I")) {
            return Integer.parseInt((String) args[0]);
        }

        return null;
    }
}
