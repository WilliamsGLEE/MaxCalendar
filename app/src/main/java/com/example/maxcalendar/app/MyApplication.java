package com.example.maxcalendar.app;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class MyApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}
