package com.perqin.wechatted;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;


public class WeChattedApp extends Application {
    @SuppressLint("StaticFieldLeak")
    public static Context appContext;

    public WeChattedApp() {
        appContext = this;
    }
}
