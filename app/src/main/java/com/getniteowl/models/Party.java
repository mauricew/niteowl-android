package com.getniteowl.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

import bolts.Task;

/**
 * Created by Maurice Wahba on 6/21/2015.
 */
@ParseClassName("party")
public class Party extends ParseObject {
    public String getName() {
        return getString("name");
    }

    public Date getStartDate() {
        return getDate("startsAt");
    }

    public Date getEndDate() {
        return getDate("expiresAt");
    }

    public ParseUser getLeader() {
        return getParseUser("owner");
    }

    public boolean getPrivacySetting() {
        return getBoolean("privacy");
    }

    public Task<List<PartyMember>> getMembersAsync() {
        ParseQuery<PartyMember> memberQuery = ParseQuery.getQuery(PartyMember.class);
        memberQuery.whereEqualTo("party", this);
        memberQuery.include("member");
        return memberQuery.findInBackground();
    }

    public Task<List<PartyPhoto>> getPhotosAsync() {
        ParseQuery<PartyPhoto> photoQuery = ParseQuery.getQuery(PartyPhoto.class);
        photoQuery.whereEqualTo("party", this);
        return photoQuery.findInBackground();
    }
}
