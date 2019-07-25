package com.example.maxcalendar.view;

import android.content.Context;
import android.view.View;

import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.util.Attrs;

import org.joda.time.LocalDate;

import java.util.List;

public class WeekView extends com.example.maxcalendar.view.CalendarView {

    public WeekView(Attrs attrs, Context context, LocalDate initDate, List<LocalDate> dates, List<DailyTask> dailyTaskList) {
        super(attrs, context, initDate, dates, dailyTaskList);
    }

    @Override
    protected boolean isCurrentMonth(LocalDate thisDate, LocalDate initDate) {
        return true;
    }

    @Override
    protected void dealSingleClickDate(LocalDate clickDate) {
        mBaseCalendarPager.onClickThisMonthOrWeekDate(clickDate);
    }

    @Override
    public LocalDate getFirstDate() {
        return mDateList.get(0);
    }

    @Override
    public void onClick(View view) {
        LocalDate clickDate = getClickDate();
        if (clickDate == null) {
            return;
        }
        dealSingleClickDate(clickDate);
    }
}
