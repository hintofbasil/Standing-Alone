package com.github.hintofbasil.standingalone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LocationFoundActivity extends BaseActivity {

    public LocationFoundActivity() {
        super(R.drawable.glaistig_title, R.layout.activity_location_found);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_found);
    }
}
