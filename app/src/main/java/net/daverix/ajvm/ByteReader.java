package net.daverix.ajvm;


import java.io.IOException;
import java.io.InputStream;

public class ByteReader {

    public static byte[] readBytes(InputStream inputStream, int size) throws IOException {
        byte[] data = new byte[size];
        int read = inputStream.read(data);
        if(read != size) {
            throw new IOException("incorrect number of bytes read: " + read + ", expected " + size);
        }
        return data;
    }
}
