package org.nand2tetris.assembler;

public class L implements Instruction {
    private final String symbol;

    public L(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toBinaryString() {
        return null;
    }
}
