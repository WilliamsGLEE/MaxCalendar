package com.example.maxcalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.maxcalendar.util.Attrs;

public class YearView extends View {

    private Paint mPaint = new Paint();
    private Paint mSchamePaint = new Paint();

    public YearView(Context context) {
        super(context, null);
    }

    public YearView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setAntiAlias(true);      // 消除锯齿
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    public YearView(Context context, Attrs attrs) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();


    }
}
