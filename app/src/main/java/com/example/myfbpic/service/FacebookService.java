package com.example.myfbpic.service;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.myfbpic.Constants;
import com.example.myfbpic.MyPicApplication;
import com.example.myfbpic.util.EventBus;
import com.example.myfbpic.util.FileUtils;
import com.example.myfbpic.util.LogUtils;
import com.example.myfbpic.util.PictureTypeEnum;
import com.example.myfbpic.util.TextUtils;
import com.example.myfbpic.view.BlurTransform;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

public class FacebookService {

    private static final String TAG = LogUtils.buildLogTag(FacebookService.class);

    public static void requestUserInformation() {
        Session session = Session.getActiveSession();

        if (session != null && session.isOpened()) {
            Request.newMeRequest(session, new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser graphUser, Response response) {
                    Bundle data = null;
                    String event;

                    if (graphUser != null) {
                        data = prepareUserIdData(graphUser.getId());
                        event = Constants.EVENT_USER_DATA;
                    } else {
                        event = Constants.EVENT_NO_USER_DATA;
                    }
                    sendMessage(event, data);
                }
            }).executeAsync();
        }
    }

    public static void requestUserPhotoRetrieval(final String userId) {
        Picasso.with(MyPicApplication.getContext()).load(TextUtils.getUserPhotoUrl(userId)).skipMemoryCache().into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    String pathToOriginalPhoto = FileUtils.writeBitmapFile(bitmap, PictureTypeEnum.Original);
                    Bitmap blurredBitmap = new BlurTransform(MyPicApplication.getContext()).transform(bitmap);
                    String pathToBlurredPhoto = FileUtils.writeBitmapFile(blurredBitmap, PictureTypeEnum.Blurred);

                    Bundle data = preparePhotoPathData(pathToOriginalPhoto, pathToBlurredPhoto);
                    sendMessage(Constants.EVENT_PHOTOS_SAVED, data);
                } catch (IOException e) {
                    LogUtils.LOGE(TAG, e.getLocalizedMessage());
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                sendMessage(Constants.EVENT_PHOTOS_NOT_SAVED, null);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private static void sendMessage(final String event, final Bundle data) {
        EventBus.getInstance().sendMessage(event, data);
    }

    private static Bundle prepareUserIdData(final String userId) {
        Bundle data = new Bundle();
        data.putString(Constants.DATA_USER_ID, userId);
        return data;
    }

    private static Bundle preparePhotoPathData(final String pathToOriginalPhoto, final String pathToBlurredPhoto) {
        Bundle data = new Bundle();
        data.putString(Constants.DATA_PHOTO_PATH_ORIGINAL, pathToOriginalPhoto);
        data.putString(Constants.DATA_PHOTO_PATH_BLURRED, pathToBlurredPhoto);
        return data;
    }
}
