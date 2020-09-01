package org.nand2tetris.assembler;

/**
 * C instruction;
 * Examples:
 * `D=A`
 * `0;JMP`
 * `ADM=A;JEQ`
 */
class C implements Instruction {
    private final Comp comp;
    private final Dest dest;
    private final Jump jump;

    public C(Comp comp) {
        this.comp = comp;
        this.dest = Dest.NULL;
        this.jump = Jump.NULL;
    }

    public C(Dest dest, Comp comp) {
        this.dest = dest;
        this.comp = comp;
        this.jump = Jump.NULL;
    }

    public C(Comp comp, Jump jump) {
        this.comp = comp;
        this.dest = Dest.NULL;
        this.jump = jump;
    }

    public C(Dest dest, Comp comp, Jump jump) {
        this.comp = comp;
        this.dest = dest;
        this.jump = jump;
    }

    @Override
    public String toBinaryString() {
        return String.format(
                "111%s%s%s", this.comp, this.dest, this.jump);
    }

    @Override
    public Type getType() {
        return Type.C;
    }

    enum Comp {
        // 0
        ZERO("0101010"),
        // 1
        ONE("0111111"),
        // -1
        MINUS_ONE("0111010"),
        // D
        D("0001100"),
        // A
        A("0110000"),
        // M
        M("1110000"),
        // !D
        NOT_D("0001101"),
        // !A
        NOT_A("0110001"),
        // !M
        NOT_M("1110001"),
        // -D
        MINUS_D("0001111"),
        // -A
        MINUS_A("0110011"),
        // -M
        MINUS_M("1110011"),
        // D+1
        D_PLUS_ONE("0011111"),
        // A+1
        A_PLUS_ONE("0110111"),
        // M+1
        M_PLUS_ONE("1110111"),
        // D-1
        D_MINUS_ONE("0001110"),
        // A-1
        A_MINUS_ONE("0110010"),
        // M-1
        M_MINUS_ONE("1110010"),
        // D+A
        D_PLUS_A("0000010"),
        // D+M
        D_PLUS_M("1000010"),
        // D-A
        D_MINUS_A("0010011"),
        // D-M
        D_MINUS_M("1010011"),
        // A-D
        A_MINUS_D("0000111"),
        // M-D
        M_MINUS_D("1000111"),
        // D&A
        D_AND_A("0000000"),
        // D&M
        D_AND_M("1000000"),
        // D|A
        D_OR_A("0010101"),
        // D|M
        D_OR_M("1010101");

        private final String binary;

        Comp(String binary) {
            this.binary = binary;
        }

        @Override
        public String toString() {
            return this.binary;
        }
    }

    enum Dest {
        // 0
        NULL("000"),
        // M=
        M("001"),
        // D=
        D("010"),
        // MD=
        MD("011"),
        // A=
        A("100"),
        // AM=
        AM("101"),
        // AD=
        AD("110"),
        // AMD=
        AMD("111");

        private final String binary;

        Dest(String binary) {
            this.binary = binary;
        }

        @Override
        public String toString() {
            return this.binary;
        }
    }

    enum Jump {
        // 0
        NULL("000"),
        // > 0
        JGT("001"),
        // == 0
        JEQ("010"),
        // >= 0
        JGE("011"),
        // > 0
        JLT("100"),
        // != 0
        JNE("101"),
        // < 0
        JLE("110"),
        // Unconditional
        JMP("111");

        private final String binary;

        Jump(String binary) {
            this.binary = binary;
        }

        @Override
        public String toString() {
            return this.binary;
        }
    }
}
