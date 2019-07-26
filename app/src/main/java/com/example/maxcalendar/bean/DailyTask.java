package com.example.maxcalendar.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.joda.time.LocalDate;

@Entity
public class DailyTask {

    @Unique
    @Id(autoincrement = true)
    private Long id;

    // 内容
    private String content;

    // 重要级别
    private int type;

    // 时间
    private String time;

    // 标题
    @NotNull
    private String title;

    // 年份
    @NotNull
    private int year;

    // 月份
    @NotNull
    private int month;

    // 日
    @NotNull
    private int day;

    @Generated(hash = 1542652020)
    public DailyTask(Long id, String content, int type, String time,
            @NotNull String title, int year, int month, int day) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.time = time;
        this.title = title;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Generated(hash = 1280034224)
    public DailyTask() {
    }

//    @Generated(hash = 1542652020)
//    public DailyTask(Long id, String content, int type, String time,
//            @NotNull String title, int year, int month, int day) {
//        this.id = id;
//        this.content = content;
//        this.type = type;
//        this.time = time;
//        this.title = title;
//        this.year = year;
//        this.month = month;
//        this.day = day;
//    }
//
//    @Generated(hash = 1280034224)
//    public DailyTask() {
//    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
