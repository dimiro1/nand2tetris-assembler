package org.nand2tetris.assembler;

public class A {
    /** Literal A instruction. Examples: `@1` `@2` */
    static class Literal implements Instruction {
        private final int value;

        public Literal(int value) {
            this.value = value;
        }

        @Override
        public String toBinaryString() {
            return String.format("0%s", Util.toBinary(value, 15));
        }
    }

    /** Variable A instruction. Examples: `@LABEL` `@max` `@min` */
    static class Variable implements Instruction {
        private final String symbol;
        private int value;

        public Variable(String symbol, int value) {
            this.symbol = symbol;
            this.value = value;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toBinaryString() {
            return String.format("0%s", Util.toBinary(value, 15));
        }
    }
}
