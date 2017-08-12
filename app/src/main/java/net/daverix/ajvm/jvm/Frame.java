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
package net.daverix.ajvm.jvm;


import java.util.Stack;

public class Frame {
    private final Stack<Object> operandStack = new Stack<>();
    private final Object[] localVariables;
    private final Object[] classConstantPool;
    private final int maxStackDepth;

    public Frame(Object[] classConstantPool,
                 int maxLocals,
                 int maxStackDepth) {
        this.localVariables = new Object[maxLocals];
        this.classConstantPool = classConstantPool;
        this.maxStackDepth = maxStackDepth;
    }

    public void push(Object object) {
        if(operandStack.size() == maxStackDepth)
            throw new StackOverflowError("can't push object onto stack, max stack depth " + maxStackDepth + " reached");

        operandStack.push(object);
    }

    public Object pop() {
        return operandStack.pop();
    }

    public void setLocalVariable(int index, Object value) {
        localVariables[index] = value;
    }

    public Object getLocalVariable(int index) {
        return localVariables[index];
    }

    public Object getConstant(int index) {
        return classConstantPool[index];
    }
}
