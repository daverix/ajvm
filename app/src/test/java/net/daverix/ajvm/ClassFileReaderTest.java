package net.daverix.ajvm;

import org.junit.Test;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;

public class ClassFileReaderTest {
    @Test
    public void readAddMethodInAdditionClass() throws IOException {
        System.out.println(new File("./").getAbsolutePath());
        File file = new File("app/build/intermediates/classes/test/debug/net/daverix/ajvm/Addition.class");

        ClassFile classFile;
        try(ClassFileReader reader = new ClassFileReader(new DataInputStream(new FileInputStream(file)))) {
            classFile = reader.read();
        }

        assertThat(classFile.getClassName()).isEqualTo("net/daverix/ajvm/Addition");
        assertThat(classFile.getSuperClassName()).isEqualTo("java/lang/Object");

        Method addMethod = classFile.getMethodByName("add");
        assertThat(addMethod).isNotNull();

        Attribute code = addMethod.getAttributeByName("Code");
        assertThat(code).isNotNull();

        CodeAttribute codeAttribute = code.asCodeAttribute();
        assertThat(codeAttribute).isNotNull();

        byte[] byteCode = codeAttribute.getCode();
        assertThat(byteCode).isNotEmpty();

        for (int i = 0; i < byteCode.length; i++) {

        }
    }
}