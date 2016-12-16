package com.github.hintofbasil.standingalone;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by will on 11/12/16.
 */
public class MapActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPreferences;
    private ImageView[] progressImageViews;

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
        if (key.equals(getString(R.string.preferences_locations_found_key))) {
            // Loctions found updated
            int progress = sharedPreferences.getInt(key, 0);
            updateProgressIndicator(progress);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void updateProgressIndicator(int progress) {
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
}
