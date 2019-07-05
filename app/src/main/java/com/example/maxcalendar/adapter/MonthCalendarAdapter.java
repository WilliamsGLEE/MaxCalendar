package com.example.maxcalendar.adapter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.maxcalendar.util.DateUtil;
import com.example.maxcalendar.view.CalendarView;
import com.example.maxcalendar.view.MonthView;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.internal.Utils;

public class MonthCalendarAdapter extends BaseCalendarAdapter {

    public MonthCalendarAdapter(Context context, LocalDate initDate) {
        super(context, initDate);
    }

    @Override
    protected CalendarView getCalendarView(int position) {
        Logger.d("RETURN MONTH VIEW");
        List<LocalDate> dateList = DateUtil.getMonthDates(mInitDate);
        return new MonthView(mContext);
    }
}
