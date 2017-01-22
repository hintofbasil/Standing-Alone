package com.github.hintofbasil.standingalone.geolocation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class GeolocationMonitorService extends Service implements LocationListener {

    private LocationManager locationManager;
    private boolean isDebuggable;

    public GeolocationMonitorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("GeolocationMonitorServi", "GeolocationMonitorService started.");

        isDebuggable = (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
        if (isDebuggable) {
            Log.d("GeolocationMonitorServi", "Debug mode enabled");
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            if (isDebuggable) {
                // Limit to 2s min time to avoid toast spam
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
            Log.i("GeolocationMonitorServi", "Registered for location updates");
        } catch (SecurityException e) {
            Log.e("GeolocationMonitorServi", "Unable to register for location updates\n" + Log.getStackTraceString(e));
        }



        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            locationManager.removeUpdates(this);
            Log.d("GeolocationMonitorServi", "GeolocationMonitorService stopped.");
        } catch (SecurityException e) {
            Log.e("GeolocationMonitorServi", "Unable to unregister for location updates\n" + Log.getStackTraceString(e));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("GeolocationMonitorServi", "New location detected " + location);
        Intent locationFoundIntent = new Intent("custom");
        locationFoundIntent.putExtra("location", location);
        LocalBroadcastManager.getInstance(this).sendBroadcast(locationFoundIntent);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
