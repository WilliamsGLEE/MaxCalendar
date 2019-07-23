package com.example.maxcalendar.view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maxcalendar.adapter.YearCalendarAdapter;
import com.example.maxcalendar.util.Attrs;

public class YearRvView extends RecyclerView {

    private YearCalendarAdapter mYearCalendarAdapter;
    private Attrs mAttrs;


    public YearRvView(Context context, Attrs attrs) {
        super(context);
        this.mAttrs = attrs;
    }

    public YearRvView(@NonNull Context context) {
        super(context);
    }
}
