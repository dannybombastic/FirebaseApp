package com.example.danny.firebaseapp.FindByLocation;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danny.firebaseapp.MyStock.FireBaseArrayStock;
import com.example.danny.firebaseapp.MyStock.FragmentStock;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.Wall.AdapterPostBusc;
import com.example.danny.firebaseapp.Wall.EmptyFragment;
import com.example.danny.firebaseapp.Wall.FirebaseArrayDesc;
import com.example.danny.firebaseapp.Wall.ReciclerViewClikListener;
import com.example.danny.firebaseapp.login.LogFrag;
import com.example.danny.firebaseapp.post.PostUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmenBuscarByLocation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmenBuscarByLocation extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseArrayDesc firebaseArray;
    private AdapterPostBusc adapterPostBusc;
    private RecyclerView recycler;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Query query;

    public FragmenBuscarByLocation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmenBuscarByLocation.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmenBuscarByLocation newInstance(String param1, String param2) {
        FragmenBuscarByLocation fragment = new FragmenBuscarByLocation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public void setAdapAndArray(FirebaseArrayDesc f, String uri){

        this.firebaseArray = f;
        this.mParam1 =  uri;


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
         View root = inflater.inflate(R.layout.fragment_fragmen_buscar_by_location, container, false);

        recycler = (RecyclerView)root.findViewById(R.id.buscarbylocation_fragment);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("postCarExpress");
        query = ref.child("pices");

        new TareaAsincrona().execute(mParam1);
        recycler.addOnItemTouchListener(new ReciclerViewClikListener(getContext(), recycler, new ReciclerViewClikListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FragmentStock m = (FragmentStock) getFragmentManager().findFragmentById(R.id.fragmen_stock);
                m = FragmentStock.newInstance("", "", ((PostUsers) adapterPostBusc.getItem(position)),R.id.activity_findBy);


                if(FirebaseAuth.getInstance().getCurrentUser() != null) {

                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fadein,R.anim.fadeout)
                            .replace(R.id.activity_findBy, m)
                            .addToBackStack("")
                            .commit();
                }else{
                    LogFrag l = (LogFrag) getFragmentManager().findFragmentById(R.id.log_frag);
                    if (l == null) {
                        l = LogFrag.newInstance();

                        getFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.fadein, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.activity_findBy, l)
                                .addToBackStack("")
                                .commit();
                    }
                }
                // adapterPostBusc.clear();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));



        return root;
    }


    private class TareaAsincrona extends AsyncTask<String,Void,Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (adapterPostBusc != null & adapterPostBusc.getItemCount() > 0) {
                recycler.setHasFixedSize(true);
                recycler.setLayoutManager(new LinearLayoutManager(getContext()));


                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                recycler.setLayoutManager(layoutManager);
                recycler.setAdapter(adapterPostBusc);


            } else {

                getFragmentManager().popBackStack();
                EmptyFragment eM = new EmptyFragment();
                if (eM != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                            .add(R.id.activity_findBy, eM)
                            .addToBackStack("")
                            .commit();


                }

            }
        }

        @Override
        protected Void doInBackground(String... params) {


            FirebaseArrayDesc  firebaseArray2 = new FirebaseArrayDesc(query,params[0]);

            List<DataSnapshot> lista = firebaseArray.mSnapshots;
            for (DataSnapshot listo: lista){
                PostUsers p = listo.getValue(PostUsers.class);
                String [] c = params[0].split(" ");
                String [] g  = listo.getValue(PostUsers.class).getToFind().split(" ");
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




            adapterPostBusc = new AdapterPostBusc<PostUsers, FindByLocation.PostViewHolder>(
                    PostUsers.class, R.layout.item_post_cardview, FindByLocation.PostViewHolder.class, firebaseArray2) {

                public void populateViewHolder(FindByLocation.PostViewHolder chatMessageViewHolder, PostUsers post, int position) {
                    String s = post.getToFind();

                    Glide.with(getContext())
                            .load(post.getPic_one())
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


            return null;
        }
    }

}
