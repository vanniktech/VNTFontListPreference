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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class VNTFontListPreference extends ListPreference {
    private static final int MIN_FONT_FILE_LENGTH = 5;

    private Font               mSelectedFontFace;
    private final String       mFontPreviewString;
    protected final List<Font> mFonts = new ArrayList<>();

    public VNTFontListPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.vnt_FontListPreference);
        final String fontDirectory = a.getString(R.styleable.vnt_FontListPreference_vnt_fontDirectory);
        mFontPreviewString = a.getString(R.styleable.vnt_FontListPreference_vnt_fontPreviewString);
        a.recycle();

        final String[] fonts;

        try {
            fonts = context.getAssets().list(fontDirectory);
        } catch (final IOException e) {
            throw new IllegalStateException("FontListPreference was not able to search for fonts in the assets/" + fontDirectory + " folder since the folder is not present. Please create it!", e);
        }

        for (final String font : fonts) {
            if (font != null && font.length() > MIN_FONT_FILE_LENGTH) {
                final String fontType = font.substring(font.length() - 3);

                if ("ttf".equals(fontType) || "otf".equals(fontType)) {
                    mFonts.add(new Font(StringFormatUtils.addAtEndIfNotPresent(fontDirectory, "/") + font));
                }
            }
        }

        if (mFonts.isEmpty()) {
            throw new IllegalStateException("FontListPreference could not find any fonts in the assets/" + fontDirectory + " folder. Please add some!");
        }
    }

    @Override
    protected void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
        mSelectedFontFace = new Font(restoreValue ? this.getPersistedString(null) : (String) defaultValue);
        this.updateSummary();
    }

    @Override
    protected Object onGetDefaultValue(final TypedArray a, final int index) {
        return a.getString(index);
    }

    @Override
    protected void onPrepareDialogBuilder(final Builder builder) {
        final CustomListPreferenceAdapter customListPreferenceAdapter = new CustomListPreferenceAdapter(mFonts, mFontPreviewString, mSelectedFontFace);

        builder.setAdapter(customListPreferenceAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                if (shouldPersist()) {
                    final Font selectedFont = mFonts.get(which);

                    if (callChangeListener(selectedFont.fontPath)) {
                        mSelectedFontFace = selectedFont;
                        updateSummary();
                        persistString(mSelectedFontFace.fontPath);
                    }
                }
                dialog.cancel();
            }
        });

        builder.setPositiveButton(null, null);
    }

    private void updateSummary() {
        if (mSelectedFontFace != null) {
            this.setSummary(mSelectedFontFace.getName());
        }
    }

    private static class CustomListPreferenceAdapter extends BaseAdapter {
        private final List<Font> mFonts;
        private final String     mFontPreviewString;
        private final Font       mSelectedFontFace;

        CustomListPreferenceAdapter(final List<Font> fonts, final String fontPreviewString, final Font selectedFontFace) {
            mFonts = fonts;
            mFontPreviewString = fontPreviewString;
            mSelectedFontFace = selectedFontFace;
        }

        @Override
        public int getCount() {
            return mFonts.size();
        }

        @Override
        public Object getItem(final int position) {
            return position;
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            final CustomHolder holder;
            final Context context = parent.getContext();

            final View view;

            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(android.R.layout.select_dialog_singlechoice, parent, false);

                holder = new CustomHolder();
                holder.checkedTextView = (CheckedTextView) view;

                view.setTag(holder);
            } else {
                view = convertView;
                holder = (CustomHolder) view.getTag();
            }

            final Font font = mFonts.get(position);

            final Typeface type = Typeface.createFromAsset(context.getAssets(), font.fontPath);
            holder.checkedTextView.setTypeface(type);
            holder.checkedTextView.setText(mFontPreviewString != null ? mFontPreviewString : font.getName());
            holder.checkedTextView.setChecked(font.equals(mSelectedFontFace));

            return view;
        }
    }

    private static class CustomHolder {
        private CheckedTextView checkedTextView;
    }

    protected static class Font {
        private static final int FILE_ENDING_LENGTH = 4;

        protected final String fontPath;

        public Font(final String fontPath) {
            this.fontPath = fontPath;
        }

        public String getName() {
            return fontPath.substring(fontPath.lastIndexOf('/') + 1, fontPath.length() - FILE_ENDING_LENGTH);
        }

        public String getPath() {
            return fontPath.substring(0, fontPath.lastIndexOf('/'));
        }

        @Override
        public boolean equals(final Object o) {
            return o instanceof Font && this.fontPath.equals(((Font) o).fontPath);
        }

        @Override
        public int hashCode() {
            return this.fontPath.hashCode();
        }
    }
}
