package com.getniteowl.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Maurice Wahba on 6/21/2015.
 */
@ParseClassName("party_members")
public class PartyMember extends ParseObject {
    public Party getParty() {
        return (Party) getParseObject("party");
    }

    public ParseUser getMember() {
        return (ParseUser) getParseUser("member");
    }
}
