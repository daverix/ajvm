package net.daverix.ajvm;

import org.junit.Before;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;

public class MathClassFileReaderTest {
    private ClassFile classFile;

    @Before
    public void setUp() throws IOException {
        //System.out.println(new File("./").getAbsolutePath());
        File file = new File("app/build/intermediates/classes/test/debug/net/daverix/ajvm/Math.class");

        try(ClassFileReader reader = new ClassFileReader(new DataInputStream(new FileInputStream(file)))) {
            classFile = reader.read();
        }
    }

    @Test
    public void readClassName() {
        assertThat(classFile.getClassName()).isEqualTo("net/daverix/ajvm/Math");
    }

    @Test
    public void readSuperClassName() {
        assertThat(classFile.getSuperClassName()).isEqualTo("java/lang/Object");
    }

    @Test
    public void readAddMethod() throws IOException {
        Method addMethod = classFile.getMethodByName("add");
        assertThat(addMethod).isNotNull();
    }

    @Test
    public void readCodeAttributeOnAddMethod() throws IOException {
        CodeInfo code = classFile.getMethodByName("add").getCodeAttribute();
        assertThat(code).isNotNull();

        byte[] byteCode = code.getCode();
        assertThat(byteCode).isNotEmpty();
    }

    @Test
    public void invokeAddOneAndTwo() throws IOException {
        Object result = Invoker.run(classFile, "add", 1, 2);
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void invokeSubtractTwoFromThree() throws IOException {
        Object result = Invoker.run(classFile, "subtract", 3, 2);
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void invokeDivideSixByTwo() throws IOException {
        Object result = Invoker.run(classFile, "divide", 6, 2);
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void invokeMultiplyTwoByThree() throws IOException {
        Object result = Invoker.run(classFile, "multiply", 2, 3);
        assertThat(result).isEqualTo(6);
    }
}
