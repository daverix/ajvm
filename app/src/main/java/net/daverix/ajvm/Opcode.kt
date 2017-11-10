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
enum class Opcode(val byteCode: Int) {
    /**
     * perform no operation
     */
    NOP(0x00),

    /**
     * push a null reference onto the stack
     */
    ACONST_NULL(0x01),

    /**
     * load the int value −1 onto the stack
     */
    ICONST_M1(0x02),

    /**
     * load the int value 0 onto the stack
     */
    ICONST_0(0x03),

    /**
     * load the int value 1 onto the stack
     */
    ICONST_1(0x04),

    /**
     * load the int value 2 onto the stack
     */
    ICONST_2(0x05),

    /**
     * load the int value 3 onto the stack
     */
    ICONST_3(0x06),

    /**
     * load the int value 4 onto the stack
     */
    ICONST_4(0x07),

    /**
     * load the int value 5 onto the stack
     */
    ICONST_5(0x08),

    /**
     * push 0L (the number zero with type long) onto the stack
     */
    LCONST_0(0x09),

    /**
     * push 1L (the number one with type long) onto the stack
     */
    LCONST_1(0x0a),

    /**
     * push 0.0f on the stack
     */
    FCONST_0(0x0b),

    /**
     * push 1.0f on the stack
     */
    FCONST_1(0x0c),

    /**
     * push 2.0f on the stack
     */
    FCONST_2(0x0d),

    /**
     * push the constant 0.0 (a double) onto the stack
     */
    DCONST_0(0x0e),

    /**
     * push the constant 0.0 (a double) onto the stack
     */
    DCONST_1(0x0f),

    /**
     * push a byte onto the stack as an integer value
     */
    BI_PUSH(0x10),

    /**
     * push a short onto the stack as an integer value
     */
    SI_PUSH(0x11),

    /**
     * push a constant #index from a constant pool (String, int, float, Class,
     * java.lang.invoke.MethodType, or java.lang.invoke.MethodHandle) onto the stack
     */
    LDC(0x12),

    /**
     * push a constant #index from a constant pool (String, int, float, Class,
     * java.lang.invoke.MethodType, or java.lang.invoke.MethodHandle) onto the stack (wide index is
     * constructed as indexbyte1 << 8 + indexbyte2)
     */
    LDC_W(0x13),

    /**
     * push a constant #index from a constant pool (double or long) onto the stack (wide index is
     * constructed as indexbyte1 << 8 + indexbyte2)
     */
    LDC2_W(0x14),

    /**
     * load an int value from a local variable #index
     */
    ILOAD(0x15),

    /**
     * load a long value from a local variable #index
     */
    LLOAD(0x16),

    /**
     * load a float value from a local variable #index
     */
    FLOAD(0x17),

    /**
     * load a double value from a local variable #index
     */
    DLOAD(0x18),

    /**
     * load a reference onto the stack from a local variable #index
     */
    ALOAD(0x19),

    /**
     * load an int value from local variable 0
     */
    ILOAD_0(0x1a),

    /**
     * load an int value from local variable 1
     */
    ILOAD_1(0x1b),

    /**
     * load an int value from local variable 2
     */
    ILOAD_2(0x1c),

    /**
     * load an int value from local variable 3
     */
    ILOAD_3(0x1d),

    /**
     * load a long value from a local variable 0
     */
    LLOAD_0(0x1e),

    /**
     * load a long value from a local variable 1
     */
    LLOAD_1(0x1f),

    /**
     * load a long value from a local variable 2
     */
    LLOAD_2(0x20),

    /**
     * load a long value from a local variable 3
     */
    LLOAD_3(0x21),

    /**
     * load a float value from local variable 0
     */
    FLOAD_0(0x22),

    /**
     * load a float value from local variable 1
     */
    FLOAD_1(0x23),

    /**
     * load a float value from local variable 2
     */
    FLOAD_2(0x24),

    /**
     * load a float value from local variable 3
     */
    FLOAD_3(0x25),

    /**
     * load a double from local variable 0
     */
    DLOAD_0(0x26),

    /**
     * load a double from local variable 1
     */
    DLOAD_1(0x27),

    /**
     * load a double from local variable 2
     */
    DLOAD_2(0x28),

    /**
     * load a double from local variable 3
     */
    DLOAD_3(0x29),

    /**
     * load a reference onto the stack from local variable 0
     */
    ALOAD_0(0x2a),

    /**
     * load a reference onto the stack from local variable 1
     */
    ALOAD_1(0x2b),

    /**
     * load a reference onto the stack from local variable 2
     */
    ALOAD_2(0x2c),

    /**
     * load a reference onto the stack from local variable 3
     */
    ALOAD_3(0x2d),

    /**
     * load an int from an array
     */
    IALOAD(0x2e),

    /**
     * load a long from an array
     */
    LALOAD(0x2f),

    /**
     * load a float from an array
     */
    FALOAD(0x30),

    /**
     * load a double from an array
     */
    DALOAD(0x31),

    /**
     * load onto the stack a reference from an array
     */
    AALOAD(0x32),

    /**
     * load a byte or Boolean value from an array
     */
    BALOAD(0x33),

    /**
     * load a char from an array
     */
    CALOAD(0x34),

    /**
     * load short from array
     */
    SALOAD(0x35),

    /**
     * store int value into variable #index
     */
    ISTORE(0x36),

    /**
     * store a long value in a local variable #index
     */
    LSTORE(0x37),

    /**
     * store a float value into a local variable #index
     */
    FSTORE(0x38),

    /**
     * store a double value into a local variable #index
     */
    DSTORE(0x39),

    /**
     * store a reference into a local variable #index
     */
    ASTORE(0x3a),

    /**
     * store int value into variable 0
     */
    ISTORE_0(0x3b),

    /**
     * store int value into variable 1
     */
    ISTORE_1(0x3c),

    /**
     * store int value into variable 2
     */
    ISTORE_2(0x3d),

    /**
     * store int value into variable 3
     */
    ISTORE_3(0x3e),

    /**
     * store a long value in a local variable 0
     */
    LSTORE_0(0x3f),

    /**
     * store a long value in a local variable 1
     */
    LSTORE_1(0x40),

    /**
     * store a long value in a local variable 2
     */
    LSTORE_2(0x41),

    /**
     * store a long value in a local variable 3
     */
    LSTORE_3(0x42),

    /**
     * store a float value in a local variable 0
     */
    FSTORE_0(0x43),

    /**
     * store a float value in a local variable 1
     */
    FSTORE_1(0x44),

    /**
     * store a float value in a local variable 2
     */
    FSTORE_2(0x45),

    /**
     * store a float value in a local variable 3
     */
    FSTORE_3(0x46),

    /**
     * store a double into local variable 0
     */
    DSTORE_0(0x47),

    /**
     * store a double into local variable 1
     */
    DSTORE_1(0x48),

    /**
     * store a double into local variable 2
     */
    DSTORE_2(0x49),

    /**
     * store a double into local variable 3
     */
    DSTORE_3(0x4a),

    /**
     * store a reference into local variable 0
     */
    ASTORE_0(0x4b),

    /**
     * store a reference into local variable 1
     */
    ASTORE_1(0x4c),

    /**
     * store a reference into local variable 2
     */
    ASTORE_2(0x4d),

    /**
     * store a reference into local variable 3
     */
    ASTORE_3(0x4e),

    /**
     * store an int into an array
     */
    IASTORE(0x4f),

    /**
     * store a long into an array
     */
    LASTORE(0x50),

    /**
     * store a float into an array
     */
    FASTORE(0x51),

    /**
     * store a double into an array
     */
    DASTORE(0x52),

    /**
     * store into a reference in an array
     */
    AASTORE(0x53),

    /**
     * store a byte or Boolean value into an array
     */
    BASTORE(0x54),

    /**
     * store a char into an array
     */
    CASTORE(0x55),

    /**
     * store short to array
     */
    SASTORE(0x56),

    /**
     * discard the top value on the stack
     */
    POP(0x57),

    /**
     * discard the top two values on the stack (or one value, if it is a double or long)
     */
    POP2(0x58),

    /**
     * duplicate the value on top of the stack
     */
    DUP(0x59),

    /**
     * insert a copy of the top value into the stack two values from the top. value1 and value2
     * must not be of the type double or long.
     */
    DUP_X1(0x5a),

    /**
     * insert a copy of the top value into the stack two (if value2 is double or long it takes up
     * the entry of value3, too) or three values (if value2 is neither double nor long) from the
     * top
     */
    DUP_X2(0x5b),

    /**
     * duplicate top two stack words (two values, if value1 is not double nor long; a single value,
     * if value1 is double or long)
     */
    DUP2(0x5c),

    /**
     * duplicate two words and insert beneath third word
     *
     * @see .DUP2
     */
    DUP2_X1(0x5d),

    /**
     * duplicate two words and insert beneath fourth word
     */
    DUP2_X2(0x5e),

    /**
     * swaps two top words on the stack (note that value1 and value2 must not be double or long)
     */
    SWAP(0x5f),

    /**
     * add two ints
     */
    IADD(0x60),

    /**
     * add two longs
     */
    LADD(0x61),

    /**
     * add two floats
     */
    FADD(0x62),

    /**
     * add two doubles
     */
    DADD(0x63),

    /**
     * subtract two ints
     */
    ISUB(0x64),

    /**
     * subtract two longs
     */
    LSUB(0x65),

    /**
     * subtract two floats
     */
    FSUB(0x66),

    /**
     * subtract a double from another
     */
    DSUB(0x67),

    /**
     * multiply two integers
     */
    IMUL(0x68),

    /**
     * multiply two longs
     */
    LMUL(0x69),

    /**
     * multiply two floats
     */
    FMUL(0x6a),

    /**
     * multiply two doubles
     */
    DMUL(0x6b),

    /**
     * divide two integers
     */
    IDIV(0x6c),

    /**
     * divide two longs
     */
    LDIV(0x6d),

    /**
     * divide two floats
     */
    FDIV(0x6e),

    /**
     * divide two doubles
     */
    DDIV(0x6f),

    /**
     * logical int remainder
     */
    IREM(0x70),

    /**
     * logical long remainder
     */
    LREM(0x71),

    /**
     * get the remainder from a division between two floats
     */
    FREM(0x72),

    /**
     * get the remainder from a division between two doubles
     */
    DREM(0x73),

    /**
     * negate int
     */
    INEG(0x74),

    /**
     * negate long
     */
    LNEG(0x75),

    /**
     * negate float
     */
    FNEG(0x76),

    /**
     * negate double
     */
    DNEG(0x77),

    /**
     * int shift left
     */
    ISHL(0x78),

    /**
     * bitwise shift left of a long value1 by int value2 positions
     */
    LSHL(0x79),

    /**
     * int arithmetic shift right
     */
    ISHR(0x7a),

    /**
     * bitwise shift right of a long value1 by int value2 positions
     */
    LSHR(0x7b),

    /**
     * int logical shift right
     */
    IUSHR(0x7c),

    /**
     * bitwise shift right of a long value1 by int value2 positions, unsigned
     */
    LUSHR(0x7d),

    /**
     * perform a bitwise AND on two integers
     */
    IAND(0x7e),

    /**
     * perform a bitwise AND on two longs
     */
    LAND(0x7f),

    /**
     * bitwise OR of two ints
     */
    IOR(0x80),

    /**
     * bitwise OR of two longs
     */
    LOR(0x81),

    /**
     * bitwise OR of two ints
     */
    IXOR(0x82),

    /**
     * bitwise OR of two longs
     */
    LXOR(0x83),

    /**
     * increment local variable #index by signed byte const
     */
    IINC(0x84),

    /**
     * convert an int into a long
     */
    I2L(0x85),

    /**
     * convert an int into a float
     */
    I2F(0x86),

    /**
     * convert an int into a double
     */
    I2D(0x87),

    /**
     * convert a long into an int
     */
    L2I(0x88),

    /**
     * convert a long into a float
     */
    L2F(0x89),

    /**
     * convert a long into a double
     */
    L2D(0x8a),

    /**
     * convert a float into an int
     */
    F2I(0x8b),

    /**
     * convert a float into a long
     */
    F2L(0x8c),

    /**
     * convert a float into a double
     */
    F2D(0x8d),

    /**
     * convert a double into an int
     */
    D2I(0x8e),

    /**
     * convert a double into a long
     */
    D2L(0x8f),

    /**
     * convert a double into a float
     */
    D2F(0x90),

    /**
     * convert an int into a byte
     */
    I2B(0x91),

    /**
     * convert an int into a character
     */
    I2C(0x92),

    /**
     * convert an int into a short
     */
    I2S(0x93),

    /**
     * push 0 if the two longs are the same, 1 if value1 is greater than value2, -1 otherwise
     */
    LCMP(0x94),

    /**
     * compare two floats, less than??
     */
    FCMPL(0x95),

    /**
     * compare two floats, greater than??
     */
    FCMPG(0x96),

    /**
     * compare two doubles, less than??
     */
    DCMPL(0x97),

    /**
     * compare two doubles, greater than??
     */
    DCMPG(0x98),

    /**
     * if value is 0, branch to instruction at branchoffset (signed short constructed from
     * unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFEQ(0x99),

    /**
     * if value is not 0, branch to instruction at branchoffset (signed short constructed from
     * unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFNE(0x9a),

    /**
     * if value is less than 0, branch to instruction at branchoffset (signed short constructed
     * from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFLT(0x9b),

    /**
     * if value is greater than or equal to 0, branch to instruction at branchoffset (signed short
     * constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFGE(0x9c),

    /**
     * if value is greater than 0, branch to instruction at branchoffset (signed short constructed
     * from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFGT(0x9d),

    /**
     * if value is less than or equal to 0, branch to instruction at branchoffset (signed short
     * constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFLE(0x9e),

    /**
     * if ints are equal, branch to instruction at branchoffset (signed short constructed from
     * unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ICMPEQ(0x9f),

    /**
     * if ints are not equal, branch to instruction at branchoffset (signed short constructed
     * from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ICMPNE(0xa0),

    /**
     * if value1 is less than value2, branch to instruction at branchoffset (signed short
     * constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ICMPLT(0xa1),

    /**
     * if value1 is greater than or equal to value2, branch to instruction at branchoffset (signed
     * short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ICMPGE(0xa2),

    /**
     * if value1 is greater than value2, branch to instruction at branchoffset (signed short
     * constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ICMPGT(0xa3),

    /**
     * if value1 is less than or equal to value2, branch to instruction at branchoffset (signed
     * short constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ICMPLE(0xa4),

    /**
     * if references are equal, branch to instruction at branchoffset (signed short constructed
     * from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ACMPEQ(0xa5),

    /**
     * if references are not equal, branch to instruction at branchoffset (signed short
     * constructed from unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IF_ACMPNE(0xa6),

    /**
     * goes to another instruction at branchoffset (signed short constructed from unsigned bytes
     * branchbyte1 << 8 + branchbyte2)
     */
    GOTO(0xa7),

    /**
     * jump to subroutine at branchoffset (signed short constructed from unsigned bytes
     * branchbyte1 << 8 + branchbyte2) and place the return address on the stack
     */
    JSR(0xa8),

    /**
     * continue execution from address taken from a local variable #index (the asymmetry with
     * [.JSR] is intentional)
     */
    RET(0xa9),

    /**
     * continue execution from an address in the table at offset index
     */
    TABLESWITCH(0xaa),

    /**
     * a target address is looked up from a table using a key and execution continues from the
     * instruction at that address
     */
    LOOKUPSWITCH(0xab),

    /**
     * return an integer from a method
     */
    IRETURN(0xac),

    /**
     * return a long from a method
     */
    LRETURN(0xad),

    /**
     * return a float from a method
     */
    FRETURN(0xae),

    /**
     * return a double from a method
     */
    DRETURN(0xaf),

    /**
     * return a reference from a method
     */
    ARETURN(0xb0),

    /**
     * return void from a method
     */
    RETURN(0xb1),

    /**
     * get a static field value of a class, where the field is identified by field reference in
     * the constant pool index (indexbyte1 << 8 + indexbyte2)
     */
    GETSTATIC(0xb2),

    /**
     * set static field to value in a class, where the field is identified by a field reference
     * index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    PUTSTATIC(0xb3),

    /**
     * get a field value of an object objectref, where the field is identified by field reference
     * in the constant pool index (indexbyte1 << 8 + indexbyte2)
     */
    GETFIELD(0xb4),

    /**
     * set field to value in an object objectref, where the field is identified by a field
     * reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    PUTFIELD(0xb5),

    /**
     * invoke virtual method on object objectref and puts the result on the stack (might be void);
     * the method is identified by method reference index in constant pool (indexbyte1 << 8 +
     * indexbyte2)
     */
    INVOKEVIRTUAL(0xb6),

    /**
     * invoke instance method on object objectref and puts the result on the stack (might be void);
     * the method is identified by method reference index in constant pool (indexbyte1 << 8 +
     * indexbyte2)
     */
    INVOKESPECIAL(0xb7),

    /**
     * invoke a static method and puts the result on the stack (might be void); the method is
     * identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    INVOKESTATIC(0xb8),

    /**
     * invokes an interface method on object objectref and puts the result on the stack (might be
     * void); the interface method is identified by method reference index in constant pool
     * (indexbyte1 << 8 + indexbyte2)
     */
    INVOKEINTERFACE(0xb9),

    /**
     * invokes a dynamic method and puts the result on the stack (might be void); the method is
     * identified by method reference index in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    INVOKEDYNAMIC(0xba),

    /**
     * load new object of type identified by class reference in constant pool index (indexbyte1
     * << 8 + indexbyte2)
     */
    NEW(0xbb),

    /**
     * load new array with count elements of primitive type identified by atype
     */
    NEWARRAY(0xbc),

    /**
     * load a new array of references of length count and component type identified by the
     * class reference index (indexbyte1 << 8 + indexbyte2) in the constant pool
     */
    ANEWARRAY(0xbd),

    /**
     * get the length of an array
     */
    ARRAYLENGTH(0xbe),

    /**
     * throws an error or exception (notice that the rest of the stack is cleared, leaving only a
     * reference to the Throwable)
     */
    ATHROW(0xbf),

    /**
     * checks whether an objectref is of a certain type, the class reference of which is in the
     * constant pool at index (indexbyte1 << 8 + indexbyte2)
     */
    CHECKCAST(0xc0),

    /**
     * determines if an object objectref is of a given type, identified by class reference index
     * in constant pool (indexbyte1 << 8 + indexbyte2)
     */
    INSTANCEOF(0xc1),

    /**
     * enter monitor for object ("grab the lock" – start of synchronized() section)
     */
    MONITORENTER(0xc2),

    /**
     * exit monitor for object ("release the lock" – end of synchronized() section)
     */
    MONITOREXIT(0xc3),

    /**
     * execute opcode, where opcode is either [.ILOAD], [.FLOAD], [.ALOAD],
     * [.LLOAD], [.DLOAD], [.ISTORE], [.FSTORE], [.ASTORE],
     * [.LSTORE], [.DSTORE], or [.RET], but assume the index is 16 bit; or
     * execute [.IINC], where the index is 16 bits and the constant to increment by is a
     * signed 16 bit short
     */
    WIDE(0xc4),

    /**
     * load a new array of dimensions dimensions with elements of type identified by class
     * reference in constant pool index (indexbyte1 << 8 + indexbyte2); the sizes of each
     * dimension is identified by count1, [count2, etc.]
     */
    MULTIANEWARRAY(0xc5),

    /**
     * if value is null, branch to instruction at branchoffset (signed short constructed from
     * unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFNULL(0xc6),

    /**
     * if value is not null, branch to instruction at branchoffset (signed short constructed from
     * unsigned bytes branchbyte1 << 8 + branchbyte2)
     */
    IFNONNULL(0xc7),

    /**
     * goes to another instruction at branchoffset (signed int constructed from unsigned bytes
     * branchbyte1 << 24 + branchbyte2 << 16 + branchbyte3 << 8 + branchbyte4)
     */
    GOTO_W(0xc8),

    /**
     * jump to subroutine at branchoffset (signed int constructed from unsigned bytes
     * branchbyte1 << 24 + branchbyte2 << 16 + branchbyte3 << 8 + branchbyte4) and place the
     * return address on the stack
     */
    JSR_W(0xc9),

    /**
     * reserved for breakpoints in Java debuggers; should not appear in any class file
     */
    BREAKPOINT(0xca),

    /**
     * reserved for implementation-dependent operations within debuggers; should not appear in
     * any class file
     */
    IMPDEP1(0xfe),

    /**
     * reserved for implementation-dependent operations within debuggers; should not appear in
     * any class file
     */
    IMPDEP2(0xff);
}

fun fromByteCode(code: Int) = Opcode.values().first { it.byteCode == code }

@Throws(IllegalAccessException::class)
fun visualize(code: ByteArray): String {
    val builder = StringBuilder()
    val reader = ByteCodeReader(code)
    while (reader.canReadByte()) {
        val bytecode = Opcode.values().firstOrNull { it.byteCode == reader.readUnsignedByte() }
        builder.append(bytecode?.name).append(" ")

        when (bytecode) {
            Opcode.NEW, Opcode.GETSTATIC, Opcode.INVOKEVIRTUAL, Opcode.INVOKESPECIAL -> builder.append(reader.readUnsignedShort()).append("\n")
            Opcode.LDC, Opcode.ILOAD, Opcode.LLOAD, Opcode.FLOAD, Opcode.DLOAD, Opcode.ALOAD -> builder.append(reader.readUnsignedByte()).append("\n")
            else -> builder.append("\n")
        }
    }

    return builder.toString()
}