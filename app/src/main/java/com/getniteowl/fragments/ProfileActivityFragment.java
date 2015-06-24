package com.getniteowl.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.getniteowl.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment {

    private ParseUser user;
    private String userId;
    private String username;
    private String photoUrl;

    private TextView profile_username;
    private ImageView profile_photo;

    public ProfileActivityFragment() {
    }
    
    public static ProfileActivityFragment create(ParseUser user) {
        ProfileActivityFragment retVal = new ProfileActivityFragment();
        retVal.user = user;
        return retVal;
    }

    public static ProfileActivityFragment create(String userId, String username, String photoUrl) {
        ProfileActivityFragment retVal = new ProfileActivityFragment();
        retVal.userId = userId;
        retVal.username = username;
        retVal.photoUrl = photoUrl;
        return retVal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_username = (TextView) rootView.findViewById(R.id.profile_username);
        profile_photo = (ImageView) rootView.findViewById(R.id.profile_photo);

        if(user == null) {
            profile_username.setText(username);
            Picasso.with(getActivity()).load(photoUrl).networkPolicy(NetworkPolicy.OFFLINE).into(profile_photo);
            ParseUser.getQuery().whereEqualTo("objectId", userId).getFirstInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    user = parseUser;
                    profile_username.setText(user.getUsername());

                    ParseFile photoObj = user.getParseFile("profile_photo");
                    if (photoObj != null) {
                        Picasso.with(getActivity()).load(photoObj.getUrl()).resize(256, 256).centerCrop().into(profile_photo, new Callback() {
                            @Override
                            public void onSuccess() {
                                profile_photo.getViewTreeObserver().addOnPreDrawListener(
                                        new ViewTreeObserver.OnPreDrawListener() {
                                            @Override
                                            public boolean onPreDraw() {
                                                profile_photo.getViewTreeObserver().removeOnPreDrawListener(this);
                                                getActivity().supportStartPostponedEnterTransition();
                                                return true;
                                            }
                                        });
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                }
            });
        }
        
        return rootView;
    }
}
