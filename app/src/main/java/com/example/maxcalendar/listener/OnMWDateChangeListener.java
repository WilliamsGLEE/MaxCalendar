package com.example.maxcalendar.listener;

import com.example.maxcalendar.calendar.CalendarPager;

import org.joda.time.LocalDate;

/**
 * 月周模式切换时，相应地调整另一个ViewPager的日期，以达到再次展开/拉下时能显示正确的日期的目的
 */
public interface OnMWDateChangeListener {

    void onMwDateChange(CalendarPager calendarPager, LocalDate localDate);
}
