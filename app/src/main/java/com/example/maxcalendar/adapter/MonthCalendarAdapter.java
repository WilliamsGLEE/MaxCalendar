package com.example.maxcalendar.adapter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.DateUtil;
import com.example.maxcalendar.view.CalendarView;
import com.example.maxcalendar.view.MonthView;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

import java.util.List;

public class MonthCalendarAdapter extends com.example.maxcalendar.adapter.BaseCalendarAdapter {

    public MonthCalendarAdapter(Attrs attrs, Context context, LocalDate startDate, LocalDate endDate, LocalDate initDate) {
        super(attrs, context, startDate, endDate, initDate);
    }

    @Override
    protected CalendarView getCalendarView(Attrs attrs, int position) {
        LocalDate localDate = mInitDate.plusMonths(position - mCrr);
        List<LocalDate> dateList = DateUtil.getMonthDates(localDate);
        return new MonthView(attrs, mContext, localDate, dateList);
    }

    @Override
    public int getIntervalCount(LocalDate startDate, LocalDate endDate) {
        return DateUtil.getIntervalMonths(startDate, endDate);
    }
}
