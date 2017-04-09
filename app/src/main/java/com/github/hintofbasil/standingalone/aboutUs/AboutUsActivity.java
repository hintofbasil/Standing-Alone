package com.github.hintofbasil.standingalone.aboutUs;

import android.os.Bundle;

import com.github.hintofbasil.standingalone.BaseActivity;
import com.github.hintofbasil.standingalone.R;

public class AboutUsActivity extends BaseActivity {

    public AboutUsActivity() {
        super(R.drawable.blank, R.layout.activity_about_us);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AutoHeightGridView characterGridContainer = (AutoHeightGridView) findViewById(R.id.character_grid_container);
        characterGridContainer.setAdapter(new CharacterGridAdapter(this));
    }
}
