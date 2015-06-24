package com.getniteowl.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Maurice Wahba on 6/21/2015.
 */
@ParseClassName("party_request")
public class PartyRequest extends ParseObject {
    public ParseUser getInviter() {
        return getParseUser("from_user");
    }

    public ParseUser getRecepient() {
        return getParseUser("invited_friend");
    }

    public Party getParty() {
        return (Party) getParseObject("party");
    }
}
