package com.example.maxcalendar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.maxcalendar.util.Attrs;
import com.example.maxcalendar.view.YearRvView;
import com.example.maxcalendar.view.YearView;

import org.joda.time.LocalDate;

public class YearCalendarAdapter extends PagerAdapter {

    private int yearCount;
    private Attrs mAttrs;
    private Context mContext;

    public YearCalendarAdapter(Context context, Attrs attrs) {
        this.mAttrs = attrs;
        this.mContext = context;

        LocalDate startDate = new LocalDate(attrs.startDateString);
        LocalDate endDate = new LocalDate(attrs.endDateString);
        yearCount = endDate.getYear() - startDate.getYear() + 1;
    }

    @Override
    public int getCount() {
        return yearCount;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        YearRvView yearView = new YearRvView(mContext, mAttrs);
        container.addView(yearView);

        return super.instantiateItem(container, position);
    }
}
