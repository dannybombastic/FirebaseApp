package com.example.danny.firebaseapp.sing_in;

import com.example.danny.firebaseapp.BasePresenter;
import com.example.danny.firebaseapp.BaseView;

/**
 * Created by danny on 24/12/2016.
 */

public interface SingInContract {
    interface View extends BaseView<Presenter> {
        void showProgress(boolean show);

        void setEmailError(String error);

        void setNameError(String error);

        void setPasswordError(String error);

        void showLoginError(String msg);

        void showPushNotifications();

        void showGooglePlayServicesDialog(int errorCode);

        void showGooglePlayServicesError();

        void showNetworkError();

    }

    interface Presenter extends BasePresenter {
        void attemptLogin(String email, String password,String name);
    }
}
