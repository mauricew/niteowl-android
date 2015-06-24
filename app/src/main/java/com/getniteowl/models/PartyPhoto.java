package com.getniteowl.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Maurice Wahba on 6/21/2015.
 */
@ParseClassName("party_photos")
public class PartyPhoto extends ParseObject {
    public ParseFile getPhoto() {
        return getParseFile("image");
    }
    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }
    public Party getParty() {
        return (Party) getParseObject("party");
    }

    public ParseUser getTakenBy() {
        return getParseUser("taken_by");
    }
}
