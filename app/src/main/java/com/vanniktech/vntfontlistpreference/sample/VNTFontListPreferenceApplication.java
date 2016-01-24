package com.vanniktech.vntfontlistpreference.sample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class VNTFontListPreferenceApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);
    }
}
