package com.severin.baron.firebase_lab.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.ValueEventListener;
import com.severin.baron.firebase_lab.Fragments.UserDetailFragment;
import com.severin.baron.firebase_lab.Model.User;
import com.severin.baron.firebase_lab.Utility.PH;
import com.severin.baron.firebase_lab.R;

public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        UserDetailFragment.OnUserDetailFragmentClosedListener {

    Firebase mFirebaseRootRef;
    Firebase mFbCurrentUser;
    String mEmail, mFullName;
    User mLocalCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(PH.GET_SHARED_PREFERENCES,
                MODE_PRIVATE);
        mFullName = sharedPreferences.getString(PH.USER_NAME, null);
        mEmail = sharedPreferences.getString(PH.USER_EMAIL, null);

        mFirebaseRootRef = new Firebase(PH.FIREBASE_URL);
        mFbCurrentUser = mFirebaseRootRef.child(PH.FB_USERS).child(mFullName);

        if (mLocalCurrentUser == null) {
            mLocalCurrentUser = new User(mEmail);
        }

        if (mLocalCurrentUser.getDisplayName() == null) {
            synchronizeUserWithFb();    // Retrieves data if possible, otherwise requests information
                                        // and pushes it to FB
        }

    }

    private void synchronizeUserWithFb() {
        mFbCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If the user information already exists in the FB DB, this will pull
                // it down
                try {
                    User pulledData = dataSnapshot.getValue(User.class);
                    mLocalCurrentUser.setActiveInRooms(pulledData.getActiveInRooms());
                    mLocalCurrentUser.setDisplayName(pulledData.getDisplayName());
                    mLocalCurrentUser.setPreferredTextColor(pulledData.getPreferredTextColor());
                    mFbCurrentUser.child("changeFlag").setValue(false);
                    // If user information is not in the FB DB, it is instead requested and pushed up
                } catch (FirebaseException e) {
                    // TODO: request user information, add it to FB
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    UserDetailFragment userDetailFragment = UserDetailFragment.newInstance(true);
                    fragmentTransaction.replace(R.id.frameLayout_overall_chatActivity, userDetailFragment);
                    fragmentTransaction.commit();
                    LinearLayout synchronizing = (LinearLayout) findViewById(R.id.layout_synchronizingWithDb_chatContent);
                    synchronizing.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                firebaseError.toException().printStackTrace();
            }
        });
        mFbCurrentUser.child("changeFlag").setValue(false);
        mFbCurrentUser.child("changeFlag").setValue(true);
    }

    @Override
    public void onUserDetailSaved(String displayName) {
        // TODO: update displayname
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
