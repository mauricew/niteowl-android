package com.getniteowl.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.getniteowl.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends DialogFragment {

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        Button regBtn = (Button) rootView.findViewById(R.id.btn_doregister);

        return rootView;
    }

    private void validateFields() {

    }

    private void registerAccount() {

    }

}
