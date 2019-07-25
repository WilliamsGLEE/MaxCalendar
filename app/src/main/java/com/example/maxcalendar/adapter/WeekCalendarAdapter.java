package com.example.maxcalendar.adapter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.DateUtil;
import com.example.maxcalendar.view.CalendarView;
import com.example.maxcalendar.view.MonthView;
import com.example.maxcalendar.view.WeekView;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

import java.util.List;

public class WeekCalendarAdapter extends com.example.maxcalendar.adapter.BaseCalendarAdapter {

    public WeekCalendarAdapter(Attrs attrs, Context context, LocalDate startDate, LocalDate endDate, LocalDate initDate, List<DailyTask> dailyTaskList) {
        super(attrs, context, startDate, endDate, initDate, dailyTaskList);
    }

    @Override
    protected CalendarView getCalendarView(Attrs attrs, int position, List<DailyTask> dailyTaskList) {
        LocalDate localDate = mInitDate.plusDays((position - mCrr) * 7);        // 周日期：*7
        List<LocalDate> dateList = DateUtil.getWeekDates(localDate);
        return new WeekView(attrs, mContext, localDate, dateList, dailyTaskList);
    }

    @Override
    public int getIntervalCount(LocalDate startDate, LocalDate endDate) {
        return DateUtil.getIntervalWeeks(startDate, endDate);
    }
}
