package com.example.maxcalendar.listener;

import org.joda.time.LocalDate;

/**
 * 日历月份/周份改变（onPagerSelected...）
 */
public interface OnCalendarSelectedChangedListener {

    void onCalendarSelected(LocalDate localDate);
}
