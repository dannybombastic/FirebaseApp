package com.example.danny.firebaseapp.sing_in;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.Login;
import com.example.danny.firebaseapp.MyService;
import com.example.danny.firebaseapp.login.LoginInteractor;
import com.example.danny.firebaseapp.usuarios.Users;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Delayed;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


/**
 * Created by danny on 24/12/2016.
 */

public class SinInteractor implements MyService.CalltokenBack{

    private BroadcastReceiver mNotificationsReceiver;
    public static final String ACTION_NOTIFY_NEW_TOKEN = "NOTIFY_NEW_TOKEN";
    private String id ="vacio_id";
    private final String URL_LOGIN = "http://dannybombastic.esy.es/admin/gmc/v1/user/login";
    private final String URL_TOKEN = "http://dannybombastic.esy.es/admin/gmc/v1/user/";
    RequestQueue queue;
    private final Activity mContext;
    private FirebaseAuth mFiresbaseAuth;
    private String token;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public SinInteractor(Activity ctx, FirebaseAuth firebaseAuth) {
       this.mContext = ctx;
        sharedPref = ctx.getSharedPreferences("fire", Context.MODE_PRIVATE);
        sharedPref = ctx.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        if (firebaseAuth != null) {
            this.mFiresbaseAuth = firebaseAuth;


            }else {
            throw new RuntimeException("Firebase Authorization es null ");
        }
             MyService.tokenInterna(this);
// Instantiate the cache
        Cache cache = new DiskBasedCache(ctx.getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        if (queue  == null) {
            queue = new RequestQueue(cache,network);
            queue.start();
                  }

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

    }

    public void singIN(String name, String mail, String password, final CallbackSingIn callback) {

        boolean n = isValidName(name,callback);
        boolean e = isValidEmail(mail,callback);
        boolean p = isValidPassword(password,callback);

       if(!(n && e && p)){
           return;
       }
        // Check red
        if (!isNetworkAvailable()) {
            callback.onNetworkConnectFailed();
            return;
        }

        // Check Google Play Service
        if (!isGooglePlayServicesAvailable(callback)) {
            return;
        }


        // Consultar Firebase Authentication
        signInUser(mail, password, name,callback);



    }

    private boolean isValidEmail(String email, SinInteractor.CallbackSingIn callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            callback.onEmailError("Escribe tu correo");
            isValid = false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback.onEmailError("Correo no válido");
            isValid = false;
        }
        // Más reglas de negocio...
        return isValid;
    }

    public boolean isValidName(String name,SinInteractor.CallbackSingIn callback){
        boolean isvalid = true;
        if(TextUtils.isEmpty(name)){

            callback.onNameError("Debes introducir un nombre");
            isvalid = false;
        }

        return isvalid;

    }
    public boolean isValidPassword(String password,SinInteractor.CallbackSingIn callback){
        boolean isvalid = true;
        if(TextUtils.isEmpty(password)){

            callback.onPasswordError("Obligatorio password de seguridad");
            isvalid = false;
        }

        if(password.length() < 9){
            callback.onPasswordError("Obligatorio nueve digitos de seguridad");
            isvalid = false;

        }

        return isvalid;

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connMgr =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable(SinInteractor.CallbackSingIn callback) {
        int statusCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(mContext);

        if (GoogleApiAvailability.getInstance().isUserResolvableError(statusCode)) {
            callback.onBeUserResolvableError(statusCode);
            return false;
        } else if (statusCode != ConnectionResult.SUCCESS) {
            callback.onGooglePlayServicesFailed();
            return false;
        }

        return true;
    }

    private void signInUser(final String email, String password, final String name, final SinInteractor.CallbackSingIn callback) {
       final String nombre = name;
        mFiresbaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            callback.onAuthFailed(task.getException().getMessage());
                            FirebaseActivity.USER_LOG = false;
                            editor.putBoolean("log",FirebaseActivity.USER_LOG).apply();
                            editor.putBoolean("nosotros",false).apply();

                        } else {

                            callback.onAuthSuccess();
                            singUserServer(nombre,email,URL_LOGIN);
                            Log.d("ids",token+id);
                            FirebaseActivity.USER_LOG = true;
                            editor.putBoolean("log",FirebaseActivity.USER_LOG);
                            editor.putString("name", name).apply();
                            editor.putString("email",email).apply();
                            editor.putBoolean("nosotros",true).apply();
                            FirebaseActivity.USER_EMAIL = email;
                            FirebaseActivity.USER_NAME = name;
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("pinkies");
                            Users user = new Users(email,name,"no apto");

                            myRef.child("users").push().setValue(user);

                          /*   mFiresbaseAuth.sendPasswordResetEmail(email);
                            if(FirebaseAuth.getInstance().getCurrentUser() !=null) {
                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                            }
                            */
                        }
                    }
                });
    }



   private void singUserServer(final String name, final String mail, final String url) {
       StringRequest postRequest = new StringRequest(Request.Method.POST, url,
               new Response.Listener<String>()
               {

                   @Override
                   public void onResponse(String response) {
                       // response
                       try {
                           JSONObject obj = new JSONObject(new String(response));
                           obj = obj.getJSONObject("user");
                           id = obj.getString("user_id");
                           Log.d("token-sent", "enviado" + token +" id:"+ id + "/"+URL_TOKEN+id  );
                         sendTokenServer(URL_TOKEN+id,token);


                           Log.d("Response", obj.getString("user_id"));
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               },
               new Response.ErrorListener()
               {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       // error
                       NetworkResponse response = error.networkResponse;
                       if (error instanceof ServerError && response != null) {
                           try {
                               String res = new String(response.data,
                                       HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                               // Now you can use any deserializer to make sense of data
                               JSONObject obj = new JSONObject(res);

                               obj = obj.getJSONObject("user");
                               Log.d("errorserver",obj.getString("user"));

                           } catch (UnsupportedEncodingException e1) {
                               // Couldn't properly decode data to string
                               e1.printStackTrace();
                           } catch (JSONException e2) {
                               // returned data is not JSONObject?
                               e2.printStackTrace();
                           }
                       }
                   }
               }
       ) {



           @Override
           protected Map<String, String> getParams()
           {
               Map<String, String>  params = new HashMap<String, String>();
               params.put("email", mail);
               params.put("name",name);
                 Log.d("parametros :",name+"  "+mail);
               return params;
           }

           @Override
           public Map<String, String> getHeaders() throws AuthFailureError {
               HashMap<String, String> headers = new HashMap<String, String>();
               // headers.put("from-data", "application/json; charset=utf-8");
               return headers;
           }
       };

       queue.add(postRequest);


        }

    private void sendTokenServer(final String url, final String token) {

        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response) {
                        // response


                            Log.d("respuesta-token", response);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);


                                Log.d("errorSent-Token",obj.toString());

                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                    }
                }
        ) {



           @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("gcm_registration_id",token);


                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("from-data", "application/json; charset=utf-8");
                return headers;
            }


        };

        queue.add(putRequest);






    }

    @Override
    public void getToken(String token) {
        this.token = token;
        Log.d("put-token :",token);
    }


    interface CallbackSingIn {

            void onNameError(String msg);

            void onEmailError(String msg);

            void onPasswordError(String msg);

            void onNetworkConnectFailed();

            void onBeUserResolvableError(int errorCode);

            void onGooglePlayServicesFailed();

            void onAuthFailed(String msg);

            void onAuthSuccess();
        }

    class Tarea extends AsyncTask<String,String,Void>{
       final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client;
        @Override
        protected void onPreExecute() {
           client = new OkHttpClient();
        }
        @Override
        protected Void doInBackground(String... params) {
            token = params[0];
            id = params[1];






                if(token!=null) {

                    try {
                       String a =  post(URL_TOKEN+id, token);
                        Log.d("token-sent", "enviado" + token +" id:"+ id + "/"+URL_TOKEN+id + a );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }





            return  null;
        }

       String post(String url, String json) throws IOException {


                RequestBody  body = new FormBody.Builder()
                        .addEncoded("Content-Type", "application/x-www-form-urlencoded")
                        .add("gcm_ragistration_id", json)
                        .build();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    //.addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .url(url)
                    .put(body)
                    .build();
            okhttp3.Response response = client.newCall(request).execute();

                if(response.isSuccessful()){

                    return response.body().string();

                }else{

                    return "respuesta vacia";
                }







        }
    }




    }
