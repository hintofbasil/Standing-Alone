package com.github.hintofbasil.standingalone.geolocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.github.hintofbasil.standingalone.MapActivity;
import com.github.hintofbasil.standingalone.R;

/**
 * Created by will on 21/01/17.
 */
public class LocationBroadcastReceiver extends BroadcastReceiver {

    // Distance (in meters) from target that still triggers event.
    public static final int DISTANCE_DELTA = 12;

    private MapActivity mapActivity;
    private SharedPreferences sharedPreferences;
    private boolean isDebuggable;
    private Location[] locations;

    public LocationBroadcastReceiver(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
        sharedPreferences = mapActivity.getSharedPreferences(mapActivity.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);

        isDebuggable = (0 != (mapActivity.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
        if (isDebuggable) {
            Log.d("GeolocationMonitorServi", "Debug mode enabled");
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Location location = intent.getParcelableExtra("location");
        mapActivity.postponeErrorMessages(location);
        if (isAtNextLocation(location)) {
            String key = mapActivity.getString(R.string.preferences_locations_found_key);
            int progress = sharedPreferences.getInt(mapActivity.getString(R.string.preferences_locations_found_key), 0);
            sharedPreferences.edit().putInt(key, (progress + 1) % 11).apply();
            Log.d("GeolocationMonitorServi", "Next location found.  Progress increased by 1");

            // Toast is only used for testing until chat activity is implemented
            if (isDebuggable) {
                Toast.makeText(mapActivity,
                        "Next location found",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isAtNextLocation(Location location) {
        int progress = sharedPreferences.getInt(mapActivity.getString(R.string.preferences_locations_found_key), 0);
        if (progress == 10) {
            Log.e("LocationBroadcastReceiv", "Progress is at 10.  GeolocationMonitorService should have stopped.");
            return false;
        }
        Location[] locations = getLocations();
        Location nextLocation = locations[progress];
        if (isDebuggable) {
            Toast.makeText(mapActivity,
                    "Distance: " + nextLocation.distanceTo(location) + " (" + location.getProvider() + ")"
                            + "\n" + location.getLatitude() + ", " + location.getLongitude(),
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
}
