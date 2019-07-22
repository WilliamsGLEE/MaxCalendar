package com.example.maxcalendar.util;

import android.content.Context;
import android.content.res.Resources;

import com.example.maxcalendar.R;
import com.example.maxcalendar.bean.Date;
import com.example.maxcalendar.bean.LunarDate;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.List;

public class DateUtil {

    private final static int plusMonthForward = 1;
    private final static int plusMonthBackward = -1;

    /**
     * 获取该月界面的所有日期
     * @param date
     * @return
     */
    public static List<LocalDate> getMonthDates(LocalDate date) {

        LocalDate lastMonth = date.plusMonths(plusMonthBackward);
        LocalDate nextMonth = date.plusMonths(plusMonthForward);

        int daysOfThisMonth = date.dayOfMonth().getMaximumValue();
        int daysOfLastMonth = lastMonth.dayOfMonth().getMaximumValue();
        int weekdayOfFirstDayOfThisMonth = new LocalDate(date.getYear(), date.getMonthOfYear(), 1).getDayOfWeek();
        int weekdayOfLastDayOfThisMonth = new LocalDate(date.getYear(), date.getMonthOfYear(), daysOfThisMonth).getDayOfWeek();

        List<LocalDate> dateList = new ArrayList<>();
        if (weekdayOfFirstDayOfThisMonth != 7) {    // 该月第一天不是周日
            for (int i = weekdayOfFirstDayOfThisMonth; i > 0; i--) {
                dateList.add(new LocalDate(lastMonth.getYear(), lastMonth.getMonthOfYear(), daysOfLastMonth - i + 1));
            }
        }
        for (int i = 1; i <= daysOfThisMonth; i++) {
            dateList.add(new LocalDate(date.getYear(), date.getMonthOfYear(), i));
        }
        if (weekdayOfLastDayOfThisMonth == 7) {
            weekdayOfLastDayOfThisMonth = 0;
        }
        for (int i = 0; i < 6 - weekdayOfLastDayOfThisMonth; i++) {
            dateList.add(new LocalDate(nextMonth.getYear(), nextMonth.getMonthOfYear(), i + 1));
        }
        return dateList;
    }

    /**
     * 获取该周界面的所有日期
     * @param localDate
     * @return
     */
    public static List<LocalDate> getWeekDates(LocalDate localDate) {
        List<LocalDate> dateList = new ArrayList<>();
        localDate = getFirstSundayOfWeek(localDate);

        for (int i = 0; i < 7; i++) {
            LocalDate date = localDate.plusDays(i);
            dateList.add(date);
        }
        return dateList;
    }

    /**
     * 获取两个日期期间的月份数
     */
    public static int getIntervalMonths(LocalDate startDate, LocalDate endDate) {
        startDate = startDate.withDayOfMonth(1);
        endDate = endDate.withDayOfMonth(1);
        return Months.monthsBetween(startDate, endDate).getMonths();
    }

    /**
     * 获取两个日期期间的星期数
     */
    public static int getIntervalWeeks(LocalDate startDate, LocalDate endDate) {
        startDate = getFirstSundayOfWeek(startDate);
        endDate = getFirstSundayOfWeek(endDate);
        return Weeks.weeksBetween(startDate, endDate).getWeeks();
    }

    /**
     * 获取该日期的第一个星期天
     */
    public static LocalDate getFirstSundayOfWeek(LocalDate localDate) {
        if (localDate.dayOfWeek().get() == 7) {
            return localDate;
        } else {
            return localDate.minusWeeks(1).withDayOfWeek(7);
        }
    }

    /**
     * 是不是相同的月份
     */
    public static boolean isEqualMonth(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear() && date1.getMonthOfYear() == date2.getMonthOfYear();
    }

    /**
     * 是不是相同的周
     */
    public static boolean isEqualWeek(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear() && date1.getMonthOfYear() == date2.getMonthOfYear() && getFirstSundayOfWeek(date1).equals(getFirstSundayOfWeek(date2));
    }



    /**
     * 是不是今天
     */
    public static boolean isToday(LocalDate date) {
        return new LocalDate().equals(date);
    }

    /**
     * 获取包括农历的完成日期实体
     */
    public static Date getDate(LocalDate localDate) {
        Date date = new Date();
        int solarYear = localDate.getYear();
        int solarMonth = localDate.getMonthOfYear();
        int solarDay = localDate.getDayOfMonth();
        LunarDate lunarDate = LunarUtil.getLunar(solarYear, solarMonth, solarDay);

        date.mLunarDate = lunarDate;
        date.mLocalDate = localDate;
        date.solarTerm = SolarTermUtil.getSolatName(solarYear, (solarMonth < 10 ? ("0" + solarMonth) : (solarMonth + "")) + solarDay);
        date.solarHoliday = HolidayUtil.getSolarHoliday(solarYear, solarMonth, solarDay);
        date.lunarHoliday = HolidayUtil.getLunarHoliday(lunarDate.lunarYear, lunarDate.lunarMonth, lunarDate.lunarDay);
        return date;
    }

    public static boolean isLastMonth(LocalDate clickDate, LocalDate initDate) {
        return clickDate.getYear() < initDate.getYear() ?
                clickDate.getYear() < initDate.getYear() :
                clickDate.getMonthOfYear() < initDate.getMonthOfYear();
    }

    public static boolean isNextMonth(LocalDate clickDate, LocalDate initDate) {
        return clickDate.getYear() > initDate.getYear() ?
                clickDate.getYear() > initDate.getYear() :
                clickDate.getMonthOfYear() > initDate.getMonthOfYear();
    }

    /**
     * 获取某月(MonthView)的高度
     */
    public static int getMonthHeight(LocalDate date, int itemHeight) {

        int daysOfThisMonth = date.dayOfMonth().getMaximumValue();
        int weekdayOfFirstDayOfThisMonth = new LocalDate(date.getYear(), date.getMonthOfYear(), 1).getDayOfWeek();
        int weekdayOfLastDayOfThisMonth = new LocalDate(date.getYear(), date.getMonthOfYear(), daysOfThisMonth).getDayOfWeek();

        if (weekdayOfLastDayOfThisMonth == 7) {
            weekdayOfLastDayOfThisMonth = 0;
        }

        int preDiff = weekdayOfFirstDayOfThisMonth % 7;
        int lastDiff = (6 - weekdayOfLastDayOfThisMonth) % 7;
        return (daysOfThisMonth + preDiff + lastDiff) / 7 * itemHeight;
    }

    public static String[] getWeekbarString(Context context) {
        Resources resources = context.getResources();
        String mon = resources.getString(R.string.MON);
        String the = resources.getString(R.string.THE);
        String wed = resources.getString(R.string.WED);
        String thr = resources.getString(R.string.THR);
        String fri = resources.getString(R.string.FRI);
        String stu = resources.getString(R.string.STU);
        String sun = resources.getString(R.string.SUN);
        return new String[]{sun, the, wed, thr, fri, stu, mon};
    }
}
