package com.github.hintofbasil.standingalone.aboutUs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ScrollView;

/**
 * Created by will on 09/04/17.
 */

public class AutoHeightGridView extends GridView {

    private ScrollView scrollView;

    public AutoHeightGridView(Context context) {
        super(context);
    }

    public AutoHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ListAdapter adapter = getAdapter();
        int columns = getNumColumns();
        int rows = (int)Math.ceil((float)adapter.getCount() / columns);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() * rows);

        scrollView.scrollTo(0, 0);
    }
}
