package com.example.danny.firebaseapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.danny.firebaseapp.notifications.PushNotificationsFragment;
import com.example.danny.firebaseapp.sing_in.FragmentSingIn;
import com.example.danny.firebaseapp.sing_in.SinInteractor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.danny.firebaseapp.FirebaseActivity.JSON;

public class MyService extends FirebaseInstanceIdService  {


    static String token_id = "vacio";

    public MyService() {



    }



    @Override
    public void onTokenRefresh() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message_id");
        myRef.setValue(token_id);



    }

   static public void tokenInterna(final CalltokenBack call){
       final Timer timer = new Timer();



                   timer.schedule(new TimerTask(){

                       private FirebaseAuth mAuth;
                       private FirebaseAuth.AuthStateListener mAuthListener;
                       @Override
                       public void run() {
                           mAuth = FirebaseAuth.getInstance();
                           mAuthListener = new FirebaseAuth.AuthStateListener() {
                               @Override
                               public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                   FirebaseUser user = firebaseAuth.getCurrentUser();
                                   if (user != null) {
                                       // User is signed in
                                      timer.cancel();
                                   } else {
                                       token_id = FirebaseInstanceId.getInstance().getToken();
                                       call.getToken(token_id);

                                   }

                               }
                           };

                                 mAuth.addAuthStateListener(mAuthListener);



                       }

                   }, 3000, 2500);






    }







    public interface CalltokenBack {
        void getToken(String token);

    }





}
