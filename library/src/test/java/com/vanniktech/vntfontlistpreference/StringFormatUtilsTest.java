package com.vanniktech.vntfontlistpreference;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class StringFormatUtilsTest {
    @Parameterized.Parameters(name = "String = {0} Suffix = {1} Expected = {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { null, "/", "/" }, { "fonts", "/", "fonts/" }, { "fonts/", "/", "fonts/" }, });
    }

    private final String string;
    private final String suffix;
    private final String expected;

    public StringFormatUtilsTest(final String string, final String suffix, final String expected) {
        this.string = string;
        this.suffix = suffix;
        this.expected = expected;
    }

    @Test
    public void testAddAtEndIfNotPresent() {
        assertEquals(expected, StringFormatUtils.addAtEndIfNotPresent(string, suffix));
    }
}
