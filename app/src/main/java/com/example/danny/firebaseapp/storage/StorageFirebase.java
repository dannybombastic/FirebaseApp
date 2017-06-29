package com.example.danny.firebaseapp.storage;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.danny.firebaseapp.FirebaseActivity;
import com.example.danny.firebaseapp.R;
import com.example.danny.firebaseapp.post.ExifUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by danny on 29/12/2016.
 */

public class StorageFirebase extends BaseStorageService{
    public static final String ACTION_UPLOAD_BYTE = "action_upload_byte";
    public static final String ACTION_UPLOAD_DATA = "action_upload_data";
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";
    private static final int NOTIF_ID_DOWNLOAD = 0;

    /** Intent Extras **/
    public static final String EXTRA_FILE_URI_BYTE = "extra_file_uri_byte";
    public static final String EXTRA_FILE_URI_DATA = "extra_file_uri_data";
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";

    boolean flag = false;
    private static final String TAG =StorageFirebase.class.getSimpleName() ;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String name = "";
    public Uri downloadUrl;
    double progress = 0;

    public StorageFirebase(){


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ACTION_UPLOAD.equals(intent.getAction())) {
           Uri uri = intent.getParcelableExtra(EXTRA_FILE_URI);




            upLoadFile(uri);
        }else if(ACTION_UPLOAD_DATA.equals(intent.getAction())){

               upLoadData((Intent) intent.getParcelableExtra(EXTRA_FILE_URI_DATA));
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-app-6af5c.appspot.com");

      /// mStorageRef.child("images/rivers.jpg");

    }

    public void upLoadFile(final Uri uri){

        Bitmap b  = null;

        try {
            InputStream i = this.getContentResolver().openInputStream(uri);
            b  = BitmapFactory.decodeStream(i);
            b =  ExifUtil.rotateBitmap(getRealPathFromURI(uri.toString()),b);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.WEBP, 20, baos);


            datas = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StorageReference photoRef = mStorageRef.child("images/"+uri.getPath());

            taskStarted();
            showUploadProgressNotification();

                UploadTask uploadTask = photoRef.putBytes(datas);


             uploadTask
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                        Uri  downloadUri =  taskSnapshot.getDownloadUrl();
                            broadcastUploadFinished(downloadUri, uri);
                            showUploadFinishedNotification(downloadUri, uri);
                            taskCompleted();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Log.d("fallo",""+exception.getMessage());
                            broadcastUploadFinished(null, uri);
                            showUploadFinishedNotification(null, uri);
                            taskCompleted();
                        }
                    });





    }

    byte[] datas ;
    public void upLoadData(final Intent data){
        try {
            ExifInterface exif = new ExifInterface(getRealPathFromURI(data.getData().toString()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        final StorageReference photoRef = mStorageRef.child("images/"+data.getData());

        taskStarted();
        showUploadProgressNotification();
        try {
            InputStream i = this.getContentResolver().openInputStream(data.getData());
            Bitmap b  = decodeUri(this.getContentResolver(),data.getData());

            b =   ExifUtil.rotateBitmap(getRealPathFromURI(data.getData().toString()),b);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.WEBP, 20, baos);

             datas = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UploadTask uploadTask = photoRef.putBytes(datas);


        uploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri  downloadUri =  taskSnapshot.getDownloadUrl();
                        broadcastUploadFinished(downloadUri, data.getData());
                        showUploadFinishedNotification(downloadUri, data.getData());
                        taskCompleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.d("fallo",""+exception.getMessage());
                        broadcastUploadFinished(null, data.getData());
                        showUploadFinishedNotification(null, data.getData());
                        taskCompleted();
                    }
                });





    }




    private boolean broadcastUploadFinished(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
        boolean success = downloadUrl != null;

        String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;

        Intent broadcast = new Intent(action)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl.toString())
                .putExtra(EXTRA_FILE_URI, fileUri.toString());
        return LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(broadcast);
    }







    private void showUploadProgressNotification() {


        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.vonoku)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Uploading...")
                .setProgress(0, 0, true)
                .setOngoing(true)
                .setAutoCancel(true);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(NOTIF_ID_DOWNLOAD, builder.build());
    }

    private void showUploadFinishedNotification(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
        // Make Intent to MainActivity
        Intent intent = new Intent(this, FirebaseActivity.class)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Make PendingIntent for notification
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* requestCode */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Set message and icon based on success or failure
        boolean success = downloadUrl != null;
        String message = success ? "Upload finished" : "Upload failed";
        int icon = success ? R.drawable.ic_menu_gallery : R.drawable.ic_menu_gallery;

       NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(NOTIF_ID_DOWNLOAD, builder.build());
    }


    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);

        return filter;
    }




    public static Bitmap decodeUri(final ContentResolver resolver, final Uri uri) throws IOException {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = false;
        opt.inMutable = true;
        return BitmapFactory.decodeStream(resolver.openInputStream(uri), null, opt);
    }


    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }





}
