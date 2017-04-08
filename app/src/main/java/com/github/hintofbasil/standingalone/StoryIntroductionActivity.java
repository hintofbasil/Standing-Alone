package com.github.hintofbasil.standingalone;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class StoryIntroductionActivity extends BaseActivity {

    public static final int ANIMATION_DURATION = 350;

    private ViewFlipper backgroundImageFlipper, introductionStoryFlipper;

    private ImageView leftButton, rightButton, letsGoButton;

    private ImageView[] progressDisplay;

    private int currentPage;
    private int maxPage;

    private MediaPlayer mediaPlayer;

    private int[] rawSoundIds;


    public StoryIntroductionActivity() {
        super(R.drawable.the_story, R.layout.activity_story_introduction);
        currentPage = 0;
        maxPage = 5;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaPlayer = MediaPlayer.create(this, getRawSoundIds()[currentPage]);
        mediaPlayer.start();
    }

    public void onLeftNavigationClicked(View view) {
        if (currentPage == 0) {
            return;
        }
        currentPage--;

        getBackgroundImageFlipper().setInAnimation(inFromLeftAnimation());
        getBackgroundImageFlipper().setOutAnimation(outToRightAnimation());
        getIntroductionStoryFlipper().setInAnimation(inFromLeftAnimation());
        getIntroductionStoryFlipper().setOutAnimation(outToRightAnimation());

        getBackgroundImageFlipper().showPrevious();
        getIntroductionStoryFlipper().showPrevious();
        updateDisplayForPage();

        mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(this, getRawSoundIds()[currentPage]);
        mediaPlayer.start();
    }

    int[] getRawSoundIds() {
        rawSoundIds = new int[7];
        rawSoundIds[0] = R.raw.intro1;
        rawSoundIds[1] = R.raw.intro2;
        rawSoundIds[2] = R.raw.intro3;
        rawSoundIds[3] = R.raw.intro4;
        rawSoundIds[4] = R.raw.intro5;
        rawSoundIds[5] = R.raw.intro6;
        rawSoundIds[6] = R.raw.intro6b;
        return rawSoundIds;
    }

    public void onRightNavigationClicked(View view) {
        if (currentPage == maxPage) {
            return;
        }
        currentPage++;

        getBackgroundImageFlipper().setInAnimation(inFromRightAnimation());
        getBackgroundImageFlipper().setOutAnimation(outToLeftAnimation());
        getIntroductionStoryFlipper().setInAnimation(inFromRightAnimation());
        getIntroductionStoryFlipper().setOutAnimation(outToLeftAnimation());

        getBackgroundImageFlipper().showNext();
        getIntroductionStoryFlipper().showNext();
        updateDisplayForPage();

        mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(this, getRawSoundIds()[currentPage]);
        mediaPlayer.start();
    }

    private void updateDisplayForPage() {
        if (currentPage == 0) {
            getLeftButton().setVisibility(View.INVISIBLE);
        } else {
            getLeftButton().setVisibility(View.VISIBLE);
        }

        if (currentPage == maxPage) {
            getRightButton().setVisibility(View.INVISIBLE);
            getLetsGoButton().setVisibility(View.VISIBLE);
        } else {
            getRightButton().setVisibility(View.VISIBLE);
            getLetsGoButton().setVisibility(View.INVISIBLE);
        }

        ImageView[] progress = getProgressDisplay();
        for(int i=0; i < progress.length; i++) {
            if (i <= currentPage) {
                progress[i].setImageResource(R.drawable.point_selected);
            } else {
                progress[i].setImageResource(R.drawable.point_unselected);
            }
        }
    }

    public void handleLetsGoButtonClick(View view) {
        mediaPlayer.release();
        Intent intent = new Intent(getApplicationContext(),
                MapActivity.class);
        startActivity(intent);
    }

    private ViewFlipper getBackgroundImageFlipper() {
        if (backgroundImageFlipper == null) {
            backgroundImageFlipper = (ViewFlipper) findViewById(R.id.background_image_flipper);
        }
        return backgroundImageFlipper;
    }

    private ImageView getLeftButton() {
        if (leftButton == null) {
            leftButton = (ImageView) findViewById(R.id.introduction_left_button);
        }
        return leftButton;
    }

    private ImageView getRightButton() {
        if (rightButton == null) {
            rightButton = (ImageView) findViewById(R.id.introduction_right_button);
        }
        return rightButton;
    }

    public ImageView getLetsGoButton() {
        if (letsGoButton == null) {
            letsGoButton = (ImageView) findViewById(R.id.lets_go_button);
        }
        return letsGoButton;
    }

    private ViewFlipper getIntroductionStoryFlipper() {
        if (introductionStoryFlipper == null) {
            introductionStoryFlipper = (ViewFlipper) findViewById(R.id.introduction_story_flipper);
        }
        return introductionStoryFlipper;
    }

    private ImageView[] getProgressDisplay() {
        if (progressDisplay == null) {
            progressDisplay = new ImageView[maxPage + 1];
            progressDisplay[0] = (ImageView) findViewById(R.id.story_progress_1);
            progressDisplay[1] = (ImageView) findViewById(R.id.story_progress_2);
            progressDisplay[2] = (ImageView) findViewById(R.id.story_progress_3);
            progressDisplay[3] = (ImageView) findViewById(R.id.story_progress_4);
            progressDisplay[4] = (ImageView) findViewById(R.id.story_progress_5);
            progressDisplay[5] = (ImageView) findViewById(R.id.story_progress_6);
        }
        return progressDisplay;
    }


    // Animation getters taken from
    // http://www.inter-fuser.com/2009/07/android-transistions-slide-in-and-slide.html
    private Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromRight.setDuration(ANIMATION_DURATION);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        outtoLeft.setDuration(ANIMATION_DURATION);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromLeft.setDuration(ANIMATION_DURATION);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  +1.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        outtoRight.setDuration(ANIMATION_DURATION);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }
}
