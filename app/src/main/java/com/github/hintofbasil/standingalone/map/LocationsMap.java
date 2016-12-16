package com.github.hintofbasil.standingalone.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.github.hintofbasil.standingalone.R;

/**
 * Created by will on 11/12/16.
 */
public class LocationsMap extends View implements GestureDetector.OnGestureListener {

    private static final int BACKGROUND_COLOR = 0xFF9BE8F0;

    private Paint paint;

    Bitmap mapBackground;
    Bitmap nextLocationImage;
    Bitmap previousLocationImage;

    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    float scaleFactor;
    float offsetX, offsetY;

    private Point[] locations;
    private int foundLocations;

    public LocationsMap(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.map_background);
        nextLocationImage = BitmapFactory.decodeResource(getResources(), R.drawable.next_location_marker);
        previousLocationImage = BitmapFactory.decodeResource(getResources(), R.drawable.previous_location_marker);

        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, this);
        scaleFactor = 1;
        offsetX = 0;
        offsetY = 0;
        locations = getLocations();
        foundLocations = 1;
    }

    private Point[] getLocations() {
        int mapWidth = mapBackground.getWidth();
        int mapHeight = mapBackground.getHeight();
        Point[] locations = new Point[2];
        locations[0] = new Point((int)(0.625 * mapWidth), (int)(0.330 * mapHeight)); // Suck
        locations[1] = new Point((int)(0.518 * mapWidth), (int)(0.261 * mapHeight)); // Stone House
        return locations;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setColor(BACKGROUND_COLOR);
        canvas.save();
        canvas.drawRect(0, 0, getWidth(), getHeight(), this.paint);
        canvas.translate(offsetX, offsetY);
        canvas.scale(scaleFactor, scaleFactor);

        canvas.drawBitmap(mapBackground, 0, 0, paint);

        for (int i=0; i < foundLocations; i++) {
            Point location = locations[i];
            int x = location.x - nextLocationImage.getWidth()/2;
            int y = location.y - nextLocationImage.getHeight();
            canvas.drawBitmap(previousLocationImage, x, y, paint);
        }

        if (foundLocations < locations.length) {
            Point location = locations[foundLocations];
            int x = location.x - nextLocationImage.getWidth()/2;
            int y = location.y - nextLocationImage.getHeight();
            canvas.drawBitmap(nextLocationImage, x, y, paint);
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retval = scaleGestureDetector.onTouchEvent(event);
        retval = gestureDetector.onTouchEvent(event) || retval;
        return retval;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        this.offsetX -= distanceX;
        this.offsetY -= distanceY;

        limitViewBounds();
        invalidate();
        return true;
    }

    private void limitViewBounds() {
        offsetX = Math.max(getMinOffsetX(), Math.min(offsetX, getMaxOffsetX()));
        offsetY = Math.max(getMinOffsetY(), Math.min(offsetY, getMaxOffsetY()));
    }

    public float getMinOffsetX() {
        return -100 * scaleFactor - mapBackground.getWidth() * scaleFactor + getWidth();
    }

    public float getMinOffsetY() {
        return -100 * scaleFactor - mapBackground.getHeight() * scaleFactor + getHeight();
    }

    public float getMaxOffsetX() {
        return 100 * scaleFactor;
    }

    public float getMaxOffsetY() {
        return 100 * scaleFactor;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float fx = detector.getFocusX();
            float fy = detector.getFocusY();

            offsetX -= fx/scaleFactor;
            offsetY -= fy/scaleFactor;

            scaleFactor *= detector.getScaleFactor();
            // Don't let the object get too small or too large.
            scaleFactor = Math.max(0.3f, Math.min(scaleFactor, 1.2f));

            offsetX += fx/scaleFactor;
            offsetY += fy/scaleFactor;

            limitViewBounds();

            invalidate();
            return true;
        }
    }
}
