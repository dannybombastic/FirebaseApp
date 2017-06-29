package com.example.danny.firebaseapp.notifications;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.data.PushNotification;
import com.example.danny.firebaseapp.data.PushNotificationsRepository;

import java.util.ArrayList;

/**
 * Muestra lista de notificaciones
 */
public class PushNotificationsFragment extends Fragment implements PushNotificationContract.View {

    public static final String ACTION_NOTIFY_NEW_PROMO = "NOTIFY_NEW_PROMO";
    private BroadcastReceiver mNotificationsReceiver;

    private RecyclerView mRecyclerView;
    private LinearLayout mNoMessagesView;
    private PushNotificationsAdapter mNotificatiosAdapter;

    private PushNotificationsPresenter mPresenter;


    public PushNotificationsFragment() {
    }

    public static PushNotificationsFragment newInstance() {
        PushNotificationsFragment fragment = new PushNotificationsFragment();
        // Setup de Argumentos
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Gets de argumentos
        }
        PushNotificationsRepository.getInstance(getActivity()).cargarMapa();
        mNotificationsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String title = intent.getStringExtra("title");
                String description = intent.getStringExtra("description");
                String expiryDate = intent.getStringExtra("expiry_date");
                String discount = intent.getStringExtra("discount");
                Toast.makeText(getActivity(),"recivido broad cast",Toast.LENGTH_LONG).show();
                mPresenter.savePushMessage(title, description, expiryDate, discount);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        mNotificatiosAdapter = new PushNotificationsAdapter();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_notifications_list);
        mNoMessagesView = (LinearLayout) root.findViewById(R.id.noMessages);
        mRecyclerView.setAdapter(mNotificatiosAdapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start(this.getContext());

        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mNotificationsReceiver, new IntentFilter(ACTION_NOTIFY_NEW_PROMO));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(mNotificationsReceiver);
    }

    @Override
    public void showNotifications(ArrayList<PushNotification> notifications) {
        mNotificatiosAdapter.replaceData(notifications);
    }

    @Override
    public void showEmptyState(boolean empty) {
        mRecyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        mNoMessagesView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void popPushNotification(PushNotification pushMessage) {
        mNotificatiosAdapter.addItem(pushMessage);
        PushNotificationsRepository.getInstance(getActivity()).cargarMapa();
    }

    @Override
    public void setPresenter(PushNotificationContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = (PushNotificationsPresenter) presenter;
        } else {
            throw new RuntimeException("El presenter de notificaciones no puede ser null");
        }
    }

}
