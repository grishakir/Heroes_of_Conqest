package com.thekingames.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


public class TimerDrawable extends Drawable {
    private ActionDraw actions[];
    private double[] percents = new double[]{1.0,
            0.875,
            0.750,
            0.625,
            0.500,
            0.375,
            0.250,
            0.125,
    };
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path callDownPath = new Path();
    private Path globalCallDownPath = new Path();
    private double percent;
    private double gPercent;

    public TimerDrawable() {
        actions = new ActionDraw[10];
    }

    private void createDrawingActions(final int width, final int height) {
        actions[0] = new ActionDraw() {
            @Override
            public void draw(Path path) {
                path.lineTo(width, 0);   //100%
            }
        };
        actions[1] = new ActionDraw() {
            @Override
            public void draw(Path path) {
                path.lineTo(width, height / 2);         //87,5%
            }
        };
        actions[2] = new ActionDraw() {
            @Override
            public void draw(Path path) {
                path.lineTo(width, height); //75%
            }
        };
        actions[3] = new ActionDraw() {
            @Override
            public void draw(Path path) {
                path.lineTo(width / 2, height);        //62,5%
            }
        };
        actions[4] = new ActionDraw() {
            @Override
            public void draw(Path path) {
                path.lineTo(0, height); //50%
            }
        };
        actions[5] = new ActionDraw() {
            @Override
            public void draw(Path path) {
                path.lineTo(0, height / 2);         //37,5%
            }
        };
        actions[6] = new ActionDraw() {
            @Override
            public void draw(Path path) {
                path.lineTo(0, 0);  //25%
            }
        };
        actions[7] = new ActionDraw() {
            @Override
            public void draw(Path path) {
                path.lineTo(width / 2, 0);           //12,5%
            }
        };
    }

    @Override
    public void draw(Canvas canvas) {
        // перенастраивам кисть на заливку
        if (percent > 0) {
            int parsedColor1 = Color.parseColor("#ed0e0e0e");
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(parsedColor1);
            canvas.drawPath(callDownPath, mPaint);
        }
        if (gPercent > 0) {
            int parsedColor2 = Color.parseColor("#94333030");
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(parsedColor2);
            canvas.drawPath(globalCallDownPath, mPaint);
        }
    }


    public void setPercent(double percent) {
        this.percent = percent;
        int width = getBounds().width();
        int height = getBounds().height();
        double radiusX = (float) (width / 2) * 3;
        double radiusY = (float) (height / 2) * 3;
        double corner = 360 * (1 - percent);
        double a = 0;
        float mY = (float) (-(radiusY * Math.cos(Math.toRadians(corner + a))) + height / 2);
        float mX = (float) ((radiusX * Math.sin(Math.toRadians(corner + a))) + width / 2);
        callDownPath.reset();
        callDownPath.moveTo(width / 2, 0);
        callDownPath.lineTo(width / 2, height / 2);
        callDownPath.lineTo(mX, mY);
        createDrawingActions(width, height);
        for (int i = 1; i < percents.length; i++) {
            if (percent >= percents[i]) {
                actions[i - 1].draw(callDownPath);
            }
        }
        callDownPath.close();
    }

    public void setGPercent(double gPercent) {
        this.gPercent = gPercent;
        int width = getBounds().width();
        int height = getBounds().height();
        double radiusX = (float) (width / 2) * 3;
        double radiusY = (float) (height / 2) * 3;
        double corner = 360 * (1 - gPercent);
        double a = 0;
        float mY = (float) (-(radiusY * Math.cos(Math.toRadians(corner + a))) + height / 2);
        float mX = (float) ((radiusX * Math.sin(Math.toRadians(corner + a))) + width / 2);
        globalCallDownPath.reset();
        globalCallDownPath.moveTo(width / 2, 0);
        globalCallDownPath.lineTo(width / 2, height / 2);
        globalCallDownPath.lineTo(mX, mY);
        createDrawingActions(width, height);
        for (int i = 1; i < percents.length; i++) {
            if (gPercent >= percents[i]) {
                actions[i - 1].draw(globalCallDownPath);
            }
        }
        globalCallDownPath.close();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

}
