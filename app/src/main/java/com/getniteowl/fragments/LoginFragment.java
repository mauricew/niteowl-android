package com.getniteowl.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getniteowl.activities.DashboardActivity;
import com.getniteowl.R;
import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.ParseException;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends DialogFragment {

    private View formView;
    private View progressView;
    private EditText usernameInput;
    private EditText passwordInput;

    public LoginFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        formView = rootView.findViewById(R.id.login_form);
        progressView = rootView.findViewById(R.id.login_progress);

        usernameInput = (EditText) rootView.findViewById(R.id.username);
        passwordInput = (EditText) rootView.findViewById(R.id.password);
        passwordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    validateInputs();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) rootView.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            }
        });

        return rootView;
    }

    private void validateInputs() {
        usernameInput.setError(null);
        passwordInput.setError(null);

        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        boolean invalid = false;

        if(TextUtils.isEmpty(username)) {
            usernameInput.setError(getString(R.string.error_field_required));
            invalid = true;
        }

        if(TextUtils.isEmpty(password)) {
            passwordInput.setError(getString(R.string.error_field_required));
            invalid = true;
        }

        if(!invalid) {
            showProgress(true);
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    showProgress(false);
                    if(e == null) {
                        Intent goToDash = new Intent(getActivity(), DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(goToDash);
                    }
                    else {
                        Toast.makeText(getActivity(), "Login error", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        formView.setVisibility(show ? View.GONE : View.VISIBLE);
        formView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                formView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

}
