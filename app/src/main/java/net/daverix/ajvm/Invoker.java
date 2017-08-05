package net.daverix.ajvm;


public class Invoker {
    public void run(ClassFile classFile, String methodName) {
        Method method = classFile.getMethodByName(methodName);


    }
}
