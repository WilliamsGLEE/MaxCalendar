package com.example.maxcalendar.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maxcalendar.R;
import com.example.maxcalendar.adapter.EventRvAdapter;
import com.example.maxcalendar.calendar.CalendarLayout;
import com.example.maxcalendar.listener.OnCalendarSelectedChangedListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarActivity extends AppCompatActivity {

    @BindView(R.id.calendarLayout_activity_calendar) CalendarLayout calendarLayout;
    @BindView(R.id.rv_activity_calendar) RecyclerView recyclerView;
    @BindView(R.id.fab_activity_calendar) FloatingActionButton fab;
    @BindView(R.id.tv_monthandday_activity_calendar) TextView dateTextView;
    @BindView(R.id.tv_year_activity_calendar) TextView yearTextView;
    @BindView(R.id.tv_lunar_activity_calendar) TextView lunarTextView;

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
//                tv_monthandday_activity_calendar.setText(localDate.toString());
//                calendarLayout.setNewMonthHeight();
                dateTextView.setText(localDate.getMonthOfYear() + R.string.monthString + localDate.getDayOfMonth() + R.string.dayString);
                yearTextView.setText(localDate.getYear());
                lunarTextView.setText("农历");
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setNestedScrollingEnabled(false);
        EventRvAdapter adapter = new EventRvAdapter(this);
        recyclerView.setAdapter(adapter);
    }
}
