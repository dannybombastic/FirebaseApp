package com.example.danny.firebaseapp.Wall;

import android.content.Context;
import android.net.Uri;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danny.firebaseapp.MyStock.FragmentStock;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.login.LogFrag;
import com.example.danny.firebaseapp.post.PostUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMainPartBuscar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMainPartBuscar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMainPartBuscar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AdapterPostBusc adapterPostBusc2;
    FirebaseArrayDesc  firebaseArray2;
    private Query query;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference ref;
    RecyclerView recycler;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentMainPartBuscar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMainPartBuscar.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMainPartBuscar newInstance(String param1, String param2) {
        FragmentMainPartBuscar fragment = new FragmentMainPartBuscar();
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

        View root = inflater.inflate(R.layout.fragment_fragment_main_part_buscar, container, false);
        recycler = (RecyclerView)root.findViewById(R.id.mainlistbuscar);
        ref = FirebaseDatabase.getInstance().getReference("postCarExpress");
        query = ref.child("pices");
        new TareaAsincrona().execute("1",mParam1);

        recycler.addOnItemTouchListener(new ReciclerViewClikListener(getActivity(), recycler, new ReciclerViewClikListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                FragmentStock m = (FragmentStock) getFragmentManager().findFragmentById(R.id.fragmen_stock);
                PostUsers postUsers = new PostUsers();


                postUsers = (PostUsers) adapterPostBusc2.getItem(position);

                m = FragmentStock.newInstance("", "", postUsers, R.id.marco);

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                            .add(R.id.marco, m)
                            .addToBackStack("")
                            .commit();
                } else {

                    LogFrag l = (LogFrag) getFragmentManager().findFragmentById(R.id.log_frag);
                    if (l == null) {
                        l = LogFrag.newInstance();

                        getFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.marco, l)
                                .addToBackStack("")
                                .commit();
                    }


                }
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
    private class TareaAsincrona extends AsyncTask<String,String,Void>{
        public String parametro = " ";

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(getContext(),"adapter count"+adapterPostBusc2.getItemCount()+mParam1,Toast.LENGTH_SHORT).show();


            if(adapterPostBusc2.getItemCount() >= 0) {

                layoutManager = new GridLayoutManager(getActivity(), 2);
                layoutManager.offsetChildrenVertical(20);
                recycler.setHasFixedSize(true);
                recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                recycler.setLayoutManager(layoutManager);
                recycler.setAdapter(adapterPostBusc2);


            }else {

                         getFragmentManager().popBackStack();
                     EmptyFragment eM = new EmptyFragment();
                        if(eM != null){
                            getFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                                    .add(R.id.marco, eM)
                                    .addToBackStack("")
                                    .commit();

                        }

            }
        }

        @Override
        protected Void doInBackground(String... params) {
            if(params[0].equalsIgnoreCase("1")) {



                adapterPostBusc2 = new AdapterPostBusc<PostUsers, MainListParts.PostViewHolder>(
                        PostUsers.class, R.layout.item_post_cardview, MainListParts.PostViewHolder.class, firebaseArray2) {

                    public void populateViewHolder(MainListParts.PostViewHolder chatMessageViewHolder, PostUsers post, int position) {
                        String s = post.getToFind();
                        String pic = post.getPic_one();
                        if(pic ==null){
                            pic = post.getPic_two();
                            if(pic==null){
                                pic = post.getPic_three();
                            }
                        }
                        Glide.with(getActivity())
                                .load(pic)
                                .asBitmap()
                                .thumbnail(0.5f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(VectorDrawableCompat.create(getResources(),R.drawable.vendyn,null))
                                .centerCrop()
                                .into(chatMessageViewHolder.iamgen);
                        chatMessageViewHolder.price.setText(post.getPrice() + "€");
                        chatMessageViewHolder.desc.setText(post.getYear());


                        mSnapshots.getItem(chatMessageViewHolder.getAdapterPosition()).getKey();

                    }
                };





        }
            return null;
    }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           firebaseArray2 = new FirebaseArrayDesc(query,mParam1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firebaseArray2.cleanup();
    }
}