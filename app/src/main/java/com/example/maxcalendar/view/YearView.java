package com.example.maxcalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.maxcalendar.bean.Months;
import com.example.maxcalendar.painter.IPainter;
import com.example.maxcalendar.painter.YCalendarPainter;
import com.example.maxcalendar.util.Attrs;
import com.orhanobut.logger.Logger;

public class YearView extends View {

    private YCalendarPainter mYCalendarPainter;
    private int mLine;      // 行数
    private int mLastCount;
    private Attrs mAttrs;
    private Months currMonth;

    private int mItemWidth;
    private int mItemHeight;

    public YearView(Context context) {
        super(context, null);
    }

    public YearView(Context context, Attrs attrs) {
        super(context);
    }

    public YearView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Attrs attrs, Months month) {
        this.mAttrs = attrs;
        this.currMonth = month;
        countLines();
    }

    public void setYearViewPainter(YCalendarPainter painter) {
        this.mYCalendarPainter = painter;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mItemWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / 7;
        mItemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / 6;

        Rect itemRect;

        int d = 1;
        for (int i = 0; i < mLine; i++) {
            if (i == 0) {               // 第一行
                for (int j = 0; j < (7 - currMonth.getFirstDayDiff()); j++) {
                    itemRect = getItemRect(i, (currMonth.getFirstDayDiff() + j));
                    mYCalendarPainter.drawYearViewDate(canvas, itemRect, d++, false);
                }
            } else if (i == mLine - 1 && mLastCount != 0) {
                for (int j = 0; j < mLastCount; j++) {
                    itemRect = getItemRect(i, j);
                    mYCalendarPainter.drawYearViewDate(canvas, itemRect, d++, false);
                }

            } else {
                for (int j = 0; j < 7; j++) {
                    itemRect = getItemRect(i, j);
                    mYCalendarPainter.drawYearViewDate(canvas, itemRect, d++, false);
                }
            }
        }
    }

    public void countLines() {
        int offset = currMonth.getDayCount() - (7 - currMonth.getFirstDayDiff());
        mLine = 1 + (offset % 7 == 0 ? 0 : 1) + offset / 7;
        mLastCount = offset % 7;
    }

    public Rect getItemRect(int row, int column) {
        return new Rect(mItemWidth * column, mItemHeight * row, mItemWidth * (column + 1), mItemHeight * (row + 1));
    }
}
