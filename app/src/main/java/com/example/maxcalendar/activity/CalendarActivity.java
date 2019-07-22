package com.example.maxcalendar.activity;

import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maxcalendar.R;
import com.example.maxcalendar.adapter.CalendarRecyclerViewAdapter;
import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.calendar.CalendarLayout;
import com.example.maxcalendar.dao.TaskDaoUtil;
import com.example.maxcalendar.listener.OnCalendarSelectedChangedListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarActivity extends AppCompatActivity {

    @BindView(R.id.calendarLayout_activity_calendar) CalendarLayout calendarLayout;
    //    @BindView(R.id.tv_activity_calendar) TextView tv_activity_calendar;
    @BindView(R.id.rv_activity_calendar) RecyclerView recyclerView;
    @BindView(R.id.fab_activity_calendar) FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        init();
    }

    @OnClick(R.id.fab_activity_calendar)
    protected void onClickFAB() {
        LocalDate selectedDate = calendarLayout.getSelectDate();
        Toast.makeText(this, selectedDate.toString(), Toast.LENGTH_SHORT).show();
    }

    protected void init() {
        calendarLayout.setCalendarSelectedListener(new OnCalendarSelectedChangedListener() {
            @Override
            public void onCalendarSelected(LocalDate localDate) {
//                tv_activity_calendar.setText(localDate.toString());
//                calendarLayout.setNewMonthHeight();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setNestedScrollingEnabled(false);
        CalendarRecyclerViewAdapter adapter = new CalendarRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
    }
}
