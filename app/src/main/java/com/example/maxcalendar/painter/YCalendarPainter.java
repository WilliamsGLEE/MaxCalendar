package com.example.maxcalendar.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.maxcalendar.bean.Date;
import com.example.maxcalendar.util.Attrs;

public class YCalendarPainter extends com.example.maxcalendar.painter.BasePainter {

    public YCalendarPainter(Context context, Attrs attrs) {
        super(context, attrs);
    }

    // 绘制年视图的每个日期
    public void drawYearViewDate(Canvas canvas, Rect rect, int day, boolean isScheme) {

        if (!isScheme) {
            drawText(canvas, mAttrs.yearViewDayTextColor, day + "", Paint.Style.FILL_AND_STROKE, mAttrs.yearViewDayTextSize,
                    mAttrs.yearViewDayStrokeWidth, rect.centerX(), getBaseLineY(rect, mTextPaint));
        } else {
            drawText(canvas, mAttrs.yearViewDaySchemeTextColor, day + "", Paint.Style.FILL_AND_STROKE, mAttrs.yearViewDayTextSize,
                    mAttrs.yearViewDayStrokeWidth, rect.centerX(), getBaseLineY(rect, mTextPaint));
        }
    }
}
