package com.example.danny.firebaseapp.Wall;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danny.firebaseapp.FindByLocation.BuscarByLocation;
import com.example.danny.firebaseapp.FindByLocation.FindByLocation;
import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.MyStock.FragmentStock;
import com.example.danny.firebaseapp.MyStock.FullScreenVendy;
import com.example.danny.firebaseapp.MyStock.MyStock;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.SpalnshFragmentList;
import com.example.danny.firebaseapp.login.LogFrag;
import com.example.danny.firebaseapp.login.LoginContract;
import com.example.danny.firebaseapp.post.PostForm;
import com.example.danny.firebaseapp.post.PostUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainListParts.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainListParts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainListParts extends Fragment {



   public static class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView iamgen;
        TextView desc;
        TextView price;
        TextView email;


        public PostViewHolder(View itemView) {
            super(itemView);
            desc = (TextView)itemView.findViewById(R.id.descCard);
            price = (TextView)itemView.findViewById(R.id.price_item);
            email = (TextView)itemView.findViewById(R.id.email_item);
            iamgen = (ImageView) itemView.findViewById(R.id.artImage);


        }
    }



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String adap = "0";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AdapterPostBusc adapterPostBusc2;
    Query query;
    DatabaseReference ref;
    private RecyclerView recycler;
    private AdapterPost<PostUsers, MainListParts.PostViewHolder> adapterPostBusc;
    private FirebaseArray firebaseArray;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab;
    private FragmentStock m;
    private SpalnshFragmentList vFragment;
    private FirebaseArrayDesc  firebaseArray2;


    private OnFragmentInteractionListener mListener;


    public static MainListParts newInstance() {
        MainListParts fragment = new MainListParts();
        // Setup de argumentos en caso de que los haya
        return fragment;
    }
    public MainListParts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainListParts.
     */
    // TODO: Rename and change types and number of parameters
    public static MainListParts newInstance(SpalnshFragmentList param1, String param2) {
        MainListParts fragment = new MainListParts();
        Bundle args = new Bundle();

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainListParts.OnFragmentInteractionListener) {
            mListener = (MainListParts.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_main_list_parts, container, false);
     //  gridView = (android.support.v7.widget.StaggeredGridLayoutManager) root.findViewById(R.id.staggeredGridView1);
        fab = (FloatingActionButton)root.findViewById(R.id.fab_fragment_main_list);
        recycler =(RecyclerView) root.findViewById(R.id.recyclerView_fragment);



         ref = FirebaseDatabase.getInstance().getReference("postCarExpress");
         query = ref.child("pices");
         firebaseArray = new FirebaseArray(query);

        new Adress2().execute("1");





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseActivity.USER_LOG) {
                    Intent i = new Intent(getContext(),PostForm.class);

                    startActivity(i);
                }else{
                    LogFrag l = (LogFrag) getFragmentManager().findFragmentById(R.id.log_frag);
                    if(l == null){
                        l = LogFrag.newInstance();
                        getFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                                .add(R.id.marco,l)
                                .addToBackStack("")
                                .commit();
                    }
                }
            }
        });

        recycler.addOnItemTouchListener(new ReciclerViewClikListener(getActivity(), recycler, new ReciclerViewClikListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                m = (FragmentStock) getFragmentManager().findFragmentById(R.id.fragmen_stock);
                PostUsers postUsers = new PostUsers();

                if(adap.equals("1")){
                    postUsers = (PostUsers) adapterPostBusc.getItem(position);
                }else{
                    postUsers = (PostUsers) adapterPostBusc2.getItem(position);
                }
                m = FragmentStock.newInstance("", "", postUsers,R.id.fragmentList);

               if(FirebaseAuth.getInstance().getCurrentUser() != null) {

                   getFragmentManager()
                           .beginTransaction()
                           .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                           .add(R.id.fragmentList, m)
                           .addToBackStack("")
                           .commit();
               }else {

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

    public void dummyMethod(){
        mListener.splashResponse();
    }

public void lista(){

    recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    adapterPostBusc = new  AdapterPost<PostUsers, MainListParts.PostViewHolder>(
            PostUsers.class,R.layout.item_post_cardview, MainListParts.PostViewHolder.class, firebaseArray) {

        public void populateViewHolder(MainListParts.PostViewHolder chatMessageViewHolder, PostUsers post, int position) {
            String s = post.getToFind();

            Glide.with(getActivity())
                    .load(post.getPic_one())
                    .asBitmap()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(VectorDrawableCompat.create(getResources(),R.drawable.vendynloading,null))
                    .centerCrop()
                    .into((chatMessageViewHolder.iamgen));
            chatMessageViewHolder.price.setText(post.getPrice() + "€");
            chatMessageViewHolder.desc.setText(post.getYear());


            mSnapshots.getItem(chatMessageViewHolder.getAdapterPosition()).getKey();

        }
    };


    recycler.setAdapter(adapterPostBusc);

    layoutManager = new GridLayoutManager(getActivity(), 2);

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
        void splashResponse();
    }

     class Adress2 extends AsyncTask<String,String,Void> {
        public String parametro = " ";


         @Override
         protected void onPreExecute() {


             vFragment = (SpalnshFragmentList)getFragmentManager().findFragmentById(R.id.splashStart);
             vFragment = SpalnshFragmentList.newInstance("splash","");

             getFragmentManager()
                     .beginTransaction()
                     .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                     .add(R.id.marco, vFragment)
                     .addToBackStack("hola")
                     .commit();

             super.onPreExecute();
         }

         @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

           if(parametro.equals("1")) {
               layoutManager = new GridLayoutManager(getActivity(), 2);
               recycler.setHasFixedSize(true);
               recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
               recycler.setLayoutManager(layoutManager);

               recycler.setAdapter(adapterPostBusc);
               mListener.splashResponse();
           }

           if(parametro.equals("2")){
               layoutManager = new GridLayoutManager(getActivity(), 2);
               recycler.setHasFixedSize(true);
               recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
               recycler.setLayoutManager(layoutManager);

               recycler.setAdapter(adapterPostBusc2);
               mListener.splashResponse();
           }

             new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                 @Override
                 public void run() {

                      if( vFragment.getFragmentManager() != null){   vFragment.getFragmentManager().popBackStack();}


                 }
             }, 2000);


        }

        @Override
        protected  Void doInBackground(String... params) {
            if(params[0].equalsIgnoreCase("1")) {
                parametro = params[0];
                adap = params[0];
                adapterPostBusc = new  AdapterPost<PostUsers, MainListParts.PostViewHolder>(
                        PostUsers.class,R.layout.item_post_cardview, MainListParts.PostViewHolder.class, firebaseArray) {

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
                                .placeholder(R.mipmap.circle_logo)
                                .centerCrop()
                                .into((chatMessageViewHolder.iamgen));
                        chatMessageViewHolder.price.setText(post.getPrice() + "€");
                        chatMessageViewHolder.desc.setText(post.getYear());


                        mSnapshots.getItem(chatMessageViewHolder.getAdapterPosition()).getKey();

                    }
                };




            }else if(params[0]=="2"){
                parametro = params[0];
                adap = params[0];
                 firebaseArray2 = new FirebaseArrayDesc(query,params[1]);
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
                 firebaseArray2 = null;
            }



            return null;
        }
    }




    public void comunicate(String comu) {

        new Adress2().execute("2",comu);
        //firebaseArray2.cleanup();
    }

  public void mainList(){
      new Adress2().execute("1");
  }




}
