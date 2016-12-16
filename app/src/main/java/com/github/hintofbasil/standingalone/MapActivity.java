package com.github.hintofbasil.standingalone;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by will on 11/12/16.
 */
public class MapActivity extends BaseActivity {

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
    }

    public void onLocationFoundCheaterClickHandler(View view) {

    }
}
