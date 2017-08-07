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
                    int iAdd2 = (int) stack.pop();
                    int iAdd1 = (int) stack.pop();
                    stack.push(iAdd1 + iAdd2);
                    break;
                case Opcodes.ISUB:
                    int iSub2 = (int) stack.pop();
                    int iSub1 = (int) stack.pop();
                    stack.push(iSub1 - iSub2);
                    break;
                case Opcodes.IMUL:
                    int iMul2 = (int) stack.pop();
                    int iMul1 = (int) stack.pop();
                    stack.push(iMul1 * iMul2);
                    break;
                case Opcodes.IDIV:
                    int iDiv2 = (int) stack.pop();
                    int iDiv1 = (int) stack.pop();
                    stack.push(iDiv1 / iDiv2);
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
