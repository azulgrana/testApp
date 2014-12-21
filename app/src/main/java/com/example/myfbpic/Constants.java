package com.example.myfbpic;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String EVENT_USER_DATA = "com.example.myfbpic.USER_DATA";
    public static final String EVENT_NO_USER_DATA = "com.example.myfbpic.NO_USER_DATA";
    public static final String EVENT_PHOTOS_SAVED = "com.example.myfbpic.PHOTOS_SAVED";
    public static final String EVENT_PHOTOS_NOT_SAVED = "com.example.myfbpic.PHOTOS_NOT_SAVED";
    public static final String EVENT_PHOTOS_CHANGE_TYPE = "com.example.myfbpic.PHOTOS_CHANGE_TYPE";

    public static final String DATA_USER_ID = "user-id";
    public static final String DATA_PHOTO_PATH_ORIGINAL = "path-original";
    public static final String DATA_PHOTO_PATH_BLURRED = "path-blurred";

    public static final List<String> FACEBOOK_PERMISSIONS = Arrays.asList("public_profile");

    public static final String APP_DIR_NAME = "/MySocialPic/";

    public static final String PREFERENCES_FILE = "com.example.myfbpic.settings";
    public static final String PREFERENCES_FILED_ORIGINAL_PHOTO_PATH = "original-photo";
    public static final String PREFERENCES_FILED_BLURRED_PHOTO_PATH = "blurred-photo";
    public static final String PREFERENCES_FILED_BLURRED = "current-effect";
}
