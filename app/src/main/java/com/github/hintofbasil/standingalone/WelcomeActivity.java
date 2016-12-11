package com.github.hintofbasil.standingalone;

import android.content.Intent;
import android.view.View;

public class WelcomeActivity extends BaseActivity {

    public WelcomeActivity() {
        super(R.drawable.the_story, R.layout.activity_welcome);
    }

    public void handleLetsGoButtonClick(View view) {
        Intent intent = new Intent(getApplicationContext(),
                StoryIntroductionActivity.class);
        startActivity(intent);
    }
}
