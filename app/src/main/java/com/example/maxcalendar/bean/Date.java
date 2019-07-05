package com.example.maxcalendar.bean;

import org.joda.time.LocalDate;

public class Date {

    public LocalDate mLocalDate;
    public LunarDate mLunarDate;

    public LocalDate getLocalDate() {
        return mLocalDate;
    }

    public void setLocalDate(LocalDate mLocalDate) {
        this.mLocalDate = mLocalDate;
    }

    public com.example.maxcalendar.bean.LunarDate getLunarDate() {
        return mLunarDate;
    }

    public void setLunarDate(com.example.maxcalendar.bean.LunarDate lunarDate) {
        mLunarDate = lunarDate;
    }
}
