package com.example.maxcalendar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.maxcalendar.listener.OnMonthSelectedListener;
import com.example.maxcalendar.painter.IPainter;
import com.example.maxcalendar.painter.YCalendarPainter;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.view.YearRvView;
import com.example.maxcalendar.view.YearView;

import org.joda.time.LocalDate;

public class YearCalendarAdapter extends PagerAdapter {

    private int yearCount;
    private Attrs mAttrs;
    private Context mContext;
    private YCalendarPainter mYCalendarPainter;
    private OnMonthSelectedListener mOnMonthSelectedListener;
    private LocalDate mStartDate;

    public YearCalendarAdapter(Context context, Attrs attrs, YCalendarPainter painter, LocalDate startDate, LocalDate endDate) {
        this.mAttrs = attrs;
        this.mContext = context;
        this.mYCalendarPainter = painter;
        this.mStartDate = startDate;

        yearCount = endDate.getYear() - startDate.getYear() + 1;
    }

    @Override
    public int getCount() {
        return yearCount;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        YearRvView yearRvView = new YearRvView(mContext, mAttrs, mYCalendarPainter);
        container.addView(yearRvView);
        yearRvView.setOnMonthSelectedListener(mOnMonthSelectedListener);
        yearRvView.setYearMonths(mStartDate.getYear() + position);
        return yearRvView;
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener listener) {
        this.mOnMonthSelectedListener = listener;
    }
}
