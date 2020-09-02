package org.nand2tetris.assembler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ATests {
    @ParameterizedTest
    @CsvSource({
        "    0, 0000000000000000",
        "    1, 0000000000000001",
        "   10, 0000000000001010",
        "   32, 0000000000100000",
        "32767, 0111111111111111",
    })
    void testTranslate(int value, String expected) {
        var cmd = new A.Literal(value);
        Assertions.assertEquals(expected, cmd.toBinaryString());
    }
}
