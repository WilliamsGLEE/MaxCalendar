package com.example.maxcalendar.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

import com.example.maxcalendar.bean.Date;
import com.example.maxcalendar.util.Attrs;

public abstract class BasePainter implements IPainter {

    protected Paint mTextPaint;
    protected Paint mRectPaint;
    protected Context mContext;
    protected Attrs mAttrs;

    public BasePainter(Context context, Attrs attrs) {
        this.mAttrs = attrs;
        this.mContext = context;
        this.mTextPaint = new Paint();
        this.mRectPaint = new Paint();
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void drawText(Canvas canvas, int color, String string, Paint.Style style, float textSize, float strokeWidth, float x, float y) {
        mTextPaint.setColor(color);
        mTextPaint.setAntiAlias(true);          // 消除锯齿
        mTextPaint.setTextSize(textSize);
        mTextPaint.setStyle(style);
        mTextPaint.setStrokeWidth(strokeWidth);
        canvas.drawText(string, x, y, mTextPaint);
    }

    @Override
    public void drawBackGround(Canvas canvas, int color, Paint.Style style, float left, float top, float right, float bottom) {
        mRectPaint.setColor(color);
        mRectPaint.setStyle(style);
        canvas.drawRect(left, top, right, bottom, mRectPaint);
    }

    // 获取Y轴的baseLine
    protected int getBaseLineY(Rect rect, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;   // 基于基线
        return (int) (rect.centerY() + distance);
    }
}
