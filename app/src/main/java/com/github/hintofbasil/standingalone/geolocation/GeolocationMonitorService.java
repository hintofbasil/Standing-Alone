package com.github.hintofbasil.standingalone.geolocation;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class GeolocationMonitorService extends Service {

    public GeolocationMonitorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("GeolocationMonitorServi", "GeolocationMonitorService started.");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("GeolocationMonitorServi", "GeolocationMonitorService stopped.");
    }

    public class LocalBinder extends Binder {
        GeolocationMonitorService getService() {
            return GeolocationMonitorService.this;
        }
    }

    private final IBinder binder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
