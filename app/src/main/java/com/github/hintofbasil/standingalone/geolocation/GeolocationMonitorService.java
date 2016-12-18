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
import android.util.Log;

public class GeolocationMonitorService extends Service implements LocationListener {

    // Distance (in meters) from target that still triggers event.
    public static final int DISTANCE_DELTA = 1;

    private LocationManager locationManager;
    private Location[] locations;

    public GeolocationMonitorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("GeolocationMonitorServi", "GeolocationMonitorService started.");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            Log.i("GeolocationMonitorServi", "Registered for location updates");
        } catch (SecurityException e) {
            Log.e("GeolocationMonitorServi", "Unable to register for location updates\n" + Log.getStackTraceString(e));
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("GeolocationMonitorServi", "GeolocationMonitorService stopped.");
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            Log.e("GeolocationMonitorServi", "Unable to unregister for location updates\n" + Log.getStackTraceString(e));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("GeolocationMonitorServi", "New location detected " + location);
    }

    private boolean checkIfAtNextLocation() {
        return false;
    }

    private Location[] getLocations() {
        if (locations == null) {
            boolean isDebuggable = (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
            if (isDebuggable) {
                locations = new TestLocationsFactory().getLocations();
            } else {
                locations = new LocationsFactory().getLocations();
            }
        }
        return locations;
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
