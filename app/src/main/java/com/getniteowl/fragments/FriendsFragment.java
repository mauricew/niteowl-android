package com.getniteowl.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.getniteowl.R;
import com.getniteowl.adapters.UsersAdapter;
import com.getniteowl.models.Friend;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import bolts.Continuation;
import bolts.Task;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        setHasOptionsMenu(true);

        final RecyclerView friendsList = (RecyclerView) rootView.findViewById(R.id.list_friends);

        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        friendsList.setLayoutManager(layout);

        Friend.getCurrentFriendsQuery().onSuccess(new Continuation<List<ParseUser>, Void>() {
            @Override
            public Void then(Task<List<ParseUser>> task) throws Exception {
                UsersAdapter adapter = new UsersAdapter(getActivity(), task.getResult());
                friendsList.setAdapter(adapter);
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_friends, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.friend_add:
                AlertDialog.Builder friendAddBuilder = new ProgressDialog.Builder(getActivity());
                friendAddBuilder.setTitle("Add Friend");
                friendAddBuilder.setMessage("What's their username?");

                final EditText friendAddUsername = new EditText(getActivity());
                friendAddBuilder.setView(friendAddUsername);

                friendAddBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar addedPopup = Snackbar.make(getView(), "Request sent to " + friendAddUsername.getText().toString(), Snackbar.LENGTH_LONG);
                        addedPopup.setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        addedPopup.show();
                    }
                });
                friendAddBuilder.setNegativeButton("Cancel", null);

                final AlertDialog dlg = friendAddBuilder.create();

                friendAddUsername.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(count >= 4) {
                            dlg.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                            ParseUser.getQuery().whereEqualTo("username", s.toString()).countInBackground(new CountCallback() {
                                @Override
                                public void done(int i, ParseException e) {
                                    if (e != null || i == 0) {
                                        friendAddUsername.setError("User does not exist.");
                                    } else {
                                        dlg.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                dlg.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class usernameCheck extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            ParseQuery<ParseUser> check = ParseUser.getQuery().whereEqualTo("username", params[0]);

            try {
                int result = check.count();
                return result > 0;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    class checkUsername implements Runnable {
        private String username;

        public checkUsername(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            ParseQuery<ParseUser> check = ParseUser.getQuery().whereEqualTo("username", username);

            try {
                int result = check.count();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
