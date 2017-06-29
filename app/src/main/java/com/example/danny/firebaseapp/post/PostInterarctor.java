package com.example.danny.firebaseapp.post;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.example.danny.firebaseapp.database.Db_Handler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vspc-PoisonRiver on 14/02/2017.
 */

public class PostInterarctor {

    private String marca;
    private String year;
    private String price;
    private Context ctx;
    private FirebaseAuth fireAuth;
    private FirebaseDatabase fireDB;
    private Db_Handler db;

    public PostInterarctor(){


    }

    public PostInterarctor(Context ctx,Db_Handler db, FirebaseAuth fireAuth, FirebaseDatabase fireDB){
          this.ctx = ctx;
          this.db = new Db_Handler(ctx,fireAuth,fireDB);
          this.fireAuth = fireAuth;
          this.fireDB = fireDB;




    }

    public boolean  postWall(String price,String localidad, String pic_one, String pic_two, String pic_three, String email,String telefono, boolean comb, String desc,String title,String toFind,PostCallBack callBack){

        boolean y = isTitle(title,callBack);

        boolean p = isValidPrice(price,callBack);
        boolean d = isValidSesc(desc,callBack);
        boolean f = isValidTel(telefono,callBack);
        if(!(y && p && d && f)){
            return false;
        }

        // Check red
        if (!isNetworkAvailable()) {
            callBack.onNetworkConnectFailed();
            return false;
        }

        // Check Google Play Service
        if (!isGooglePlayServicesAvailable(callBack)) {
            return false;

        }



                        callBack.onSuccess();
            return db.uploadPost(price,localidad,pic_one,pic_two,pic_three,email,telefono,comb,desc,title,toFind);



    }




    private boolean isTitle(String title, PostCallBack callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(title)) {
            callback.onTitle("Escribe una Titulo");
            isValid = false;
        }
        if( title.length() < 4 ){
            callback.onTitle("Debe facilitar un titulo");
            isValid = false;
        }
        return isValid;
    }
    private boolean isValidTel(String telefono, PostCallBack callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(telefono)) {
            callback.onYearTelf("Escribe una Telefono");
            isValid = false;
        }
        if( telefono.length() < 9 ){
            callback.onYearTelf("El telefono es erroneo");
            isValid = false;
        }
        return isValid;
    }
    private boolean isValidPrice(String price, PostCallBack callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(price)) {
            callback.onPiceError("Escribe tu Precio");
            isValid = false;
        }
        return isValid;
    }


    private boolean isValidSesc(String desc, PostCallBack callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(desc)) {
            callback.onDescError("Escribe una Descripcion");
            isValid = false;
        }

        if(desc.length() < 3){
            callback.onDescError("Debe de contener mas de 3 letras");
            isValid = false;
        }
        return isValid;
    }







    private boolean isNetworkAvailable() {
        ConnectivityManager connMgr =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable(PostCallBack callback) {
        int statusCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(ctx);

        if (GoogleApiAvailability.getInstance().isUserResolvableError(statusCode)) {
            callback.onBeUserResolvableError(statusCode);
            return false;
        } else if (statusCode != ConnectionResult.SUCCESS) {
            callback.onGooglePlayServicesFailed();
            return false;
        }

        return true;
    }




    public interface PostCallBack{
        void onNetworkConnectFailed();
        void onBeUserResolvableError(int errorCode);
        void onGooglePlayServicesFailed();
        void onTitle(String msg);
        void onYearTelf(String msg);
        void onPiceError(String msg);
        void onDescError(String msg);
        void onSuccess();


    }

}
