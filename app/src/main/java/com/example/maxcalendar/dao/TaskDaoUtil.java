package com.example.maxcalendar.dao;

import android.content.Context;

import com.example.maxcalendar.bean.DailyTask;
import com.example.maxcalendar.bean.DailyTaskDao;
import com.example.maxcalendar.bean.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class TaskDaoUtil {

    private static final boolean DUBUG = true;
    private static DaoManager mManager;
    private DailyTaskDao mDailyTaskDao;
    private DaoSession mDaoSession;

    public TaskDaoUtil(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
        mDaoSession = mManager.getDaoSession();
        mManager.setDebug(DUBUG);
    }

    /**
     * 添加数据，如果有重复则覆盖
     */
    public static void insertTask(DailyTask task) {
        mManager.getDaoSession().insertOrReplace(task);
    }

    /**
     * 添加多条数据，需要开辟新的线程
     */
    public void insertMultTask(final List<DailyTask> tasks) {
        mManager.getDaoSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (DailyTask task : tasks) {
                    mManager.getDaoSession().insertOrReplace(task);
                }
            }
        });
    }


    /**
     * 删除数据
     */
    public static void deleteTask(DailyTask task) {
        mManager.getDaoSession().delete(task);
    }

    /**
     * 删除全部数据
     */
    public static void deleteAll(Class cls) {
        mManager.getDaoSession().deleteAll(cls);
    }

    /**
     * 更新数据
     */
    public static void updateTask(DailyTask task) {
        mManager.getDaoSession().update(task);
    }

    /**
     * 按照主键返回单条数据
     */
    public DailyTask listOneTask(long key) {
        return mManager.getDaoSession().load(DailyTask.class, key);
    }

    /**
     * 根据指定条件查询数据
     */
//    public List<DailyTask> queryTaskTestMethod() {
//        //查询构建器
//        QueryBuilder<DailyTask> builder = mManager.getDaoSession().queryBuilder(DailyTask.class);
//        List<DailyTask> list = builder.where(DailyTaskDao.Properties.Date.ge(1)).where(DailyTaskDao.Properties.Date.like("20190627")).list();
//        return list;
//    }

    /**
     * 根据日期查找当日日程
     */
    public static List<DailyTask> queryTaskByYMD(int year, int month, int day) {
        QueryBuilder<DailyTask> builder = mManager.getDaoSession().queryBuilder(DailyTask.class);
        return builder.where(DailyTaskDao.Properties.Year.eq(year)).
                where(DailyTaskDao.Properties.Month.eq(month)).
                where(DailyTaskDao.Properties.Day.eq(day)).list();
    }

    /**
     * 查询全部数据
     */
    public static List<DailyTask> queryAll() {
        return mManager.getDaoSession().loadAll(DailyTask.class);
    }
}
