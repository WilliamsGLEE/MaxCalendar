package com.example.maxcalendar.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

import com.example.maxcalendar.bean.Date;
import com.example.maxcalendar.calendar.ICalendar;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.OtherUtil;
import com.orhanobut.logger.Logger;

public class MWCalendarPainter extends BasePainter implements IMWPainter {

    public MWCalendarPainter(Context context, Attrs attrs) {
        super(context, attrs);
    }

    @Override
    public void drawRealToday(Canvas canvas, Rect rect, Date date, boolean isSelected) {
        if (isSelected) {
            drawBackGround(canvas, mAttrs.selectBackgroundColor, Paint.Style.FILL, rect.left + mAttrs.itemRectPadding,
                    rect.top + mAttrs.itemRectPadding, rect.right - mAttrs.itemRectPadding, rect.bottom - mAttrs.itemRectPadding);
        }
        // 绘制数字
        drawText(canvas, mAttrs.todayTextColor, date.mLocalDate.getDayOfMonth() + "", Paint.Style.FILL_AND_STROKE,
                mAttrs.calendarDateNumberSize, mAttrs.numberStrokeWidth, rect.centerX(), rect.centerY());

        // 绘制农历
        drawText(canvas, mAttrs.todayTextColor, getLunarText(date), Paint.Style.FILL_AND_STROKE, mAttrs.calendarDateLunarTextSize,
                mAttrs.lunarStrokeWidth, rect.centerX(), rect.centerY() + mAttrs.lunarDistance);
    }


    @Override
    public void drawDayNotInThisMonth(Canvas canvas, Rect rect, Date date) {

        // 绘制数字
        drawText(canvas, mAttrs.notThisMonthTextColor, date.mLocalDate.getDayOfMonth() + "", Paint.Style.FILL_AND_STROKE,
                mAttrs.calendarDateNumberSize, mAttrs.numberStrokeWidth, rect.centerX(), rect.centerY());

        // 绘制农历
        drawText(canvas, mAttrs.notThisMonthTextColor, getLunarText(date), Paint.Style.FILL_AND_STROKE, mAttrs.calendarDateLunarTextSize,
                mAttrs.lunarStrokeWidth, rect.centerX(), rect.centerY() + mAttrs.lunarDistance);
    }

    @Override
    public void drawDayThisMonthNotToday(Canvas canvas, Rect rect, Date date, boolean isSelected) {

        if (isSelected) {
            drawBackGround(canvas, mAttrs.selectBackgroundColor, Paint.Style.FILL, rect.left + mAttrs.itemRectPadding,
                    rect.top + mAttrs.itemRectPadding, rect.right - mAttrs.itemRectPadding, rect.bottom - mAttrs.itemRectPadding);
        }
        // 绘制数字
        drawText(canvas, mAttrs.thisMonthTextColor, date.mLocalDate.getDayOfMonth() + "", Paint.Style.FILL_AND_STROKE,
                mAttrs.calendarDateNumberSize, mAttrs.numberStrokeWidth, rect.centerX(), rect.centerY());

        // 绘制农历
        drawText(canvas, mAttrs.notThisMonthTextColor, getLunarText(date), Paint.Style.FILL_AND_STROKE, mAttrs.calendarDateLunarTextSize,
                mAttrs.lunarStrokeWidth, rect.centerX(), rect.centerY() + mAttrs.lunarDistance);
    }

    @Override
    public void drawSchemaDate(Canvas canvas, Rect rect, int type) {
        drawBackGround(canvas, mContext.getResources().getColor(OtherUtil.eventTypeToColor(type)), Paint.Style.FILL, rect.left + mAttrs.itemRectPadding + mAttrs.schemeRectPaddingWidth,
                rect.top + mAttrs.itemRectPadding + mAttrs.schemeRectPaddingTop, rect.right - mAttrs.itemRectPadding - mAttrs.schemeRectPaddingWidth,
                rect.bottom - mAttrs.itemRectPadding - mAttrs.schemeRectPaddingBottom);
    }

    // 获取当日农历栏
    private String getLunarText(Date date) {

        String lunarString;
        if (!TextUtils.isEmpty(date.solarHoliday)) {        // 农历栏优先节日
            lunarString = date.solarHoliday;
        } else if (!TextUtils.isEmpty(date.lunarHoliday)) {
            lunarString = date.lunarHoliday;
        } else if (!TextUtils.isEmpty(date.solarTerm)) {
            lunarString = date.solarTerm;
        } else {
            lunarString = date.getLunarDate().lunarDrawStr;
        }
        return lunarString;
    }
}


//package com.example.maxcalendar.painter;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.text.TextUtils;
//
//import com.example.maxcalendar.bean.Date;
//import com.example.maxcalendar.calendar.ICalendar;
//import com.example.maxcalendar.util.Attrs;
//import com.example.maxcalendar.util.OtherUtil;
//import com.orhanobut.logger.Logger;
//
//public class MWCalendarPainter implements IPainter {
//
//    private Paint mTextPaint;
//    private Paint mRectPaint;
//    private Context mContext;
//    private Attrs mAttrs;
//
//    public MWCalendarPainter(Context context, Attrs attrs) {
//        this.mAttrs = attrs;
//        this.mContext = context;
//        this.mTextPaint = new Paint();
//        this.mRectPaint = new Paint();
//        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
//        this.mTextPaint.setTextSize(15);
//    }
//
//    @Override
//    public void drawADate(Canvas canvas, Rect rect, Date date) {
//        mTextPaint.setColor(Color.BLACK);
//        canvas.drawText(date.mLocalDate.getDayOfMonth() + "", rect.centerX(), getBaseLineY(rect, mTextPaint), mTextPaint);
//    }
//
//    @Override
//    public void drawRealToday(Canvas canvas, Rect rect, Date date, boolean isSelected) {
//        if (isSelected) {
//            drawItemBackGround(canvas, rect, mAttrs.selectBackgroundColor);
//        }
//        drawNumber(canvas, rect, mAttrs.todayTextColor, date);
//        drawLunarText(canvas, rect, mAttrs.todayTextColor, date);
//    }
//
//
//    @Override
//    public void drawDayNotInThisMonth(Canvas canvas, Rect rect, Date date) {
//        drawNumber(canvas, rect, mAttrs.notThisMonthTextColor, date);
//        drawLunarText(canvas, rect, mAttrs.notThisMonthTextColor, date);
//    }
//
//    @Override
//    public void drawDayThisMonthNotToday(Canvas canvas, Rect rect, Date date, boolean isSelected) {
//        if (isSelected) {
//            drawItemBackGround(canvas, rect, mAttrs.selectBackgroundColor);
//        }
//        drawNumber(canvas, rect, mAttrs.thisMonthTextColor, date);
//        drawLunarText(canvas, rect, mAttrs.notThisMonthTextColor, date);
//    }
//
//    @Override
//    public void drawSchemaDate(Canvas canvas, Rect rect, Date date, int schema) {
//
//    }
//
//    @Override
//    public void drawThisMonthAndWeek() {
//
//    }
//
//    public void drawNumber(Canvas canvas, Rect rect, int color, Date date) {
//        mTextPaint.setColor(color);
//        mTextPaint.setAntiAlias(true);      // 消除锯齿
//        mTextPaint.setTextSize(mAttrs.calendarDateNumberSize);
//        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mTextPaint.setStrokeWidth(mAttrs.numberStrokeWidth);
////        canvas.drawText(date.mLocalDate.getDayOfMonth() + "", rect.centerX(), getBaseLineY(rect, mTextPaint), mTextPaint);
//        canvas.drawText(date.mLocalDate.getDayOfMonth() + "", rect.centerX(), rect.centerY(), mTextPaint);
//    }
//
//    public void drawLunarText(Canvas canvas, Rect rect, int color, Date date) {
//        mTextPaint.setColor(color);
//        mTextPaint.setAntiAlias(true);
//        mTextPaint.setTextSize(mAttrs.calendarDateLunarTextSize);
//        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mTextPaint.setStrokeWidth(mAttrs.lunarStrokeWidth);
//        String lunarString;
//        if (!TextUtils.isEmpty(date.solarHoliday)) {        // 农历栏优先节日
//            lunarString = date.solarHoliday;
//        } else if (!TextUtils.isEmpty(date.lunarHoliday)) {
//            lunarString = date.lunarHoliday;
//        } else if (!TextUtils.isEmpty(date.solarTerm)) {
//            lunarString = date.solarTerm;
//        } else {
//            lunarString = date.getLunarDate().lunarDrawStr;
//        }
//        canvas.drawText(lunarString + "", rect.centerX(), rect.centerY() + mAttrs.lunarDistance, mTextPaint);
//    }
//
//    public void drawItemBackGround(Canvas canvas, Rect rect, int color) {
//        mRectPaint.setColor(color);
//        mRectPaint.setStyle(Paint.Style.FILL);
//        canvas.drawRect(rect.left + mAttrs.itemRectPadding, rect.top + mAttrs.itemRectPadding,
//                rect.right - mAttrs.itemRectPadding, rect.bottom - mAttrs.itemRectPadding, mRectPaint);
//    }
//
//
//    private int getBaseLineY(Rect rect, Paint paint) {
//        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
//
//        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;   // 基于基线
//        return (int) (rect.centerY() + distance);
//    }
//
//}
