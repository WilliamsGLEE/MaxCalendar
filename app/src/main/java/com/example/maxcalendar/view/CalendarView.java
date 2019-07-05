package com.example.maxcalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.maxcalendar.bean.Date;
import com.example.maxcalendar.calendar.CalendarPager;
import com.example.maxcalendar.painter.CalendarPainter;
import com.example.maxcalendar.painter.IPainter;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

public class CalendarView extends View {

    private int mRowNum = 6;    //多少列
    private CalendarPager mCalendarPager;     //pager代码

    public CalendarView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        //pager代码
        if (mCalendarPager == null) {
            mCalendarPager = (CalendarPager) getParent();
        }
        IPainter painter = mCalendarPager.getCalendarPainter();

        //test
//        IPainter painter = new CalendarPainter();

        for (int i = 0; i < mRowNum; i++) {
            for (int j = 0; j < 7; j++) {
                Rect itemRect = getRect(i, j);
                Date date = new Date();
                date.setLocalDate(new LocalDate(2019, 7, 1));
                painter.drawADate(canvas, itemRect, date);
            }
        }

    }

    /**
     *  获取每个元素矩形
     */
    private Rect getRect(int row, int column) {

        int width = getMeasuredWidth();
        int weight = getMeasuredHeight();
        // TODO
        return new Rect(column * width / 7, row * weight /mRowNum, (column + 1) * width / 7, (row + 1) * weight /mRowNum);
    }
}
