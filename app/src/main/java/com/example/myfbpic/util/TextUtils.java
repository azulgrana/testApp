package com.example.myfbpic.util;

public class TextUtils {

    private static final String URL_PREFIX = "https://graph.facebook.com/";
    private static final String URL_POSTFIX = "/picture?type=large&width=1080";

    public static String getUserPhotoUrl(final String userId) {
        return URL_PREFIX + userId + URL_POSTFIX;
    }
}
