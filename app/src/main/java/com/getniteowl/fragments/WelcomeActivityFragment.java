package com.getniteowl.fragments;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.*;
import com.facebook.login.widget.LoginButton;
import com.getniteowl.*;
import com.getniteowl.activities.DashboardActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.json.JSONObject;


/**
 * A placeholder fragment containing a simple view.
 */
public class WelcomeActivityFragment extends Fragment {

    private FragmentManager fm;
    private Button loginButton;
    private Button registerButton;
    private LoginButton fbButton;
    private CallbackManager fbCallback;

    public WelcomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);

        fm = getFragmentManager();
        loginButton = (Button) rootView.findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ftLogin = fm.beginTransaction()
                        .replace(R.id.container, new LoginFragment())
                        .addToBackStack(null);
                ftLogin.commit();
            }
        });
        registerButton = (Button) rootView.findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ftRegister = fm.beginTransaction()
                        .replace(R.id.container, new RegisterFragment())
                        .addToBackStack(null);
                ftRegister.commit();
            }
        });

        fbCallback = CallbackManager.Factory.create();
        fbButton = (LoginButton) rootView.findViewById(R.id.btn_facebook_login);
        fbButton.setFragment(this);
        fbButton.setReadPermissions("public_profile", "email", "user_friends");
        fbButton.registerCallback(fbCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ParseFacebookUtils.logInInBackground(loginResult.getAccessToken(), new LogInCallback() {
                    @Override
                    public void done(final ParseUser user, ParseException e) {
                        final Intent goToDash = new Intent(getActivity(), DashboardActivity.class);
                        goToDash.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        if(user.isNew()) {
                            // Do stuff
                            GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject fbUser, GraphResponse graphResponse) {
                                    String gender = fbUser.optString("gender");
                                    String email = fbUser.optString("email");
                                    String id = fbUser.optString("id");

                                    user.put("gender", gender);
                                    user.put("email", email);

                                    //Photo stuff comes later!!
                                    //String photoUrl = "http://graph.facebook.com/" + id + "/picture?type=large";

                                }
                            });
                        }
                        else {
                            goToDash.putExtra("returning", true);
                            startActivity(goToDash);
                        }
                    }
                });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                throw e;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbCallback.onActivityResult(requestCode, resultCode, data);
    }
}
