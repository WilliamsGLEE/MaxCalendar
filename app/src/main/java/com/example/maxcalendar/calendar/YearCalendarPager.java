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
    private Context mContext;
    private Attrs mAttrs;
    private YCalendarPainter mYCalendarPainter;
    private OnMonthSelectedListener mOnMonthSelectedListener;
    private LocalDate startDate;
    private LocalDate endDate;

    public YearCalendarPager(@NonNull Context context) {
        super(context, null);
    }

    public YearCalendarPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        this.mContext = context;
//        this.mAttrs = AttrsUtil.getAttrs(context, attrs);
//        this.mYCalendarPainter = new YCalendarPainter(context, mAttrs);
//
//        startDate = new LocalDate(mAttrs.startDateString);
//        endDate = new LocalDate(mAttrs.endDateString);
    }

    public YearCalendarPager(Context context,  Attrs attrs, YCalendarPainter painter) {
        super(context);
//        this.mAttrs = attrs;
//        this.mYCalendarPainter = painter;
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener listener) {
        this.mOnMonthSelectedListener = listener;
    }

    public void init(Attrs attrs) {

//        this.mContext = context;
        this.mAttrs = attrs;
        this.mYCalendarPainter = new YCalendarPainter(getContext(), mAttrs);

        startDate = new LocalDate(mAttrs.startDateString);
        endDate = new LocalDate(mAttrs.endDateString);

        mYearCalendarAdapter = new YearCalendarAdapter(getContext(), mAttrs, mYCalendarPainter, startDate, endDate);
        mYearCalendarAdapter.setOnMonthSelectedListener(mOnMonthSelectedListener);
        setAdapter(mYearCalendarAdapter);
        setCurrentItem(new LocalDate().getYear() - startDate.getYear());

        Logger.d("TTTTESTTTT : " + startDate.toString() + " , " + endDate.toString() + " , " + new LocalDate().getYear() + " , " + (new LocalDate().getYear() - startDate.getYear()) + " , " + getCurrentItem());
    }

    void scrollToYear(int year, boolean smoothScroll) {
        setCurrentItem(year - startDate.getYear(), smoothScroll);
    }
}
