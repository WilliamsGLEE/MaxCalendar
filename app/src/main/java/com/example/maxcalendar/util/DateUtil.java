package com.example.maxcalendar.util;

import com.example.maxcalendar.bean.Date;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

import java.util.List;

public class DateUtil {

    public static List<LocalDate> getMonthDates(LocalDate date) {

        LocalDate lastMonth = date.plusMonths(-1);
        LocalDate nextMonth = date.plusMonths(1);

        int days = date.getDayOfMonth();
//        int



        Logger.d("date :  " + date.toString());
        return null;
    }
}
