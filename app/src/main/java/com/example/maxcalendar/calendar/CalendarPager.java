package com.example.maxcalendar.calendar;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.example.maxcalendar.adapter.BaseCalendarAdapter;
import com.example.maxcalendar.painter.CalendarPainter;
import com.example.maxcalendar.painter.IPainter;
import com.example.maxcalendar.view.CalendarView;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

public abstract class CalendarPager extends ViewPager implements ICalendar {

    protected Context mContext;
    private IPainter mIPainter;
    protected BaseCalendarAdapter mBaseCalendarAdapter;
    protected LocalDate mInitDate;

    public CalendarPager(@NonNull Context context, IPainter iPainter) {
        super(context);

        this.mIPainter = iPainter;
        init(context);
    }

    protected void init(Context context) {
        this.mContext = context;
        mInitDate = new LocalDate();
        mBaseCalendarAdapter = getCalendarAdapter(mContext, mInitDate);
        setAdapter(mBaseCalendarAdapter);

        Logger.d("testtt  " + "init  " + "adapter : " + mBaseCalendarAdapter.getClass());

//        int currItem = mBaseCalendarAdapter.get
        setCurrentItem(0);

//        drawView(0);
    }

    private void drawView(int position) {
        CalendarView currCalendarView = findViewWithTag(position);
        currCalendarView.invalidate();
    }

    @Override
    public void method() {

    }

    protected abstract BaseCalendarAdapter getCalendarAdapter(Context context, LocalDate initDate);


    public IPainter getCalendarPainter() {
        return mIPainter;
    }


}
