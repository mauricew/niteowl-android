package com.getniteowl.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getniteowl.R;
import com.getniteowl.adapters.FriendsAdapter;
import com.getniteowl.models.Party;
import com.getniteowl.models.PartyMember;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PartyCentralMembersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PartyCentralMembersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyCentralMembersFragment extends Fragment {
    private Party party;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param party The party.
     * @return A new instance of fragment PartyCentralMembersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PartyCentralMembersFragment newInstance(Party party) {
        PartyCentralMembersFragment fragment = new PartyCentralMembersFragment();
        fragment.party = party;
        return fragment;
    }

    public PartyCentralMembersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_party_central_members, container, false);

        final RecyclerView partyMembers = (RecyclerView) rootView.findViewById(R.id.party_members);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        partyMembers.setLayoutManager(layout);

        party.getMembersAsync().continueWith(new Continuation<List<PartyMember>, Void>() {
            @Override
            public Void then(Task<List<PartyMember>> task) throws Exception {
                if (task.isFaulted()) {
                    throw task.getError();
                }

                List<ParseUser> members = new ArrayList<ParseUser>();
                for (PartyMember pm : task.getResult()) {
                    members.add(pm.getMember());
                }
                partyMembers.setAdapter(new FriendsAdapter(getActivity(), members));
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

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
