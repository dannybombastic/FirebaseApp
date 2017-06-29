package com.example.danny.firebaseapp.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.Location.MyLocation;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.database.Db_Handler;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateProfile extends Fragment implements ProfileInteractor.ProfileCallBack{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    TextView adress;
    Db_Handler db;
    Button fab;
    FloatingActionButton fam_map;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText email,name,telefono,nameUpdate;

    ProfileInteractor interactor;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UpdateProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateProfile newInstance(String param1, String param2) {
        UpdateProfile fragment = new UpdateProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_update_profile, container, false);
        email = (EditText)root.findViewById(R.id.new_password);
        telefono = (EditText)root.findViewById(R.id.update_telefono);

        name = (EditText)root.findViewById(R.id.old_password);
        nameUpdate = (EditText)root.findViewById(R.id.textVemailFragment);
        fab = (Button) root.findViewById(R.id.editText25);
        fam_map =(FloatingActionButton)root.findViewById(R.id.fabMap);
        sharedPref = getActivity().getApplication().getSharedPreferences("fire",Context.MODE_PRIVATE);

        editor = sharedPref.edit();
        nameUpdate.setHint(FirebaseActivity.USER_NAME);
        telefono.setHint(sharedPref.getString("telefono","**"));


        fam_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            mapa(mListener);


            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        interactor = new ProfileInteractor(this.getActivity(),db, FirebaseAuth.getInstance(), FirebaseActivity.dataBase);




        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nameUpdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameUpdate.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        telefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    telefono.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        telefono.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean procesado = false;

                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                    // actualizar

                    update();

                    // Ocultar teclado virtual
                    InputMethodManager imm =
                            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    procesado = true;
                }
                return procesado;
            }
        });


        return root;
    }

public void mapa(UpdateProfile.OnFragmentInteractionListener map){

    map.onFragmentInteraction(null);

}
    public void update(){
               editor.putBoolean("tel",true).apply();
               editor.putString("telefono",telefono.getText().toString()).apply();
     interactor.updateProfile(nameUpdate.getText().toString(),FirebaseActivity.USER_EMAIL,name.getText().toString(),email.getText().toString(),this);

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

    @Override
    public void onNetworkConnectFailed() {

    }

    @Override
    public void onBeUserResolvableError(int errorCode) {

    }

    @Override
    public void onGooglePlayServicesFailed() {

    }

    @Override
    public void onEmailError(String msg) {
      email.setError(msg);
    }

    @Override
    public void onNameError(String msg) {
        nameUpdate.setError(msg);
    }

    @Override
    public void onPasswordError(String msg) {
        name.setHint(msg);
        email.setHint(msg);

    }

    @Override
    public void onSuccess() {
        Toast.makeText(getActivity().getApplicationContext(),"Update-Success",Toast.LENGTH_SHORT).show();

    }
}
