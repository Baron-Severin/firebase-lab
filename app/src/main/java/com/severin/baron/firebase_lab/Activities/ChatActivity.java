package com.severin.baron.firebase_lab.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.severin.baron.firebase_lab.Fragments.UserDetailFragment;
import com.severin.baron.firebase_lab.Model.Message;
//import com.severin.baron.firebase_lab.Model.MessageList;
import com.severin.baron.firebase_lab.Model.User;
import com.severin.baron.firebase_lab.Utility.MessageAdapter;
import com.severin.baron.firebase_lab.Utility.PH;
import com.severin.baron.firebase_lab.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        UserDetailFragment.OnUserDetailFragmentClosedListener {

    Firebase mFirebaseRootRef, mFbCurrentUser, mFbRooms, mFbCurrentRoom, mOldRoom;
    String mEmail, mFullName;
    User mLocalCurrentUser;
    Context mContext;
    List<Message> currentChatMessages;
    MessageAdapter adapter;
//    MessageList mCurrentRoomMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mContext = this;
        if (currentChatMessages == null) {
            currentChatMessages = new ArrayList<>();
        }

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

        initFbInstances();

        if (mLocalCurrentUser == null) {
            mLocalCurrentUser = new User(mEmail);
        }

        if (mLocalCurrentUser.getDisplayName() == null) {
            synchronizeUserWithFb();    // Retrieves data if possible, otherwise requests information
                                        // and pushes it to FB
        }

        joinRoom(PH.DEFAULT_ROOM_ID);
//        mFbCurrentRoom = mFbRooms.child(String.valueOf(0));

//        Message message = new Message(" has left the room", mLocalCurrentUser.getUserId());
//
//
//        mFbCurrentRoom.child(PH.MESSAGE_LIST).push().setValue(message);
//        mFbCurrentRoom.child(PH.MESSAGE_LIST).push().setValue(message);
//        mFbCurrentRoom.child(PH.MESSAGE_LIST).push().setValue(message);
//        mFbCurrentRoom.child(PH.MESSAGE_LIST).push().setValue(message);
    }

    public void initFbInstances() {
        if (mFirebaseRootRef == null) {
            mFirebaseRootRef = new Firebase(PH.FIREBASE_URL);
        }
        if (mFbCurrentUser == null) {
            mFbCurrentUser = mFirebaseRootRef.child(PH.FB_USERS).child(mFullName);
        }
        if (mFbRooms == null) {
            mFbRooms = mFirebaseRootRef.child(PH.FB_ROOMS);
        }
    }

    private void synchronizeUserWithFb() {
        mFbCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User pulledData = dataSnapshot.getValue(User.class);
                if (pulledData.getDisplayName() != null &&
                        !pulledData.getDisplayName().equals("")) {
                    mLocalCurrentUser.setActiveInRooms(pulledData.getActiveInRooms());
                    mLocalCurrentUser.setDisplayName(pulledData.getDisplayName());
                    mLocalCurrentUser.setPreferredTextColor(pulledData.getPreferredTextColor());
                    mFbCurrentUser.child("changeFlag").setValue(false);
                } else {
                    // If user information is not in the FB DB, it is instead requested and pushed up
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    UserDetailFragment userDetailFragment = UserDetailFragment.newInstance(true);
                    userDetailFragment.passContext(mContext);
                    transaction.replace(R.id.linearLayout_overall_chatActivity, userDetailFragment);
                    transaction.commit();
                }
                LinearLayout synchronizing = (LinearLayout)
                        findViewById(R.id.layout_synchronizingWithDb_chatContent);
                synchronizing.setVisibility(View.GONE);
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
    public void onUserDetailSaved(String displayName, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();

        mLocalCurrentUser.setDisplayName(displayName);
        pushCurrentUserDetailsToFb();
    }

    private void pushCurrentUserDetailsToFb() {
        mFbCurrentUser.setValue(mLocalCurrentUser);

    }

    private void joinRoom(final long roomId) {
        mFbCurrentRoom = mFbRooms.child(String.valueOf(roomId));

        mFbCurrentRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Pull down messages, which are seen as a LinkedHashMap
                LinkedHashMap<String, LinkedHashMap<String, Message>> obby
                        = (LinkedHashMap<String, LinkedHashMap<String, Message>>)
                        dataSnapshot.getValue(Object.class);
                // Pull out the actual list of messages
                LinkedHashMap<String, Message> secondStep = obby.get(PH.MESSAGE_LIST);
                Set<String> keySet = secondStep.keySet();
                Log.d("SEVTEST ", "Clearing chat messages");
                currentChatMessages.clear();

                // Loop over keyset
                for (String key : keySet) {
                    ObjectMapper mapper = new ObjectMapper();
                    Message message = null;
                    // Break the resulting values down into byte arrays, then use an ObjectMapper
                    // to build them into Messages
                    try {
                        byte[] json = mapper.writeValueAsBytes(secondStep.get(key));
                        message = mapper.readValue(json, Message.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    currentChatMessages.add(message);
                }
                Collections.sort(currentChatMessages);
                //TODO:    updateChatText(dataSnapshot);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Message message = new Message(PH.HAS_LEFT_ROOM, mLocalCurrentUser.getUserId());
                mOldRoom.child(PH.MESSAGE_LIST).push().setValue(message);
            }
        });
//        getSupportActionBar().setTitle();  // TODO: set this to room name
        //TODO: add a "user joined" message.  this will return data as well
        mFbCurrentRoom.child(PH.MESSAGE_LIST).push().setValue(new Message
                        (PH.HAS_JOINED_ROOM, new Date(), mLocalCurrentUser.getUserId()));

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_chat_chatActivity);
            adapter = new MessageAdapter(this, currentChatMessages);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
