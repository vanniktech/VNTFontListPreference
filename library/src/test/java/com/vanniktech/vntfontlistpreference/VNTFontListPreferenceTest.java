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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class VNTFontListPreferenceTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private Context      mContext;
    private AttributeSet mAttributeSet;
    private AssetManager mAssets;

    @Before
    public void setUp() {
        mContext = spy(RuntimeEnvironment.application.getApplicationContext());
        mAssets = spy(RuntimeEnvironment.application.getAssets());
        mAttributeSet = mock(AttributeSet.class);

        doReturn(mAssets).when(mContext).getAssets();
    }

    @Test
    public void testVNTFontListPreferenceShouldThrowIllegalArgumentExceptionWhenDirectoryCanNotBeFound() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("FontListPreference was not able to search for fonts in the assets/null folder since the folder is not present. Please create it!");
        new VNTFontListPreference(mContext, mAttributeSet);
    }

    @Test
    public void testVNTFontListPreferenceShouldThrowIllegalArgumentExceptionWhenNoFontsWereFound() throws IOException {
        doReturn(new String[] {}).when(mAssets).list("fonts");
        doReturn(mAssets).when(mContext).getAssets();

        final TypedArray typedArray = mock(TypedArray.class);
        doReturn("fonts").when(typedArray).getString(R.styleable.VNTFontListPreference_vnt_fontDirectory);

        doReturn(typedArray).when(mContext).obtainStyledAttributes(mAttributeSet, R.styleable.VNTFontListPreference);

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("FontListPreference could not find any fonts in the assets/fonts folder. Please add some!");
        new VNTFontListPreference(mContext, mAttributeSet);
    }

    @Test
    public void testVNTFontListPreferenceShouldFindOTFAndTTFFilesWhenPresentInDirectoryWithoutSlashAtTheEnd() throws IOException {
        final TypedArray typedArray = mock(TypedArray.class);
        doReturn("fonts").when(typedArray).getString(R.styleable.VNTFontListPreference_vnt_fontDirectory);

        doReturn(typedArray).when(mContext).obtainStyledAttributes(mAttributeSet, R.styleable.VNTFontListPreference);

        this.testFindFontsInDirectory("fonts");
    }

    @Test
    public void testVNTFontListPreferenceShouldFindOTFAndTTFFilesWhenPresentInDirectoryWithSlashAtTheEnd() throws IOException {
        final TypedArray typedArray = mock(TypedArray.class);
        doReturn("fonts/").when(typedArray).getString(R.styleable.VNTFontListPreference_vnt_fontDirectory);

        doReturn(typedArray).when(mContext).obtainStyledAttributes(mAttributeSet, R.styleable.VNTFontListPreference);

        this.testFindFontsInDirectory("fonts/");
    }

    @Test
    public void testVNTFontListPreferenceShouldSkipNonWrongFiles() throws IOException {
        final String path = "fonts/";
        final TypedArray typedArray = mock(TypedArray.class);
        doReturn(path).when(typedArray).getString(R.styleable.VNTFontListPreference_vnt_fontDirectory);

        doReturn(typedArray).when(mContext).obtainStyledAttributes(mAttributeSet, R.styleable.VNTFontListPreference);

        doReturn(new String[] { "Test.ttf", "Test.otf", "Test", "", "a", null }).when(mAssets).list(path);

        final ArrayList<VNTFontListPreference.Font> fonts = new VNTFontListPreference(mContext, mAttributeSet).mFonts;

        assertEquals(2, fonts.size());
        assertEquals("fonts/Test.ttf", fonts.get(0).fontPath);
        assertEquals("fonts/Test.otf", fonts.get(1).fontPath);
    }

    private void testFindFontsInDirectory(final String path) throws IOException {
        doReturn(new String[] { "Sans.ttf", "Arial.otf", "Arial.txt", "Sans-serif.tft", "Arial-bold.oft" }).when(mAssets).list(path);

        final ArrayList<VNTFontListPreference.Font> fonts = new VNTFontListPreference(mContext, mAttributeSet).mFonts;

        assertEquals(2, fonts.size());
        assertEquals("fonts/Sans.ttf", fonts.get(0).fontPath);
        assertEquals("fonts/Arial.otf", fonts.get(1).fontPath);
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
