package com.example.maxcalendar.painter;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 最基本的绘制类
 */
public interface IPainter {

    void drawText(Canvas canvas, int color, String string, Paint.Style style, float textSize, float strokeWidth, float x, float y);

    void drawBackGround(Canvas canvas, int color, Paint.Style style, float left, float top, float right, float bottom);

}
