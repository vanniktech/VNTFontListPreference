package com.vanniktech.vntfontlistpreference;

final class StringFormatUtils {
    public static String addAtEndIfNotPresent(final String string, final String suffix) {
        return string == null ? suffix : !string.endsWith(suffix) ? string + suffix : string;
    }

    private StringFormatUtils() {
        throw new AssertionError("No instances.");
    }
}
