package com.example.danny.firebaseapp.login;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.danny.firebaseapp.Login;

/**
 * Presentador del login
 */
public class LoginPresenter implements LoginContract.Presenter, LoginInteractor.Callback {

    private final LoginContract.View mLoginView;
    private LoginInteractor mLoginInteractor;

    public LoginPresenter(@NonNull LoginContract.View loginView,
                          @NonNull LoginInteractor loginInteractor) {
        mLoginView = loginView;
        loginView.setPresenter(this);
        mLoginInteractor = loginInteractor;
    }

    @Override
    public void start(Context context) {
        // Comprobar si el usuario está logueado
    }

    @Override
    public boolean attemptLogin(String email, String password) {
        mLoginView.showProgress(true);

        return  mLoginInteractor.login(email, password, this);
    }

    @Override
    public void onEmailError(String msg) {
        mLoginView.showProgress(false);
        mLoginView.setEmailError(msg);
    }

    @Override
    public void onPasswordError(String msg) {
        mLoginView.showProgress(false);
        mLoginView.setPasswordError(msg);
    }

    @Override
    public void onAuthSuccess() {
//        mLoginView.showPushNotifications();
    }

    @Override
    public void onAuthFailed(String msg) {
        mLoginView.showProgress(false);
        mLoginView.showLoginError(msg);
    }

    @Override
    public void onBeUserResolvableError(int errorCode) {
        mLoginView.showProgress(false);
        mLoginView.showGooglePlayServicesDialog(errorCode);
    }

    @Override
    public void onGooglePlayServicesFailed() {
        mLoginView.showGooglePlayServicesError();
    }

    @Override
    public void onNetworkConnectFailed() {
        mLoginView.showProgress(false);
        mLoginView.showNetworkError();
    }


}
