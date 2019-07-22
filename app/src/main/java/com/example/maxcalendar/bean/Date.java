package com.example.maxcalendar.bean;

import org.joda.time.LocalDate;

public class Date {

    public LocalDate mLocalDate;
    public LunarDate mLunarDate;
    public String solarHoliday;//公历节日
    public String lunarHoliday;//农历节日
    public String solarTerm;//节气


    public LocalDate getLocalDate() {
        return mLocalDate;
    }

    public void setLocalDate(LocalDate mLocalDate) {
        this.mLocalDate = mLocalDate;
    }

    public LunarDate getLunarDate() {
        return mLunarDate;
    }

    public void setLunarDate(LunarDate lunarDate) {
        mLunarDate = lunarDate;
    }
}
