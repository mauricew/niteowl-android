package com.getniteowl.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Maurice Wahba on 6/21/2015.
 */
@ParseClassName("user_friendrequests")
public class FriendRequest extends ParseObject {
    public ParseUser getRequestor() {
        return getParseUser("user");
    }

    public ParseUser getRecipient() {
        return getParseUser("friend");
    }
}
