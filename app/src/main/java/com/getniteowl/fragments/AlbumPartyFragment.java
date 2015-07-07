package com.getniteowl.fragments;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getniteowl.R;
import com.getniteowl.adapters.AlbumAdapter;
import com.getniteowl.models.Party;
import com.getniteowl.models.PartyPhoto;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumPartyFragment extends Fragment {
    private Party party;

    public AlbumPartyFragment() {
        // Required empty public constructor
    }

    public static AlbumPartyFragment newInstance(Party party) {
        AlbumPartyFragment retVal = new AlbumPartyFragment();
        retVal.party = party;
        return retVal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_album_party, container, false);
        getActivity().findViewById(R.id.app_bar).setVisibility(View.GONE);
        ((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar) rootView.findViewById(R.id.toolbar_albumparty));
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((CollapsingToolbarLayout)rootView.findViewById(R.id.ctl_albumparty)).setTitle(party.getName());

        final RecyclerView partyAlbum = (RecyclerView) rootView.findViewById(R.id.list_album_party);

        GridLayoutManager layoutMan = new GridLayoutManager(getActivity(), 2);
        layoutMan.setOrientation(GridLayoutManager.VERTICAL);
        partyAlbum.setLayoutManager(layoutMan);

        party.getPhotosAsync().onSuccess(new Continuation<List<PartyPhoto>, Void>() {
            @Override
            public Void then(Task<List<PartyPhoto>> task) throws Exception {
                List<PartyPhoto> photos = task.getResult();
                partyAlbum.setAdapter(new AlbumAdapter(getActivity(), photos));
                ((TextView) rootView.findViewById(R.id.scrapbook_album_count)).setText(photos.size() + " photos");
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            ((AppCompatActivity)getActivity()).getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getActivity().findViewById(R.id.app_bar).setVisibility(View.VISIBLE);
    }
}
