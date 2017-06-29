package com.example.danny.firebaseapp.profile;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;

import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.database.Db_Handler;
import com.example.danny.firebaseapp.login.LoginInteractor;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by danny on 18/01/2017.
 */

public class ProfileInteractor {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String  name,email,password;
    private Activity mContext;
    Db_Handler db;



    public ProfileInteractor(Activity ctx, Db_Handler db, FirebaseAuth auth, FirebaseDatabase fDb){
        this.mContext = ctx;
        this.db = db;
        this.db = new Db_Handler(mContext,auth,fDb);

        sharedPref = mContext.getSharedPreferences("fire", Context.MODE_PRIVATE);
        sharedPref = mContext.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void updateProfile(String name,String email, String old,String password, ProfileCallBack callBack){

        boolean n = isValidName(name,callBack);
        boolean e = isValidEmail(email,callBack);
        boolean ol = isValidOldPassword(old,callBack);
        boolean nw = isValidPassword(password,callBack);


        if (!(n && e && ol && nw )) {
            return;
        }
        // Check red
        if (!isNetworkAvailable()) {
            callBack.onNetworkConnectFailed();
            return ;
        }

        // Check Google Play Service
        if (!isGooglePlayServicesAvailable(callBack)) {
            return ;
        }
           if(password.length() >5) {
               db.changeEmailPassword(email, password, old);
           }
        if(db.updateChild(name, FirebaseActivity.USER_EMAIL,editor)){
            callBack.onSuccess();
        }


    }



    private boolean isValidEmail(String email, ProfileCallBack callback) {
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
    private boolean isValidName(String name, ProfileCallBack callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(name)) {
            callback.onNameError("Escribe tu nombre");
            isValid = false;
        }
        return isValid;
    }
    private boolean isValidPassword(String password, ProfileCallBack callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(password) && password.length() > 8) {
            callback.onPasswordError("Escribe tu contraseña");
            isValid = false;
        }

        // Más reglas de negocio...
        return isValid;
    }
    private boolean isValidOldPassword(String password, ProfileCallBack callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(password) && password.length() > 8) {
            callback.onPasswordError("Escribe tu contraseña");
            isValid = false;
        }

        // Más reglas de negocio...
        return isValid;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connMgr =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable(ProfileCallBack callback) {
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



   public interface ProfileCallBack{
       void onNetworkConnectFailed();
       void onBeUserResolvableError(int errorCode);
       void onGooglePlayServicesFailed();
       void onEmailError(String msg);
       void onNameError(String msg);
       void onPasswordError(String msg);
       void onSuccess();


   }
}
