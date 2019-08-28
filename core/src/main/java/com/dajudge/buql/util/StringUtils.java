package com.dajudge.buql.util;

public final class StringUtils {
    private StringUtils() {

    }

    public static String repeat(final String string, final String sep, final int times) {
        final StringBuilder ret = new StringBuilder();
        for (int i = 0; i < times; i++) {
            if (i != 0) {
                ret.append(sep);
            }
            ret.append(string);
        }
        return ret.toString();
    }

    public static String quote(final String s) {
        return '"' + s + '"';
    }
}
