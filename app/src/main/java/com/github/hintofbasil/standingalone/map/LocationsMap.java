package com.github.hintofbasil.standingalone.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by will on 11/12/16.
 */
public class LocationsMap extends View {

    private static final int BACKGROUND_COLOR = 0xFF9BE8F0;

    private Paint paint;

    public LocationsMap(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setColor(BACKGROUND_COLOR);
        canvas.drawRect(0, 0, getWidth(), getHeight(), this.paint);
    }
}
