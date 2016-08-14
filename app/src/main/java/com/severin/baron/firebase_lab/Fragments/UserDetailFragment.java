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
import android.widget.TextView;

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

    private OnUserDetailFragmentClosedListener mListener;

    public UserDetailFragment() {
        // Required empty public constructor
    }

    public static UserDetailFragment newInstance(boolean isNewUser) {
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putBoolean(PH.NEW_USER, isNewUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isNewUser = getArguments().getBoolean(PH.NEW_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View userDetailFragment = inflater.inflate(R.layout.fragment_user_detail, container, false);

        TextView newUserText = (TextView) userDetailFragment.findViewById(R.id.textView_newUser_userDetailFragment);
        if (isNewUser) {
            newUserText.setVisibility(View.VISIBLE);
        } else {
            newUserText.setVisibility(View.GONE);
        }

        return userDetailFragment;
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
        void onUserDetailSaved(String displayName);
    }
}
