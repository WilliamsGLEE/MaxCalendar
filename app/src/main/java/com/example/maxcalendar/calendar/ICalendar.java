package com.example.maxcalendar.calendar;

import com.example.maxcalendar.listener.OnCalendarSelectedChangedListener;
import com.example.maxcalendar.listener.OnMonthCalendarScrolledListener;
import com.example.maxcalendar.util.Attrs;

public interface ICalendar {

    Attrs getAttrs();

    void setCalendarSelectedListener(OnCalendarSelectedChangedListener listener);

    void setMonthCalendarScrollListener(OnMonthCalendarScrolledListener listener);
}
