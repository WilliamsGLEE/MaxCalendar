package com.example.maxcalendar.view;

import android.content.Context;
import android.view.View;

import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.DateUtil;

import org.joda.time.LocalDate;

import java.util.List;

public class MonthView extends com.example.maxcalendar.view.CalendarView {

    public MonthView(Attrs attrs, Context context, LocalDate initDate, List<LocalDate> dates, List<DailyTask> dailyTaskList) {
        super(attrs, context, initDate, dates, dailyTaskList);
    }

    @Override
    protected boolean isCurrentMonth(LocalDate thisDate, LocalDate initDate) {
        return DateUtil.isEqualMonth(thisDate, initDate);
    }

    @Override
    protected void dealSingleClickDate(LocalDate clickDate) {
        if (DateUtil.isLastMonth(clickDate, mInitDate)) {
            mBaseCalendarPager.onClickLastMonthDate(clickDate);
        } else if (DateUtil.isNextMonth(clickDate, mInitDate)) {
            mBaseCalendarPager.onClickNextMonthDate(clickDate);
        } else {
            mBaseCalendarPager.onClickThisMonthOrWeekDate(clickDate);
        }
    }


    @Override
    public void onClick(View view) {

        LocalDate clickDate = getClickDate();
        if (clickDate == null) {
            return;
        }
        dealSingleClickDate(clickDate);
    }

    @Override
    public LocalDate getFirstDate() {
        return new LocalDate(mInitDate.getYear(), mInitDate.getMonthOfYear(), 1);
    }
}
