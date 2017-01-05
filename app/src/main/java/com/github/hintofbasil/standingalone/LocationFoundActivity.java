package com.github.hintofbasil.standingalone;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LocationFoundActivity extends BaseActivity {

    public LocationFoundActivity() {
        super(R.drawable.glaistig_title, R.layout.activity_location_found);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_found);

        ImageView backgroundImage = (ImageView) findViewById(R.id.background_image);
        backgroundImage.setImageResource(R.drawable.glaistig_background);
    }

    public void handleMapButtonClicked(View view) {
        // This should always be opened from the map
        // so closing should take user back to map
        this.finish();
    }
}
