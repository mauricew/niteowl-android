package com.getniteowl.fragments;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.getniteowl.R;
import com.getniteowl.adapters.AlbumPartyAdapter;
import com.getniteowl.models.Party;
import com.getniteowl.models.PartyPhoto;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * A placeholder fragment containing a simple view.
 */
public class AlbumActivityFragment extends Fragment {
    private String partyId;

    public AlbumActivityFragment() {
    }

    public static AlbumActivityFragment newInstance(String partyId) {
        AlbumActivityFragment fragment = new AlbumActivityFragment();
        fragment.partyId = partyId;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_album, container, false);

        final RecyclerView partyAlbum = (RecyclerView) rootView.findViewById(R.id.list_album_party);

        GridLayoutManager layoutMan = new GridLayoutManager(getActivity(), 2);
        layoutMan.setOrientation(GridLayoutManager.VERTICAL);
        partyAlbum.setLayoutManager(layoutMan);

        ParseQuery<Party> getParty = ParseQuery.getQuery(Party.class).whereEqualTo("objectId", partyId);
        getParty.getFirstInBackground(new GetCallback<Party>() {
            @Override
            public void done(final Party party, ParseException e) {
                party.getPhotosAsync().onSuccess(new Continuation<List<PartyPhoto>, Void>() {
                    @Override
                    public Void then(Task<List<PartyPhoto>> task) throws Exception {
                        List<PartyPhoto> photos = task.getResult();
                        partyAlbum.setAdapter(new AlbumPartyAdapter(getActivity(), photos));
                        final ImageView headerImage = (ImageView) getActivity().findViewById(R.id.list_scrapbook_image);
                        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.ctl_albumparty)).setTitle(party.getName());
                        ((TextView) getActivity().findViewById(R.id.scrapbook_album_count)).setText(photos.size() + " photos");
                        Picasso.with(getActivity()).load(photos.get(0).getPhoto().getUrl()).resize(1000,1000).centerCrop().into(headerImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                headerImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                    @Override
                                    public boolean onPreDraw() {
                                        headerImage.getViewTreeObserver().removeOnPreDrawListener(this);
                                        getActivity().supportStartPostponedEnterTransition();
                                        return true;
                                    }
                                });
                            }

                            @Override
                            public void onError() {

                            }
                        });
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
            }
        });

        return rootView;
    }
}
