package com.severin.baron.firebase_lab.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.severin.baron.firebase_lab.Activities.ChatActivity;
import com.severin.baron.firebase_lab.R;
import com.severin.baron.firebase_lab.Utility.PH;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserDetailFragment.OnUserDetailFragmentClosedListener} interface
 * to handle interaction events.
 * Use the {@link UserDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserDetailFragment extends DialogFragment {

    private static boolean isNewUser;
    EditText editText;
    Context parentContext;
    String oldDisplayName;

    private OnUserDetailFragmentClosedListener mListener;

    public UserDetailFragment() {
        // Required empty public constructor
    }

    public static UserDetailFragment newInstance(boolean isNewUser, String oldDisplayName) {
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putBoolean(PH.NEW_USER, isNewUser);
        args.putString(PH.USER_NAME, oldDisplayName);
        fragment.setArguments(args);
        return fragment;
    }

    public void passContext(Context context){
        parentContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isNewUser = getArguments().getBoolean(PH.NEW_USER);
            oldDisplayName = getArguments().getString(PH.USER_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        editText = (EditText) view.findViewById(R.id.editText_chooseUsername_userDetailFragment);
        editText.setText(oldDisplayName);

        TextView newUserText = (TextView) view.findViewById(R.id.textView_newUser_userDetailFragment);
        if (isNewUser) {
            newUserText.setVisibility(View.VISIBLE);
        } else {
            newUserText.setVisibility(View.GONE);
        }

        Button button = (Button) view.findViewById(R.id.button_saveInformation_userDetailFragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserDetailFragmentClosedListener) {
            mListener = (OnUserDetailFragmentClosedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void saveUserInformation(){
        if (!editText.getText().toString().equals("")) {
            OnUserDetailFragmentClosedListener callback =
                    (OnUserDetailFragmentClosedListener) parentContext;
            callback.onUserDetailSaved(editText.getText().toString(), this);
        } else {
            new Toast(parentContext).makeText(parentContext,
                    "Please fill in all fields before continuing", Toast.LENGTH_LONG).show();
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnUserDetailFragmentClosedListener {
        void onUserDetailSaved(String displayName, Fragment fragment);
    }
}
