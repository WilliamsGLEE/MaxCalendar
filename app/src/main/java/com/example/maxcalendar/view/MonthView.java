package com.example.maxcalendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.DateUtil;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

import java.util.List;

public class MonthView extends CalendarView {

    public MonthView(Attrs attrs, Context context, LocalDate initDate, List<LocalDate> dates) {
        super(attrs, context, initDate, dates);
    }

    @Override
    protected boolean isCurrentMonth(LocalDate thisDate, LocalDate initDate) {
        return DateUtil.isEqualMonth(thisDate, initDate);
    }

    @Override
    protected void dealSingleClickDate(LocalDate clickDate) {
        if (DateUtil.isLastMonth(clickDate, mInitDate)) {
            mCalendarPager.onClickLastMonthDate(clickDate);
        } else if (DateUtil.isNextMonth(clickDate, mInitDate)) {
            mCalendarPager.onClickNextMonthDate(clickDate);
        } else {
            mCalendarPager.onClickThisMonthOrWeekDate(clickDate);
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
