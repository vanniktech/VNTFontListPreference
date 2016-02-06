package com.vanniktech.vntfontlistpreference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.util.AttributeSet;

@RunWith(RobolectricTestRunner.class)
public class VNTFontListPreferenceTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private Context                      context;
    private AttributeSet                 attributeSet;
    private AssetManager                 assets;

    @Before
    public void setUp() {
        context = spy(RuntimeEnvironment.application.getApplicationContext());
        assets = spy(RuntimeEnvironment.application.getAssets());
        attributeSet = mock(AttributeSet.class);

        doReturn(assets).when(context).getAssets();
    }

    @Test
    public void testVNTFontListPreferenceShouldThrowIllegalArgumentExceptionWhenDirectoryCanNotBeFound() throws IOException {
        final TypedArray typedArray = mock(TypedArray.class);
        doReturn("fonts").when(typedArray).getString(R.styleable.vnt_FontListPreference_vnt_fontDirectory);

        doReturn(typedArray).when(context).obtainStyledAttributes(attributeSet, R.styleable.vnt_FontListPreference);

        doThrow(IOException.class).when(assets).list("fonts");

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("FontListPreference was not able to search for fonts in the assets/fonts folder since the folder is not present. Please create it!");
        new VNTFontListPreference(context, attributeSet);
    }

    @Test
    public void testVNTFontListPreferenceShouldThrowIllegalArgumentExceptionWhenNoFontsWereFound() throws IOException {
        doReturn(new String[] {}).when(assets).list("fonts");

        final TypedArray typedArray = mock(TypedArray.class);
        doReturn("fonts").when(typedArray).getString(R.styleable.vnt_FontListPreference_vnt_fontDirectory);

        doReturn(typedArray).when(context).obtainStyledAttributes(attributeSet, R.styleable.vnt_FontListPreference);

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("FontListPreference could not find any fonts in the assets/fonts folder. Please add some!");
        new VNTFontListPreference(context, attributeSet);
    }

    @Test
    public void testVNTFontListPreferenceShouldFindOTFAndTTFFilesWhenPresentInDirectoryWithoutSlashAtTheEnd() throws IOException {
        final TypedArray typedArray = mock(TypedArray.class);
        doReturn("fonts").when(typedArray).getString(R.styleable.vnt_FontListPreference_vnt_fontDirectory);

        doReturn(typedArray).when(context).obtainStyledAttributes(attributeSet, R.styleable.vnt_FontListPreference);

        this.testFindFontsInDirectory("fonts");
    }

    @Test
    public void testVNTFontListPreferenceShouldFindOTFAndTTFFilesWhenPresentInDirectoryWithSlashAtTheEnd() throws IOException {
        final TypedArray typedArray = mock(TypedArray.class);
        doReturn("fonts/").when(typedArray).getString(R.styleable.vnt_FontListPreference_vnt_fontDirectory);

        doReturn(typedArray).when(context).obtainStyledAttributes(attributeSet, R.styleable.vnt_FontListPreference);

        this.testFindFontsInDirectory("fonts/");
    }

    @Test
    public void testVNTFontListPreferenceShouldSkipNonWrongFiles() throws IOException {
        final String path = "fonts/";
        final TypedArray typedArray = mock(TypedArray.class);
        doReturn(path).when(typedArray).getString(R.styleable.vnt_FontListPreference_vnt_fontDirectory);

        doReturn(typedArray).when(context).obtainStyledAttributes(attributeSet, R.styleable.vnt_FontListPreference);

        doReturn(new String[] { "Test.ttf", "Test.otf", "Test", "", "a", null }).when(assets).list(path);

        final List<VNTFontListPreference.Font> fonts = new VNTFontListPreference(context, attributeSet).fonts;

        assertEquals(2, fonts.size());
        assertEquals("fonts/Test.ttf", fonts.get(0).fontPath);
        assertEquals("fonts/Test.otf", fonts.get(1).fontPath);
    }

    private void testFindFontsInDirectory(final String path) throws IOException {
        doReturn(new String[] { "Sans.ttf", "Arial.otf", "Arial.txt", "Sans-serif.tft", "Arial-bold.oft" }).when(assets).list(path);

        final List<VNTFontListPreference.Font> fonts = new VNTFontListPreference(context, attributeSet).fonts;

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
