package com.example.maxcalendar.adapter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.maxcalendar.view.CalendarView;

import org.joda.time.LocalDate;

public class WeekAdapterAdapter extends BaseCalendarAdapter {


    public WeekAdapterAdapter(Context context, LocalDate date) {
        super(context, date);
    }

    @Override
    protected CalendarView getCalendarView(int position) {
        return null;
    }
}
