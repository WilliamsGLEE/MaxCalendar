package com.example.maxcalendar.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DailyTask {

    @Unique
    @Id(autoincrement = true)
    private Long id;

    /**
     * 内容
     */
    @NotNull
    private String content;

    /**
     * 日期
     */
    @NotNull
    private String date;

    /**
     * 重要级别
     */
    private int type;

    @Generated(hash = 1320201693)
    public DailyTask(Long id, @NotNull String content, @NotNull String date,
                     int type) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.type = type;
    }

    @Generated(hash = 1280034224)
    public DailyTask() {
    }

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

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
