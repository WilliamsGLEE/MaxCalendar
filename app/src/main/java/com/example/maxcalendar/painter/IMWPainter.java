package com.example.maxcalendar.painter;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.maxcalendar.bean.Date;

public interface IMWPainter {

    /**
     * 绘制当天
     */
    void drawRealToday(Canvas canvas, Rect rect, Date date, boolean isSelected);

    /**
     * 绘制界面上不是该月的日期
     */
    void drawDayNotInThisMonth(Canvas canvas, Rect rect, Date date);

    /**
     * 绘制本月不是今天的日期
     */
    void drawDayThisMonthNotToday(Canvas canvas, Rect rect, Date date, boolean isSelected);

    /**
     * 绘制日程（根据不同优先级）
     */
    void drawSchemaDate(Canvas canvas, Rect rect, int type);

}
