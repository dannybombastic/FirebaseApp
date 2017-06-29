package com.example.danny.firebaseapp;
import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.danny.firebaseapp.FindByLocation.BuscarByLocation;
import com.example.danny.firebaseapp.FindByLocation.FindByLocation;
import com.example.danny.firebaseapp.Location.MyLocation;
import com.example.danny.firebaseapp.MyStock.FragmentStock;
import com.example.danny.firebaseapp.MyStock.FullScreenVendy;
import com.example.danny.firebaseapp.MyStock.MyStock;
import com.example.danny.firebaseapp.Wall.FragmentMainPartBuscar;
import com.example.danny.firebaseapp.Wall.MainListParts;
import com.example.danny.firebaseapp.Wall.Wall;
import com.example.danny.firebaseapp.database.Db_Handler;
import com.example.danny.firebaseapp.login.LogFrag;
import com.example.danny.firebaseapp.login.LoginInteractor;
import com.example.danny.firebaseapp.login.LoginPresenter;
import com.example.danny.firebaseapp.notifications.PushNotificationsFragment;
import com.example.danny.firebaseapp.notifications.PushNotificationsPresenter;
import com.example.danny.firebaseapp.post.PostForm;
import com.example.danny.firebaseapp.profile.MapProfile;
import com.example.danny.firebaseapp.sing_in.FragmentSingIn;
import com.example.danny.firebaseapp.sing_in.SinInteractor;
import com.example.danny.firebaseapp.sing_in.Sing_presenter;
import com.example.danny.firebaseapp.storage.StorageFirebase;
import com.example.danny.firebaseapp.usuarios.Users;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




import okhttp3.MediaType;




public class FirebaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,Login.Callback,
        FragmentSingIn.CallbackSin,View.OnClickListener,MyLocation.OnLocalityReady,LogFrag.OnFragmentInteractionListener,MainListParts.OnFragmentInteractionListener,FragmentStock.OnFragmentInteractionListener,GoogleApiClient.OnConnectionFailedListener,FullScreenVendy.OnFragmentInteractionListener,BuscarByLocation.OnFragmentInteractionListenerBy,FragmentMainPartBuscar.OnFragmentInteractionListener{


    @IntDef({GRANTED, DENIED, BLOCKED_OR_NEVER_ASKED })
    public @interface PermissionStatus {}
    public BuscarByLocation by;
    private MainListParts main;
    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;
    public SpalnshFragmentList vFragment;
    int a = 0;


    String emailid, gender, bday, username;
    // googleapiClient
    private FragmentSingIn mSingInFragment;
    private GoogleApiClient mGoogleApiClient;
    CallbackManager mCallbackManager;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private ProgressBar mProgressDialog;
    private Uri mDownloadUrl = null;
    private Uri mFileUri = null;
    public static boolean USER_LOG = false;
    private Bundle bFacebookData;
    public static double LATITUD = 0d;
    public static double LONGITUD = 0d;


    public static String USER_NAME = "name";
    public static String USER_EMAIL = "email";
    public static String USER_PIC = "vacio";
    public static String USER_PASS = "659011563";
    public static String USER_LOCALITY = "default";


    private static final String LOGED = "LOGED";
    public static final String ACTION_NOTIFY_NEW_PROMO = "NOTIFY_NEW_PROMO";

    private static final int RC_TAKE_PICTURE = 101;
    public static final String KEY_DOWNLOAD_URL = "extra_download_url";
    private static final String KEY_FILE_URI = "key_file_uri";
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    // fire base references
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public DatabaseReference myRef;
    public static FirebaseDatabase dataBase;
    private BroadcastReceiver mBroadcastReceiver;
    private PushNotificationsFragment mNotificationsFragment;
    private PushNotificationsPresenter pushPresenter;

    public Login frag;
    private TextView email_header, name_header;
    private FloatingActionButton fab_header;
    Users u;
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor editor;
    boolean loged = false;
    boolean isUsreRegister = false;
    ImageView imageView;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;

    NavigationView navigationView;
    AccessTokenTracker accessTokenTracker;
    Db_Handler db;
    DrawerLayout drawer;
    View l;
    MyLocation my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        MultiDex.install(this);

        super.onCreate(savedInstanceState);

       // FacebookSdk.sdkInitialize(this.getApplicationContext());

      FacebookSdk.getApplicationContext();
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();

        }
        setContentView(R.layout.activity_firebase);
        sharedPref = getSharedPreferences("fire", Context.MODE_PRIVATE);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        dataBase = FirebaseDatabase.getInstance();
        myRef = dataBase.getReference();
        db = new Db_Handler(getApplicationContext(), mAuth, dataBase);

        Db_Handler.PATH = sharedPref.getString("child", "**");


        // googleclientoptios request
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addOnConnectionFailedListener(this)
                .build();



        isRegister();

             /// sh de facebook
      /*  PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.danny.firebaseapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }


*/


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // Do something for lollipop and above versions

            if(getPermissionStatus(this,Manifest.permission.CAMERA) == BLOCKED_OR_NEVER_ASKED){

                checkAndRequestPermissions();
            }else {
                   // Do something ...
            }


        }


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

/*
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer.openDrawer(GravityCompat.START);

            }
        }, 500);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawers();

            }
        }, 2000);

        */

        Toast.makeText(getApplicationContext(), FirebaseActivity.LATITUD + " " + FirebaseActivity.LONGITUD + " " + FirebaseActivity.USER_LOCALITY, Toast.LENGTH_LONG).show();

           // nav header
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final View v = navigationView.getHeaderView(0);
        navigationView.getMenu().getItem(0).setVisible(false);
        imageView = (ImageView) v.findViewById(R.id.pic_user);
        mProgressDialog = (ProgressBar) v.findViewById(R.id.progre2);
        email_header = (TextView) v.findViewById(R.id.email_header);
        name_header = (TextView) v.findViewById(R.id.name_header);
        fab_header = (FloatingActionButton) v.findViewById(R.id.fab_header);
        fab_header.setOnClickListener(this);





        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            loged = savedInstanceState.getBoolean(LOGED,false);
            Db_Handler.PATH = savedInstanceState.getString("path");
        }


        photoUser();


        //   mNotificationsFragment =
        //         (PushNotificationsFragment) getSupportFragmentManager()
        //   .findFragmentById(R.id.content_firebase);
        //    if (mNotificationsFragment == null) {
        //       mNotificationsFragment = PushNotificationsFragment.newInstance();
        //      getSupportFragmentManager()
        //   .beginTransaction()
        //               .replace(R.id.marco, mNotificationsFragment)
        //               .commit();
        //   }

        //  pushPresenter = new PushNotificationsPresenter(mNotificationsFragment,FirebaseMessaging.getInstance());



       main = (MainListParts) getSupportFragmentManager().findFragmentById(R.id.fragmentList);
        if (main == null) {
            main = MainListParts.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                    .add(R.id.marco, main)
                    .commit();
        }



        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {

                hideProgressDialog();
                Toast.makeText(getApplicationContext(), "broad cast:" + intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL) + intent.getStringExtra(StorageFirebase.EXTRA_FILE_URI), Toast.LENGTH_LONG).show();
                USER_PIC = intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL);

                db = new Db_Handler(getApplicationContext(), mAuth, dataBase);
                db.selectIdChild(USER_EMAIL, "pic", USER_PIC, editor);
                editor.putString("pic_user", intent.getStringExtra(StorageFirebase.EXTRA_DOWNLOAD_URL)).apply();
                photoUser();

                switch (intent.getAction()) {


                    case StorageFirebase.UPLOAD_COMPLETED:
                    case StorageFirebase.UPLOAD_ERROR:
                        onUploadResultIntent(intent);
                        break;
                }

            }
        };


    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getIdToken());
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed." + task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(getApplicationContext(), "Authentication task." + mAuth.getCurrentUser().getEmail(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {


                    }
                });
    }



    private void onUploadResultIntent(Intent intent) {
        // Got a new intent from MyUploadService with a success or failure
        mDownloadUrl = intent.getParcelableExtra(StorageFirebase.EXTRA_DOWNLOAD_URL);
        mFileUri = intent.getParcelableExtra(StorageFirebase.EXTRA_FILE_URI);


    }

    private void showProgressDialog() {

        mProgressDialog.setVisibility(ProgressBar.VISIBLE);
    }

    private void hideProgressDialog() {
        mProgressDialog.setVisibility(ProgressBar.INVISIBLE);
    }



    private void uploadFromUri(Uri fileUri) {
        Log.d("upload", "uploadFromUri:src:" + fileUri.toString());

        // Save the File URI
        mFileUri = fileUri;

        // Clear the last download, if any

        mDownloadUrl = null;

        // Toast message in case the user does not see the notificatio
        Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(new Intent(this, StorageFirebase.class)
                .putExtra(StorageFirebase.EXTRA_FILE_URI, fileUri)
                .setAction(StorageFirebase.ACTION_UPLOAD));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.firebase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(by==null) {
                if (USER_LOG) {
                    by = BuscarByLocation.newInstance("", "");

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                            .add(R.id.marco, by)
                            .addToBackStack("")
                            .commit();

                } else {
                    LogFrag l = (LogFrag) getSupportFragmentManager().findFragmentById(R.id.log_frag);
                    if (l == null) {
                        l = LogFrag.newInstance();

                        getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                                .replace(R.id.marco, l)
                                .addToBackStack("")
                                .commit();
                    }
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override              // navigation drawer
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

            if (USER_LOG) {

                Intent i = new Intent(getApplicationContext(), MapProfile.class);
                startActivity(i);
            } else {
                LogFrag l = (LogFrag) getSupportFragmentManager().findFragmentById(R.id.log_frag);
                if (l == null) {
                    l = LogFrag.newInstance();

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                            .replace(R.id.marco, l)
                            .addToBackStack("")
                            .commit();
                }
            }


        } else if (id == R.id.nav_vender) {

            if (USER_LOG) {
                Intent i = new Intent(getApplicationContext(), PostForm.class);
                startActivity(i);
            } else {

                LogFrag l = (LogFrag) getSupportFragmentManager().findFragmentById(R.id.log_frag);

                if (l == null) {

                    l = LogFrag.newInstance();

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                            .replace(R.id.marco, l)
                            .addToBackStack("")
                            .commit();
                }
            }

        } else if (id == R.id.nav_buscar) {

            if (USER_LOG) {
                Intent i = new Intent(getApplicationContext(), Wall.class);
                startActivity(i);
            } else {

                LogFrag l = (LogFrag) getSupportFragmentManager().findFragmentById(R.id.log_frag);

                if (l == null) {
                    l = LogFrag.newInstance();

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                            .replace(R.id.marco, l)
                            .addToBackStack("")
                            .commit();
                }
            }

        } else if (id == R.id.cosas) {


            if (USER_LOG) {
                Intent i = new Intent(getApplicationContext(), MyStock.class);
                startActivity(i);
            } else {

                LogFrag l = (LogFrag) getSupportFragmentManager().findFragmentById(R.id.log_frag);

                if (l == null) {

                    l = LogFrag.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fadein, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .replace(R.id.marco, l)
                            .addToBackStack("")
                            .commit();
                }
            }

        } else if (id == R.id.nav_share) {


        } else if (id == R.id.nav_log_out) {

            // log out user
            mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
            signOut();
            LoginManager.getInstance().logOut();
            // restore default values in sharePreferences
            editor.putString("name", this.getResources().getString(R.string.loginforname)).putString("email", this.getResources().getString(R.string.loginformail)).apply();
            editor.putString("pic_user", "**").apply();
            editor.putBoolean("log", false).apply();
            editor.putString("child", "**");
            editor.putBoolean("face", false).apply();
            editor.putBoolean("nosotros", false).apply();
            editor.putBoolean("gmail", false).apply();
            USER_LOG = false;
            photoUser();
            email_header.setText(this.getResources().getString(R.string.loginformail));
            name_header.setText(this.getResources().getString(R.string.loginforname));

        } else if (id == R.id.find_by) {

            if (USER_LOG) {
                Intent i = new Intent(getApplicationContext(), FindByLocation.class);
                startActivity(i);
            } else {

                LogFrag l = (LogFrag) getSupportFragmentManager().findFragmentById(R.id.log_frag);

                if (l == null) {
                    l = LogFrag.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fadein, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .replace(R.id.marco, l)
                            .addToBackStack("")
                            .commit();
                }
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == RC_TAKE_PICTURE) {

            if (resultCode == RESULT_OK) {
                mFileUri = data.getData();
                Log.d("uri-fire", mFileUri.toString());


                if (USER_LOG) {

                    Glide.with(this.getApplicationContext())
                            .load(mFileUri)
                            .centerCrop()
                            .crossFade()
                            .into(imageView);
                }


                if (mFileUri != null) {
                    uploadFromUri(mFileUri);
                    Toast.makeText(this, mFileUri.getPath(), Toast.LENGTH_LONG).show();
                } else {
                    Log.w("photo-fali", "File URI is null");
                }
            } else {
                hideProgressDialog();
                Toast.makeText(this, "Taking picture failed.", Toast.LENGTH_SHORT).show();
            }
        }
        if (mCallbackManager != null) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase

                GoogleSignInAccount account = result.getSignInAccount();

                firebaseAuthWithGoogle(account);

                u = new Users(account.getEmail(), account.getDisplayName(), account.getPhotoUrl().toString());
                editor.putBoolean("google", true).apply();

                Toast.makeText(getApplicationContext(), "start activiti on result ok ;" + isUsreRegister + " " + mAuth.getCurrentUser(), Toast.LENGTH_LONG).show();
            } else {

                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                Toast.makeText(getApplicationContext(), "no logeado " + data.getDataString(), Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
                editor.putBoolean("google", false).apply();
            }

        }


    }


              // not in use


    public void photoUser() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);


        Log.i("Url-foto", sharedPref.getString("pic_user", "**"));
        if (USER_LOG) {

            String s = sharedPref.getString("pic_user", "**");

            Glide.with(this.getApplicationContext())
                    .load(s)
                    .asBitmap()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(VectorDrawableCompat.create(getResources(), R.drawable.vendyn, null))
                    .centerCrop()
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });


        } else {

            Glide.with(this.getApplicationContext())
                    .load(R.drawable.vendyn)
                    .asBitmap()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(VectorDrawableCompat.create(getResources(), R.drawable.vendyn, null))
                    .fitCenter()
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });

        }

    }


    @Override
    public void onSaveInstanceState(Bundle out) {

        out.putParcelable(KEY_FILE_URI, mFileUri);
        out.putParcelable(KEY_DOWNLOAD_URL, mDownloadUrl);
        out.putBoolean(LOGED, loged);
        out.putString("path", Db_Handler.PATH);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
        mDownloadUrl = savedInstanceState.getParcelable(KEY_DOWNLOAD_URL);
        //   loged = savedInstanceState.getParcelable(LOGED);
        Db_Handler.PATH = (String) savedInstanceState.get("path");
        photoUser();
    }



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fab_header) {

            if (USER_LOG) {
                if (sharedPref.getBoolean("nosotros", false)) {
                    launchCamera();
                    showProgressDialog();
                }
            }
        }


    }

    private void launchCamera() {
        Log.d("camera-on", "launchCamera");

        // Pick an image from storage
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, RC_TAKE_PICTURE);
    }


    private void isRegister() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    USER_LOG = true;

                }
                if (user != null) {

                    if (sharedPref.getBoolean("nosotros", false)) {
                        USER_LOG = true;
                        Toast.makeText(getApplicationContext(), "Logeado con VendYn "+ user.getDisplayName(), Toast.LENGTH_LONG).show();

                        USER_EMAIL = user.getEmail();

                        USER_NAME = user.getDisplayName();


                        if(user.getPhotoUrl() != null){

                            USER_PIC = user.getPhotoUrl().toString();

                        }


                        db.getName(USER_EMAIL, imageView, name_header, email_header, editor);

                        if(USER_NAME != null){  db.pathUser(USER_EMAIL);}
                        editor.putString("name", USER_NAME).apply();
                        editor.putString("email", USER_EMAIL).apply();
                        editor.putString("pic_user", USER_PIC).apply();
                        editor.putBoolean("google", false).apply();
                        editor.putBoolean("face", false).apply();
                        editor.putBoolean("nosotros", true).apply();
                        editor.putBoolean("log", USER_LOG).apply();

                    }

                    if (sharedPref.getBoolean("google", false)) {
                        if (u != null) {
                            db.selectIdChildRepeat(u);
                            Toast.makeText(getApplicationContext(), "Logeado con GooGle ", Toast.LENGTH_LONG).show();
                            getSupportFragmentManager().popBackStack();
                            USER_LOG = true;
                            email_header.setText(u.getEmail());
                            name_header.setText(u.getName());
                            imageView.setImageURI(Uri.parse(u.getPic()));
                            USER_EMAIL = u.getEmail();
                            USER_NAME = u.getName();
                            USER_PIC = u.getPic();
                            db.pathUser(USER_EMAIL);
                            editor.putString("name", USER_NAME).apply();
                            editor.putString("email", USER_EMAIL).apply();
                            editor.putString("pic_user", USER_PIC).apply();
                            editor.putBoolean("google", true).apply();
                            editor.putBoolean("face", false).apply();
                            editor.putBoolean("nosotros", false).apply();
                            editor.putBoolean("log", USER_LOG).apply();


                        }
                        db.pathUser(USER_EMAIL);
                        editor.putBoolean("log", true).apply();
                        USER_EMAIL = sharedPref.getString("email", "**");
                        USER_NAME = sharedPref.getString("name", "**");
                        USER_PIC = sharedPref.getString("pic_user", "**");
                        db.getName(USER_EMAIL, imageView, name_header, email_header, editor);



                    }

                    if (sharedPref.getBoolean("face", false)) {

                        getSupportFragmentManager().popBackStack();
                        Toast.makeText(getApplicationContext(), "Logeado con FaceBook ", Toast.LENGTH_LONG).show();
                        db.selectIdChildRepeat(new Users(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString()));
                        USER_LOG = true;
                        email_header.setText(user.getEmail());
                        name_header.setText(user.getDisplayName());

                        USER_EMAIL = user.getUid();
                        USER_NAME = user.getDisplayName();
                        USER_PIC = user.getPhotoUrl().toString();
                        db.pathUser(USER_EMAIL);
                        editor.putString("name", USER_NAME).apply();
                        editor.putString("email", USER_EMAIL).apply();
                        editor.putString("pic_user", USER_PIC).apply();
                        editor.putBoolean("log", USER_LOG).apply();
                        editor.putBoolean("google", false).apply();
                        editor.putBoolean("face", true).apply();
                        editor.putBoolean("nosotros", false).apply();
                        db.getName(USER_EMAIL, imageView, name_header, email_header, editor);
                    }

                    if (my == null) {
                        my = new MyLocation(getApplicationContext(), db);
                        Toast.makeText(getApplicationContext(), " " + my.canGetLocation() + " loc" + my.getLongitude(), Toast.LENGTH_SHORT).show();
                    }

                    // User is signed in

                    Log.d("login", "onAuthStateChanged:signed_in:" + user.getUid());

                    photoUser();

                } else {
                   // mAuth.signOut();
                    Db_Handler.PATH = "**";
                    USER_LOG = false;
                    // User is signed out
                    editor.putBoolean("tel", false).apply();
                    editor.putString("telefono", "659...").apply();
                    Toast.makeText(getApplicationContext(), "no logeado", Toast.LENGTH_SHORT).show();
                    editor.putString("name", getResources().getString(R.string.loginforname)).putString("email", getResources().getString(R.string.loginformail)).apply();
                    editor.putString("pic_user", "**").apply();
                    editor.putBoolean("log", false).apply();
                    editor.putString("child", "**").apply();
                    USER_LOG = false;
                    photoUser();
                    email_header.setText(getResources().getString(R.string.loginformail));
                    name_header.setText(getResources().getString(R.string.loginforname));
                    editor.putBoolean("google", false).apply();
                    editor.putBoolean("face", false).apply();
                    editor.putBoolean("nosotros", false).apply();
                    LoginManager.getInstance().logOut();



                }
                // ...
            }
        };


    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkAndRequestPermissions() {

        int phoneCall = ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE);
        int mediaControl = ContextCompat.checkSelfPermission(this, Manifest.permission.MEDIA_CONTENT_CONTROL);
        int camera = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
        int extStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int win = ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (win != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SYSTEM_ALERT_WINDOW);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);

        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (extStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if(mediaControl != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.MEDIA_CONTENT_CONTROL);
        }
        if(camera != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if(phoneCall != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            return false;
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {

                    for (int i = 0; i == permissions.length ; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Snackbar.make(getCurrentFocus(), "Permission Granted, Now you can access location data. :" + 1, Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(getCurrentFocus(), "Permission Denied, You cannot access location data.", Snackbar.LENGTH_LONG).show();
                        }

                    }


                } else {

                    Snackbar.make(getCurrentFocus(), "Permission Denied, You cannot access location data.", Snackbar.LENGTH_LONG).show();

                }


                break;
        }
    }


 /// google client onConectionFailed
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }




    public void loginFacebook() {


        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        handleFacebookAccessToken(loginResult.getAccessToken());


                        editor.putBoolean("face", true).apply();


                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("LoginActivity", response.toString());
                                // Get facebook data from login
                                bFacebookData = getFacebookData(object);

                                Toast.makeText(getApplicationContext(), "onComplete datal", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,birthday"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();



                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(FirebaseActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                        editor.putBoolean("face", false).apply();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(FirebaseActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        editor.putBoolean("face", false).apply();

                    }
                });


    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        try {

            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("public_profile"))
                bundle.putString("public_profile", object.getString("public_profile"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
           return bundle;
        } catch (JSONException e) {
            Log.d(TAG, "Error parsing JSON");
        }
        return bundle;
    }






    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(FirebaseActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            editor.putBoolean("face",false).apply();
                        }else{


                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the

                            // signed in user can be handled in the listener.
                            Toast.makeText(FirebaseActivity.this, "Authentication succes."+task.getResult().getUser().getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            editor.putBoolean("face",true).apply();
                        }

                        // ...
                    }
                });
    }



    @Override
    public void onlocaliti(boolean localityReady) {
       navigationView.getMenu().getItem(0).setVisible(localityReady);

    }


    @PermissionStatus
    public static int getPermissionStatus(Activity activity, String androidPermissionName) {
        if(ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)){
                return BLOCKED_OR_NEVER_ASKED;
            }
            return DENIED;
        }
        return GRANTED;
    }



    @Override
    public void onFragmentInteraction(String uri) {


        switch (uri){
            case "reg":
                FragmentSingIn sing =
                        (FragmentSingIn) getSupportFragmentManager()
                                .findFragmentById(R.id.frag);
                if (sing == null) {
                    sing = FragmentSingIn.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .replace(R.id.marco, sing)
                            .commit();
                    Sing_presenter a = new Sing_presenter(sing, new SinInteractor(this, mAuth));
                }
                break;

              case "nos":

                Login l =(Login) getSupportFragmentManager().findFragmentById(R.id.frag);
                if (l == null) {
                    l = Login.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .replace(R.id.marco, l)
                            .commit();
                    LoginPresenter a = new LoginPresenter(l, new LoginInteractor(this, mAuth));
                }
                break;

            case "g":

                signIn();
                break;

            case "face":
                loginFacebook();
                break;
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    @Override
    public void onFragmentInteractionby(String uri) {
              Toast.makeText(getApplicationContext(),uri,Toast.LENGTH_SHORT).show();
        FragmentMainPartBuscar f = (FragmentMainPartBuscar)getSupportFragmentManager().findFragmentById(R.id.fragmainlistbuscar);
        f = FragmentMainPartBuscar.newInstance(uri,uri);
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.marco, f)
                .commit();
        by= null;
    }



    @Override
    public void splashResponse() {

         }

      // Activity cycle life
    @Override
    protected void onStart() {
        super.onStart();
      //  FacebookSdk.getApplicationContext();
        mAuth.addAuthStateListener(mAuthListener);


     //     USER_EMAIL = sharedPref.getString("email","**");
        db = new Db_Handler(getApplicationContext(), mAuth, dataBase);
     //   db.pathUser(USER_EMAIL);
      //  USER_NAME = sharedPref.getString("name","**");
        //    USER_PIC = sharedPref.getString("pic_user","**");

        Db_Handler.PATH = sharedPref.getString("child", "**");
        Db_Handler.EMAIL = sharedPref.getString("email", "**");
        if (my == null) {

            my = new MyLocation(this, db);
            Toast.makeText(getApplicationContext(), "puedo tener locacion " + my.canGetLocation() + " location :" + my.getLongitude() + " " + FirebaseActivity.USER_LOCALITY, Toast.LENGTH_SHORT).show();
            LATITUD = my.getLatitude();
            LONGITUD = my.getLongitude();
        }
        //  photoUser();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mBroadcastReceiver, StorageFirebase.getIntentFilter());

    }



    @Override
    protected void onPause() {
        super.onPause();
      //  FacebookSdk.getApplicationContext();

    }

    @Override
    protected void onResume() {
        super.onResume();
      //  FacebookSdk.getApplicationContext();
        //      if(vFragment != null) {
        //      vFragment.getFragmentManager().popBackStack();
        //
        //   }

      //  getFragmentManager().popBackStack("hola",0);
        photoUser();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
       // getFragmentManager().popBackStack();
    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(mAuthListener);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(StorageFirebase.EXTRA_DOWNLOAD_URL)) {
            onUploadResultIntent(intent);
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        event.startTracking();

        if(event.isLongPress()){

            this.finish();
        }

        if(keyCode == KeyEvent.KEYCODE_BACK) {



            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);


            } else {

                this.a++;
                if(this.a>=3){
                    Toast.makeText(getApplicationContext(),"Mantenga dos segundos para salir",Toast.LENGTH_SHORT).show();
                    this.a = 0;
                }

                getSupportFragmentManager().popBackStack();
                if(by!= null)by= null;
            }
        }
        return false;
    }




    @Override
    public void onInvokeGooglePlayServices(int codeError) {

        showPlayServicesErrorDialog(codeError);
    }

    void showPlayServicesErrorDialog(final int errorCode) {
        Dialog dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(
                        FirebaseActivity.this,
                        errorCode,
                        REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @Override
    public void inInvokeGooglePlayServices(int codeError) {
        showPlayServicesErrorDialog(codeError);
    }

}
