package org.nand2tetris.assembler;

import java.io.*;

public class Assembler {
    private final Parser parser;
    private final OutputStream outputStream;

    public Assembler(InputStream inputStream, OutputStream outputStream) {
        this.parser = new Parser(inputStream);
        this.outputStream = outputStream;
    }

    public void assembly() throws IOException {
        var instructions = this.parser.parse();

        for (var instruction : instructions) {
            outputStream.write(instruction.toBinaryString().getBytes());
            outputStream.write("\n".getBytes());
        }
    }
}
