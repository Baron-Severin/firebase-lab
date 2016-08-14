package com.severin.baron.firebase_lab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    GoogleApiClient mGoogleApiClient;

    // https://fir-lab-2c624.firebaseio.com/

    String mFullName, mEmail;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_mainActivity);

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


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SharedPreferences sharedPreferences = getSharedPreferences(PH.GET_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        mEmail = sharedPreferences.getString(PH.USER_EMAIL, null);
        mFullName = sharedPreferences.getString(PH.USER_NAME, null);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from
        //   GoogleSignInApi.getSignInIntent(...);
        if (requestCode == PH.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                // Get account information
                mFullName = PH.NULL;
                mEmail = PH.NULL;
                SharedPreferences sharedPreferences = getSharedPreferences(PH.GET_SHARED_PREFERENCES,
                        Context.MODE_PRIVATE);
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

                beginChatActivity();
            }
        }
    }

    private void signIn() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, PH.RC_SIGN_IN);
    }

    private void beginChatActivity() {
        progressBar.setVisibility(View.GONE);
        if (verifyUserInfoIsSaved()) {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        } else {
            Toast toast = new Toast(this);
            toast.makeText(this, "No login information found, please check your internet connection " +
                    "and try again", Toast.LENGTH_LONG).show();
        }
    }

    private boolean verifyUserInfoIsSaved() {
        if (mEmail != null && mFullName != null) {
            return true;
        } else {
            return false;
        }
    }




}
