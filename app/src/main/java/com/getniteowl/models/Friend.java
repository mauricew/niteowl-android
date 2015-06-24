package com.getniteowl.models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by Maurice Wahba on 6/21/2015.
 */
@ParseClassName("user_friends")
public class Friend extends ParseObject {
    public static Task<List<ParseUser>> getCurrentFriendsQuery() {
        ParseQuery<Friend> friend_fromMe = ParseQuery.getQuery(Friend.class).whereEqualTo("user", ParseUser.getCurrentUser());
        ParseQuery<Friend> friend_toMe = ParseQuery.getQuery(Friend.class).whereEqualTo("friend", ParseUser.getCurrentUser());

        List<ParseQuery<Friend>> subqueries = new ArrayList<ParseQuery<Friend>>();
        subqueries.add(friend_fromMe);
        subqueries.add(friend_toMe);

        ParseQuery<Friend> retVal = ParseQuery.or(subqueries);
        retVal.include("user");
        retVal.include("friend");

        return retVal.findInBackground().onSuccess(new Continuation<List<Friend>, List<ParseUser>>() {

            @Override
            public List<ParseUser> then(Task<List<Friend>> task) throws Exception {
                List<Friend> result = task.getResult();
                List<ParseUser> retVal = new ArrayList<ParseUser>();
                ParseUser curUser = ParseUser.getCurrentUser();

                for (Friend f : result) {
                    ParseUser fUser = f.getParseUser("user");
                    if (fUser.getUsername().equals(curUser.getUsername())) {
                        fUser = f.getParseUser("friend");
                    }
                    retVal.add(fUser);
                }

                return retVal;
            }
        });
    }
}
