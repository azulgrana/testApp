package com.example.myfbpic.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.myfbpic.Constants;
import com.example.myfbpic.MyPicApplication;
import com.example.myfbpic.R;
import com.example.myfbpic.service.FacebookService;
import com.example.myfbpic.util.EventBus;
import com.example.myfbpic.util.SharedPrefs;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

public class MyPictureFragment extends Fragment {

    private ProgressBar mProgressBar;

    private String mPathToOriginalPhoto;
    private String mPathToBlurredPhoto;

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (Constants.EVENT_USER_DATA.equals(action)) {
                String userId = intent.getStringExtra(Constants.DATA_USER_ID);
                FacebookService.requestUserPhotoRetrieval(userId);
            } else if (Constants.EVENT_NO_USER_DATA.equals(action)) {
                hideProgressBar();
            } else if (Constants.EVENT_PHOTOS_SAVED.equals(action)) {
                mPathToOriginalPhoto = intent.getStringExtra(Constants.DATA_PHOTO_PATH_ORIGINAL);
                mPathToBlurredPhoto = intent.getStringExtra(Constants.DATA_PHOTO_PATH_BLURRED);
                savePhotoPreferences();
                attachPicturesToView();
            } else if (Constants.EVENT_PHOTOS_NOT_SAVED.equals(action)) {
                attachErrorPlaceholderToView();
            } else if (Constants.EVENT_PHOTOS_CHANGE_TYPE.equals(action)) {
                attachPicturesToView();
            }
        }
    };
    private SharedPrefs mPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadPhotoPaths();
        prepareEventBus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_picture, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.toolbar_progress_bar);
        checkNotNull(mProgressBar);
        showProgressBar();

        if (mPathToOriginalPhoto.isEmpty()) {
            FacebookService.requestUserInformation();
        } else {
            attachPicturesToView();
        }

        setupRefreshButton();
    }

    private void loadPhotoPaths() {
        mPreferences = new SharedPrefs();

        mPathToOriginalPhoto = mPreferences.getStringPreference(Constants.PREFERENCES_FILED_ORIGINAL_PHOTO_PATH);
        mPathToBlurredPhoto = mPreferences.getStringPreference(Constants.PREFERENCES_FILED_BLURRED_PHOTO_PATH);
    }

    private void savePhotoPreferences() {
        mPreferences.setStringPreference(Constants.PREFERENCES_FILED_ORIGINAL_PHOTO_PATH, mPathToOriginalPhoto);
        mPreferences.setStringPreference(Constants.PREFERENCES_FILED_BLURRED_PHOTO_PATH, mPathToBlurredPhoto);
    }

    private void attachPicturesToView() {
        final String pathToPhoto;
        boolean shouldBeBlurred = mPreferences.getBoolPreference(Constants.PREFERENCES_FILED_BLURRED);

        if (shouldBeBlurred) {
            pathToPhoto = mPathToBlurredPhoto;
        } else {
            pathToPhoto = mPathToOriginalPhoto;
        }

        if (getActivity() != null) {
            final ImageView userPhoto = (ImageView) getActivity().findViewById(R.id.user_pic);
            checkNotNull(userPhoto);

            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(MyPicApplication.getContext()).load(new File(pathToPhoto))
                            .placeholder(R.drawable.photo_placeholder)
                            .error(R.drawable.photo_error)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    userPhoto.setImageBitmap(bitmap);
                                    hideProgressBar();
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    hideProgressBar();
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                }
            });
        }
    }

    private void attachErrorPlaceholderToView() {
        ImageView userPhoto = (ImageView) getActivity().findViewById(R.id.user_pic);
        checkNotNull(userPhoto);
        userPhoto.setImageResource(R.drawable.photo_error);

        hideProgressBar();
    }

    private void setupRefreshButton() {
        FloatingActionButton refreshButton = (FloatingActionButton) getActivity().findViewById(R.id.btn_refresh);
        checkNotNull(refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showProgressBar();
                FacebookService.requestUserInformation();
            }

        });
    }

    private void prepareEventBus() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.EVENT_USER_DATA);
        intentFilter.addAction(Constants.EVENT_NO_USER_DATA);
        intentFilter.addAction(Constants.EVENT_PHOTOS_SAVED);
        intentFilter.addAction(Constants.EVENT_PHOTOS_NOT_SAVED);
        intentFilter.addAction(Constants.EVENT_PHOTOS_CHANGE_TYPE);

        EventBus.getInstance().setListener(mMessageReceiver, intentFilter);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

}
