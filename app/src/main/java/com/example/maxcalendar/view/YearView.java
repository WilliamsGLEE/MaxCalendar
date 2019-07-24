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

//        mItemWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / 7;
//        mItemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / 6;
//
//        Logger.d("TTTTTESTTTTT : " + mItemWidth + " , " + mItemHeight);

        countLines();
    }

    public void measureSize(int height, int width) {
//        mItemWidth = width;
//        mItemHeight = height;

//        mItemWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / 7;
//        mItemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / 6;
    }


    public void setYearViewPainter(YCalendarPainter painter) {
        this.mYCalendarPainter = painter;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        YearRvView yearRvView = (YearRvView) getParent();
//        IPainter painter = yearRvView.getYearViewPainter();

        mItemWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / 7;
        mItemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / 6;

        Logger.d("TTTTTESTTTTT : " + mItemWidth + " , " + mItemHeight);


        Rect itemRect;

        int d = 1;
        for (int i = 0; i < mLine; i++) {
            if (i == 0) {               // 第一行
                for (int j = 0; j < (7 - currMonth.getFirstDayDiff()); j++) {

                    itemRect = getItemRect(i, (currMonth.getFirstDayDiff() + j));
                    mYCalendarPainter.drawYearViewDate(canvas, itemRect, d++, false);

//                    ++d;
//                    canvas.drawText(String.valueOf(j + 1), mDiff * w + j * w + pLeft + w / 2, h, isScheme(d) ? mSchemePaint : mPaint);
                }
            } else if (i == mLine - 1 && mLastCount != 0) {
//                int first = currMonth.getDayCount() - mLastCount + 1;
                for (int j = 0; j < mLastCount; j++) {
//                    ++d;

                    itemRect = getItemRect(i, j);
                    mYCalendarPainter.drawYearViewDate(canvas, itemRect, d++, false);

//                    canvas.drawText(String.valueOf(first), j * w + pLeft + w / 2, (i + 1) * h, isScheme(d) ? mSchemePaint : mPaint);
//                    ++first;
                }
            } else {
//                int first = i * 7 - mDiff + 1;
//                for (int j = 0; j < 7; j++) {
//                    ++d;
//                    canvas.drawText(String.valueOf(first), j * w + pLeft + w / 2, (i + 1) * h, isScheme(d) ? mSchemePaint : mPaint);
//                    ++first;
//                }

                for (int j = 0; j < 7; j++) {
                    itemRect = getItemRect(i, j);
                    mYCalendarPainter.drawYearViewDate(canvas, itemRect, d++, false);
                }
//                int first = i * 7 - mDiff + 1;
//                for (int j = 0; j < 7; j++) {
//                    ++d;
//                    canvas.drawText(String.valueOf(first), j * w + pLeft + w / 2, (i + 1) * h, isScheme(d) ? mSchemePaint : mPaint);
//                    ++first;
//                }

            }
        }
    }

    public void countLines() {
        int offset = currMonth.getDayCount() - (7 - currMonth.getFirstDayDiff());
        mLine = 1 + (offset % 7 == 0 ? 0 : 1) + offset / 7;
        mLastCount = offset % 7;
    }

    public Rect getItemRect(int row, int column) {

//        int itemWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / 7;
//        int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / 6;

        return new Rect(mItemWidth * column, mItemHeight * row, mItemWidth * (column + 1), mItemHeight * (row + 1));

    }

}
