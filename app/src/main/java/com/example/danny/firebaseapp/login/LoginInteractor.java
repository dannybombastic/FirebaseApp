package com.example.danny.firebaseapp.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.example.danny.firebaseapp.FirebaseActivity;

import com.example.danny.firebaseapp.usuarios.Users;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Interactor del login
 */
public class LoginInteractor {

    private final Context mContext;
    private FirebaseAuth mFirebaseAuth;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public LoginInteractor(Activity context, FirebaseAuth firebaseAuth) {
        mContext = context;

        sharedPref = context.getSharedPreferences("fire", Context.MODE_PRIVATE);
        sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if (firebaseAuth != null) {
            mFirebaseAuth = firebaseAuth;
        } else {
            throw new RuntimeException("La instancia de FirebaseAuth no puede ser null");
        }
    }



    public boolean login(String email, String password, final Callback callback) {
        // Check lógica
        boolean c1 = isValidEmail(email, callback);
        boolean c2 = isValidPassword(password, callback);
        if (!(c1 && c2)) {
            return false;
        }

        // Check red
        if (!isNetworkAvailable()) {
            callback.onNetworkConnectFailed();
            return false;
        }

        // Check Google Play Service
        if (!isGooglePlayServicesAvailable(callback)) {
            return false;
        }

        // Consultar Firebase Authentication
        return signInUser(email, password, callback);





    }

    private boolean isValidEmail(String email, Callback callback) {
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

    private boolean isValidPassword(String password, Callback callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(password)) {
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

    private boolean isGooglePlayServicesAvailable(Callback callback) {
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
    boolean bandera = false;
    private boolean signInUser(final String email, String password, final Callback callback) {

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            callback.onAuthFailed(task.getException().getMessage());
                            FirebaseActivity.USER_LOG =false;
                            editor.putBoolean("log",FirebaseActivity.USER_LOG).apply();
                            editor.putBoolean("nosotros",false).apply();
                        } else {
                            callback.onAuthSuccess();

                            FirebaseActivity.USER_LOG =true;
                            editor.putBoolean("log",FirebaseActivity.USER_LOG).apply();
                            editor.putString("email",email).apply();
                            editor.putBoolean("nosotros",true).apply();
                            FirebaseActivity.USER_EMAIL = email;

                            bandera =true ;

                        }
                    }
                });
       return bandera;
    }

    interface Callback {

        void onEmailError(String msg);

        void onPasswordError(String msg);

        void onNetworkConnectFailed();

        void onBeUserResolvableError(int errorCode);

        void onGooglePlayServicesFailed();

        void onAuthFailed(String msg);

        void onAuthSuccess();
    }

}
