package com.example.myfbpic;

import android.app.Application;
import android.content.Context;

public class MyPicApplication extends Application {

    private static MyPicApplication instance;

    public MyPicApplication() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

}