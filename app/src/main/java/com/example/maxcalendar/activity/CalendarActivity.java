package com.example.maxcalendar.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maxcalendar.R;
import com.example.maxcalendar.adapter.EventRvAdapter;
import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.calendar.CalendarLayout;
import com.example.maxcalendar.calendar.YearCalendarPager;
import com.example.maxcalendar.constant.Constant;
import com.example.maxcalendar.dao.TaskDaoUtil;
import com.example.maxcalendar.listener.OnCalendarSelectedChangedListener;
import com.example.maxcalendar.listener.OnMonthSelectedListener;
import com.example.maxcalendar.util.DateUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarActivity extends AppCompatActivity {

    @BindView(R.id.calendarLayout_activity_calendar)
    CalendarLayout calendarLayout;
    @BindView(R.id.rv_activity_calendar)
    RecyclerView recyclerView;
    @BindView(R.id.fab_activity_calendar)
    FloatingActionButton fab;
    @BindView(R.id.tv_monthandday_activity_calendar)
    TextView dateTextView;
    @BindView(R.id.tv_year_activity_calendar)
    TextView yearTextView;
    @BindView(R.id.tv_lunar_activity_calendar)
    TextView lunarTextView;
    @BindView(R.id.yearPager_activity_calendar)
    YearCalendarPager yearPager;
    @BindView(R.id.ibtn_backToday_activity_calendar)
    ImageButton imageButton;


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

    @OnClick(R.id.tv_monthandday_activity_calendar)
    protected void showYearPager() {
        yearPager.setVisibility(View.VISIBLE);
        calendarLayout.showYearPager();
    }

    @OnClick(R.id.ibtn_backToday_activity_calendar)
    protected void backToday() {
        calendarLayout.jumptoADate(new LocalDate());
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void init() {
        calendarLayout.setCalendarSelectedListener(new OnCalendarSelectedChangedListener() {
            @Override
            public void onCalendarSelected(LocalDate localDate) {

                dateTextView.setText(localDate.getMonthOfYear() + getResources().getString(R.string.monthString) +
                        localDate.getDayOfMonth() + getResources().getString(R.string.dayString));
                yearTextView.setText(String.valueOf(localDate.getYear()));
                lunarTextView.setText(DateUtil.getLunarText(DateUtil.getDate(localDate)));

                querySchemeDate(localDate);
            }
        });
        yearPager.init(calendarLayout.getAttrs());
        yearPager.setOnMonthSelectedListener(new OnMonthSelectedListener() {
            @Override
            public void onMonthSelected(int year, int month) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EventRvAdapter adapter = new EventRvAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    public void querySchemeDate(LocalDate localDate) {

    }

    public void test() {

        TaskDaoUtil taskDaoUtil = new TaskDaoUtil(this);
        DailyTask task1 = new DailyTask();
        task1.setYear(2019);
        task1.setMonth(7);
        task1.setDay(25);
        task1.setTime("12:00 ~ 24:00");
        task1.setContent("aaa");
        task1.setType(Constant.RED);

        DailyTask task2 = new DailyTask();
        task2.setYear(2019);
        task2.setMonth(8);
        task2.setDay(1);
        task2.setTime("13:00 ~ 23:00");
        task2.setContent("bbb");
        task2.setType(Constant.RED);

//        DailyTask task3 = new DailyTask();
//        task3.setContent("ccc");
//        task3.setDate("20190627");
//        task3.setType(3);

        List<DailyTask> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
//        tasks.add(task3);

        taskDaoUtil.insertMultTask(tasks);

//        List<DailyTask> result = taskDaoUtil.queryTaskByDate("20190627");
//        Logger.d("length : " + result.size());
//        for (int i = 0; i < result.size(); i++) {
//            Logger.d(result.get(i).getId());
//            Logger.d(result.get(i).getContent());
//            Logger.d(result.get(i).getDate());
//            Logger.d(result.get(i).getType());
//        }

    }
}
