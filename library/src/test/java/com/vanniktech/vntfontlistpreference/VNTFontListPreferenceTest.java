/*
 * Copyright (C) 2014-2015 Vanniktech - Niklas Baudy <http://vanniktech.de/Imprint>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vanniktech.vntfontlistpreference;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class VNTFontListPreferenceTest {
    private Context      mContext;
    private AttributeSet mAttributeSet;
    private AssetManager mAssets;

    @Before
    public void setUp() {
        mContext = spy(Robolectric.application.getApplicationContext());
        mAssets = spy(Robolectric.application.getAssets());

        when(mContext.getAssets()).thenReturn(mAssets);

        final TypedArray typedArray = mock(TypedArray.class);
        when(typedArray.getString(R.styleable.VNTFontListPreference_fontDirectory)).thenReturn("fonts");

        mAttributeSet = any(AttributeSet.class);
        when(mContext.obtainStyledAttributes(mAttributeSet, any(int[].class))).thenReturn(typedArray);
    }

    @Test(expected = IllegalStateException.class)
    public void testVNTFontListPreferenceShouldThrowIllegalArgumentExceptionWhenDirectoryCanNotBeFound() {
        new VNTFontListPreference(mContext, mAttributeSet);
    }

    @Test(expected = IllegalStateException.class)
    public void testVNTFontListPreferenceShouldThrowIllegalArgumentExceptionWhenNoFontsWereFound() throws IOException {
        doReturn(new String[] {}).when(mAssets).list("fonts");
        new VNTFontListPreference(mContext, mAttributeSet);
    }

    public void testVNTFontListPreferenceShouldFindOTFAndTTFFilesWhenPresentInDirectory() throws IOException {
        doReturn(new String[] { "Sans.ttf", "Arial.otf", "Arial.txt", "Sans-serif.tft", "Arial-bold.oft" }).when(mAssets).list("fonts");

        final ArrayList<VNTFontListPreference.Font> fonts = new VNTFontListPreference(mContext, mAttributeSet).mFonts;

        assertEquals(2, fonts.size());
        assertEquals("fonts/Sans.ttf", fonts.get(0).fontPath);
        assertEquals("fonts/Arial.ttf", fonts.get(1).fontPath);
    }

    @Test
    public void testFontGetPathShouldReturnPathWhenFontPathIsValid() {
        assertEquals("fonts", new VNTFontListPreference.Font("fonts/DNTitling.ttf").getPath());
    }

    @Test
    public void testFontGetNameShouldReturnNameWhenFontPathIsValid() {
        assertEquals("DNTitling", new VNTFontListPreference.Font("fonts/DNTitling.ttf").getName());
    }

    @Test
    public void testFontEqualsShouldReturnTrueWhenFontPathsAreEqual() {
        assertTrue(new VNTFontListPreference.Font("fonts/MyFont.otf").equals(new VNTFontListPreference.Font("fonts/MyFont.otf")));
    }

    @Test
    public void testFontEqualsShouldReturnFalseWhenFontPathsAreNotEqual() {
        assertFalse(new VNTFontListPreference.Font("fonts/MyFont_2.otf").equals(new VNTFontListPreference.Font("fonts/MyFont.otf")));
    }

    @Test
    public void testFontHashCodeShouldReturnTrueWhenFontPathsAreEqual() {
        assertEquals(new VNTFontListPreference.Font("fonts/MyFont.otf").hashCode(), new VNTFontListPreference.Font("fonts/MyFont.otf").hashCode());
    }

    @Test
    public void testFontHashCodeShouldReturnFalseWhenFontPathsAreNotEqual() {
        assertNotEquals(new VNTFontListPreference.Font("fonts/MyFont_2.otf").hashCode(), new VNTFontListPreference.Font("fonts/MyFont.otf").hashCode());
    }
}
