package com.example.maxcalendar.activity;

import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maxcalendar.R;
import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.dao.TaskDaoUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);


    }
}
