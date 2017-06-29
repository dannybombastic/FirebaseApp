package com.example.danny.firebaseapp.notifications;

import android.content.Context;
import android.text.TextUtils;

import com.example.danny.firebaseapp.data.PushNotification;
import com.example.danny.firebaseapp.data.PushNotificationsRepository;
import com.example.danny.firebaseapp.login.LoginInteractor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

/**
 * Presentador de las notificaciones
 */
public class PushNotificationsPresenter implements PushNotificationContract.Presenter {
    private Context context;
    private  FirebaseMessaging mFCMInteractor = null;

    private final PushNotificationContract.View mNotificationView;

    private  LoginInteractor log_int = null;



    public PushNotificationsPresenter(PushNotificationContract.View notificationView,
                                      LoginInteractor interactor) {
        mNotificationView = notificationView;


        notificationView.setPresenter(this);
        log_int = interactor;
    }

    public PushNotificationsPresenter(PushNotificationContract.View notificationView,
                                      FirebaseMessaging interactor) {
        mNotificationView = notificationView;


        notificationView.setPresenter(this);
        mFCMInteractor = interactor;
    }






    @Override
    public void start(Context context) {
        this.context = context;
        registerAppClient();

        PushNotificationsRepository.getInstance(context).cargarMapa();
        loadNotifications();

    }

    @Override
    public void registerAppClient() {
        mFCMInteractor.subscribeToTopic("promos");
    }

    @Override
    public void loadNotifications() {
        PushNotificationsRepository.getInstance(this.context).getPushNotifications(
                new PushNotificationsRepository.LoadCallback() {
                    @Override
                    public void onLoaded(ArrayList<PushNotification> notifications) {
                        PushNotificationsRepository.getInstance(context).cargarMapa();
                        if (notifications.size() > 0) {
                            mNotificationView.showEmptyState(false);
                            mNotificationView.showNotifications(notifications);
                        } else {
                            mNotificationView.showEmptyState(true);
                        }
                    }
                }
        );
    }

    @Override
    public void savePushMessage(String title, String description,
                                String expiryDate, String discount) {
        PushNotification pushMessage = new PushNotification();

        pushMessage.setTitle(title);
        pushMessage.setDescription(description);
        pushMessage.setExpiryDate(expiryDate);
        pushMessage.setDiscount(TextUtils.isEmpty(discount) ? 0 : Float.parseFloat(discount));

        PushNotificationsRepository.getInstance(this.context).savePushNotification(pushMessage);
        PushNotificationsRepository.getInstance(this.context).cargarMapa();


        mNotificationView.showEmptyState(false);
        mNotificationView.popPushNotification(pushMessage);
    }


}
