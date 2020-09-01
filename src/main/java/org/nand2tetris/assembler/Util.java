package org.nand2tetris.assembler;

class Util {
    public static String toBinary(int n, int length) {
        return String.format("%" + length + "s", Integer.toBinaryString(n)).replaceAll(" ", "0");
    }

    static String getBaseName(String fileName) {
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }
}
