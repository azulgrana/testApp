package com.example.myfbpic.util;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.example.myfbpic.MyPicApplication;

public class EventBus {

    private static EventBus instance;

    private EventBus() {

    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }

        return instance;
    }

    public void sendMessage(String event, Bundle data) {
        Intent intent = new Intent(event);
        intent.putExtras(data);
        sendBroadcast(intent);
    }

    public void sendMessage(String event) {
        sendBroadcast(new Intent(event));
    }

    private void sendBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(MyPicApplication.getContext()).sendBroadcast(intent);
    }

    public void setListener(BroadcastReceiver receiver, IntentFilter intentFilter) {
        LocalBroadcastManager.getInstance(MyPicApplication.getContext()).registerReceiver(receiver, intentFilter);
    }
}
