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
import com.orhanobut.logger.Logger;

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
//                mCalendarLayout.showOtherPager();
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
                        mCalendarLayout.refreshDailyTask();
                    }
                });
                fragment.show(getSupportFragmentManager(), "check");
            }
        });
        mRecyclerView.setAdapter(mEventRvAdapter);
        mEventRvAdapter.notifyDataSetChanged();
    }

    public void querySchemeDate(LocalDate localDate) {

        List<DailyTask> dailyTaskList = mGreenDaoHelper.queryTaskByYMD(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth());
        mEventRvAdapter.updateData(dailyTaskList);
        mCalendarLayout.refreshContentY();
        Logger.d("TTESTT querySchemeDate " + mEventRvAdapter.getItemCount());
    }

    public void test() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mYearPager.setVisibility(View.GONE);
    }
}
