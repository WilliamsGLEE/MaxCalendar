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
        this.mContext = context;
        this.mAttrs = AttrsUtil.getAttrs(context, attrs);
        this.mYCalendarPainter = new YCalendarPainter(context, mAttrs);

        startDate = new LocalDate(mAttrs.startDateString);
        endDate = new LocalDate(mAttrs.endDateString);
    }

    public YearCalendarPager(Context context,  Attrs attrs, YCalendarPainter painter) {
        super(context);
        this.mAttrs = attrs;
        this.mYCalendarPainter = painter;

    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener listener) {
        this.mOnMonthSelectedListener = listener;
    }



    private void init() {
        mYearCalendarAdapter = new YearCalendarAdapter(mContext, mAttrs, mYCalendarPainter, startDate, endDate);
        mYearCalendarAdapter.setOnMonthSelectedListener(mOnMonthSelectedListener);
        setCurrentItem(new LocalDate().getYear() - startDate.getYear());
    }
}
