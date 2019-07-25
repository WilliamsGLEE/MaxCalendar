package com.example.maxcalendar.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maxcalendar.R;
import com.example.maxcalendar.activity.fragment.AddNewEventDialogFragment;
import com.example.maxcalendar.adapter.EventRvAdapter;
import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.calendar.CalendarLayout;
import com.example.maxcalendar.calendar.YearCalendarPager;
import com.example.maxcalendar.constant.Constant;
import com.example.maxcalendar.dao.TaskDaoUtil;
import com.example.maxcalendar.listener.OnCalendarSelectedChangedListener;
import com.example.maxcalendar.listener.OnClickRvListener;
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

    LocalDate mSelectedDate;
    EventRvAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        init();
        test();
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
        yearTextView.setVisibility(View.INVISIBLE);
        lunarTextView.setVisibility(View.INVISIBLE);
        dateTextView.setText(yearPager.getCurrentItem() + 1900);
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

        adapter = new EventRvAdapter(this);

        calendarLayout.setCalendarSelectedListener(new OnCalendarSelectedChangedListener() {
            @Override
            public void onCalendarSelected(LocalDate localDate) {

                dateTextView.setText(localDate.getMonthOfYear() + getResources().getString(R.string.monthString) +
                        localDate.getDayOfMonth() + getResources().getString(R.string.dayString));
                yearTextView.setText(String.valueOf(localDate.getYear()));
                lunarTextView.setText(DateUtil.getLunarText(DateUtil.getDate(localDate)));

                mSelectedDate = localDate;
//                adapter.
                querySchemeDate(localDate);
            }
        });

        yearPager.init(calendarLayout.getAttrs());
        yearPager.setOnMonthSelectedListener(new OnMonthSelectedListener() {
            @Override
            public void onMonthSelected(int year, int month) {
                calendarLayout.jumptoADate(new LocalDate(year, month, 1));
                yearPager.setVisibility(View.GONE);
                calendarLayout.showOtherPager();
                yearTextView.setVisibility(View.VISIBLE);
                lunarTextView.setVisibility(View.VISIBLE);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        EventRvAdapter adapter = new EventRvAdapter(this);
        adapter.setFooterView(LayoutInflater.from(this).inflate(R.layout.item_footer_rv_activity_calendar, recyclerView, false));       //  ???
        adapter.setOnRecyclerViewListener(new OnClickRvListener() {
            @Override
            public void onItemClick(int position) {
                // TODO show item
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }

            @Override
            public void onFooterViewClick() {
                AddNewEventDialogFragment fragment = new AddNewEventDialogFragment();
                fragment.setCurrentDate(mSelectedDate);
                fragment.setOnClickListener(new AddNewEventDialogFragment.onClickListener() {
                    @Override
                    public void onPositiveClick(DailyTask dailyTask) {
                        TaskDaoUtil.insertTask(dailyTask);
                    }
                });
                fragment.show(getSupportFragmentManager(), "check");
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void querySchemeDate(LocalDate localDate) {

        List<DailyTask> dailyTaskList;

        dailyTaskList = TaskDaoUtil.queryTaskByYMD(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth());

        Logger.d("TESST : " + dailyTaskList.size());

//        if (dailyTaskList != null && dailyTaskList.size() > 0) {
            adapter.updateData(dailyTaskList);
//        }
//        adapter.setFooterView(LayoutInflater.from(this).inflate(R.layout.item_footer_rv_activity_calendar, recyclerView, false));
//        adapter.notifyDataSetChanged();
    }

    public void test() {

        TaskDaoUtil taskDaoUtil = new TaskDaoUtil(this);
        DailyTask task1 = new DailyTask();
        task1.setYear(2019);
        task1.setTitle("ddddddd");
        task1.setMonth(7);
        task1.setDay(25);
        task1.setTime("12:00 ~ 24:00");
        task1.setContent("aaa");
        task1.setType(Constant.RED);

        DailyTask task2 = new DailyTask();
        task2.setYear(2019);
        task2.setMonth(8);
        task2.setTitle("zzzzzzzz");
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
