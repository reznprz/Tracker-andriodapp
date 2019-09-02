package com.example.macbookpro.tracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by macbookpro on 4/3/18.
 */

public class MyService extends Service {


    @Override
    public void onCreate() {
        final Intent intent=new Intent();
        intent.setAction("com.example.broadcast.MY_NOTIFICATION");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Log.e("....", "sleep");
                        //Checks for update every 3 minutes.
                        Thread.sleep(180000);
                    } catch (InterruptedException e) {
                        // Restore interrupt status.
                        Thread.currentThread().interrupt();
                    }
                    // Stop the service using the startId, so that we don't stop
                    // the service in the middle of handling another jo
                    sendBroadcast(intent);
                }
            }
        });
        thread.start();


    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

}
