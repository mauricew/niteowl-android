package com.getniteowl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.getniteowl.fragments.DashboardActivityFragment;
import com.getniteowl.R;
import com.getniteowl.fragments.FriendsFragment;
import com.getniteowl.fragments.PartyCentralFragment;
import com.getniteowl.fragments.ScrapbookFragment;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.Random;


public class DashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView drawer;
    private Toolbar appBar;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set up Toolbar & Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        appBar = (Toolbar) findViewById(R.id.app_bar);
        drawer = (NavigationView) findViewById(R.id.drawer_main);
        final View mainDash = findViewById(R.id.dashboard);
        final FragmentManager fm = getSupportFragmentManager();

        setSupportActionBar(appBar);

        // Check if logged in
        final ParseUser user = ParseUser.getCurrentUser();
        if(user == null) {
            Intent toWelcome = new Intent(this, WelcomeActivity.class);
            toWelcome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(toWelcome);
        }

        else {
            // Do something nice for our returning users.
            if(getIntent().getBooleanExtra("returning", false)) {
                String[] returnGreetings = new String[]{"Welcome back %1$s.", "Hey %1$s! Been a while.", "Good to see you again %1$s."};
                int chosenGreet = new Random().nextInt(returnGreetings.length - 1);

                Snackbar.make(findViewById(R.id.container), String.format(returnGreetings[chosenGreet], user.getUsername()), Snackbar.LENGTH_LONG).show();
            }

            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, appBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
            drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    Snackbar.make(mainDash, menuItem.getTitle() + " pressed.", Snackbar.LENGTH_LONG).show();
                    menuItem.setCheckable(true);
                    menuItem.setChecked(true);

                    drawerLayout.closeDrawers();

                    Fragment destinationFragment;
                    String prevTitle = getTitle().toString();

                    if (menuItem.getTitle().toString().equals("Logout")) {
                        final Intent toWelcome = new Intent(getApplicationContext(), WelcomeActivity.class);
                        toWelcome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        ParseUser.logOutInBackground(new LogOutCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    startActivity(toWelcome);
                                }
                            }
                        });
                    } else {
                        String destTitle = menuItem.getTitle().toString();
                        switch (destTitle) {
                            case "Friends":
                                destinationFragment = new FriendsFragment();
                                break;
                            case "Albums":
                                destinationFragment = new ScrapbookFragment();
                                break;
                            case "Party Central":
                                destinationFragment = new PartyCentralFragment();
                                break;
                            default:
                                destinationFragment = new DashboardActivityFragment();
                                break;
                        }

                        setTitle(menuItem.getTitle().toString());

                        fm.beginTransaction()
                                .replace(R.id.container, destinationFragment)
                                .addToBackStack(getTitle().toString()).commit();
                    }

                    return true;
                }
            });

            fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    int num = fm.getBackStackEntryCount();
                    Menu drawerMenu = drawer.getMenu();
                    if (num > 0) {
                        String title = fm.getBackStackEntryAt(num - 1).getName();
                        if (title == null) {
                            title = getString(R.string.app_name);
                        }
                        getSupportActionBar().setTitle(title);

                        for (int i = 0; i < drawerMenu.size(); i++) {
                            MenuItem curItem = drawerMenu.getItem(i);
                            curItem.setChecked(false);
                            if (curItem.getTitle() == title) {
                                curItem.setChecked(true);
                            }
                        }
                    } else {
                        String title = getString(R.string.app_name);
                        getSupportActionBar().setTitle(title);

                        for (int i = 0; i < drawerMenu.size(); i++) {
                            MenuItem curItem = drawerMenu.getItem(i);
                            curItem.setChecked(false);
                        }
                    }
                }
            });

            fm.beginTransaction().add(R.id.container, new DashboardActivityFragment()).commit();

            // Header for nav drawer
            TextView drawerUsername = (TextView) findViewById(R.id.drawer_profile_username);
            drawerUsername.setText(user.getUsername());
            TextView drawerEmail = (TextView) findViewById(R.id.drawer_profile_email);
            final ImageView drawerPhoto = (ImageView) findViewById(R.id.drawer_profile_photo);
            drawerEmail.setText(user.getEmail());

            // Round photo!
            if(user.getParseFile("profile_photo") != null) {
                Picasso.with(this).load(user.getParseFile("profile_photo").getUrl()).resize(256, 256).centerCrop().into(drawerPhoto);
            }
            findViewById(R.id.drawer_header).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toMyProfile = new Intent(DashboardActivity.this, ProfileActivity.class);

                    toMyProfile.putExtra("userId", user.getObjectId());

                    startActivity(toMyProfile,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(DashboardActivity.this
                                    , new Pair(drawerPhoto, "profilePhoto")).toBundle()
                    );
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
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

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(drawer)) {
            drawerLayout.closeDrawers();
        }
        else {
            super.onBackPressed();
        }
    }
}
