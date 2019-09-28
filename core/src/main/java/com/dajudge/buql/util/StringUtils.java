package com.dajudge.buql.util;

import static java.util.Locale.US;

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

    public static String lowercaseFirstLetter(final String fieldName) {
        return fieldName.isEmpty() ? "" : fieldName.substring(0, 1).toLowerCase(US) + fieldName.substring(1);
    }
}
