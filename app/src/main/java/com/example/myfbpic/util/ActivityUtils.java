package com.example.myfbpic.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.myfbpic.MyPicApplication;

public class ActivityUtils {

    public static boolean isInternetConnectionActive() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyPicApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
