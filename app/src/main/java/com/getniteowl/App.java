package com.getniteowl;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.getniteowl.models.*;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParsePush;

/**
 * Created by Maurice Wahba on 6/20/2015.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        //ParseCrashReporting.enable(getApplicationContext());

        // ORM
        ParseObject.registerSubclass(Friend.class);
        ParseObject.registerSubclass(FriendRequest.class);
        ParseObject.registerSubclass(Party.class);
        ParseObject.registerSubclass(PartyMember.class);
        ParseObject.registerSubclass(PartyPhoto.class);
        ParseObject.registerSubclass(PartyRequest.class);

        Parse.initialize(this, "glqHvyHqHE47WYCnw3NSpoQbE5mqCLD6TWkSTgQs",
                "nDnMLlCu1NuAqziCKPhOGHYWXEsakDhEQOahQv9B");

        //ParsePush.subscribeInBackground("");

        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);
    }
}
