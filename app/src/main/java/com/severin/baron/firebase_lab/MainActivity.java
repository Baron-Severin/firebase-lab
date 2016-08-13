package com.severin.baron.firebase_lab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
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



        SignInButton signInButton = (SignInButton) findViewById(R.id.signInButton_MainActivity);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // TODO: TEMP
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printStuff();
            }
        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        mEmail = sharedPreferences.getString(PH.USER_EMAIL, null);
        mFullName = sharedPreferences.getString(PH.USER_NAME, null);
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
                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try {
                    mFullName = acct.getDisplayName();
                    editor.putString(PH.USER_NAME, mFullName);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    mEmail = acct.getEmail();
                    editor.putString(PH.USER_EMAIL, mEmail);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                editor.commit();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //TODO: TEMP
    public void printStuff() {
        Log.d("SEVTEST ", "name: " + mFullName + " email: " + mEmail);
    }




}
