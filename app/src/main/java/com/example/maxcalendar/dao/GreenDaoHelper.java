package com.example.maxcalendar.dao;

import android.content.Context;

import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.bean.DailyTaskDao;
import com.example.maxcalendar.bean.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class GreenDaoHelper {

    private static GreenDaoManager mManager;
    private DailyTaskDao mDailyTaskDao;
    private DaoSession mDaoSession;

    public GreenDaoHelper(Context context) {
        mManager = GreenDaoManager.getInstance();
        mManager.init(context);
        mDaoSession = mManager.getDaoSession();
    }

    // 添加数据，如果有重复则覆盖
    public synchronized void insertTask(DailyTask task) {
        mDaoSession.insertOrReplace(task);
    }

    // 添加多条数据，需要开辟新的线程
    public synchronized void insertMultTask(final List<DailyTask> tasks) {
        mDaoSession.runInTx(new Runnable() {
            @Override
            public void run() {
                for (DailyTask task : tasks) {
                    mManager.getDaoSession().insertOrReplace(task);
                }
            }
        });
    }

    // 删除数据
    public synchronized void deleteTask(DailyTask task) {
        mDaoSession.delete(task);
    }

    // 删除全部数据
    public synchronized void deleteAll(Class cls) {
        mDaoSession.deleteAll(cls);
    }

    // 更新数据
    public synchronized void updateTask(DailyTask task) {
        mDaoSession.update(task);
    }

    // 按照主键返回单条数据
    public DailyTask listOneTask(long key) {
        return mDaoSession.load(DailyTask.class, key);
    }

    // 根据日期查找当日日程
    public List<DailyTask> queryTaskByYMD(int year, int month, int day) {
        QueryBuilder<DailyTask> builder = mDaoSession.queryBuilder(DailyTask.class);
        return builder.where(DailyTaskDao.Properties.Year.eq(year)).
                where(DailyTaskDao.Properties.Month.eq(month)).
                where(DailyTaskDao.Properties.Day.eq(day)).list();
    }

    // 查询全部数据
    public List<DailyTask> queryAll() {
        return mDaoSession.loadAll(DailyTask.class);
    }
}
