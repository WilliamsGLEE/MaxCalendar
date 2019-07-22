package com.example.maxcalendar.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.DateUtil;
import com.example.maxcalendar.view.CalendarView;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

public abstract class BaseCalendarAdapter extends PagerAdapter {

    protected Context mContext;
    protected int mCount;
    protected int mCrr;
    protected LocalDate mInitDate;
    protected Attrs mAttrs;


    public BaseCalendarAdapter(Attrs attrs, Context context, LocalDate startDate, LocalDate endDate, LocalDate initDate) {
        this.mContext = context;
        this.mInitDate = initDate;
        this.mAttrs = attrs;
        this.mCount = getIntervalCount(startDate, endDate) + 1;
        this.mCrr = getIntervalCount(startDate, initDate);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {     // 向ViewPager中添加子View
        CalendarView calendarView = getCalendarView(mAttrs, position);
        calendarView.setTag(position);
        container.addView(calendarView);        // container数量保持3
        return calendarView;                // 返回值object，就是View的id。
        // ViewPager里面对每个页面的管理是key-value形式的，也就是说每个page都有个对应的id（id是object类型），需要对page操作的时候都是通过id来完成的
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);        // 每次滑动的时候都会销毁最远那个view
    }

    @Override
    public int getCount() {
        return mCount;
    }

    protected abstract CalendarView getCalendarView(Attrs attrs, int position);

    /**
     * 当前页的位置
     */
    public int getCurrentPosition() {
        return mCrr;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {   // 这个函数就是用来告诉框架，这个view的id是不是这个object。
        // 谷歌官方推荐把view当id用，所以常规的instantiateItem（）函数的返回值是你自己定义的view，而isViewFromObject（）的返回值是view == object。
        return view == object;
    }

    public abstract int getIntervalCount(LocalDate startDate, LocalDate endDate);

}
