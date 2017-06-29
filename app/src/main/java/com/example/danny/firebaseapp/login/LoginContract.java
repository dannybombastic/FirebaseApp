package com.example.danny.firebaseapp.login;


import com.example.danny.firebaseapp.BasePresenter;
import com.example.danny.firebaseapp.BaseView;

/**
 * Interacción MVP en Login
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void showProgress(boolean show);

        void setEmailError(String error);

        void setPasswordError(String error);

        void showLoginError(String msg);

        void showPushNotifications();

        void showGooglePlayServicesDialog(int errorCode);

        void showGooglePlayServicesError();

        void showNetworkError();
    }

    interface Presenter extends BasePresenter {
        boolean attemptLogin(String email, String password);
    }
}
