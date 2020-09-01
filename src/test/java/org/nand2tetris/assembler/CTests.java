package org.nand2tetris.assembler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class CTests {
    @ParameterizedTest
    @MethodSource("testTranslateArguments")
    void testTranslate(Instruction instruction, String expected) {
        Assertions.assertEquals(expected, instruction.toBinaryString());
    }

    static Stream<Arguments> testTranslateArguments() {
        return Stream.of(
                // D=M
                Arguments.arguments(new C(C.Dest.D, C.Comp.M), "1111110000010000"),
                // D=D-M
                Arguments.arguments(new C(C.Dest.D, C.Comp.D_MINUS_M), "1111010011010000"),
                // A=D-1
                Arguments.arguments(new C(C.Dest.A, C.Comp.D_MINUS_ONE), "1110001110100000"),
                // M=D
                Arguments.arguments(new C(C.Dest.M, C.Comp.D), "1110001100001000"),
                // D;JMP
                Arguments.arguments(new C(C.Comp.D, C.Jump.JMP), "1110001100000111"),
                // 0;JMP
                Arguments.arguments(new C(C.Comp.ZERO, C.Jump.JMP), "1110101010000111"),
                // M;JGT
                Arguments.arguments(new C(C.Comp.M, C.Jump.JGT), "1111110000000001")
        );
    }
}
