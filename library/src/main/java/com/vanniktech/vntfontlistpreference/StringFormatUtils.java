package com.vanniktech.vntfontlistpreference;

final class StringFormatUtils {
    private StringFormatUtils() {
        throw new AssertionError("No instances.");
    }

    public static String addAtEndIfNotPresent(final String string, final String suffix) {
        return string == null ? suffix : !string.endsWith(suffix) ? string + suffix : string;
    }
}
