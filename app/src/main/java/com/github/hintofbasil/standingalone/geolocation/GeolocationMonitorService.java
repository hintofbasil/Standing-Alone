package com.github.hintofbasil.standingalone.geolocation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.github.hintofbasil.standingalone.LocationFoundActivity;
import com.github.hintofbasil.standingalone.LocationFoundEnum;
import com.github.hintofbasil.standingalone.R;

public class GeolocationMonitorService extends Service implements LocationListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    // Distance (in meters) from target that still triggers event.
    public static final int DISTANCE_DELTA = 2;

    private LocationManager locationManager;
    private Location[] locations;

    private int progress;
    private SharedPreferences sharedPreferences;

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
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
            Log.i("GeolocationMonitorServi", "Registered for location updates");
        } catch (SecurityException e) {
            Log.e("GeolocationMonitorServi", "Unable to register for location updates\n" + Log.getStackTraceString(e));
        }

        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        progress = sharedPreferences.getInt(getString(R.string.preferences_locations_found_key), 0);

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
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("GeolocationMonitorServi", "New location detected " + location);
        if (isAtNextLocation(location)) {
            String key = getString(R.string.preferences_locations_found_key);
            sharedPreferences.edit().putInt(key, progress + 1).apply();
            Log.d("GeolocationMonitorServi", "Next location found.  Progress increased by 1");

            // Toast is only used for testing until chat activity is implemented
            if (isDebuggable) {
                Toast.makeText(getApplicationContext(),
                        "Next location found",
                        Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(getApplicationContext(),
                    LocationFoundActivity.class);
            intent.putExtra(LocationFoundActivity.EXTRA_LOCATION_FOUND_PROGRESS, LocationFoundEnum.get(progress));
            startActivity(intent);
        }
    }

    private boolean isAtNextLocation(Location location) {
        Location[] locations = getLocations();
        Location nextLocation = locations[progress];
        if (isDebuggable) {
            Toast.makeText(getApplicationContext(),
                    "Distance: " + nextLocation.distanceTo(location),
                    Toast.LENGTH_SHORT).show();
        }
        return nextLocation.distanceTo(location) <= DISTANCE_DELTA;
    }

    private Location[] getLocations() {
        if (locations == null) {
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preferences_locations_found_key))) {
            progress = sharedPreferences.getInt(key, 0);
        }
        if (progress == 9) {
            Log.d("GeolocationMonitorServi", "Last location found.  Stopping monitor service");
            this.stopSelf();
        }
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
