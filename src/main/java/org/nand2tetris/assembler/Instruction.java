package org.nand2tetris.assembler;

interface Instruction {
    String toBinaryString();
    Type getType();

    enum Type {
        A, C
    }
}
