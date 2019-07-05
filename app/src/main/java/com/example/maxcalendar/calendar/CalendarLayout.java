package com.example.maxcalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.maxcalendar.painter.CalendarPainter;
import com.example.maxcalendar.painter.IPainter;
import com.orhanobut.logger.Logger;

public class CalendarLayout extends FrameLayout {

    private CalendarPager mCalendarPager;
    private IPainter mIPainter;

    //日历的高度
    private int height = 1000;

    public CalendarLayout(Context context) {
        this(context, null);
    }

    public CalendarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CalendarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mIPainter = new CalendarPainter();

        Logger.d("CalendarLayout......");

        mCalendarPager = new MonthCalendarPager(context, mIPainter);

        addView(mCalendarPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int measureWidth = getMeasuredWidth();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        mCalendarPager.layout(0 + paddingLeft, 0, measureWidth - paddingRight, height);
    }

    /**
     * 当View中所有的子控件均被映射成xml后触发
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //TODO childView
    }
}
