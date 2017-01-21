package com.github.hintofbasil.standingalone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.hintofbasil.standingalone.geolocation.GeolocationMonitorService;
import com.github.hintofbasil.standingalone.map.LocationsMap;

/**
 * Created by will on 11/12/16.
 */
public class MapActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPreferences;
    private ImageView[] progressImageViews;
    private TextView progressText;
    private LocationsMap locationsMap;
    private Intent geolocationMonitorServiceIntent;

    public MapActivity() {
        super(R.drawable.map_title, R.layout.activity_map);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView locationFoundCheater = (ImageView) findViewById(R.id.location_found_cheater);
        boolean isDebuggable = ( 0 != ( getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (isDebuggable) {
            Log.d("LocationFoundCheater", "Debug mode enabled, cheat button visible");
            locationFoundCheater.setVisibility(View.VISIBLE);
        }
        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        int progress = sharedPreferences.getInt(getString(R.string.preferences_locations_found_key), 0);
        updateProgress(progress);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled;

        // May need try catch
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gps_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getString(R.string.enable_location_services_message));
            dialog.setPositiveButton(getString(R.string.enable_location_services_positive), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            dialog.setNegativeButton(getString(R.string.enable_location_services_negative), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("GeolocationMonitorServi", "Geo-location services not enabled");
                }
            });
            dialog.show();
        }

        geolocationMonitorServiceIntent = new Intent(getApplicationContext(),
                GeolocationMonitorService.class);
        startService(geolocationMonitorServiceIntent);
    }

    public void onLocationFoundCheaterClickHandler(View view) {
        String locationsFoundKey = getString(R.string.preferences_locations_found_key);
        int locationsFoundCount = sharedPreferences.getInt(locationsFoundKey, 0);
        locationsFoundCount = (locationsFoundCount + 1) % 10;
        sharedPreferences.edit().putInt(locationsFoundKey, locationsFoundCount).apply();

        // Must check if not 0 as cheat is cyclical
        if (locationsFoundCount > 0) {
            Intent intent = new Intent(getApplicationContext(),
                    LocationFoundActivity.class);
            intent.putExtra(LocationFoundActivity.EXTRA_LOCATION_FOUND_PROGRESS, LocationFoundEnum.get(locationsFoundCount));
            startActivity(intent);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preferences_locations_found_key))) {
            // Loctions found updated
            int progress = sharedPreferences.getInt(key, 0);
            updateProgress(progress);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        stopService(geolocationMonitorServiceIntent);
    }

    private void updateProgress(int progress) {
        updateProgressPointImages(progress);
        updateProgressText(progress);
        getLocationsMap().setFoundLocations(progress);
    }

    private void updateProgressText(int progress) {
        getProgressText().setText(String.format("%d/9", progress));
    }

    private void updateProgressPointImages(int progress) {
        ImageView[] progressViews = getProgressImageViews();
        for (int i = 0; i < progressViews.length; i++) {
            Drawable drawable;
            if (i < progress) {
                drawable = getResources().getDrawable(R.drawable.point_selected);
            } else {
                drawable = getResources().getDrawable(R.drawable.point_unselected);
            }
            progressViews[i].setImageDrawable(drawable);
        }
    }

    public ImageView[] getProgressImageViews() {
        if (progressImageViews == null) {
            progressImageViews = new ImageView[9];
            progressImageViews[0] = (ImageView) findViewById(R.id.story_progress_1);
            progressImageViews[1] = (ImageView) findViewById(R.id.story_progress_2);
            progressImageViews[2] = (ImageView) findViewById(R.id.story_progress_3);
            progressImageViews[3] = (ImageView) findViewById(R.id.story_progress_4);
            progressImageViews[4] = (ImageView) findViewById(R.id.story_progress_5);
            progressImageViews[5] = (ImageView) findViewById(R.id.story_progress_6);
            progressImageViews[6] = (ImageView) findViewById(R.id.story_progress_7);
            progressImageViews[7] = (ImageView) findViewById(R.id.story_progress_8);
            progressImageViews[8] = (ImageView) findViewById(R.id.story_progress_9);
        }
        return progressImageViews;
    }

    public TextView getProgressText() {
        if (progressText == null) {
            progressText = (TextView) findViewById(R.id.story_progress_text);
        }
        return progressText;
    }

    public LocationsMap getLocationsMap() {
        if (locationsMap == null) {
            locationsMap = (LocationsMap) findViewById(R.id.locations_map);
        }
        return locationsMap;
    }
}
