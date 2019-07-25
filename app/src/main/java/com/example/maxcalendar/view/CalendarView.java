package com.example.maxcalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.calendar.BaseCalendarPager;
import com.example.maxcalendar.constant.Constant;
import com.example.maxcalendar.dao.TaskDaoUtil;
import com.example.maxcalendar.painter.IMWPainter;
import com.example.maxcalendar.painter.IPainter;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.DateUtil;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public abstract class CalendarView extends View implements View.OnClickListener {

    protected int mRowNum;    //行数
    protected BaseCalendarPager mBaseCalendarPager;     //pager代码
    protected List<LocalDate> mDateList;
    protected LocalDate mInitDate;
    protected int mItemWidth;
    protected int mItemHeight;
    protected List<Rect> mRectList;
    protected LocalDate mSelectDate;
    protected float mX, mY;     // 点击的X,Y坐标
    protected List<DailyTask> mDailyTaskList;
    protected List<DailyTask> dailyTaskInDay;     // ?

    public CalendarView(Attrs attrs, Context context, LocalDate initDate, List<LocalDate> dates, List<DailyTask> dailyTaskList) {
        super(context);
        this.mInitDate = initDate;
        this.mDateList = dates;
        this.mRowNum = mDateList.size() / 7;
        this.mItemHeight = attrs.mItemHeight;
        this.mDailyTaskList = dailyTaskList;
        dailyTaskInDay = new ArrayList<>();
        mRectList = new ArrayList<>();
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBaseCalendarPager == null) {
            mBaseCalendarPager = (BaseCalendarPager) getParent();
        }

        LocalDate startDate = mBaseCalendarPager.getStartDate();
        LocalDate endDate = mBaseCalendarPager.getEndDate();
        mSelectDate = mBaseCalendarPager.getSelectedDate();

        IMWPainter painter = mBaseCalendarPager.getCalendarPainter();

        mRectList.clear();

        for (int i = 0; i < mRowNum; i++) {
            for (int j = 0; j < 7; j++) {

                LocalDate itemDate = mDateList.get(i * 7 + j);
                Rect itemRect = getRect(i, j);
                mRectList.add(itemRect);
                dailyTaskInDay.clear();

                if (!(itemDate.isBefore(startDate) || itemDate.isAfter(endDate))) {
                    if (isCurrentMonth(itemDate, mInitDate)) {      // 绘制的是当月
                        if (DateUtil.isToday(itemDate)) {
                            painter.drawRealToday(canvas, itemRect, DateUtil.getDate(itemDate), itemDate.equals(mSelectDate));
                        } else {
                            painter.drawDayThisMonthNotToday(canvas, itemRect, DateUtil.getDate(itemDate), itemDate.equals(mSelectDate));
                        }

                        // 获取当天日程的最大紧急度
//                        List<DailyTask> dailyTaskInDay = TaskDaoUtil.queryTaskByYMD(itemDate.getYear(), itemDate.getMonthOfYear(), itemDate.getDayOfMonth());

                        for (int k = 0; k < mDailyTaskList.size(); k++) {
                            if (mDailyTaskList.get(k).getYear() == itemDate.getYear()) {
                                if (mDailyTaskList.get(k).getMonth() == itemDate.getMonthOfYear()) {
                                    if (mDailyTaskList.get(k).getDay() == itemDate.getDayOfMonth()) {
                                        dailyTaskInDay.add(mDailyTaskList.get(k));
                                    }
                                }
                            }
                        }

                        if (dailyTaskInDay != null && dailyTaskInDay.size() > 0) {
                            int maxUrgent = Constant.YELLOW;                    // 最大紧急度
                            for (int k = 0; k < dailyTaskInDay.size(); k++) {
                                if (dailyTaskInDay.get(k).getType() < maxUrgent) {
                                    maxUrgent = dailyTaskInDay.get(k).getType();
                                }
                            }
                            painter.drawSchemaDate(canvas, itemRect, maxUrgent);            // 绘制日程标记
                        }

                    } else {         // 不是当月
                        painter.drawDayNotInThisMonth(canvas, itemRect, DateUtil.getDate(itemDate));
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                mX = event.getX();
                mY = event.getY();
                break;
        }
        return super.onTouchEvent(event);
    }

    public LocalDate getClickDate() {
        if (mItemHeight == 0 || mItemWidth == 0) {
            return null;
        }
        int indexX = (int) mX / mItemWidth;
        if (indexX >= 7) {
            indexX = 6;
        }
        int indexY = (int) mY / mItemHeight;
        int position = indexY * 7 + indexX;
        if (position >= 0 && position < mDateList.size())
            return mDateList.get(position);
        return null;
    }

    // 获取每个元素矩形
    private Rect getRect(int row, int column) {
        this.mItemWidth = getMeasuredWidth() / 7;
        return new Rect(mItemWidth * column, mItemHeight * row, mItemWidth * (column + 1), mItemHeight * (row + 1));
    }

    // 选中的日期到顶部的距离
    public int getDistanceFromTop(LocalDate localDate) {
        int selectIndex = mDateList.indexOf(localDate) / 7;
        return selectIndex * mItemHeight;
    }

    protected abstract boolean isCurrentMonth(LocalDate thisDate, LocalDate initDate);

    protected abstract void dealSingleClickDate(LocalDate clickDate);

    public abstract LocalDate getFirstDate();

    public LocalDate getInitDate() {
        return mInitDate;
    }
}
