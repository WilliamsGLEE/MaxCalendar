package com.example.maxcalendar.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.maxcalendar.R;
import com.example.maxcalendar.activity.fragment.AddNewEventDialogFragment;
import com.example.maxcalendar.adapter.EventRvAdapter;
import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.calendar.CalendarLayout;
import com.example.maxcalendar.calendar.YearCalendarPager;
import com.example.maxcalendar.constant.Constant;
import com.example.maxcalendar.dao.GreenDaoHelper;
import com.example.maxcalendar.listener.OnCalendarSelectedChangedListener;
import com.example.maxcalendar.listener.OnClickRvListener;
import com.example.maxcalendar.listener.OnMonthSelectedListener;
import com.example.maxcalendar.util.DateUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarActivity extends AppCompatActivity {

    @BindView(R.id.calendarLayout_activity_calendar)
    CalendarLayout mCalendarLayout;
    @BindView(R.id.rv_activity_calendar)
    RecyclerView mRecyclerView;
    // TODO FLOATING ACTION BUTTON
    @BindView(R.id.tv_monthandday_activity_calendar)
    TextView mDateTextView;
    @BindView(R.id.tv_year_activity_calendar)
    TextView mYearTextView;
    @BindView(R.id.tv_lunar_activity_calendar)
    TextView mLunarTextView;
    @BindView(R.id.yearPager_activity_calendar)
    YearCalendarPager mYearPager;
    @BindView(R.id.ibtn_backToday_activity_calendar)
    ImageButton mImageButton;

    LocalDate mSelectedDate;
    EventRvAdapter mEventRvAdapter;
    GreenDaoHelper mGreenDaoHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        init();
        test();
    }

    @OnClick(R.id.tv_monthandday_activity_calendar)
    protected void showYearPager() {
        mYearPager.setVisibility(View.VISIBLE);
        mCalendarLayout.showYearPager();
        mYearTextView.setVisibility(View.INVISIBLE);
        mLunarTextView.setVisibility(View.INVISIBLE);
        mDateTextView.setText((mYearPager.getCurrentItem() + mYearPager.getStartYear()) + "");
    }

    @OnClick(R.id.ibtn_backToday_activity_calendar)
    protected void backToday() {
        mCalendarLayout.jumptoADate(new LocalDate());
    }

    protected void init() {

        mEventRvAdapter = new EventRvAdapter();
        mGreenDaoHelper = new GreenDaoHelper(this);

        mCalendarLayout.setCalendarSelectedListener(new OnCalendarSelectedChangedListener() {
            @Override
            public void onCalendarSelected(LocalDate localDate) {

                mDateTextView.setText(localDate.getMonthOfYear() + getResources().getString(R.string.monthString) +
                        localDate.getDayOfMonth() + getResources().getString(R.string.dayString));
                mYearTextView.setText(String.valueOf(localDate.getYear()));
                mLunarTextView.setText(DateUtil.getLunarText(DateUtil.getDate(localDate)));

                mSelectedDate = localDate;
                querySchemeDate(localDate);
            }
        });


        mYearPager.setOnMonthSelectedListener(new OnMonthSelectedListener() {
            @Override
            public void onMonthSelected(int year, int month) {

                mCalendarLayout.jumptoADate(new LocalDate(year, month, 1));
                mYearPager.setVisibility(View.GONE);
                mCalendarLayout.showMWPager();
                mYearTextView.setVisibility(View.VISIBLE);
                mLunarTextView.setVisibility(View.VISIBLE);
            }
        });
        mYearPager.init(mCalendarLayout.getAttrs());

        mYearPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mDateTextView.setText((position + mYearPager.getStartYear()) + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEventRvAdapter.setFooterView(LayoutInflater.from(this).inflate(R.layout.item_footer_rv_activity_calendar, mRecyclerView, false));
        mEventRvAdapter.setOnRecyclerViewListener(new OnClickRvListener() {
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
                        mGreenDaoHelper.insertTask(dailyTask);
                    }
                });
                fragment.show(getSupportFragmentManager(), "check");
            }
        });
        mRecyclerView.setAdapter(mEventRvAdapter);
        mEventRvAdapter.notifyDataSetChanged();
    }

    public void querySchemeDate(LocalDate localDate) {

        List<DailyTask> dailyTaskList;
        dailyTaskList = mGreenDaoHelper.queryTaskByYMD(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth());
        mEventRvAdapter.updateData(dailyTaskList);
    }

    public void test() {

//        DailyTask task1 = new DailyTask();
//        task1.setYear(2019);
//        task1.setTitle("Maxhub日历答辩");
//        task1.setMonth(7);
//        task1.setDay(26);
//        task1.setTime("10:00 ~ 11:00");
//        task1.setContent("aaa");
//        task1.setType(Constant.RED);
//
//        DailyTask task2 = new DailyTask();
//        task2.setYear(2019);
//        task2.setMonth(7);
//        task2.setTitle("501会议室开会");
//        task2.setDay(4);
//        task2.setTime("14:00 ~ 15:00");
//        task2.setContent("bbb");
//        task2.setType(Constant.ORANGE);
//
//        DailyTask task3 = new DailyTask();
//        task3.setYear(2019);
//        task3.setMonth(7);
//        task3.setTitle("实习生培训");
//        task3.setDay(15);
//        task3.setTime("13:00 ~ 23:00");
//        task3.setContent("bbb");
//        task3.setType(Constant.BLUE);
//
//        DailyTask task4 = new DailyTask();
//        task4.setYear(2019);
//        task4.setMonth(7);
//        task4.setTitle("休息");
//        task4.setDay(31);
//        task4.setTime("13:00 ~ 23:00");
//        task4.setContent("bbb");
//        task4.setType(Constant.GREEN);
//
//        DailyTask task5 = new DailyTask();
//        task5.setYear(2019);
//        task5.setMonth(7);
//        task5.setTitle("参观CVTE");
//        task5.setDay(2);
//        task5.setTime("13:00 ~ 23:00");
//        task5.setContent("bbb");
//        task5.setType(Constant.YELLOW);
//
//        DailyTask task6 = new DailyTask();
//        task6.setYear(2019);
//        task6.setTitle("继续完善项目");
//        task6.setMonth(7);
//        task6.setDay(26);
//        task6.setTime("14:00 ~ 17:00");
//        task6.setContent("aaa");
//        task6.setType(Constant.BLUE);
//
//        DailyTask task7 = new DailyTask();
//        task7.setYear(2019);
//        task7.setTitle("二产培训");
//        task7.setMonth(7);
//        task7.setDay(26);
//        task7.setTime("19:00 ~ 21:00");
//        task7.setContent("aaa");
//        task7.setType(Constant.GREEN);
//
//        List<DailyTask> tasks = new ArrayList<>();
//        tasks.add(task1);
//        tasks.add(task2);
//        tasks.add(task3);
//        tasks.add(task4);
//        tasks.add(task5);
//        tasks.add(task6);
//        tasks.add(task7);
//
//        mGreenDaoHelper.insertMultTask(tasks);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mYearPager.setVisibility(View.GONE);
    }
}
