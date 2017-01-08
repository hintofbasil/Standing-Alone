package com.github.hintofbasil.standingalone;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationFoundActivity extends BaseActivity {

    public static final String EXTRA_LOCATION_FOUND_PROGRESS = "com.github.hintofbasil.EXTRA_LOCATION_FOUND_PROGRESS";

    public LocationFoundActivity() {
        // Override title image in onCreate
        super(R.drawable.glaistig_title, R.layout.activity_location_found);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_found);

        final LocationFoundEnum details = (LocationFoundEnum) getIntent().getSerializableExtra(EXTRA_LOCATION_FOUND_PROGRESS);

        ImageView titleImageView = (ImageView) findViewById(R.id.titleText);
        titleImageView.setImageResource(details.titleDrawableId);

        final ImageView characterImageView = (ImageView) findViewById(R.id.location_found_character_image);
        characterImageView.setImageResource(details.characterDrawableId);
        ViewGroup.LayoutParams layoutParams = characterImageView.getLayoutParams();
        layoutParams.height = (int)(layoutParams.height * details.scale);
        layoutParams.width = (int)(layoutParams.width * details.scale);

        ImageView backgroundImage = (ImageView) findViewById(R.id.background_image);
        backgroundImage.setImageResource(details.backgroundDrawableId);

        TextView speechTextView = (TextView) findViewById(R.id.location_found_text);
        speechTextView.setMovementMethod(new ScrollingMovementMethod());

        String speechText = getString(details.textStringId);
        speechTextView.setText(speechText);
    }

    public void handleMapButtonClicked(View view) {
        // This should always be opened from the map
        // so closing should take user back to map
        this.finish();
    }
}
