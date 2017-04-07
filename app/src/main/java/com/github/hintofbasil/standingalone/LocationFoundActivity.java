package com.github.hintofbasil.standingalone;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationFoundActivity extends BaseActivity {

    public static final String EXTRA_LOCATION_FOUND_PROGRESS = "com.github.hintofbasil.EXTRA_LOCATION_FOUND_PROGRESS";
    public static final String LOCATION_FOUND_REPLAY = "com.github.hintofbasil.LOCATION_FOUND_REPLAY";

    private CharSequence[] textArray;
    private int[] textTimings;
    private int textStatus = 0;
    private TextView speechTextView;

    private Drawable speechBubbleBottomLeft;
    private Drawable speechBubbleBottomRight;
    private ImageView speechBubbleBottomView;

    private boolean brownieSpeaking;
    private boolean endWithNoSpeech;

    private ImageView mapButton;

    private boolean autoNext;
    private Handler timingHandler;
    private Runnable updateTextRunnable;

    private ImageView backButton;
    private ImageView nextButton;

    public LocationFoundActivity() {
        // Override title image in onCreate
        super(R.drawable.glaistig_title, R.layout.activity_location_found);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_found);

        timingHandler = new Handler();
        updateTextRunnable = new Runnable() {
            @Override
            public void run() {
                updateText();
            }
        };

        backButton = (ImageView) findViewById(R.id.back_button);
        nextButton = (ImageView) findViewById(R.id.next_button);

        speechBubbleBottomLeft = getResources().getDrawable(R.drawable.speech_bubble_bottom);
        speechBubbleBottomRight = getResources().getDrawable(R.drawable.speech_bubble_bottom_reverse);
        speechBubbleBottomView = (ImageView) findViewById(R.id.speech_bubble_bottom);

        final LocationFoundEnum details = (LocationFoundEnum) getIntent().getSerializableExtra(EXTRA_LOCATION_FOUND_PROGRESS);

        brownieSpeaking = details.beginWithBrownie;
        endWithNoSpeech = details.endWithNoSpeech;

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

        mapButton = (ImageView) findViewById(R.id.map_button);
        boolean isDebuggable = ( 0 != ( getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (isDebuggable) {
            Log.d("LocationFoundCheater", "Debug mode enabled, map button visible");
            enableMapButton();
        }

        boolean isReplay = getIntent().getBooleanExtra(LOCATION_FOUND_REPLAY, false);

        if (isReplay) {
            enableMapButton();
        }

        textArray = getResources().getTextArray(details.textStringId);
        textTimings = details.textTimings;
        timingHandler.postDelayed(updateTextRunnable, 0);
    }

    private void updateText() {
        speechTextView.setText(textArray[textStatus]);
        speechTextView.scrollTo(0, 0);
        if (endWithNoSpeech && textStatus == textArray.length -1) {
            speechBubbleBottomView.setVisibility(View.INVISIBLE);
        } else if (brownieSpeaking) {
            speechBubbleBottomView.setImageDrawable(speechBubbleBottomLeft);
        } else {
            speechBubbleBottomView.setImageDrawable(speechBubbleBottomRight);
        }
        if (textStatus == textArray.length -1) {
            enableMapButton();
            nextButton.setVisibility(View.INVISIBLE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }
        brownieSpeaking = !brownieSpeaking;
        textStatus++;
        if (autoNext && textArray.length > textStatus) {
            // TODO find out why sometimes throws exception
            int delay;
            try {
                delay = textTimings[textStatus - 1];
            } catch (ArrayIndexOutOfBoundsException ex) {
                delay = 8000;
            }
            timingHandler.postDelayed(updateTextRunnable, delay);
        }
    }

    private void enableMapButton() {
        mapButton.setClickable(true);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This should always be opened from the map
                // so closing should take user back to map
                LocationFoundActivity.this.finish();
            }
        });
        mapButton.setVisibility(View.VISIBLE);
    }

    public void handleAutoSpeechButtonClick(View view) {
        setAutoNextEnabled(!autoNext);
    }

    private void setAutoNextEnabled(boolean enabled) {
        if (enabled == autoNext) {
            return;
        }
        if (!enabled) {
            timingHandler.removeCallbacks(updateTextRunnable);
        } else {
            if (textArray.length > textStatus) {
                // TODO find out why sometimes throws exception
                int delay;
                try {
                    delay = textTimings[textStatus - 1];
                } catch (ArrayIndexOutOfBoundsException ex) {
                    delay = 8000;
                }
                timingHandler.postDelayed(updateTextRunnable, delay);
            }
        }
        autoNext = enabled;
    }

    public void handleLeftButtonClick(View view) {
        setAutoNextEnabled(false);
    }

    public void handleRightButtonClick(View view) {
        updateText();
        // Reset timer
        if (autoNext) {
            setAutoNextEnabled(false);
            setAutoNextEnabled(true);
        }
    }
}
