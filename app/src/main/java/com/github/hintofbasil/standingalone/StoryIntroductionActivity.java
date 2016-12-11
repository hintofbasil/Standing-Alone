package com.github.hintofbasil.standingalone;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class StoryIntroductionActivity extends BaseActivity {

    public static final int ANIMATION_DURATION = 350;

    private ViewFlipper backgroundImageFlipper, introductionStoryFlipper;

    private ImageView leftButton, rightButton;

    private ImageView[] progressDisplay;

    private int currentPage;
    private int maxPage;

    public StoryIntroductionActivity() {
        super(R.drawable.the_story, R.layout.activity_story_introduction);
        currentPage = 0;
        maxPage = 4;
    }

    public void onLeftNavigationClicked(View view) {
        currentPage--;

        getBackgroundImageFlipper().setInAnimation(inFromLeftAnimation());
        getBackgroundImageFlipper().setOutAnimation(outToRightAnimation());
        getIntroductionStoryFlipper().setInAnimation(inFromLeftAnimation());
        getIntroductionStoryFlipper().setOutAnimation(outToRightAnimation());

        getBackgroundImageFlipper().showPrevious();
        getIntroductionStoryFlipper().showPrevious();
        updateDisplayForPage();
    }

    public void onRightNavigationClicked(View view) {
        currentPage++;

        getBackgroundImageFlipper().setInAnimation(inFromRightAnimation());
        getBackgroundImageFlipper().setOutAnimation(outToLeftAnimation());
        getIntroductionStoryFlipper().setInAnimation(inFromRightAnimation());
        getIntroductionStoryFlipper().setOutAnimation(outToLeftAnimation());

        getBackgroundImageFlipper().showNext();
        getIntroductionStoryFlipper().showNext();
        updateDisplayForPage();
    }

    private void updateDisplayForPage() {
        if (currentPage == 0) {
            getLeftButton().setVisibility(View.INVISIBLE);
        } else {
            getLeftButton().setVisibility(View.VISIBLE);
        }

        if (currentPage == maxPage) {
            getRightButton().setVisibility(View.INVISIBLE);
        } else {
            getRightButton().setVisibility(View.VISIBLE);
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
