package com.example.maxcalendar.painter;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.maxcalendar.bean.Date;

public interface IPainter {

    /**
     * 绘制当天
     */
    void drawToday();

    /**
     * 绘制当前月和周
     */
    void drawThisMonthAndWeek();

    /**
     * 绘制一个数字
     */
    void drawADate(Canvas canvas, Rect rect, Date date);
}
