VNTFontListPreference
=====================

This is an easy to use custom preference, which opens a dialog with a list of available fonts. The selected font gets automatically saved and you are able to set the font directory as well as the defaultValue.

    <com.vanniktech.vntfontlistpreference.VNTFontListPreference
        xmlns:vntfontlistpreference="http://schemas.android.com/apk/res/com.vanniktech.vntfontlistpreference"
        android:defaultValue="@string/font_face_default_value"
        android:key="preference_font_face"
        android:title="@string/font_face"
        vntfontlistpreference:fontDirectory="@string/font_face_font_directory" />

# Setup

To get this working in your project, make sure to copy the `VNTFontListPreference` class.

Afterwards to go your preference XML file and copy the above mentioned XML tag. You may need to modifiy the package description, depending on where you have copied the file.

The declaration of the custom resource id `vntfontlistpreference` happens with `xmlns:vntfontlistpreference="http://schemas.android.com/apk/res/com.vanniktech.vntfontlistpreference"`. Make sure you replace `com.vanniktech.vntfontlistpreference` with your application package name. You may move the declaration of the custom resource id directly in your `PreferenceScreen` tag, below the android declaration, to make it accessible globally.

Also you need a file under `res/values` called `attrs.xml` with the following content:

    <?xml version="1.0" encoding="utf-8"?>
    <resources>

        <declare-styleable name="VNTFontListPreference">
            <attr name="fontDirectory" format="string" />
        </declare-styleable>

    </resources>

Afterwards you are good to go and can run your project!

# Get default font

    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    final String defaultFont = sharedPreferences.getString("preference_font_face", this.getString(R.string.font_face_default_value));
    final Typeface typeface = Typeface.createFromAsset(this.getAssets(), font);

# Preview

![Image of VNTFontListPreference](res/drawable/preview.png)

# License

Copyright (c) 2014 Niklas Baudy

Licensed under the Apache License, Version 2.0