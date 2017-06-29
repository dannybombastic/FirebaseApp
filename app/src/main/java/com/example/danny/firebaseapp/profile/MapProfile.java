package com.example.danny.firebaseapp.profile;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.database.Db_Handler;
import com.example.danny.firebaseapp.fragmentPagerAdapter.MyAdapterViewPages;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapProfile extends AppCompatActivity implements MapLocation.OnFragmentInteractionListener,UpdateProfile.OnFragmentInteractionListener{


    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_profile);


             viewPager = (ViewPager)findViewById(R.id.viePager);
        List<Fragment> lista = new ArrayList<Fragment>();
        lista.add(UpdateProfile.newInstance("",""));
        lista.add(MapLocation.newInstance("",""));
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        MyAdapterViewPages adapterViewPages = new MyAdapterViewPages(getSupportFragmentManager(),lista);
        viewPager.setAdapter(adapterViewPages);
        getSupportActionBar().setTitle("");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(getApplicationContext(),"Your Location",Toast.LENGTH_SHORT).show();
          viewPager.setCurrentItem(1,true);

    }
}
