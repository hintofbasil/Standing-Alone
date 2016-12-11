package com.github.hintofbasil.standingalone;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by will on 10/12/16.
 */
public class BaseActivity extends AppCompatActivity {

    private int titleImageResource;

    public BaseActivity(int titleImageResource) {
        this.titleImageResource = titleImageResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = LayoutInflater.from(this);
        View customActionBarView = inflater.inflate(R.layout.custom_toolbar, null);
        ImageView titleText = (ImageView) customActionBarView.findViewById(R.id.titleText);
        titleText.setImageResource(titleImageResource);

        // Set size dynamically
        // Can't find way to set in layout XML
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        customActionBarView.setMinimumWidth(size.x);

        actionBar.setCustomView(customActionBarView);

    }

}
