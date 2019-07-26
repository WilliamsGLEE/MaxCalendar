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

public class MWCalendarPainter extends BasePainter implements com.example.maxcalendar.painter.IMWPainter {

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