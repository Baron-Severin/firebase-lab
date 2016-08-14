package com.severin.baron.firebase_lab;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by erikrudie on 8/12/16.
 */
public class FirebaseContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
