package org.nand2tetris.assembler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AssemblerTest {

    @Test
    void testAssembly() throws IOException {
        var asm = this.getClass().getResourceAsStream("/a.asm");
        var hack = this.getClass().getResourceAsStream("/a_expected.hack");
        var out = new ByteArrayOutputStream();

        var assembler = new Assembler(asm, out);
        assembler.assembly();

        Assertions.assertEquals(new String(out.toByteArray()), new String(hack.readAllBytes()));
    }
}
