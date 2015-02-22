VNTFontListPreference
=====================

[![Build Status](https://travis-ci.org/vanniktech/VNTFontListPreference.svg?branch=master)](https://travis-ci.org/vanniktech/VNTFontListPreference)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-VNTFontListPreference-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/798)

This is an easy to use custom preference, which opens a dialog with a list of available fonts. The selected font gets automatically saved and you are able to set the font directory as well as the defaultValue.

```xml
<com.vanniktech.vntfontlistpreference.VNTFontListPreference
    xmlns:vntfontlistpreference="http://schemas.android.com/apk/res-auto"
    android:defaultValue="@string/font_face_default_value"
    android:key="preference_font_face"
    android:title="@string/font_face"
    vntfontlistpreference:fontDirectory="@string/font_face_font_directory" />
```

`vntfontlistpreference:fontDirectory` can be something like `fonts`. Please make sure that you don't have a slash at the end. You would then to copy the `ttf` and / or `otf` files into the directory `assets/fonts`.

# Setup

**build.gradle**

```groovy
repositories {
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}

dependencies {
    compile 'com.vanniktech:vntfontlistpreference:0.1.0-SNAPSHOT'
}
```

Go to your preference XML file and insert the above mentioned XML tag. Afterwards you are good to go and can run your project!

# Get default font

```java
final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
final String defaultFont = sharedPreferences.getString("preference_font_face", this.getString(R.string.font_face_default_value));
final Typeface typeface = Typeface.createFromAsset(this.getAssets(), font);
```

# Preview

![Image of VNTFontListPreference](app/src/main/res/drawable/preview.png)

# License

Copyright (C) 2014-2015 Vanniktech - Niklas Baudy

Licensed under the Apache License, Version 2.0