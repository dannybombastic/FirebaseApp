package com.example.danny.firebaseapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class ServiceMsg extends FirebaseMessagingService {
    private static final String TAG = MyService.class.getSimpleName();
     Bitmap bit;
    public ServiceMsg() {
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "¡Mensaje recibido!");
        displayNotification(remoteMessage.getNotification(), remoteMessage.getData());
        sendNewPromoBroadcast(remoteMessage);

      /*  if (remoteMessage.getData().size() > 0) {
            Log.d("mensaje", "Message data payload: " + remoteMessage.getData().toString());

            Intent i = new Intent("NOTIFICATION");
            i.putExtra("mensaje",remoteMessage.getData().toString());
            getApplicationContext().sendBroadcast(i);

        }
        if (remoteMessage.getNotification() != null) {
            Log.d("notification", "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }
*/

    }

    @Override
    public void onSendError(String s, Exception e) {
            super.onSendError(s, e);

        e.printStackTrace();

    }





    private void sendNewPromoBroadcast(RemoteMessage remoteMessage) {
        Intent intent = new Intent(FirebaseActivity.ACTION_NOTIFY_NEW_PROMO);
        intent.putExtra("title", remoteMessage.getNotification().getTitle());
        intent.putExtra("description", remoteMessage.getNotification().getBody());
        intent.putExtra("expiry_date", remoteMessage.getData().get("expiry_date"));
        intent.putExtra("discount", remoteMessage.getData().get("discount"));
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(intent);
    }


    static int i = 0;
    private void displayNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(FirebaseActivity.class);

        Intent intent = new Intent(getApplicationContext(), FirebaseActivity.class);
        stackBuilder.addNextIntent(intent);

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_menu_gallery);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(notification.getTitle());
        builder.setContentText(notification.getBody());
        builder.setSound(defaultSoundUri);
        builder.setAutoCancel(true);

        builder.setPriority(android.support.v7.app.NotificationCompat.PRIORITY_MAX);







        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_gallery)
                .setContentIntent(pendingIntent)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(01, builder.build());
    }

    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth) {
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeigth / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

}
