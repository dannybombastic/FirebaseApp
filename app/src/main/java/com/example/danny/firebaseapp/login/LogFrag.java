package com.example.danny.firebaseapp.login;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danny.firebaseapp.R;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LogFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LogFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button gMail;
    private TextView nOs,reg;
    LoginButton log;
    private OnFragmentInteractionListener mListener;

    public LogFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Log.
     */
    // TODO: Rename and change types and number of parameters
    public static LogFrag newInstance() {
        LogFrag fragment = new LogFrag();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_log, container, false);
        reg = (TextView)root.findViewById(R.id.nosotros_registro);
        log = (LoginButton)root.findViewById(R.id.login_facebook);
        gMail =(Button)root.findViewById(R.id.registrar_con_google);
        nOs = (TextView) root.findViewById(R.id.registrar_con_nosotros);
        log.setFragment(this);
        log.setReadPermissions(Arrays.asList("email"));
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrden(mListener,"reg");
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrden(mListener,"face");
            }
        });
        gMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrden(mListener,"g");

            }
        });
        nOs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrden(mListener,"nos");
            }
        });

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
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

    public void sendOrden(OnFragmentInteractionListener call,String orden){
        call.onFragmentInteraction(orden);

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
        void onFragmentInteraction(String msg);
    }
}
