package com.example.danny.firebaseapp.sing_in;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by danny on 24/12/2016.
 */

public class Sing_presenter  implements SingInContract.Presenter ,SinInteractor.CallbackSingIn{

        private final SingInContract.View mSingView;
        private SinInteractor sinInteractor;

    public Sing_presenter(@NonNull SingInContract.View sinGinView,@NonNull SinInteractor sinInteracor){

        this.mSingView = sinGinView;
        sinGinView.setPresenter(this);
       this.sinInteractor = sinInteracor;

    }






    @Override
    public void onNameError(String msg) {
        mSingView.showProgress(true);
        mSingView.setNameError(msg);

    }

    @Override
    public void onEmailError(String msg) {
        mSingView.showProgress(true);
        mSingView.setEmailError(msg);
    }

    @Override
    public void onPasswordError(String msg) {
         mSingView.showProgress(true);
         mSingView.setPasswordError(msg);
    }

    @Override
    public void onNetworkConnectFailed() {
        mSingView.showProgress(true);
        mSingView.showNetworkError();

    }

    @Override
    public void onBeUserResolvableError(int errorCode) {
        mSingView.showProgress(true);
        mSingView.showGooglePlayServicesDialog(errorCode);
    }

    @Override
    public void onGooglePlayServicesFailed() {
        mSingView.showProgress(true);
        mSingView.showGooglePlayServicesError();

    }

    @Override
    public void onAuthFailed(String msg) {
       mSingView.showProgress(true);
        mSingView.showLoginError(msg);
    }

    @Override
    public void onAuthSuccess() {
        mSingView.showProgress(true);
         mSingView.showPushNotifications();
    }

    @Override
    public void start(Context context) {
        //if (FirebaseAuth.getInstance().getCurrentUser() == null) {
           // context.startActivity(new Intent(context, FirebaseActivity.class));

       // }


    }

    @Override
    public void attemptLogin(String email, String password,String name) {
        mSingView.showProgress(true);
        sinInteractor.singIN(name,email,password,this);

    }
}
