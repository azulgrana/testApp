package com.example.myfbpic.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myfbpic.Constants;
import com.example.myfbpic.MyPicApplication;

public class SharedPrefs {

    private SharedPreferences preferences;

    public SharedPrefs() {
        preferences = MyPicApplication.getContext().getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    public String getStringPreference(String name) {
        return preferences.getString(name, "");
    }

    public Boolean getBoolPreference(String name) {
        return preferences.getBoolean(name, true);
    }

    public void setStringPreference(String name, String value) {
        preferences.edit().putString(name, value).apply();
    }

    public void setBoolPreference(String name, boolean value) {
        preferences.edit().putBoolean(name, value).apply();
    }
}
