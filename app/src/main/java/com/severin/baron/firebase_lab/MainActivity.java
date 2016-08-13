package com.severin.baron.firebase_lab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.severin.baron.firebase_lab.Model.Message;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    GoogleApiClient mGoogleApiClient;

    // https://fir-lab-2c624.firebaseio.com/

    String mFullName, mEmail;
    public static final int RC_SIGN_IN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure sign-in to request the user's ID, email address, and basic profile. ID and
// basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

// Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
//  enableAutoManage(this, this) should resolve from within anAppCompatActivity,
//  but doesn't.  So... hopefully not important?
//                .enableAutoManage(this, this)  //
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);



        // TODO: test code below
//        Message first, second, third;
//        try {
//            first = new Message("first", "");
//            Thread.sleep(1000);
//            second = new Message("second", "");
//            Thread.sleep(1000);
//            third = new Message("third", "");
//            Thread.sleep(1000);
//
//
//        ArrayList<Message> arrayList = new ArrayList<>();
//        arrayList.add(second);
//        arrayList.add(third);
//        arrayList.add(first);
//
//        for (Message message : arrayList) {
//            Log.d("SEVTEST1 ", message.toString() + " " + message.getTimeStamp());
//        }
//
//        Collections.sort(arrayList);
//
//        for (Message message : arrayList) {
//            Log.d("SEVTEST2 ", message.toString() + " " + message.getTimeStamp());
//        }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from
        //   GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                // Get account information
                mFullName = PH.NULL;
                mEmail = PH.NULL;
                try {
                    mFullName = acct.getDisplayName();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    mEmail = acct.getEmail();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }






}
