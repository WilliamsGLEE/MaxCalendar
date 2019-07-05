package com.example.maxcalendar.calendar;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.maxcalendar.adapter.BaseCalendarAdapter;
import com.example.maxcalendar.adapter.MonthCalendarAdapter;
import com.example.maxcalendar.painter.IPainter;

import org.joda.time.LocalDate;

public class MonthCalendarPager extends CalendarPager {

    public MonthCalendarPager(@NonNull Context context, IPainter iPainter) {
        super(context, iPainter);
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Context context, LocalDate initDate) {
        return new MonthCalendarAdapter(context, initDate);
    }
}
