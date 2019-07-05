package com.example.maxcalendar.calendar;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.maxcalendar.adapter.BaseCalendarAdapter;
import com.example.maxcalendar.painter.IPainter;

import org.joda.time.LocalDate;

public class WeekCalendarPager extends CalendarPager {

    public WeekCalendarPager(@NonNull Context context, IPainter iPainter) {
        super(context, iPainter);
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Context context, LocalDate initDate) {
        return null;
    }
}
