package com.github.hintofbasil.standingalone;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hintofbasil.standingalone.aboutUs.AboutUsActivity;
import com.github.hintofbasil.standingalone.geolocation.GeolocationMonitorService;
import com.github.hintofbasil.standingalone.geolocation.LocationBroadcastReceiver;
import com.github.hintofbasil.standingalone.map.LocationsMap;

import static com.github.hintofbasil.standingalone.LocationFoundActivity.EXTRA_LOCATION_FOUND_PROGRESS;

/**
 * Created by will on 11/12/16.
 */
public class MapActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int ACCESS_FINE_LOCATION_REQUEST = 0;

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

    private boolean paused;

    private boolean backgroundServiceRunning;

    private ImageView treasureIcon;

    private Vibrator vibrator;

    private NotificationManager notificationManager;

    public MapActivity() {
        super(R.drawable.map_title, R.layout.activity_map);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paused = false;
        backgroundServiceRunning = false;

        treasureIcon = (ImageView) findViewById(R.id.treasure_icon);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        ImageView locationFoundCheater = (ImageView) findViewById(R.id.location_found_cheater);
        boolean isDebuggable = ( 0 != ( getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (isDebuggable) {
            Log.d("LocationFoundCheater", "Debug mode enabled, cheat button visible");
            locationFoundCheater.setVisibility(View.VISIBLE);
        }
        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        int progress = sharedPreferences.getInt(getString(R.string.preferences_locations_found_key), 0);
        updateProgress(progress, false);

        int permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionGranted == PackageManager.PERMISSION_GRANTED) {
            locationReceiver = new LocationBroadcastReceiver(this);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_REQUEST);
        }

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

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if (progress < 10) {
            startGeolocationService();
            noDataErrorHandler.postDelayed(noDataErrorHandlerRunnable, ERROR_DELAY_TIME);
            noGPSErrorHandler.postDelayed(noGPSErrorHandlerRunnable, ERROR_DELAY_TIME);
        }
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
        paused = false;
        boolean notificationLaunched = sharedPreferences.getBoolean("NOTIFICATION_LAUNCHED", true);
        if (notificationLaunched) {
            int progress = sharedPreferences.getInt(getString(R.string.preferences_locations_found_key), 0);
            updateProgress(progress, true);
        } else {
            if (!backgroundServiceRunning) {
                startGeolocationService();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    public void handleErrorClick(View view) {
        openLocationSettings();
    }

    private void openLocationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION_REQUEST);
            }
        } else {
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_REQUEST:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationReceiver = new LocationBroadcastReceiver(this);
                } else {
                    Toast.makeText(this, getString(R.string.enable_location_services_message), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void startGeolocationService() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean has_permission = this.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED;

        if (!gps_enabled || !has_permission) {
            openLocationSettings();
        }

        // Check they were enabled
        if (!gps_enabled || !has_permission) {
            Log.d("MapActivity", "Location permission not granted");
            return;
        }

        // Register for location events to detect errors
        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, new IntentFilter("custom"));

        geolocationMonitorServiceIntent = new Intent(getApplicationContext(),
                GeolocationMonitorService.class);
        startService(geolocationMonitorServiceIntent);
        backgroundServiceRunning = true;
    }

    private void stopGeolocationService() {
        if (geolocationMonitorServiceIntent != null) {
            stopService(geolocationMonitorServiceIntent);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver);
        backgroundServiceRunning = false;
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
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        stopGeolocationService();
        noDataErrorHandler.removeCallbacks(noDataErrorHandlerRunnable);
        noGPSErrorHandler.removeCallbacks(noGPSErrorHandlerRunnable);
    }

    public void updateProgress(int progress, boolean showText) {
        updateProgressPointImages(progress);
        updateProgressText(progress);
        getLocationsMap().setFoundLocations(progress);

        if (progress == 10) {
            treasureIcon.setVisibility(View.VISIBLE);
        } else {
            treasureIcon.setVisibility(View.GONE);
        }

        // Must check if not 0 as cheat is cyclical
        if (showText && progress > 0) {
            Intent intent = new Intent(getApplicationContext(),
                    LocationFoundActivity.class);
            intent.putExtra(EXTRA_LOCATION_FOUND_PROGRESS, LocationFoundEnum.get(progress));
            startActivity(intent);
        }
    }

    public void launchNotification(int progress) {

        LocationFoundEnum details = LocationFoundEnum.get(progress);

        int backgroundId = details.backgroundDrawableId;
        int characterId = details.characterDrawableId;

        // Brownie is a special case as background and character are empty
        if (details == LocationFoundEnum.BROWNIE) {
            backgroundId = R.drawable.introduction_background_1;
            characterId = R.drawable.brownie;
        }

        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.custom_notification);
        remoteViews.setImageViewResource(R.id.notification_title, details.titleDrawableId);
        remoteViews.setImageViewResource(R.id.notification_background, backgroundId);
        remoteViews.setImageViewResource(R.id.notification_character, characterId);

        Uri uri = Uri.parse("android.resource://com.github.hintofbasil.standingalone/" + details.foundSoundFile);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.brownie)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVibrate(new long[] {1000})
                .setSound(uri)
                .setContent(remoteViews);

        Intent intent = new Intent(this, LocationFoundActivity.class);
        intent.putExtra(EXTRA_LOCATION_FOUND_PROGRESS, details);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(pendingIntent);

        int notificationId = 001;

        notificationManager.notify(notificationId, builder.build());
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
            if (!paused) {
                vibrator.vibrate(1000);
                updateProgress(progress, true);
            } else {
                updateProgress(progress, false);
                launchNotification(progress);
                sharedPreferences.edit().putBoolean("NOTIFICATION_LAUNCHED", true).apply();
                stopGeolocationService();
            }
        }
    }

    public void handleTreasureIconClick(View view) {
        Intent intent = new Intent(this, AboutUsActivity.class);
        startActivity(intent);
    }

}
