package com.getniteowl.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getniteowl.R;
import com.getniteowl.adapters.PartyCentralPagerAdapter;
import com.getniteowl.models.Party;
import com.getniteowl.models.PartyMember;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PartyCentralFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PartyCentralFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public PartyCentralFragment() {
        // Required empty public constructor
    }

    private ParseQuery<PartyMember> partyCheckQuery() {
        Date now = new Date();
        ParseUser curUser = ParseUser.getCurrentUser();
        ParseQuery<Party> curParty = ParseQuery.getQuery(Party.class);
        curParty.whereLessThanOrEqualTo("startsAt", now);
        curParty.whereGreaterThanOrEqualTo("expiresAt", now);

        ParseQuery<PartyMember> curMemberParty = ParseQuery.getQuery(PartyMember.class);
        curMemberParty.whereMatchesQuery("party", curParty);
        curMemberParty.whereEqualTo("member", curUser);
        curMemberParty.whereDoesNotExist("leftAt");
        curMemberParty.include("party");

        return curMemberParty;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_party_central, container, false);

        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.partycentral_pager);

        viewPager.setOffscreenPageLimit(3);

        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.partycentral_tabs);


        final View noParty = rootView.findViewById(R.id.partycentral_noparty);

        partyCheckQuery().findInBackground(new FindCallback<PartyMember>() {
            @Override
            public void done(List<PartyMember> list, ParseException e) {
                if(e != null || list.size() == 0) {
                    // No party
                    tabLayout.setEnabled(false);
                    tabLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    noParty.setVisibility(View.VISIBLE);
                }
                else {
                    // Party!!!
                    PartyCentralPagerAdapter pagerAdapter = new PartyCentralPagerAdapter(
                        ((AppCompatActivity) getActivity()).getSupportFragmentManager(), list.get(0).getParty()
                    );
                    viewPager.setAdapter(pagerAdapter);

                    tabLayout.setupWithViewPager(viewPager);
                }
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
