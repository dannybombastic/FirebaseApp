package com.example.danny.firebaseapp.MyStock;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danny.firebaseapp.FindByLocation.FindByLocation;
import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.Wall.AdapterPostBusc;
import com.example.danny.firebaseapp.Wall.EmptyFragment;
import com.example.danny.firebaseapp.Wall.FirebaseArrayDesc;
import com.example.danny.firebaseapp.Wall.ReciclerViewClikListener;
import com.example.danny.firebaseapp.post.PostUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentBusqueda.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentBusqueda#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBusqueda extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AdapterStock adapterPostBusc;
    private FireBaseArrayStock fireBaseArrayStock;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Query query;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recycler;
    RecyclerView.LayoutManager layoutManager;
    public FragmentBusqueda() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBusqueda.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBusqueda newInstance(String param1, String param2) {
        FragmentBusqueda fragment = new FragmentBusqueda();
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
       View root = inflater.inflate(R.layout.fragment_fragment_busqueda, container, false);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("postCarExpress");

        query = ref.child("pices");

        recycler = (RecyclerView)root.findViewById(R.id.fragment_busqueda);
        new Adress2().execute("1");
        recycler.addOnItemTouchListener(new ReciclerViewClikListener(getContext(), recycler, new ReciclerViewClikListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                FragmentStock m = (FragmentStock) getFragmentManager().findFragmentById(R.id.fragmen_stock);
                m = FragmentStock.newInstance("", "", ((PostUsers) adapterPostBusc.getItem(position)), R.id.stock);

                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.stock, m)
                        .addToBackStack("")
                        .commit();


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
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


    public void setAdapAndArray(FireBaseArrayStock f,String uri){

        this.fireBaseArrayStock = f;
        this.mParam1 =  uri;


    }

    private class Adress2 extends AsyncTask<String,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            layoutManager = new GridLayoutManager(getContext(), 2);
            recycler.setHasFixedSize(true);
            layoutManager.offsetChildrenVertical(20);
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(adapterPostBusc);
            if (adapterPostBusc.getItemCount() >= 0) {







            } else {
                getFragmentManager().popBackStack();
                EmptyFragment eM = new EmptyFragment();
                if (eM != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                            .add(R.id.stock, eM)
                            .addToBackStack("")
                            .commit();

                }


            }
        }

        @Override
        protected  Void doInBackground(String... params) {
            if(params[0]=="1") {


              //  adapterPostBusc.clear();
                FireBaseArrayStock.set(mParam1,true);
                FireBaseArrayStock.banderaBuscar = true;
                FireBaseArrayStock firebaseArray2 = new FireBaseArrayStock(query, FirebaseActivity.USER_EMAIL);

                List<DataSnapshot> lista = fireBaseArrayStock.mSnapshots;
                for (DataSnapshot listo: lista){
                    PostUsers p = listo.getValue(PostUsers.class);
                    String [] c = mParam1.split(" ");
                    String [] g  = (listo.getValue(PostUsers.class)).getToFind().split(" ");
                    Iterator<String> a = Arrays.asList(g).iterator();


                    while (a.hasNext()){
                        String palabra = a.next();
                        for (int j =0;j < c.length;j++){
                            if(!c[j].contains(palabra)){
                                firebaseArray2.mSnapshots.remove(listo);
                            }
                        }

                    }



                }

                adapterPostBusc = new AdapterStock<PostUsers, MyStock.PostViewHolder>(
                        PostUsers.class, R.layout.item_post_cardview, MyStock.PostViewHolder.class, firebaseArray2) {

                    public void populateViewHolder(MyStock.PostViewHolder chatMessageViewHolder, PostUsers post, int position) {
                        String s = post.getToFind();
                        String pic = post.getPic_one();
                        if(pic ==null){
                            pic = post.getPic_two();
                            if(pic==null){
                                pic = post.getPic_three();
                            }
                        }
                        Glide.with(getContext())
                                .load(pic)
                                .asBitmap()
                                .thumbnail(0.5f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.mipmap.circle_logo)
                                .centerCrop()
                                .into(chatMessageViewHolder.iamgen);
                        chatMessageViewHolder.price.setText(post.getPrice() + "€");
                        chatMessageViewHolder.desc.setText(post.getYear());


                        mSnapshots.getItem(chatMessageViewHolder.getAdapterPosition()).getKey();

                    }
                };



            }else if(params[0]=="2"){

            }



            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FireBaseArrayStock.set(mParam1,false);
    }
}
