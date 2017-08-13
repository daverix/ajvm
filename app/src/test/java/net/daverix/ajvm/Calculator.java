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

public class Calculator {
    public static void main(String[] args) {
        int first = Integer.parseInt(args[0]);
        String operator = args[1];
        int second = Integer.parseInt(args[2]);

        switch (operator) {
            case "+":
                System.out.println(first + second);
                break;
            case "-":
                System.out.println(first - second);
                break;
            case "/":
                System.out.println(first / second);
                break;
            case "*":
                System.out.println(first * second);
                break;
            case "%":
                System.out.println(first % second);
                break;
            default:
                System.err.println("Unknown operator " + operator);
                break;
        }
    }
}
