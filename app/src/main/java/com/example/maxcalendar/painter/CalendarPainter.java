package com.example.maxcalendar.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.maxcalendar.bean.Date;
import com.example.maxcalendar.calendar.ICalendar;
import com.orhanobut.logger.Logger;

public class CalendarPainter implements IPainter {

    private Paint mPaint;
    private Context mContext;

    public CalendarPainter() {
        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(15);
    }

    @Override
    public void drawToday() {

    }

    @Override
    public void drawThisMonthAndWeek() {

    }

    @Override
    public void drawADate(Canvas canvas, Rect rect, Date date) {
        mPaint.setColor(Color.LTGRAY);
        canvas.drawRect(rect, mPaint);
        mPaint.setColor(Color.BLACK);
        canvas.drawText(date.mLocalDate.getDayOfMonth() + "", rect.centerX(), getBaseLineY(rect, mPaint), mPaint);

        Logger.d("c : " + date.mLocalDate.getDayOfMonth() + "rectX : " + rect.centerX() + "rectY : " + rect.centerY());
    }

    private int getBaseLineY(Rect rect, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;   // 基于基线
        return (int) (rect.centerY() + distance);
    }
}
