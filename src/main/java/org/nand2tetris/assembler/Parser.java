package org.nand2tetris.assembler;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Parse Hack assembly files and convert it to a list of instruction which can be later converted to
 * binary.
 */
class Parser {
    private final InputStream input;
    private final Map<String, Integer> symbols;
    private final Map<String, C.Dest> destMap;
    private final Map<String, C.Comp> compMap;
    private final Map<String, C.Jump> jumpMap;
    private int sourceLineNumber = 0;
    private int outputLineNumber = 0;
    private int variableNumber = 16;

    public Parser(final InputStream input) {
        this.input = input;

        this.symbols =
                new HashMap<>(
                        Map.ofEntries(
                                Map.entry("R0", 0),
                                Map.entry("R1", 1),
                                Map.entry("R2", 2),
                                Map.entry("R3", 3),
                                Map.entry("R4", 4),
                                Map.entry("R5", 5),
                                Map.entry("R6", 6),
                                Map.entry("R7", 7),
                                Map.entry("R8", 8),
                                Map.entry("R9", 9),
                                Map.entry("R10", 10),
                                Map.entry("R11", 11),
                                Map.entry("R12", 12),
                                Map.entry("R13", 13),
                                Map.entry("R14", 14),
                                Map.entry("R15", 15),
                                Map.entry("SP", 0),
                                Map.entry("LCL", 1),
                                Map.entry("ARG", 2),
                                Map.entry("THIS", 3),
                                Map.entry("THAT", 4),
                                Map.entry("SCREEN", 16384),
                                Map.entry("KBD", 24576)));

        this.destMap =
                Map.ofEntries(
                        Map.entry("null", C.Dest.NULL),
                        Map.entry("M", C.Dest.M),
                        Map.entry("D", C.Dest.D),
                        Map.entry("MD", C.Dest.MD),
                        Map.entry("A", C.Dest.A),
                        Map.entry("AM", C.Dest.AM),
                        Map.entry("AD", C.Dest.AD),
                        Map.entry("AMD", C.Dest.AMD));

        this.compMap =
                Map.ofEntries(
                        Map.entry("0", C.Comp.ZERO),
                        Map.entry("1", C.Comp.ONE),
                        Map.entry("-1", C.Comp.MINUS_ONE),
                        Map.entry("D", C.Comp.D),
                        Map.entry("A", C.Comp.A),
                        Map.entry("!D", C.Comp.NOT_D),
                        Map.entry("!A", C.Comp.NOT_A),
                        Map.entry("-D", C.Comp.MINUS_D),
                        Map.entry("-A", C.Comp.MINUS_A),
                        Map.entry("D+1", C.Comp.D_PLUS_ONE),
                        Map.entry("A+1", C.Comp.A_PLUS_ONE),
                        Map.entry("D-1", C.Comp.D_MINUS_ONE),
                        Map.entry("A-1", C.Comp.A_MINUS_ONE),
                        Map.entry("D+A", C.Comp.D_PLUS_A),
                        Map.entry("D-A", C.Comp.D_MINUS_A),
                        Map.entry("A-D", C.Comp.A_MINUS_D),
                        Map.entry("D&A", C.Comp.D_AND_A),
                        Map.entry("D|A", C.Comp.D_OR_A),
                        Map.entry("M", C.Comp.M),
                        Map.entry("!M", C.Comp.NOT_M),
                        Map.entry("-M", C.Comp.MINUS_M),
                        Map.entry("M+1", C.Comp.M_PLUS_ONE),
                        Map.entry("M-1", C.Comp.M_MINUS_ONE),
                        Map.entry("D+M", C.Comp.D_PLUS_M),
                        Map.entry("D-M", C.Comp.D_MINUS_M),
                        Map.entry("M-D", C.Comp.M_MINUS_D),
                        Map.entry("D&M", C.Comp.D_AND_M),
                        Map.entry("D|M", C.Comp.D_OR_M));

        this.jumpMap =
                Map.ofEntries(
                        Map.entry("null", C.Jump.NULL),
                        Map.entry("JGT", C.Jump.JGT),
                        Map.entry("JEQ", C.Jump.JEQ),
                        Map.entry("JGE", C.Jump.JGE),
                        Map.entry("JLT", C.Jump.JLT),
                        Map.entry("JNE", C.Jump.JNE),
                        Map.entry("JLE", C.Jump.JLE),
                        Map.entry("JMP", C.Jump.JMP));
    }

    public List<Instruction> parse() {
        var instructions = new ArrayList<Instruction>();
        var scanner = new Scanner(this.input).useDelimiter("\n");

        while (scanner.hasNext()) {
            this.sourceLineNumber++;
            var line = scanner.next();
            line = line.replaceAll("//(.+)", "");
            line = line.strip();

            // Ignore empty lines
            if (line.equals("")) {
                continue;
            } else if (line.startsWith("(")) {
                // label definition
                parseLabelDeclaration(line);
                continue;
            } else if (line.startsWith("@")) {
                // A instruction
                instructions.add(parseAInstruction(line));
            } else {
                // C instruction
                instructions.add(parseCInstruction(line));
            }

            this.outputLineNumber++;
        }

        // Update labels
        for (var instruction : instructions) {
            if (A.Variable.class.equals(instruction.getClass())) {
                var i = (A.Variable) instruction;
                if (this.symbols.containsKey(i.getSymbol())) {
                    i.setValue(this.symbols.get(i.getSymbol()));
                }
            }
        }
        return instructions;
    }

    private void parseLabelDeclaration(final String line) {
        var matcher = Pattern.compile("\\((.+)\\)").matcher(line);
        if (!matcher.matches()) {
            throw new ParserError(
                    String.format("label expected at line %d", this.sourceLineNumber));
        }
        var label = matcher.group(1).strip();
        symbols.put(label, this.outputLineNumber);
    }

    private Instruction parseAInstruction(final String line) {
        var literalMatcher = Pattern.compile("@(\\d+)").matcher(line);
        if (literalMatcher.matches()) {
            // Literal
            var value = Integer.parseInt(literalMatcher.group(1).strip());
            return new A.Literal(value);
        } else {
            // Variable
            var variableMatcher = Pattern.compile("@(.+)").matcher(line);
            if (!variableMatcher.matches()) {
                throw new ParserError(
                        String.format("variable expected at line %d", this.sourceLineNumber));
            }

            var symbol = variableMatcher.group(1).strip();
            var value = 0;

            if (this.symbols.containsKey(symbol)) {
                value = this.symbols.get(symbol);
            } else {
                value = this.variableNumber++;
                this.symbols.put(symbol, value);
            }
            return new A.Variable(symbol, value);
        }
    }

    private Instruction parseCInstruction(final String line) {
        var commandParts = line.split("[=|;]");
        C.Comp decodedComp;
        C.Jump decodedJmp;
        C.Dest decodedDest;

        switch (commandParts.length) {
            case 0:
                throw new ParserError(
                        String.format("malformed command at line %d", this.sourceLineNumber));
            case 1:
                if (!this.compMap.containsKey(commandParts[0])) {
                    throw new ParserError(
                            String.format(
                                    "unknown command '%s' at line %d",
                                    commandParts[0], this.sourceLineNumber));
                }
                decodedComp = this.compMap.get(commandParts[0]);
                return new C(decodedComp);
            case 2:
                if (this.jumpMap.containsKey(commandParts[1])) {
                    // Jump Command
                    decodedComp = this.compMap.get(commandParts[0]);
                    decodedJmp = this.jumpMap.get(commandParts[1]);
                    return new C(decodedComp, decodedJmp);
                } else {
                    // Assign command
                    decodedDest = this.destMap.get(commandParts[0]);
                    decodedComp = this.compMap.get(commandParts[1]);
                    return new C(decodedDest, decodedComp);
                }
            case 3:
                decodedDest = this.destMap.get(commandParts[0]);
                decodedComp = this.compMap.get(commandParts[1]);
                decodedJmp = this.jumpMap.get(commandParts[1]);
                return new C(decodedDest, decodedComp, decodedJmp);
        }

        throw new ParserError(String.format("malformed command at line %d", this.sourceLineNumber));
    }

    static class ParserError extends RuntimeException {
        ParserError(String message) {
            super(message);
        }
    }
}
