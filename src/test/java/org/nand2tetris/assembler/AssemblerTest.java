package org.nand2tetris.assembler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AssemblerTest {

    @ParameterizedTest
    @CsvSource({
        "/a.asm, /a_expected.hack",
        "/Rect.asm, /Rect_expected.hack",
        "/Max.asm, /Max_expected.hack",
        "/Pong.asm, /Pong_expected.hack",
    })
    void testAssembly(String source, String expected) throws IOException {
        var asm = this.getClass().getResourceAsStream(source);
        var hack = this.getClass().getResourceAsStream(expected);
        var out = new ByteArrayOutputStream();

        var assembler = new Assembler(asm, out);
        assembler.assembly();

        Assertions.assertEquals(new String(hack.readAllBytes()), new String(out.toByteArray()));
    }
}
