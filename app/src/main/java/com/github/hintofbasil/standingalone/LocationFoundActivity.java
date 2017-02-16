package com.github.hintofbasil.standingalone;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationFoundActivity extends BaseActivity {

    public static final String EXTRA_LOCATION_FOUND_PROGRESS = "com.github.hintofbasil.EXTRA_LOCATION_FOUND_PROGRESS";
    public static final int SPEACH_DELAY_MILLI = 8000;

    private CharSequence[] textArray;
    private int textStatus = 0;
    private TextView speechTextView;

    private Drawable speechBubbleBottomLeft;
    private Drawable speechBubbleBottomRight;
    private ImageView speechBubbleBottomView;

    public LocationFoundActivity() {
        // Override title image in onCreate
        super(R.drawable.glaistig_title, R.layout.activity_location_found);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_found);

        speechBubbleBottomLeft = getResources().getDrawable(R.drawable.speech_bubble_bottom);
        speechBubbleBottomRight = getResources().getDrawable(R.drawable.speech_bubble_bottom_reverse);
        speechBubbleBottomView = (ImageView) findViewById(R.id.speech_bubble_bottom);

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

        speechTextView = (TextView) findViewById(R.id.location_found_text);
        speechTextView.setMovementMethod(new ScrollingMovementMethod());

        textArray = getResources().getTextArray(details.textStringId);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateText();
            }
        }, 0);
    }

    private void updateText() {
        speechTextView.setText(textArray[textStatus]);
        if (textStatus == textArray.length -1) {
            speechBubbleBottomView.setVisibility(View.INVISIBLE);
        } else if (textStatus % 2 == 0) {
            speechBubbleBottomView.setImageDrawable(speechBubbleBottomLeft);
        } else {
            speechBubbleBottomView.setImageDrawable(speechBubbleBottomRight);
        }
        textStatus++;
        if (textArray.length > textStatus) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateText();
                }
            }, SPEACH_DELAY_MILLI);
        }
    }

    public void handleMapButtonClicked(View view) {
        // This should always be opened from the map
        // so closing should take user back to map
        this.finish();
    }
}
