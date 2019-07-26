package com.example.maxcalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.maxcalendar.adapter.YearCalendarAdapter;
import com.example.maxcalendar.listener.OnMonthSelectedListener;
import com.example.maxcalendar.painter.MWCalendarPainter;
import com.example.maxcalendar.painter.IPainter;
import com.example.maxcalendar.painter.YCalendarPainter;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.AttrsUtil;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

public class YearCalendarPager extends ViewPager {

    private YearCalendarAdapter mYearCalendarAdapter;
    private Attrs mAttrs;
    private YCalendarPainter mYCalendarPainter;
    private OnMonthSelectedListener mOnMonthSelectedListener;
    private LocalDate mStartDate;
    private LocalDate mEndDate;

    public YearCalendarPager(@NonNull Context context) {
        super(context, null);
    }

    public YearCalendarPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener listener) {
        this.mOnMonthSelectedListener = listener;
    }

    public void init(Attrs attrs) {
        this.mAttrs = attrs;
        this.mYCalendarPainter = new YCalendarPainter(getContext(), mAttrs);

        mStartDate = new LocalDate(mAttrs.startDateString);
        mEndDate = new LocalDate(mAttrs.endDateString);

        mYearCalendarAdapter = new YearCalendarAdapter(getContext(), mAttrs, mYCalendarPainter, mStartDate, mEndDate);
        mYearCalendarAdapter.setOnMonthSelectedListener(mOnMonthSelectedListener);
        setAdapter(mYearCalendarAdapter);
        setCurrentItem(new LocalDate().getYear() - mStartDate.getYear());
    }

    void scrollToYear(int year, boolean smoothScroll) {
        setCurrentItem(year - mStartDate.getYear(), smoothScroll);
    }

    public int getStartYear() {
        return mStartDate.getYear();
    }
}
