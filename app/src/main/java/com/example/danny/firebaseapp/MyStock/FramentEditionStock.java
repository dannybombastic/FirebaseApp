package com.example.danny.firebaseapp.MyStock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.camera.Camera;
import com.example.danny.firebaseapp.post.PostForm;
import com.example.danny.firebaseapp.post.PostUsers;
import com.example.danny.firebaseapp.storage.StorageFirebase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FramentEditionStock.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FramentEditionStock#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FramentEditionStock extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private BroadcastReceiver mBroadcastReceiver;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView photo_uno,photo_dos,photo_tre;
    public ProgressBar uno,dos,tres;
    private Button update;
    private EditText price,desc,tel,titulo;
    private static  PostUsers postUsers;
    // TODO: Rename and change types of parameters
    private String mParam1,p_one,p_two,p_three;
    private String mParam2;


    private OnFragmentInteractionListener mListener;


    public FramentEditionStock() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment FramentEditionStock.
     */
    // TODO: Rename and change types and number of parameters
    public static FramentEditionStock newInstance(String param1, PostUsers user) {
        FramentEditionStock fragment = new FramentEditionStock();
         postUsers = user;
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

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

        View root = inflater.inflate(R.layout.fragment_frament_edition_stock, container, false);
        photo_uno = (ImageView)root.findViewById(R.id.imagen_uno_edition);
        photo_dos = (ImageView)root.findViewById(R.id.imagen_dos_edition);
        photo_tre = (ImageView)root.findViewById(R.id.imagen_tres_edition);
        uno = (ProgressBar)root.findViewById(R.id.progressbar_edition_uno);
        dos  =   (ProgressBar)root.findViewById(R.id.progressbar_edition_dos);
        tres =   (ProgressBar)root.findViewById(R.id.progressbar_edition_tres);
        price  =   (EditText)root.findViewById(R.id.precio_edition);
        desc   =   (EditText)root.findViewById(R.id.descripcion_edition);
        titulo =   (EditText)root.findViewById(R.id.titulo_edition);
        tel    =   (EditText)root.findViewById(R.id.tel_edition);
        update =(Button)root.findViewById(R.id.actualizar_edition);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostUsers pt = new PostUsers();
                pt.setPic_one(p_one);
                pt.setPic_two(p_two);
                pt.setPic_three(p_three);
                pt.setTel(tel.getText().toString());
                pt.setDesc(desc.getText().toString());
                pt.setYear(titulo.getText().toString());
                pt.setPrice(price.getText().toString());
                pt.setToFind(postUsers.getToFind());
                pt.setLocalidad(postUsers.getLocalidad());
                pt.setName(postUsers.getName());
                pt.setEmail(postUsers.getEmail());
                pt.setToFind(postUsers.getToFind()+desc.getText().toString()+" "+titulo);

                mListener.onFragmentInteraction(pt);
            }
        });
        photo_uno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             mListener.onAccion(v);
            }
        });

        photo_dos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mListener.onAccion(v);
            }
        });
        photo_tre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAccion(v);

            }
        });

        setFormulario(postUsers);
        setImagen(postUsers.getPic_one(),postUsers.getPic_two(),postUsers.getPic_three());


        return root;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(PostUsers uri) {
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
        void onFragmentInteraction(PostUsers user);
        void onAccion(View v);
    }


    public void setImagen(String... b){
        String [] fotos = b;


        if(fotos[0]!= null){
             p_one = fotos[0];
            Glide.with(getContext())
                    .load(fotos[0])
                    .asBitmap()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(VectorDrawableCompat.create(getResources(),R.drawable.vendyn,null))
                    .centerCrop()
                    .into(photo_uno);

        }

        if(fotos[1]!= null){
            p_two = fotos[1];
            Glide.with(getContext())
                    .load(fotos[1])
                    .asBitmap()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(VectorDrawableCompat.create(getResources(),R.drawable.vendyn,null))
                    .centerCrop()
                    .into(photo_dos);

        }

        if(fotos[2]!= null){
            p_three = fotos[2];
            Glide.with(getContext())
                    .load(fotos[2])
                    .asBitmap()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(VectorDrawableCompat.create(getResources(),R.drawable.vendyn,null))
                    .centerCrop()
                    .into(photo_tre);

        }


    }

    public void setFormulario(PostUsers post){

        titulo.setText(post.getYear());
        desc.setText(post.getDesc());
        tel.setText(post.getTel());
        price.setText(post.getPrice());

    }
    public void setPhoto_uno(String photo,int numero){

        if(numero == 1) {
            Glide.with(getContext())
                    .load(photo)
                    .asBitmap()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(VectorDrawableCompat.create(getResources(), R.drawable.vendyn, null))
                    .centerCrop()
                    .into(photo_uno);
            p_one = photo;
            uno.setVisibility(ProgressBar.INVISIBLE);
        }
        if(numero == 2) {
            Glide.with(getContext())
                    .load(photo)
                    .asBitmap()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(VectorDrawableCompat.create(getResources(), R.drawable.vendyn, null))
                    .centerCrop()
                    .into(photo_dos);
            p_two = photo;
            dos.setVisibility(ProgressBar.INVISIBLE);
        }
        if(numero == 3) {
            Glide.with(getContext())
                    .load(photo)
                    .asBitmap()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(VectorDrawableCompat.create(getResources(), R.drawable.vendyn, null))
                    .centerCrop()
                    .into(photo_tre);
            p_three = photo;
            tres.setVisibility(ProgressBar.INVISIBLE);
        }

    }








}
