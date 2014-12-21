package com.example.myfbpic.model;

import com.example.myfbpic.service.FacebookService;
import com.example.myfbpic.util.FileUtils;
import com.facebook.Session;

public class FacebookHelper {

    private Session mSession;

    private boolean isUserLoggedIn = false;

    public FacebookHelper() {
        retrieveActiveSession();
    }

    public void logoutUser() {
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            session.closeAndClearTokenInformation();
        }
    }

    public void getUserData() {
        FacebookService.requestUserInformation();
    }

    public void handleUserLogout() {
        FileUtils.removeFilesForStorage();
    }

    public void setSession(Session session) {
        mSession = session;
    }

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setUserLoggedIn(final boolean isLoggedIn) {
        isUserLoggedIn = isLoggedIn;
    }

    private void retrieveActiveSession() {
        Session session = Session.getActiveSession();
        if (session != null) {
            mSession = session;
        }
    }

}
