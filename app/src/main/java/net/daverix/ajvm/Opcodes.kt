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
package net.daverix.ajvm

/**
 * Opcodes for Java taken from https://en.wikipedia.org/wiki/Java_bytecode_instruction_listings
 */
object Opcodes {
    /**
     * perform no operation
     */
    val NOP = 0x00
    /**
     * push a null reference onto the stack
     */
    val ACONST_NULL = 0x01
    /**
     * load the int value −1 onto the stack
     */
    val ICONST_M1 = 0x02
    /**
     * load the int value 0 onto the stack
     */
    val ICONST_0 = 0x03
    /**
     * load the int value 1 onto the stack
     */
    val ICONST_1 = 0x04
    /**
     * load the int value 2 onto the stack
     */
    val ICONST_2 = 0x05
    /**
     * load the int value 3 onto the stack
     */
    val ICONST_3 = 0x06
    /**
     * load the int value 4 onto the stack
     */
    val ICONST_4 = 0x07
    /**
     * load the int value 5 onto the stack
     */
    val ICONST_5 = 0x08
    /**
     * push 0L (the number zero with type long) onto the stack
     */
    val LCONST_0 = 0x09
    /**
     * push 1L (the number one with type long) onto the stack
     */
    val LCONST_1 = 0x0a
    /**
     * push 0.0f on the stack
     */
    val FCONST_0 = 0x0b
    /**
     * push 1.0f on the stack
     */
    val FCONST_1 = 0x0c
    /**
     * push 2.0f on the stack
     */
    val FCONST_2 = 0x0d
    /**
     * push the constant 0.0 (a double) onto the stack
     */
    val DCONST_0 = 0x0e
    /**
     * push the constant 0.0 (a double) onto the stack
     */
    val DCONST_1 = 0x0f
    /**
     * push a byte onto the stack as an integer value
     */
    val BI_PUSH = 0x10
    /**
     * push a short onto the stack as an integer value
     */
    val SI_PUSH = 0x11
    /**
     * push a constant #index from a constant pool (String, int, float, Class,
     * java.lang.invoke.MethodType, or java.lang.invoke.MethodHandle) onto the stack
     */
    val LDC = 0x12
    /**
     * push a constant #index from a constant pool (String, int, float, Class,
     * java.lang.invoke.MethodType, or java.lang.invoke.MethodHandle) onto the stack (wide index is
     * constructed as indexbyte1 << 8 + indexbyte2)
     */
    val LDC_W = 0x13
    /**
     * push a constant #index from a constant pool (double or long) onto the stack (wide index is
     * constructed as indexbyte1 << 8 + indexbyte2)
     */
    val LDC2_W = 0x14
    /**
     * load an int value from a local variable #index
     */
    val ILOAD = 0x15
    /**
     * load a long value from a local variable #index
     */
    val LLOAD = 0x16
    /**
     * load a float value from a local variable #index
     */
    val FLOAD = 0x17
    /**
     * load a double value from a local variable #index
     */
    val DLOAD = 0x18
    /**
     * load a reference onto the stack from a local variable #index
     */
    val ALOAD = 0x19
    /**
     * load an int value from local variable 0
     */
    val ILOAD_0 = 0x1a
    /**
     * load an int value from local variable 1
     */
    val ILOAD_1 = 0x1b
    /**
     * load an int value from local variable 2
     */
    val ILOAD_2 = 0x1c
    /**
     * load an int value from local variable 3
     */
    val ILOAD_3 = 0x1d
    /**
     * load a long value from a local variable 0
     */
    val LLOAD_0 = 0x1e
    /**
     * load a long value from a local variable 1
     */
    val LLOAD_1 = 0x1f
    /**
     * load a long value from a local variable 2
     */
    val LLOAD_2 = 0x20
    /**
     * load a long value from a local variable 3
     */
    val LLOAD_3 = 0x21
    /**
     * load a float value from local variable 0
     */
    val FLOAD_0 = 0x22
    /**
     * load a float value from local variable 1
     */
    val FLOAD_1 = 0x23
    /**
     * load a float value from local variable 2
     */
    val FLOAD_2 = 0x24
    /**
     * load a float value from local variable 3
     */
    val FLOAD_3 = 0x25
    /**
     * load a double from local variable 0
     */
    val DLOAD_0 = 0x26
    /**
     * load a double from local variable 1
     */
    val DLOAD_1 = 0x27
    /**
     * load a double from local variable 2
     */
    val DLOAD_2 = 0x28
    /**
     * load a double from local variable 3
     */
    val DLOAD_3 = 0x29
    /**
     * load a reference onto the stack from local variable 0
     */
    val ALOAD_0 = 0x2a
    /**
     * load a reference onto the stack from local variable 1
     */
    val ALOAD_1 = 0x2b
    /**
     * load a reference onto the stack from local variable 2
     */
    val ALOAD_2 = 0x2c
    /**
     * load a reference onto the stack from local variable 3
     */
    val ALOAD_3 = 0x2d
    /**
     * load an int from an array
     */
    val IALOAD = 0x2e
    /**
     * load a long from an array
     */
    val LALOAD = 0x2f
    /**
     * load a float from an array
     */
    val FALOAD = 0x30
    /**
     * load a double from an array
     */
    val DALOAD = 0x31
    /**
     * load onto the stack a reference from an array
     */
    val AALOAD = 0x32
    /**
     * load a byte or Boolean value from an array
     */
    val BALOAD = 0x33
    /**
     * load a char from an array
     */
    val CALOAD = 0x34
    /**
     * load short from array
     */
    val SALOAD = 0x35
    /**
     * store int value into variable #index
     */
    val ISTORE = 0x36
    /**
     * store a long value in a local variable #index
     */
    val LSTORE = 0x37
    /**
     * store a float value into a local variable #index
     */
    val FSTORE = 0x38
    /**
     * store a double value into a local variable #index
     */
    val DSTORE = 0x39
    /**
     * store a reference into a local variable #index
     */
    val ASTORE = 0x3a
    /**
     * store int value into variable 0
     */
    val ISTORE_0 = 0x3b
    /**
     * store int value into variable 1
     */
    val ISTORE_1 = 0x3c
    /**
     * store int value into variable 2
     */
    val ISTORE_2 = 0x3d
    /**
     * store int value into variable 3
     */
    val ISTORE_3 = 0x3e
    /**
     * store a long value in a local variable 0
     */
    val LSTORE_0 = 0x3f
    /**
     * store a long value in a local variable 1
     */
    val LSTORE_1 = 0x40
    /**
     * store a long value in a local variable 2
     */
    val LSTORE_2 = 0x41
    /**
     * store a long value in a local variable 3
     */
    val LSTORE_3 = 0x42
    /**
     * store a float value in a local variable 0
     */
    val FSTORE_0 = 0x43
    /**
     * store a float value in a local variable 1
     */
    val FSTORE_1 = 0x44
    /**
     * store a float value in a local variable 2
     */
    val FSTORE_2 = 0x45
    /**
     * store a float value in a local variable 3
     */
    val FSTORE_3 = 0x46
    /**
     * store a double into local variable 0
     */
    val DSTORE_0 = 0x47
    /**
     * store a double into local variable 1
     */
    val DSTORE_1 = 0x48
    /**
     * store a double into local variable 2
     */
    val DSTORE_2 = 0x49
    /**
     * store a double into local variable 3
     */
    val DSTORE_3 = 0x4a
    /**
     * store a reference into local variable 0
     */
    val ASTORE_0 = 0x4b
    /**
     * store a reference into local variable 1
     */
    val ASTORE_1 = 0x4c
    /**
     * store a reference into local variable 2
     */
    val ASTORE_2 = 0x4d
    /**
     * store a reference into local variable 3
     */
    val ASTORE_3 = 0x4e
    /**
     * store an int into an array
     */
    val IASTORE = 0x4f
    /**
     * store a long into an array
     */
    val LASTORE = 0x50
    /**
     * store a float into an array
     */
    val FASTORE = 0x51
    /**
     * store a double into an array
     */
    val DASTORE = 0x52
    /**
     * store into a reference in an array
     */
    val AASTORE = 0x53
    /**
     * store a byte or Boolean value into an array
     */
    val BASTORE = 0x54
    /**
     * store a char into an array
     */
    val CASTORE = 0x55
    /**
     * store short to array
     */
    val SASTORE = 0x56
    /**
     * discard the top value on the stack
     */
    val POP = 0x57
    /**
     * discard the top two values on the stack (or one value, if it is a double or long)
     */
    val POP2 = 0x58
    /**
     * duplicate the value on top of the stack
     */
    val DUP = 0x59
    /**
     * insert a copy of the top value into the stack two values from the top. value1 and value2
     * must not be of the type double or long.
     */
    val DUP_X1 = 0x5a
    /**
     * insert a copy of the top value into the stack two (if value2 is double or long it takes up
     * the entry of value3, too) or three values (if value2 is neither double nor long) from the
     * top
     */
    val DUP_X2 = 0x5b
    /**
     * duplicate top two stack words (two values, if value1 is not double nor long; a single value,
     * if value1 is double or long)
     */
    val DUP2 = 0x5c
    /**
     * duplicate two words and insert beneath third word
     *
     * @see .DUP2
     */
    val DUP2_X1 = 0x5d
    /**
     * duplicate two words and insert beneath fourth word
     */
    val DUP2_X2 = 0x5e
    /**
     * swaps two top words on the stack (note that value1 and value2 must not be double or long)
     */
    val SWAP = 0x5f
    /**
     * add two ints
     */
    val IADD = 0x60
    /**
     * add two longs
     */
    val LADD = 0x61
    /**
     * add two floats
     */
    val FADD = 0x62
    /**
     * add two doubles
     */
    val DADD = 0x63
    /**
     * subtract two ints
     */
    val ISUB = 0x64
    /**
     * subtract two longs
     */
    val LSUB = 0x65
    /**
     * subtract two floats
     */
    val FSUB = 0x66
    /**
     * subtract a double from another
     */
    val DSUB = 0x67
    /**
     * multiply two integers
     */
    val IMUL = 0x68
    /**
     * multiply two longs
     */
    val LMUL = 0x69
    /**
     * multiply two floats
     */
    val FMUL = 0x6a
    /**
     * multiply two doubles
     */
    val DMUL = 0x6b
    /**
     * divide two integers
     */
    val IDIV = 0x6c
    /**
     * divide two longs
     */
    val LDIV = 0x6d
    /**
     * divide two floats
     */
    val FDIV = 0x6e
    /**
     * divide two doubles
     */
    val DDIV = 0x6f
    /**
     * logical int remainder
     */
    val IREM = 0x70
    /**
     * logical long remainder
     */
    val LREM = 0x71
    /**
     * get the remainder from a division between two floats
     */
    val FREM = 0x72
    /**
     * get the remainder from a division between two doubles
     */
    val DREM = 0x73
    /**
     * negate int
     */
    val INEG = 0x74
    /**
     * negate long
     */
    val LNEG = 0x75
    /**
     * negate float
     */
    val FNEG = 0x76
    /**
     * negate double
     */
    val DNEG = 0x77
    /**
     * int shift left
     */
    val ISHL = 0x78
    /**
     * bitwise shift left of a long value1 by int value2 positions
     */
    val LSHL = 0x79
    /**
     * int arithmetic shift right
     */
    val ISHR = 0x7a
    /**
     * bitwise shift right of a long value1 by int value2 positions
     */
    val LSHR = 0x7b
    /**
     * int logical shift right
     */
    val IUSHR = 0x7c
    /**
     * bitwise shift right of a long value1 by int value2 positions, unsigned
     */
    val LUSHR = 0x7d
    /**
     * perform a bitwise AND on two integers
     */
    val IAND = 0x7e
    /**
     * perform a bitwise AND on two longs
     */
    val LAND = 0x7f
    /**
     * bitwise OR of two ints
     */
    val IOR = 0x80
    /**
     * bitwise OR of two longs
     */
    val LOR = 0x81
    /**
     * bitwise OR of two ints
     */
    val IXOR = 0x82
    /**
     * bitwise OR of two longs
     */
    val LXOR = 0x83
    /**
     * increment local variable #index by signed byte const
     */
    val IINC = 0x84
    /**
     * convert an int into a long
     */
    val I2L = 0x85
    /**
     * convert an int into a float
     */
    val I2F = 0x86
    /**
     * convert an int into a double
     */
    val I2D = 0x87
    /**
     * convert a long into an int
     */
    val L2I = 0x88
    /**
     * convert a long into a float
     */
    val L2F = 0x89
    /**
     * convert a long into a double
     */
    val L2D = 0x8a
    /**
     * convert a float into an int
     */
    val F2I = 0x8b
    /**
     * convert a float into a long
     */
    val F2L = 0x8c
    /**
     * convert a float into a double
     */
    val F2D = 0x8d
    /**
     * convert a double into an int
     */
    val D2I = 0x8e
    /**
     * convert a double into a long
     */
    val D2L = 0x8f
    /**
     * convert a double into a float
     */
    val D2F = 0x90
    /**
     * convert an int into a byte
     */
    val I2B = 0x91
    /**
     * convert an int into a character
     */
    val I2C = 0x92
    /**
     * convert an int into a short
     */
    val I2S = 0x93
    /**
     * push 0 if the two longs are the same, 1 if value1 is greater than value2, -1 otherwise
     */
    val LCMP = 0x94
    /**
     * compare two floats, less than??
     */
    val FCMPL = 0x95
    /**
     * compare two floats, greater than??
     */
    val FCMPG = 0x96
    /**
     * compare two doubles, less than??
     */
    val DCMPL = 0x97
    /**
     * compare two doubles, greater than??
     */
    val DCMPG = 0x98
    /**
     * if value is 0, branch to instruction at branchoffset (signed short constructed from
     * unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IFEQ = 0x99
    /**
     * if value is not 0, branch to instruction at branchoffset (signed short constructed from
     * unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IFNE = 0x9a
    /**
     * if value is less than 0, branch to instruction at branchoffset (signed short constructed
     * from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IFLT = 0x9b
    /**
     * if value is greater than or equal to 0, branch to instruction at branchoffset (signed short
     * constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IFGE = 0x9c
    /**
     * if value is greater than 0, branch to instruction at branchoffset (signed short constructed
     * from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IFGT = 0x9d
    /**
     * if value is less than or equal to 0, branch to instruction at branchoffset (signed short
     * constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IFLE = 0x9e
    /**
     * if ints are equal, branch to instruction at branchoffset (signed short constructed from
     * unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IF_ICMPEQ = 0x9f
    /**
     * if ints are not equal, branch to instruction at branchoffset (signed short constructed
     * from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IF_ICMPNE = 0xa0
    /**
     * if value1 is less than value2, branch to instruction at branchoffset (signed short
     * constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IF_ICMPLT = 0xa1
    /**
     * if value1 is greater than or equal to value2, branch to instruction at branchoffset (signed
     * short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IF_ICMPGE = 0xa2
    /**
     * if value1 is greater than value2, branch to instruction at branchoffset (signed short
     * constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IF_ICMPGT = 0xa3
    /**
     * if value1 is less than or equal to value2, branch to instruction at branchoffset (signed
     * short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IF_ICMPLE = 0xa4
    /**
     * if references are equal, branch to instruction at branchoffset (signed short constructed
     * from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IF_ACMPEQ = 0xa5
    /**
     * if references are not equal, branch to instruction at branchoffset (signed short
     * constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IF_ACMPNE = 0xa6
    /**
     * goes to another instruction at branchoffset (signed short constructed from unsigned bytes
     * branchbyte1 << 8 + branchbyte2)
     */
    val GOTO = 0xa7
    /**
     * jump to subroutine at branchoffset (signed short constructed from unsigned bytes
     * branchbyte1 << 8 + branchbyte2) and place the return address on the stack
     */
    val JSR = 0xa8
    /**
     * continue execution from address taken from a local variable #index (the asymmetry with
     * [.JSR] is intentional)
     */
    val RET = 0xa9
    /**
     * continue execution from an address in the table at offset index
     */
    val TABLESWITCH = 0xaa
    /**
     * a target address is looked up from a table using a key and execution continues from the
     * instruction at that address
     */
    val LOOKUPSWITCH = 0xab
    /**
     * return an integer from a method
     */
    val IRETURN = 0xac
    /**
     * return a long from a method
     */
    val LRETURN = 0xad
    /**
     * return a float from a method
     */
    val FRETURN = 0xae
    /**
     * return a double from a method
     */
    val DRETURN = 0xaf
    /**
     * return a reference from a method
     */
    val ARETURN = 0xb0
    /**
     * return void from a method
     */
    val RETURN = 0xb1
    /**
     * get a static field value of a class, where the field is identified by field reference in
     * the constant pool index (indexbyte1 << 8 + indexbyte2)
     */
    val GETSTATIC = 0xb2
    /**
     * set static field to value in a class, where the field is identified by a field reference
     * index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    val PUTSTATIC = 0xb3
    /**
     * get a field value of an object objectref, where the field is identified by field reference
     * in the constant pool index (indexbyte1 << 8 + indexbyte2)
     */
    val GETFIELD = 0xb4
    /**
     * set field to value in an object objectref, where the field is identified by a field
     * reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    val PUTFIELD = 0xb5
    /**
     * invoke virtual method on object objectref and puts the result on the stack (might be void);
     * the method is identified by method reference index in constant pool (indexbyte1 << 8 +
     * indexbyte2)
     */
    val INVOKEVIRTUAL = 0xb6
    /**
     * invoke instance method on object objectref and puts the result on the stack (might be void);
     * the method is identified by method reference index in constant pool (indexbyte1 << 8 +
     * indexbyte2)
     */
    val INVOKESPECIAL = 0xb7
    /**
     * invoke a static method and puts the result on the stack (might be void); the method is
     * identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    val INVOKESTATIC = 0xb8
    /**
     * invokes an interface method on object objectref and puts the result on the stack (might be
     * void); the interface method is identified by method reference index in constant pool
     * (indexbyte1 << 8 + indexbyte2)
     */
    val INVOKEINTERFACE = 0xb9
    /**
     * invokes a dynamic method and puts the result on the stack (might be void); the method is
     * identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    val INVOKEDYNAMIC = 0xba
    /**
     * load new object of type identified by class reference in constant pool index (indexbyte1
     * << 8 + indexbyte2)
     */
    val NEW = 0xbb
    /**
     * load new array with count elements of primitive type identified by atype
     */
    val NEWARRAY = 0xbc
    /**
     * load a new array of references of length count and component type identified by the
     * class reference index (indexbyte1 << 8 + indexbyte2) in the constant pool
     */
    val ANEWARRAY = 0xbd
    /**
     * get the length of an array
     */
    val ARRAYLENGTH = 0xbe
    /**
     * throws an error or exception (notice that the rest of the stack is cleared, leaving only a
     * reference to the Throwable)
     */
    val ATHROW = 0xbf
    /**
     * checks whether an objectref is of a certain type, the class reference of which is in the
     * constant pool at index (indexbyte1 << 8 + indexbyte2)
     */
    val CHECKCAST = 0xc0
    /**
     * determines if an object objectref is of a given type, identified by class reference index
     * in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    val INSTANCEOF = 0xc1
    /**
     * enter monitor for object ("grab the lock" – start of synchronized() section)
     */
    val MONITORENTER = 0xc2
    /**
     * exit monitor for object ("release the lock" – end of synchronized() section)
     */
    val MONITOREXIT = 0xc3
    /**
     * execute opcode, where opcode is either [.ILOAD], [.FLOAD], [.ALOAD],
     * [.LLOAD], [.DLOAD], [.ISTORE], [.FSTORE], [.ASTORE],
     * [.LSTORE], [.DSTORE], or [.RET], but assume the index is 16 bit; or
     * execute [.IINC], where the index is 16 bits and the constant to increment by is a
     * signed 16 bit short
     */
    val WIDE = 0xc4
    /**
     * load a new array of dimensions dimensions with elements of type identified by class
     * reference in constant pool index (indexbyte1 << 8 + indexbyte2); the sizes of each
     * dimension is identified by count1, [count2, etc.]
     */
    val MULTIANEWARRAY = 0xc5
    /**
     * if value is null, branch to instruction at branchoffset (signed short constructed from
     * unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IFNULL = 0xc6
    /**
     * if value is not null, branch to instruction at branchoffset (signed short constructed from
     * unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    val IFNONNULL = 0xc7
    /**
     * goes to another instruction at branchoffset (signed int constructed from unsigned bytes
     * branchbyte1 << 24 + branchbyte2 << 16 + branchbyte3 << 8 + branchbyte4)
     */
    val GOTO_W = 0xc8
    /**
     * jump to subroutine at branchoffset (signed int constructed from unsigned bytes
     * branchbyte1 << 24 + branchbyte2 << 16 + branchbyte3 << 8 + branchbyte4) and place the
     * return address on the stack
     */
    val JSR_W = 0xc9
    /**
     * reserved for breakpoints in Java debuggers; should not appear in any class file
     */
    val BREAKPOINT = 0xca
    /**
     * reserved for implementation-dependent operations within debuggers; should not appear in
     * any class file
     */
    val IMPDEP1 = 0xfe
    /**
     * reserved for implementation-dependent operations within debuggers; should not appear in
     * any class file
     */
    val IMPDEP2 = 0xff

    @Throws(IllegalAccessException::class)
    fun getName(code: Int): String? {
        val declaredFields = Opcodes::class.java.declaredFields
        for (field in declaredFields) {
            if (field.getInt(null) == code) {
                return field.name.toLowerCase()
            }
        }
        return null
    }

    @Throws(IllegalAccessException::class)
    fun visualize(code: ByteArray): String {
        val builder = StringBuilder()
        val reader = ByteCodeReader(code)
        while (reader.canReadByte()) {
            val bytecode = reader.readUnsignedByte()
            val opcodeName = getName(bytecode)
            if (opcodeName != null) {
                builder.append(opcodeName).append(" ")
            } else {
                builder.append(bytecode).append(" (unknown) ")
            }

            when (bytecode) {
                Opcodes.NEW, Opcodes.GETSTATIC, Opcodes.INVOKEVIRTUAL, Opcodes.INVOKESPECIAL -> builder.append(reader.readUnsignedShort()).append("\n")
                Opcodes.LDC, Opcodes.ILOAD, Opcodes.LLOAD, Opcodes.FLOAD, Opcodes.DLOAD, Opcodes.ALOAD -> builder.append(reader.readUnsignedByte()).append("\n")
                else -> builder.append("\n")
            }
        }

        return builder.toString()
    }
}
