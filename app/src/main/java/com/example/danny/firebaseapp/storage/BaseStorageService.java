package com.example.danny.firebaseapp.storage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by danny on 29/12/2016.
 */

public class BaseStorageService extends Service{
    private static final String TAG = BaseStorageService.class.getCanonicalName();
    private int mNumTasks = 0;
    @Nullable
    @Override











    public IBinder onBind(Intent intent) {
        return null;
    }


    public void taskStarted() {
        changeNumberOfTasks(1);
    }

    public void taskCompleted() {
        changeNumberOfTasks(-1);
    }



    private synchronized void changeNumberOfTasks(int delta) {
        Log.d(TAG, "changeNumberOfTasks:" + mNumTasks + ":" + delta);
        mNumTasks += delta;

        // If there are no tasks left, stop the service
        if (mNumTasks <= 0) {
            Log.d(TAG, "stopping");
            stopSelf();
        }
    }
}
