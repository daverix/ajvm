package net.daverix.ajvm.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Echo {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            do {
                line = reader.readLine();
                System.out.println(line);
            } while(line != null && !line.equals("x"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
