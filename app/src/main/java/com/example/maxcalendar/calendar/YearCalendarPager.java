package com.example.maxcalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.maxcalendar.adapter.YearCalendarAdapter;
import com.example.maxcalendar.painter.CalendarPainter;
import com.example.maxcalendar.painter.IPainter;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.AttrsUtil;

public class YearCalendarPager extends ViewPager {

    private YearCalendarAdapter mYearCalendarAdapter;
    private Context mContext;
    private Attrs mAttrs;
    private IPainter mIPainter;

    public YearCalendarPager(@NonNull Context context) {
        super(context, null);
    }

    public YearCalendarPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mAttrs = AttrsUtil.getAttrs(context, attrs);
        this.mIPainter = new CalendarPainter(context, mAttrs);
    }

    public YearCalendarPager(Context context, IPainter iPainter, Attrs attrs) {
        super(context);
        this.mAttrs = attrs;
        this.mIPainter = iPainter;

    }

    private void init() {
        mYearCalendarAdapter = new YearCalendarAdapter(mContext, mAttrs);
    }
}
