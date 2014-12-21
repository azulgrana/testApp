package com.example.myfbpic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myfbpic.Constants;
import com.example.myfbpic.R;
import com.example.myfbpic.model.FacebookHelper;
import com.example.myfbpic.util.ActivityUtils;
import com.example.myfbpic.util.EventBus;
import com.example.myfbpic.util.LogUtils;
import com.example.myfbpic.util.SharedPrefs;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = LogUtils.buildLogTag(MainActivity.class);

    private LoginFragment mLoginFragment;
    private MyPictureFragment mMyPictureFragment;

    private ProgressBar mProgressBar;

    private UiLifecycleHelper mUiHelper;
    private FacebookHelper mFbHelper;
    private SharedPrefs mPreferences;

    private Session.StatusCallback mCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUiHelper = new UiLifecycleHelper(this, mCallback);
        mUiHelper.onCreate(savedInstanceState);
        mFbHelper = new FacebookHelper();

        mPreferences = new SharedPrefs();

        setupToolBar();
        checkUserLoggedIn();

        mLoginFragment = new LoginFragment();
        mMyPictureFragment = new MyPictureFragment();

        if (mFbHelper.isUserLoggedIn()) {
            replaceFragment(mMyPictureFragment);
        } else {
            replaceFragment(mLoginFragment);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (!ActivityUtils.isInternetConnectionActive()) {
            Toast.makeText(this, R.string.no_inet_connection, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mFbHelper.isUserLoggedIn()) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            MenuItem blurItem = menu.findItem(R.id.action_blur);
            if (mPreferences.getBoolPreference(Constants.PREFERENCES_FILED_BLURRED)) {
                blurItem.setTitle(R.string.action_photo_unblur);
            } else {
                blurItem.setTitle(R.string.action_photo_blur);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_blur:
                boolean blurred;
                if (item.getTitle().equals(getString(R.string.action_photo_blur))) {
                    item.setTitle(R.string.action_photo_unblur);
                    blurred = true;
                } else {
                    item.setTitle(R.string.action_photo_blur);
                    blurred = false;
                }

                mPreferences.setBoolPreference(Constants.PREFERENCES_FILED_BLURRED, blurred);
                EventBus.getInstance().sendMessage(Constants.EVENT_PHOTOS_CHANGE_TYPE);
                break;
            case R.id.action_log_out:
                mFbHelper.logoutUser();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState());
        }
        mUiHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUiHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUiHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUiHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void onSessionStateChange(final Session session, SessionState state) {
        mFbHelper.setSession(session);

        if (state.isOpened()) {
            LogUtils.LOGI(TAG, "User got just logged in");
            handleUserLogin();

        } else if (state.isClosed()) {
            LogUtils.LOGI(TAG, "User got just logged out");
            handleUserLogout();
        }

        invalidateOptionsMenu();
    }

    private void handleUserLogin() {
        mFbHelper.setUserLoggedIn(true);
        replaceFragment(mMyPictureFragment);
    }

    private void handleUserLogout() {
        mFbHelper.setUserLoggedIn(false);
        replaceFragment(mLoginFragment);
        mFbHelper.handleUserLogout();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        checkNotNull(toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (ProgressBar) findViewById(R.id.toolbar_progress_bar);
        checkNotNull(mProgressBar);
        hideProgressBar();
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void checkUserLoggedIn() {
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            mFbHelper.setUserLoggedIn(true);
        } else {
            mFbHelper.setUserLoggedIn(false);
        }
    }

}
