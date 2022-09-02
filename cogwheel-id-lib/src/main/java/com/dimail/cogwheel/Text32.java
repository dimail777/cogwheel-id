package com.dimail.cogwheel;

import java.util.HashMap;

// ordered Base32 according to long ordering
public class Text32 {

    public static String text32(long num) {
        int shift = LONG_BIT_LENGTH - SYMBOL_BIT_LENGTH;
        StringBuilder builder = new StringBuilder();
        while (shift > 0) {
            long digit = (num >>> shift) & SYMBOL_BIT_MASK;
            builder.append(SYMBOL_MAP.get(digit));
            shift -= SYMBOL_BIT_LENGTH;
        }
        int mod = LONG_BIT_LENGTH % SYMBOL_BIT_LENGTH;
        long digit = num & ((1 << mod) - 1);
        builder.append(SYMBOL_MAP.get(digit));

        return builder.toString();
    }

    private static final HashMap<Long, Character> SYMBOL_MAP = new HashMap<>();

    static {
        SYMBOL_MAP.put(0L, '2');
        SYMBOL_MAP.put(1L, '3');
        SYMBOL_MAP.put(2L, '4');
        SYMBOL_MAP.put(3L, '5');
        SYMBOL_MAP.put(4L, '6');
        SYMBOL_MAP.put(5L, '7');
        SYMBOL_MAP.put(6L, 'A');
        SYMBOL_MAP.put(7L, 'B');
        SYMBOL_MAP.put(8L, 'C');
        SYMBOL_MAP.put(9L, 'D');

        SYMBOL_MAP.put(10L, 'E');
        SYMBOL_MAP.put(11L, 'F');
        SYMBOL_MAP.put(12L, 'G');
        SYMBOL_MAP.put(13L, 'H');
        SYMBOL_MAP.put(14L, 'I');
        SYMBOL_MAP.put(15L, 'J');
        SYMBOL_MAP.put(16L, 'K');
        SYMBOL_MAP.put(17L, 'L');
        SYMBOL_MAP.put(18L, 'M');
        SYMBOL_MAP.put(19L, 'N');

        SYMBOL_MAP.put(20L, 'O');
        SYMBOL_MAP.put(21L, 'P');
        SYMBOL_MAP.put(22L, 'Q');
        SYMBOL_MAP.put(23L, 'R');
        SYMBOL_MAP.put(24L, 'S');
        SYMBOL_MAP.put(25L, 'T');
        SYMBOL_MAP.put(26L, 'U');
        SYMBOL_MAP.put(27L, 'V');
        SYMBOL_MAP.put(28L, 'W');
        SYMBOL_MAP.put(29L, 'X');
        SYMBOL_MAP.put(30L, 'Y');
        SYMBOL_MAP.put(31L, 'Z');

    }

    private static final int LONG_BIT_LENGTH = 64;
    private static final int SYMBOL_BIT_LENGTH = 5;
    private static final int SYMBOL_BIT_MASK = 0b011111;


}
