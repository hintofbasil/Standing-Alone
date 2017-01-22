package com.github.hintofbasil.standingalone;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hintofbasil.standingalone.geolocation.GeolocationMonitorService;
import com.github.hintofbasil.standingalone.geolocation.LocationBroadcastReceiver;
import com.github.hintofbasil.standingalone.map.LocationsMap;

/**
 * Created by will on 11/12/16.
 */
public class MapActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int ERROR_DELAY_TIME = 60000; // One minute

    private SharedPreferences sharedPreferences;
    private ImageView[] progressImageViews;
    private TextView progressText;
    private LocationsMap locationsMap;
    private Intent geolocationMonitorServiceIntent;
    private BroadcastReceiver locationReceiver;

    private Handler noDataErrorHandler;
    private LinearLayout noDataError;
    private Runnable noDataErrorHandlerRunnable;
    private Handler noGPSErrorHandler;
    private LinearLayout noGPSError;
    private Runnable noGPSErrorHandlerRunnable;

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
        int progress = sharedPreferences.getInt(getString(R.string.preferences_locations_found_key), 0);
        updateProgress(progress, false);

        locationReceiver = new LocationBroadcastReceiver(this);

        noDataError = (LinearLayout) findViewById(R.id.no_data_found_error);
        noGPSError = (LinearLayout) findViewById(R.id.no_gps_data_found_error);

        noDataErrorHandlerRunnable = new Runnable() {
            @Override
            public void run() {
                // Hide less significant error
                if (noGPSError.getVisibility() == View.VISIBLE) {
                    noGPSError.setVisibility(View.GONE);
                }
                noDataError.setVisibility(View.VISIBLE);
                noDataErrorHandler.postDelayed(this, ERROR_DELAY_TIME);
            }
        };
        noDataErrorHandler = new Handler();

        noGPSErrorHandlerRunnable = new Runnable() {
            @Override
            public void run() {
                // Only show more significant error
                if(noDataError.getVisibility() == View.VISIBLE) {
                    return;
                }
                noDataError.setVisibility(View.VISIBLE);
                noDataErrorHandler.postDelayed(this, ERROR_DELAY_TIME);
            }
        };
        noGPSErrorHandler = new Handler();
    }

    public void postponeErrorMessages(Location location) {
        // Reset timers
        noDataErrorHandler.removeCallbacks(noDataErrorHandlerRunnable);
        noDataErrorHandler.postDelayed(noDataErrorHandlerRunnable, ERROR_DELAY_TIME);
        noDataError.setVisibility(View.GONE);
        if (location.getProvider().equals("gps")) {
            noGPSErrorHandler.removeCallbacks(noGPSErrorHandlerRunnable);
            noGPSErrorHandler.postDelayed(noGPSErrorHandlerRunnable, ERROR_DELAY_TIME);
            noGPSError.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        int progress = sharedPreferences.getInt(getString(R.string.preferences_locations_found_key), 0);
        if (progress < 10) {
            startGeolocationService();
            noDataErrorHandler.postDelayed(noDataErrorHandlerRunnable, ERROR_DELAY_TIME);
            noGPSErrorHandler.postDelayed(noGPSErrorHandlerRunnable, ERROR_DELAY_TIME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        stopGeolocationService();
        noDataErrorHandler.removeCallbacks(noDataErrorHandlerRunnable);
        noGPSErrorHandler.removeCallbacks(noGPSErrorHandlerRunnable);
    }

    public void handleErrorClick(View view) {
        openLocationSettings();
    }

    private void openLocationSettings() {
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

    private void startGeolocationService() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean has_permission = this.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED;

        if (!gps_enabled || !has_permission) {
            openLocationSettings();
        }

        // Register for location events to detect errors
        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, new IntentFilter("custom"));

        geolocationMonitorServiceIntent = new Intent(getApplicationContext(),
                GeolocationMonitorService.class);
        startService(geolocationMonitorServiceIntent);
    }

    private void stopGeolocationService() {
        stopService(geolocationMonitorServiceIntent);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver);
    }

    public void onLocationFoundCheaterClickHandler(View view) {
        String locationsFoundKey = getString(R.string.preferences_locations_found_key);
        int locationsFoundCount = sharedPreferences.getInt(locationsFoundKey, 0);
        locationsFoundCount = (locationsFoundCount + 1) % 11;
        sharedPreferences.edit().putInt(locationsFoundKey, locationsFoundCount).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(geolocationMonitorServiceIntent);
    }

    public void updateProgress(int progress, boolean showText) {
        updateProgressPointImages(progress);
        updateProgressText(progress);
        getLocationsMap().setFoundLocations(progress);

        // Must check if not 0 as cheat is cyclical
        if (showText && progress > 0) {
            Intent intent = new Intent(getApplicationContext(),
                    LocationFoundActivity.class);
            intent.putExtra(LocationFoundActivity.EXTRA_LOCATION_FOUND_PROGRESS, LocationFoundEnum.get(progress));
            startActivity(intent);
        }
    }

    private void updateProgressText(int progress) {
        if (progress == 9 || progress == 10) {
            getProgressText().setText(String.format("%d/10", progress));
        } else {
            getProgressText().setText(String.format("%d/9", progress));
        }
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

        if (progress == 9 || progress == 10) {
            progressViews[9].setVisibility(View.VISIBLE);
        } else {
            progressViews[9].setVisibility(View.GONE);
        }
    }

    public ImageView[] getProgressImageViews() {
        if (progressImageViews == null) {
            progressImageViews = new ImageView[10];
            progressImageViews[0] = (ImageView) findViewById(R.id.story_progress_1);
            progressImageViews[1] = (ImageView) findViewById(R.id.story_progress_2);
            progressImageViews[2] = (ImageView) findViewById(R.id.story_progress_3);
            progressImageViews[3] = (ImageView) findViewById(R.id.story_progress_4);
            progressImageViews[4] = (ImageView) findViewById(R.id.story_progress_5);
            progressImageViews[5] = (ImageView) findViewById(R.id.story_progress_6);
            progressImageViews[6] = (ImageView) findViewById(R.id.story_progress_7);
            progressImageViews[7] = (ImageView) findViewById(R.id.story_progress_8);
            progressImageViews[8] = (ImageView) findViewById(R.id.story_progress_9);
            progressImageViews[9] = (ImageView) findViewById(R.id.story_progress_10);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preferences_locations_found_key))) {
            // Loctions found updated
            int progress = sharedPreferences.getInt(key, 0);
            updateProgress(progress, true);
        }
    }


}
