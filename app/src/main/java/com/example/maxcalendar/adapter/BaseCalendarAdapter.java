package com.example.maxcalendar.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.maxcalendar.view.CalendarView;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

public abstract class BaseCalendarAdapter extends PagerAdapter {

    protected Context mContext;
    private int mCount;
    private int mCrr;
    protected LocalDate mInitDate;


    public BaseCalendarAdapter(Context context, LocalDate initDate) {
        this.mContext = context;
        this.mInitDate = initDate;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Logger.d("testtt  instantiateItem...");
        CalendarView calendarView = getCalendarView(position);
        calendarView.setTag(position);
        container.addView(calendarView);
        return calendarView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 1;
    }

    protected abstract CalendarView getCalendarView(int position);

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
