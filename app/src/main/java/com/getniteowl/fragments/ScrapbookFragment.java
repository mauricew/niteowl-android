package com.getniteowl.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getniteowl.R;
import com.getniteowl.adapters.ScrapbookAdapter;
import com.getniteowl.models.PartyPhoto;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScrapbookFragment extends Fragment {


    public ScrapbookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scrapbook, container, false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.move));
            setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.move));
        }

        final RecyclerView listScrapbook = (RecyclerView) rootView.findViewById(R.id.list_scrapbook_main);

        GridLayoutManager layoutMan = new GridLayoutManager(getActivity(), 2);
        layoutMan.setOrientation(GridLayoutManager.VERTICAL);
        listScrapbook.setLayoutManager(layoutMan);

        ScrapbookAdapter adapter = new ScrapbookAdapter(getActivity());
        listScrapbook.setAdapter(adapter);

        ScrapbookAdapter.GetYourParties(adapter);
        return rootView;
    }


}
