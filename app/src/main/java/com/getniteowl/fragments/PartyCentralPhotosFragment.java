package com.getniteowl.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getniteowl.R;
import com.getniteowl.adapters.AlbumAdapter;
import com.getniteowl.models.Party;
import com.getniteowl.models.PartyPhoto;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PartyCentralPhotosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PartyCentralPhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyCentralPhotosFragment extends Fragment {
    private Party party;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param party The party.
     * @return A new instance of fragment PartyCentralPhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PartyCentralPhotosFragment newInstance(Party party) {
        PartyCentralPhotosFragment fragment = new PartyCentralPhotosFragment();
        fragment.party = party;
        return fragment;
    }

    public PartyCentralPhotosFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_party_central_photos, container, false);

        final RecyclerView partyPhotos = (RecyclerView) rootView.findViewById(R.id.party_photos);
        final SwipeRefreshLayout swlPhotos = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh_party_photos);
        SwipeRefreshLayout.OnRefreshListener swipeListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                party.getPhotosAsync().onSuccess(new Continuation<List<PartyPhoto>, Void>() {
                    @Override
                    public Void then(Task<List<PartyPhoto>> task) throws Exception {
                        List<PartyPhoto> photos = task.getResult();
                        partyPhotos.setAdapter(new AlbumAdapter(getActivity(), photos));

                        swlPhotos.setRefreshing(false);

                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
            }
        };

        GridLayoutManager layoutMan = new GridLayoutManager(getActivity(), 2);
        layoutMan.setOrientation(GridLayoutManager.VERTICAL);
        partyPhotos.setLayoutManager(layoutMan);

        swipeListener.onRefresh();
        swlPhotos.setOnRefreshListener(swipeListener);

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
