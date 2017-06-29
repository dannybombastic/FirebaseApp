package com.example.danny.firebaseapp.FindByLocation;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.MyStock.FragmentStock;
import com.example.danny.firebaseapp.MyStock.FullScreenVendy;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.Wall.AdapterPostBusc;
import com.example.danny.firebaseapp.Wall.FirebaseArrayDesc;
import com.example.danny.firebaseapp.Wall.ReciclerViewClikListener;
import com.example.danny.firebaseapp.Wall.Wall;
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

public class FindByLocation extends AppCompatActivity implements FragmentStock.OnFragmentInteractionListener,BuscarByLocation.OnFragmentInteractionListenerBy,FullScreenVendy.OnFragmentInteractionListener{



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

  private   String busqueda;
  private   RecyclerView recycler;
  private   AdapterPostBusc<PostUsers, FindByLocation.PostViewHolder> adapterPostBusc;
  private   FirebaseArrayDesc firebaseArray;
  private   RecyclerView.LayoutManager layoutManager;
  private   Query query;
  private   FragmentStock m;
   private BuscarByLocation l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_by_location);

        //   final AdapterPost<PostUsers, PostViewHolder> adapter;

        getSupportActionBar().setTitle("");
        busqueda = FirebaseActivity.USER_LOCALITY;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("postCarExpress");
        query = ref.child("pices");
        //  final FirebaseArray firebaseArray = new FirebaseArray(query);

         Toast.makeText(getApplicationContext(),busqueda,Toast.LENGTH_LONG).show();


        recycler = (RecyclerView) findViewById(R.id.recyclerView_locality);




              new Adress2().execute();
                recycler.addOnItemTouchListener(new ReciclerViewClikListener(getParent(), recycler, new ReciclerViewClikListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        m = (FragmentStock) getSupportFragmentManager().findFragmentById(R.id.fragmen_stock);
                        m = FragmentStock.newInstance("", "", (adapterPostBusc.getItem(position)),R.id.activity_findBy);


                        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.fadein,R.anim.fadeout)
                                    .replace(R.id.activity_findBy, m)
                                    .addToBackStack("")
                                    .commit();
                        }else{
                            LogFrag l = (LogFrag) getSupportFragmentManager().findFragmentById(R.id.log_frag);
                            if (l == null) {
                                l = LogFrag.newInstance();

                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
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





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            l = (BuscarByLocation) getSupportFragmentManager().findFragmentById(R.id.log_frag);
            l = BuscarByLocation.newInstance("", "");

    if (l != null) {


        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .add(R.id.activity_findBy, l)
                .addToBackStack("")
                .commit();

    }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.firebase, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public class Adress2 extends AsyncTask<String,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            recycler.setHasFixedSize(true);

            layoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(adapterPostBusc);

        }

        @Override
        protected  Void doInBackground(String... params) {

            busqueda = busqueda.replace("á","a");
            busqueda = busqueda.replace("é","e");
            busqueda = busqueda.replace("í","i");
            busqueda = busqueda.replace("ó","o");
            busqueda = busqueda.replace("ú","u");
            firebaseArray = new FirebaseArrayDesc(query,busqueda);

                adapterPostBusc = new AdapterPostBusc<PostUsers, FindByLocation.PostViewHolder>(
                        PostUsers.class, R.layout.item_post_cardview, FindByLocation.PostViewHolder.class, firebaseArray) {

                    public void populateViewHolder(FindByLocation.PostViewHolder chatMessageViewHolder, PostUsers post, int position) {
                        String s = post.getToFind();
                        String pic = post.getPic_one();
                        if(pic ==null){
                            pic = post.getPic_two();
                            if(pic==null){
                                pic = post.getPic_three();
                            }
                        }
                        Glide.with(getApplicationContext())
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







            return null;
        }
    }

    @Override
    public void onFragmentInteractionby(String uri) {


        FragmenBuscarByLocation fr = (FragmenBuscarByLocation) getSupportFragmentManager().findFragmentById(R.id.frag_by_location_busqueda);
        fr = FragmenBuscarByLocation.newInstance(uri,uri);
        fr.setAdapAndArray(firebaseArray,uri);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .replace(R.id.activity_findBy, fr)
                .addToBackStack("")
                .commit();
        l = null;

    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }



}
