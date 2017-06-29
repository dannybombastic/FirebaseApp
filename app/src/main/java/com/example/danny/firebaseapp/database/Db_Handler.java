package com.example.danny.firebaseapp.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.util.Printer;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.Wall.ComentariosUsuarios;
import com.example.danny.firebaseapp.post.PostInterarctor;
import com.example.danny.firebaseapp.post.PostUsers;
import com.example.danny.firebaseapp.storage.StorageFirebase;
import com.example.danny.firebaseapp.usuarios.Users;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

;

/**
 * Created by danny on 07/01/2017.
 */

public class Db_Handler {
    RequestQueue queue;
   public String id ="vacio_id";
    public static String PATH = null;
    public static  String NAME = "Usuario";
    public static String EMAIL = "Email";
    private String PIC = "Foto";
    public DatabaseReference myRef_pic;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseDatabase dataBase;
    private ValueEventListener user_name;
    private ValueEventListener user_pic;
    private Query query;
    private final String URL_LOGIN = "http://dannybombastic.esy.es/admin/gmc/v1/user/login";
    private final String URL_TOKEN = "http://dannybombastic.esy.es/admin/gmc/v1/user/";
    private Context ctx;
   private boolean banderaRepeat = false;

              // constructor
    public Db_Handler(Context ctx , FirebaseAuth mAuth,FirebaseDatabase dataBase){
        this.ctx = ctx;

        if(mAuth != null && dataBase != null){
            this.dataBase = dataBase;
            this.mAuth = mAuth;
            this.dataBase = dataBase;

        }
        // Instantiate the cache
        Cache cache = new DiskBasedCache(ctx.getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        if (queue  == null) {
            queue = new RequestQueue(cache,network);
            queue.start();
        }

           }


           //  actualizamos el campo field con el valor de value
    public  void selectIdChild(String email, final String field, final String value, final SharedPreferences.Editor editor){
        myRef = FirebaseDatabase.getInstance().getReference("pinkies");
        query=  myRef.child("users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren().iterator().hasNext()){

                    DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                    String key = nodeDataSnapshot.getKey(); // this key
                    String path = "/" + dataSnapshot.getKey() + "/" + key;
                    myRef.child(path).child(field).setValue(value);
                    editor.putString("child",path).apply();
                    PATH = path;

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    synchronized public  boolean selectIdChildRepeat(final Users user){
        myRef = FirebaseDatabase.getInstance().getReference("pinkies");
        query=  myRef.child("users").orderByChild("email").equalTo(user.getEmail());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(!dataSnapshot.getChildren().iterator().hasNext()) {
                    userGmail(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return banderaRepeat;
    }



    public void getName(final String email, final ImageView view, final TextView name, final TextView mail, final SharedPreferences.Editor editor){

        myRef =FirebaseDatabase.getInstance().getReference("pinkies");

        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("count-fire",""+dataSnapshot.getChildrenCount());
                Users us = new Users(email, null,null);
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if( postSnapshot.getValue(Users.class)instanceof Users) {
                        Users a = postSnapshot.getValue(Users.class);
                           if(a != null && us != null) {
                               if (us.equals(a)) {
                                   us.setEmail(a.getEmail());
                                   us.setName(a.getName());
                                   us.setPic(a.getPic());
                                   PATH = postSnapshot.getKey();
                                   continue;
                               }
                           }else {
                               return;
                           }
                    }
                }


                   // adding statics values to statics variables
                NAME = us.getName();
                EMAIL = us.getEmail();
                PIC = us.getPic();
                FirebaseActivity.USER_NAME = NAME;
                FirebaseActivity.USER_EMAIL = EMAIL;
                FirebaseActivity.USER_PIC = PIC;
                editor.putString("name",NAME).apply();

                editor.putString("pic_user",PIC).apply();

                name.setText(us.getName());
                if(us.getEmail().contains("@")){
                    mail.setText(us.getEmail());
                    editor.putString("email",EMAIL).apply();
                }else{
                    mail.setText("Login With FaceBook");

                }

                Glide.with(ctx.getApplicationContext())
                        .load(us.getPic())
                        .asBitmap()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.circle_logo)
                        .centerCrop()
                        .into(new BitmapImageViewTarget(view) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                view.setImageDrawable(circularBitmapDrawable);
                            }
                        });

           }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

                Log.w("DB-CLL", "Failed to read value."+ error.getDetails());
            }

        });

    }





            // to get the root node of a User
    public void pathUser(String email){
        myRef = FirebaseDatabase.getInstance().getReference("pinkies");
        query=  myRef.child("users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.getChildren().iterator().hasNext()) {
                   DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();  // iterador
                   String key = nodeDataSnapshot.getKey(); // this key
                   String path = "/" + dataSnapshot.getKey() + "/" + key;

                   PATH = dataSnapshot.getKey();

                getLocation();
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

     public static  void addComentario(String coment_string,String email_user,ComentariosUsuarios coment){

         coment.setArea(coment_string);
         coment.setUser(email_user);
         FirebaseDatabase.getInstance().getReference("comentuser").push().setValue(coment);
     }

    public void ubicacion(Location loc){

        if(PATH.length() > 4){
            this.myRef = FirebaseDatabase.getInstance().getReference("users");
            this.myRef = myRef.child(PATH);
            GeoFire geoLoc = new GeoFire(myRef);
        geoLoc.setLocation(FirebaseActivity.USER_NAME,new GeoLocation(loc.getLatitude(),loc.getLongitude()));
        }
    }


    //  method to obtein the location of a specific user
    public void getLocation(){
      if(PATH != null) {
          this.myRef = FirebaseDatabase.getInstance().getReference("users");

          this.myRef = myRef.child(PATH);
          GeoFire geoFire = new GeoFire(myRef);
          geoFire.getLocation(FirebaseActivity.USER_NAME, new LocationCallback() {
              @Override
              public void onLocationResult(String key, GeoLocation location) {
                  if (location != null) {

                      Toast.makeText(ctx.getApplicationContext(), "Location" + key + "lat :" + location.latitude + "long:" + location.longitude, Toast.LENGTH_LONG).show();
                  } else {
                      Toast.makeText(ctx.getApplicationContext(), "no location found", Toast.LENGTH_SHORT).show();
                  }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });
      }
    }

boolean bandera = false;
    public  boolean updateChild(final String name, final String email , final SharedPreferences.Editor editor){
        myRef = FirebaseDatabase.getInstance().getReference("pinkies");
        query=  myRef.child("users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                String key = nodeDataSnapshot.getKey(); // this key
                String path = "/" + dataSnapshot.getKey() + "/" + key;



                     myRef.child(path).child("name").setValue(name);
                     myRef.child(path).child("email").setValue(email);
                     editor.putString("name", name).apply();
                     editor.putString("email", email).apply();
                     editor.putString("child", path).apply();
                     FirebaseActivity.USER_NAME = name;
                     FirebaseActivity.USER_EMAIL = email;
                     PATH = path;
                     bandera = true;


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                bandera = false;
            }
        });
        return bandera;
    }


     boolean ban = false;
   public boolean changeEmailPassword(final String email, final String password, String oldPassword){

       final FirebaseUser user;
       user = FirebaseAuth.getInstance().getCurrentUser();

       AuthCredential credential = EmailAuthProvider.getCredential(FirebaseActivity.USER_EMAIL,oldPassword);

       user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   ban =true;
                   user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(!task.isSuccessful()){
                               Toast.makeText(ctx.getApplicationContext(),"Try again Password",Toast.LENGTH_SHORT).show();
                                ban = false;
                           }else {
                               ban= true;
                               Toast.makeText(ctx.getApplicationContext(),"Update password success",Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
                   user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(!task.isSuccessful()){
                               Toast.makeText(ctx.getApplicationContext(),"Try again email",Toast.LENGTH_SHORT).show();

                           }else {
                               ban = true;
                               Toast.makeText(ctx.getApplicationContext(),"Update email success",Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }else {
                        ban = false;
                   Toast.makeText(ctx.getApplicationContext(),"Autentificate fail",Toast.LENGTH_SHORT).show();
               }
           }
       });
       return ban;
   }

    boolean b  = false;
   synchronized public boolean uploadPost(String price,String localidad, String pic_one, String pic_two, String pic_three, String email,String telefono, boolean comb, String desc,String year,String toFind){


       PostUsers post = new PostUsers();
       post.setDesc(desc);
       post.setPrice(price);
       post.setLocalidad(localidad);
       post.setPic_one(pic_one);
       post.setPic_two(pic_two);
       post.setMarca(FirebaseActivity.USER_PIC);
       post.setMotor(PATH);
       post.setPic_three(pic_three);
       post.setEmail(email);
       post.setComb(comb);
       post.setToFind(toFind);
       post.setTel(telefono);
       post.setYear(year);

       post.setName(FirebaseActivity.USER_NAME);
       FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference myRef = database.getReference("postCarExpress");

       myRef.child("pices").push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               b = true;
           }

       });

     return b;
   }





    public void setNAME(String NAME) {
        Db_Handler.NAME = NAME;
    }

    public void setEMAIL(String EMAIL) {
        Db_Handler.EMAIL = EMAIL;
    }

    public void setPIC(String PIC) {
        this.PIC = PIC;
    }

    public String getNAME() {
        return NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public String getPIC() {
        return PIC;
    }






    @Override
    protected void finalize() throws Throwable {
        super.finalize();
         if(myRef!=null){myRef.removeEventListener(user_name);}
        if (query!=null){query.removeEventListener(user_pic);}

    }


    public void userGmail(Users user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("pinkies");
            myRef.child("users").push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ctx.getApplicationContext(), "Update user success  :", Toast.LENGTH_SHORT).show();
                    }
                }
            });

             singUserServer(user.getName(),user.getEmail(),URL_LOGIN);

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
                            sendTokenServer(URL_TOKEN+id, FirebaseInstanceId.getInstance().getToken());
                            Log.d("token-sent", "enviado" +" id:"+ id + "/"+URL_TOKEN+id  );



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
}
