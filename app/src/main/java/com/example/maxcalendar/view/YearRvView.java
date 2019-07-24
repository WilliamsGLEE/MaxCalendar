package com.example.maxcalendar.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maxcalendar.adapter.YearRvAdapter;
import com.example.maxcalendar.bean.Months;
import com.example.maxcalendar.listener.OnClickRvListener;
import com.example.maxcalendar.listener.OnMonthSelectedListener;
import com.example.maxcalendar.painter.MWCalendarPainter;
import com.example.maxcalendar.painter.IPainter;
import com.example.maxcalendar.painter.YCalendarPainter;
import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.util.AttrsUtil;
import com.example.maxcalendar.util.DateUtil;

public class YearRvView extends RecyclerView {

    private YearRvAdapter mYearRvAdapter;
    private Attrs mAttrs;
    private YCalendarPainter mYCalendarPainter;
    private OnMonthSelectedListener mOnMonthSelectedListener;

    public YearRvView(@NonNull Context context) {
        super(context, null);
    }

    public YearRvView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = AttrsUtil.getAttrs(context, attrs);
        this.mYCalendarPainter = new YCalendarPainter(context, mAttrs);
        init(context);
    }

    public YearRvView(Context context, Attrs attrs, YCalendarPainter painter) {
        super(context);
        this.mAttrs = attrs;
        this.mYCalendarPainter = painter;
        init(context);
    }

    public void init(Context context) {
        mYearRvAdapter = new YearRvAdapter(context, mAttrs, mYCalendarPainter);
        setLayoutManager(new GridLayoutManager(context, 3));
        setAdapter(mYearRvAdapter);
        mYearRvAdapter.setOnRecyclerViewListener(new OnClickRvListener() {
            @Override
            public void onItemClick(int position) {
                if (mOnMonthSelectedListener != null) {
                    Months month = mYearRvAdapter.getItem(position);
                    if (month == null) {
                        return;
                    }
                    mOnMonthSelectedListener.onMonthSelected(month.getYear(), month.getMonth());
                }
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }

            @Override
            public void onFooterViewClick() {

            }
        });
    }

    // 对 RecyclerView 添加月份数据
    public void setYearMonths(int year) {

        for (int i = 1; i <= 12; i++) {
            Months month = new Months();
            month.setFirstDayDiff(DateUtil.getFirstDayOfMonthDiff(year, i));
            month.setDayCount(DateUtil.getMonthDaysCount(year, i));
            month.setMonth(i);
            month.setYear(year);
            mYearRvAdapter.addItem(month);
        }
    }


    public IPainter getYearViewPainter() {
        return mYCalendarPainter;
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener listener) {
        this.mOnMonthSelectedListener = listener;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int height = MeasureSpec.getSize(heightSpec);
        int width = MeasureSpec.getSize(widthSpec);
        mYearRvAdapter.setItemSize(height / mAttrs.yearViewRvRows, width / mAttrs.yearViewRvColumns);
    }
}
