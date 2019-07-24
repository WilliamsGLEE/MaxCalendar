package com.example.maxcalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.maxcalendar.adapter.BaseCalendarAdapter;
import com.example.maxcalendar.adapter.MonthCalendarAdapter;
import com.example.maxcalendar.painter.IMWPainter;
import com.example.maxcalendar.painter.IPainter;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.DateUtil;

import org.joda.time.LocalDate;

public class MonthCalendarPager extends com.example.maxcalendar.calendar.BaseCalendarPager {

    private int mPreMonthHeight, mNextMonthHeight, mCurrMonthHeight;

    public MonthCalendarPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MonthCalendarPager(@NonNull Context context, Attrs attrs, IMWPainter imwPainter) {
        super(context, attrs, imwPainter);
    }

    @Override
    protected void init(Context context) {
        // 设置初始monthHeight
        updateMonthViewHeight(mInitDate);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = mCurrMonthHeight;

        if (getParent() != null && getParent() instanceof com.example.maxcalendar.calendar.CalendarLayout) {
            mCalendarLayout = (com.example.maxcalendar.calendar.CalendarLayout) getParent();
        }

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {  // positionOffset 当前所在页面偏移百分比(系统根据手机左边缘所处位置计算值)
                int height;
                int dy;

                if (position < getCurrentItem()) {      // 右滑(显示左边的页面)
                    dy = (int) ((mCurrMonthHeight - mPreMonthHeight) * positionOffset);
                    height = mPreMonthHeight + dy;
                } else {                            // 左滑(显示右边的页面)
                    dy = (int) ((mNextMonthHeight - mCurrMonthHeight) * positionOffset);
                    height = mCurrMonthHeight + dy;
                }

                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = height;
                setLayoutParams(params);
                mOnMonthCalendarScrolledListener.onMonthCalendarScroll(height);
            }

            @Override
            public void onPageSelected(int position) {
                drawView(position, true, null);
                updateMonthViewHeight(mSelectedDate);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected BaseCalendarAdapter getCalendarAdapter(Attrs attrs, Context context, LocalDate startDate, LocalDate endDate, LocalDate initDate) {
        return new MonthCalendarAdapter(attrs, context, startDate, endDate, initDate);
    }

    @Override
    protected int getIntervalTwoDates(LocalDate currDate, LocalDate jumpDate) {
        return DateUtil.getIntervalMonths(currDate, jumpDate);
    }

    @Override
    protected LocalDate getIntervalDate(LocalDate localDate, int count) {
        LocalDate date = localDate.plusMonths(count);
        return date;
    }

    public void updateMonthViewHeight(LocalDate localDate) {
        if (getVisibility() == VISIBLE) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();

            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }

            layoutParams.height = DateUtil.getMonthHeight(localDate, mAttrs.mItemHeight);       // npe
            setLayoutParams(layoutParams);
        }

        if (mCalendarLayout != null) {
            mAttrs.mIndexDate = localDate;
        }

        mAttrs.mIndexDate = localDate;

        mCurrMonthHeight = DateUtil.getMonthHeight(localDate, mAttrs.mItemHeight);
        mPreMonthHeight = DateUtil.getMonthHeight(localDate.minusMonths(1), mAttrs.mItemHeight);
        mNextMonthHeight = DateUtil.getMonthHeight(localDate.minusMonths(-1), mAttrs.mItemHeight);
    }

}
