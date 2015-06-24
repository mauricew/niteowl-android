package com.getniteowl.fragments;

import android.app.Activity;
import android.graphics.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getniteowl.R;
import com.getniteowl.models.Party;
import com.getniteowl.models.PartyPhoto;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PartyCentralInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PartyCentralInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyCentralInfoFragment extends Fragment {
    private Party party;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param party The party.
     * @return A new instance of fragment PartyCentralInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PartyCentralInfoFragment newInstance(Party party) {
        PartyCentralInfoFragment fragment = new PartyCentralInfoFragment();
        fragment.party = party;
        return fragment;
    }

    public PartyCentralInfoFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_party_central_info, container, false);

        final SupportMapFragment mapFragment = new SupportMapFragment();
        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {
                getChildFragmentManager().beginTransaction().replace(R.id.mappy, mapFragment).commit();
            }
        }, 100);

        /*
        SupportMapFragment mapFragment = (SupportMapFragment)(getChildFragmentManager().findFragmentById(R.id.map));
        */

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        googleMap.setOnMyLocationChangeListener(null);

                        SetPhotoMarkers(googleMap);
                    }
                });
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

    public void SetPhotoMarkers(final GoogleMap map) {
        party.getPhotosAsync().continueWith(new Continuation<List<PartyPhoto>, Void>() {
            @Override
            public Void then(Task<List<PartyPhoto>> task) throws Exception {
                List<PartyPhoto> photos = task.getResult();
                for(PartyPhoto p : photos) {
                    ParseGeoPoint loc = p.getLocation();
                    if(loc != null) {
                        String label = DateFormat.getDateFormat(getActivity()).format(p.getCreatedAt());//p.getCreatedAt().
                        MarkerOptions marker = new MarkerOptions()
                                .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                                .title("Photo!").snippet("Taken " + label);
                        map.addMarker(marker);
                    }
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

}
