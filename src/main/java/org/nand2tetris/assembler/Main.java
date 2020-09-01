package org.nand2tetris.assembler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: Assembler Program.asm");
            return;
        }

        var compiledFilename = String.format("%s.hack", Util.getBaseName(args[0]));
        var inputFile = Files.newInputStream(Paths.get(args[0]));
        var outputFile = Files.newOutputStream(Paths.get(compiledFilename));

        System.out.println("\uD83C\uDFC1 Assembling \uD83C\uDFC1");
        Assembler assembler = new Assembler(inputFile, outputFile);
        assembler.assembly();
        System.out.println("\uD83C\uDFC6 Done \uD83C\uDFC6");
    }
}
