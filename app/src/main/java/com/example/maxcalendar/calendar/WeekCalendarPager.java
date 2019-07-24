package com.example.maxcalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.maxcalendar.adapter.BaseCalendarAdapter;
import com.example.maxcalendar.adapter.WeekCalendarAdapter;
import com.example.maxcalendar.painter.IMWPainter;
import com.example.maxcalendar.painter.IPainter;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.DateUtil;

import org.joda.time.LocalDate;

public class WeekCalendarPager extends com.example.maxcalendar.calendar.BaseCalendarPager {

    public WeekCalendarPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WeekCalendarPager(@NonNull Context context, Attrs attrs, IMWPainter imwPainter) {
        super(context, attrs, imwPainter);
    }

    @Override
    protected void init(Context context) {
        this.mContext = context;
        mInitDate = new LocalDate();

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                drawView(position, true, null);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Attrs attrs, Context context, LocalDate startDate, LocalDate endDate, LocalDate initDate) {
        return new WeekCalendarAdapter(attrs, context, startDate, endDate, initDate);
    }

    @Override
    protected int getIntervalTwoDates(LocalDate currDate, LocalDate jumpDate) {
        return DateUtil.getIntervalWeeks(currDate, jumpDate);
    }

    @Override
    protected LocalDate getIntervalDate(LocalDate localDate, int count) {
        return localDate.plusWeeks(count);
    }


}
