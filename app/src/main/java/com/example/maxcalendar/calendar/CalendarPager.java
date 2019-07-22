package com.example.maxcalendar.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.maxcalendar.adapter.BaseCalendarAdapter;
import com.example.maxcalendar.listener.OnCalendarSelectedChangedListener;
import com.example.maxcalendar.listener.OnMWDateChangeListener;
import com.example.maxcalendar.listener.OnMonthCalendarScrolledListener;
import com.example.maxcalendar.painter.CalendarPainter;
import com.example.maxcalendar.painter.IPainter;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.AttrsUtil;
import com.example.maxcalendar.util.DateUtil;
import com.example.maxcalendar.view.CalendarView;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

import java.util.List;

public abstract class CalendarPager extends ViewPager implements ICalendar {

    protected Context mContext;
    private IPainter mIPainter;
    protected BaseCalendarAdapter mBaseCalendarAdapter;
    protected LocalDate mInitDate;
    protected LocalDate mStartDate, mEndDate;
    protected Attrs mAttrs;
    private boolean mIsInflateCompleted;
    protected LocalDate mSelectedDate;
    private List<LocalDate> mSchemeDateList;
    private boolean isFirstDraw = true;        // 是否第一次绘制
    public CalendarLayout mCalendarLayout;

    protected OnCalendarSelectedChangedListener mOnCalendarSelectedChangedListener;
    protected OnMonthCalendarScrolledListener mOnMonthCalendarScrolledListener;
    private OnMWDateChangeListener mOnMWDateChangeListener;     // 月周切换是折叠中心的回调


    public CalendarPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = AttrsUtil.getAttrs(context, attrs);
        this.mIPainter = new CalendarPainter(context, mAttrs);
        this.mContext = context;
    }

    public CalendarPager(@NonNull Context context, Attrs attrs, IPainter iPainter) {
        super(context);
        this.mAttrs = attrs;
        this.mIPainter = iPainter;
        this.mContext = context;
    }

    protected abstract void init(Context context);

    @Override
    protected void dispatchDraw(Canvas canvas) {        // dispatchDraw：ViewGroup绘制子容器（View.draw()中会调用）
        super.dispatchDraw(canvas);
        if (!mIsInflateCompleted) {
            drawView(getCurrentItem(), true, null);
            mIsInflateCompleted = true;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getParent() != null && getParent() instanceof CalendarLayout) {
            mCalendarLayout = (CalendarLayout) getParent();
        }
        initDate();
        init(mContext);
    }

    protected void initDate() {
//        mCalendarLayout = (CalendarLayout) getParent();
        mInitDate = new LocalDate();
        mStartDate = new LocalDate(mAttrs.startDateString);
        mEndDate = new LocalDate(mAttrs.endDateString);
        mSelectedDate = mInitDate;

        mBaseCalendarAdapter = getCalendarAdapter(mAttrs, mContext, mStartDate, mEndDate, mInitDate);

        setAdapter(mBaseCalendarAdapter);
        setCurrentItem(mBaseCalendarAdapter.getCurrentPosition());
    }


    protected void drawView(int position, boolean reSelect, LocalDate mwChangeDate) {   // reSelect：true：正常变换 mSelectDate
        //           false：通过 OnMWDateChangeListener 变换 mSelectDate
        CalendarView currCalendarView = findViewWithTag(position);
        if (currCalendarView == null) {
            return;
        }
        LocalDate tempDate = mSelectedDate;

        if (!isFirstDraw) {     // 日历第一次打开的时候显示的是当天
            mSelectedDate = currCalendarView.getFirstDate();
        }

        if (reSelect && !isFirstDraw) {             // 调整1：当调整到周模式的时候取消选定 mSelectedDate 为FirstDate，而是选定为月日历选定的日期
            LocalDate date = DateUtil.getFirstSundayOfWeek(mSelectedDate);

            if (date.equals(mSelectedDate) && DateUtil.isEqualWeek(tempDate, mSelectedDate)) {
                mSelectedDate = tempDate;
            }
        }
        if (!reSelect) {            // 调整2：通过 OnMWDateChangeListener 回调时
            mSelectedDate = mwChangeDate;
        }

        isFirstDraw = false;

        currCalendarView.invalidate();

        if (reSelect) {
            callback(reSelect);
        }

    }

    public void onClickLastMonthDate(LocalDate clickDate) {
        jumpDate(clickDate, true);
    }

    public void onClickNextMonthDate(LocalDate clickDate) {
        jumpDate(clickDate, true);
    }

    public void onClickThisMonthOrWeekDate(LocalDate clickDate) {
        if (!mSelectedDate.equals(clickDate)) {
            mSelectedDate = clickDate;
            notifyCalendar();
        }
    }

    public void jumpDate(LocalDate toDate, boolean reSelect) {
        if (!isDateAvailable(toDate)) {
            dealUnAvailableDate();
        }
        CalendarView currCalendarView = findViewWithTag(getCurrentItem());
        LocalDate currDate = currCalendarView.getInitDate();
        int offSet = getIntervalTwoDates(currDate, toDate);
        mSelectedDate = toDate;

        mAttrs.mIndexDate = mSelectedDate;

        if (offSet == 0) {
            drawView(getCurrentItem(), reSelect, toDate);
        } else {
            setCurrentItem(getCurrentItem() + offSet, Math.abs(offSet) == 1);
            drawView(getCurrentItem(), reSelect, toDate);
        }
    }

    public void notifyCalendar() {
        for (int i = 0; i < getChildCount(); i++) {     // 数量为3
            CalendarView calendarView = (CalendarView) getChildAt(i);
            if (calendarView != null) {
                calendarView.invalidate();
            }
        }
        callback(true);
    }

    // 获取当前月/周的第一个日期
    public LocalDate getFirstDate() {
        CalendarView currCalendarView = findViewWithTag(getCurrentItem());
        if (currCalendarView != null) {
            return currCalendarView.getFirstDate();
        }
        return null;
    }

    // localDate到顶部的距离
    public int getDistanceFromTop(LocalDate localDate) {
        CalendarView currCalendarView = findViewWithTag(getCurrentItem());
        if (currCalendarView != null) {
            return currCalendarView.getDistanceFromTop(localDate);
        }
        return 0;
    }


    // PivotDate到顶部的距离
    public int getPivotDistanceFromTop() {
        CalendarView currCalendarView = findViewWithTag(getCurrentItem());
        if (currCalendarView != null) {
            return getDistanceFromTop(mSelectedDate);
        }
        return 0;
    }

    public boolean isDateAvailable(LocalDate date) {
        return date.isAfter(mStartDate) && date.isBefore(mEndDate);
    }

    public void dealUnAvailableDate() {
        if (getVisibility() != VISIBLE) {
            return;
        }
        // TODO : TOAST
    }

    public void callback(boolean reSelect) {
        post(new Runnable() {
            @Override
            public void run() {
                if (mOnCalendarSelectedChangedListener != null && getVisibility() == VISIBLE) {     // 只有当前pager可见时才调用onCalendarSelected
                    // 不可见时说明只是另一个calendar滑动，该calendar不需要显示更新选中日期
                    mOnCalendarSelectedChangedListener.onCalendarSelected(mSelectedDate);
                }

                if (mOnMWDateChangeListener != null) {
                    mOnMWDateChangeListener.onMwDateChange(CalendarPager.this, mSelectedDate);
                }
            }
        });
    }

    public LocalDate getSelectedDate() {
        return mSelectedDate;
    }

    public LocalDate getStartDate() {
        return mStartDate;
    }

    public LocalDate getEndDate() {
        return mEndDate;
    }

    @Override
    public void setCalendarSelectedListener(OnCalendarSelectedChangedListener listener) {
        this.mOnCalendarSelectedChangedListener = listener;
    }

    @Override
    public void setMonthCalendarScrollListener(OnMonthCalendarScrolledListener listener) {
        this.mOnMonthCalendarScrolledListener = listener;
    }

    public void setOnMWDateChangeListener(OnMWDateChangeListener onMWDateChangeListener) {
        this.mOnMWDateChangeListener = onMWDateChangeListener;
    }

    @Override
    public Attrs getAttrs() {
        return mAttrs;
    }

    protected abstract BaseCalendarAdapter getCalendarAdapter(Attrs attrs, Context context, LocalDate startDate, LocalDate endDate, LocalDate initDate);

    protected abstract int getIntervalTwoDates(LocalDate currDate, LocalDate jumpDate);

    protected abstract LocalDate getIntervalDate(LocalDate localDate, int count);

    public IPainter getCalendarPainter() {
        return mIPainter;
    }


}
