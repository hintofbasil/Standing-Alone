package com.github.hintofbasil.standingalone;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by will on 11/12/16.
 */
public class MapActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPreferences;

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
    }

    public void onLocationFoundCheaterClickHandler(View view) {
        String locationsFoundKey = getString(R.string.preferences_locations_found_key);
        int locationsFoundCount = sharedPreferences.getInt(locationsFoundKey, 0);
        locationsFoundCount = (locationsFoundCount + 1) % 10;
        sharedPreferences.edit().putInt(locationsFoundKey, locationsFoundCount).apply();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
