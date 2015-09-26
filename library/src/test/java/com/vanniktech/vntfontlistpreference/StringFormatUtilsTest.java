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

    private final String mString;
    private final String mSuffix;
    private final String mExpected;

    public StringFormatUtilsTest(final String string, final String suffix, final String expected) {
        mString = string;
        mSuffix = suffix;
        mExpected = expected;
    }

    @Test
    public void testAddAtEndIfNotPresent() {
        assertEquals(mExpected, StringFormatUtils.addAtEndIfNotPresent(mString, mSuffix));
    }
}
