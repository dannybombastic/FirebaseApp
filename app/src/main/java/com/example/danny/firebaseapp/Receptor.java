package com.example.danny.firebaseapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Receptor extends BroadcastReceiver {
    String mensaje = " ";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        mensaje = intent.getStringExtra("mensaje");
        Toast.makeText(context," broad cast : "+mensaje,Toast.LENGTH_LONG).show();
    }
}
