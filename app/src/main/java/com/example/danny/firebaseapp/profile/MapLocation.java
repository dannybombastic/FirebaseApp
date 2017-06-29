package com.example.danny.firebaseapp.profile;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.sing_in.SingInContract;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapLocation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapLocation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapLocation extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MapView mapView;
    Location loc;
    GoogleMap map;
    TextView adress;
    Bundle mBundle;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MapLocation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapLocation.
     */
    // TODO: Rename and change types and number of parameters
    public static MapLocation newInstance(String param1, String param2) {
        MapLocation fragment = new MapLocation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getActivity());

            mBundle = savedInstanceState;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_map_location,container,false);
        MapsInitializer.initialize(this.getActivity());
        mapView = (MapView)root.findViewById(R.id.mapViewfragment);
        mapView.onCreate(mBundle);

         adress = (TextView)root.findViewById(R.id.texVadressfragment);



        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.setMyLocationEnabled(true);

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(FirebaseActivity.LATITUD,FirebaseActivity.LONGITUD), 10);
                map.animateCamera(cameraUpdate);
                loc = new Location("MiMundo");
                loc.setLatitude(FirebaseActivity.LATITUD);
                loc.setLongitude(FirebaseActivity.LONGITUD);
                new MapLocation.Adress2().execute();

            }
        });

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
        void onFragmentInteraction(Uri uri);
    }


    public void loadAdress(Location loc){
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this.getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                }
                strAdd = strReturnedAddress.toString();
                adress.setText(strAdd);
                Log.w("My-Current ", "" + strReturnedAddress.toString());
            } else {
                Log.w("My-Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current", "Canont get Address!");
        }
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    public class Adress2 extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {


            loadAdress(loc);

            return null;
        }
    }
}
