/*
    Java Virtual Machine for Android
    Copyright (C) 2017 David Laurell

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
 */
package net.daverix.ajvm;


import java.io.IOException;
import java.util.Stack;

public class Invoker {
    public static Object run(ClassFile classFile, String methodName, Object... args) throws IOException {
        Method method = classFile.getMethodByName(methodName);
        CodeInfo codeAttribute = method.getCodeAttribute();
        byte[] code = codeAttribute.getCode();

        Stack<Object> stack = new Stack<>();

        int index = 0;
        while (index < code.length) {
            int byteCode = code[index++] & 0xFF;
            switch (byteCode) {
                case Opcodes.ILOAD_0:
                    stack.push(args[0]);
                    break;
                case Opcodes.ILOAD_1:
                    stack.push(args[1]);
                    break;
                case Opcodes.IADD:
                    int addSecond = (int) stack.pop();
                    int addFirst = (int) stack.pop();
                    stack.push(addFirst + addSecond);
                    break;
                case Opcodes.ISUB:
                    int subSecond = (int) stack.pop();
                    int subFirst = (int) stack.pop();
                    stack.push(subFirst - subSecond);
                    break;
                case Opcodes.IRETURN:
                    return stack.pop();
                default:
                    throw new IllegalStateException("Unknown bytecode: " + Integer.toHexString(byteCode));
            }
        }

        return null;
    }
}
