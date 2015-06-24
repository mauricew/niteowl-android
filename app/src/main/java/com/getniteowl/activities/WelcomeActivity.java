package com.getniteowl.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.getniteowl.R;
import com.getniteowl.fragments.WelcomeActivityFragment;


public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar_welcome);
        setSupportActionBar(toolbar);

        // Initial view
        getSupportFragmentManager().beginTransaction().add(R.id.container, new WelcomeActivityFragment()).commit();
    }
}
