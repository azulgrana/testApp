package com.example.myfbpic.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfbpic.Constants;
import com.example.myfbpic.R;
import com.facebook.widget.LoginButton;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LoginButton authButton = (LoginButton) getActivity().findViewById(R.id.btn_login);
        authButton.setReadPermissions(Constants.FACEBOOK_PERMISSIONS);
    }
}
