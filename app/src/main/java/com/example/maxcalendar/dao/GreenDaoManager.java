package com.example.maxcalendar.dao;

import android.content.Context;

import com.example.maxcalendar.bean.DaoMaster;
import com.example.maxcalendar.bean.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

public class GreenDaoManager {

    private static final String DB_NAME = "maxcalendar.db";      //数据库名称
    private volatile static GreenDaoManager mGreenDaoManager;       //多线程访问
    private static DaoMaster.DevOpenHelper mHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private Context mContext;

    // 使用单例模式获得操作数据库的对象
    static GreenDaoManager getInstance() {
        GreenDaoManager instance = null;
        if (mGreenDaoManager == null) {
            synchronized (GreenDaoManager.class) {
                if (instance == null) {
                    instance = new GreenDaoManager();
                    mGreenDaoManager = instance;
                }
            }
        }
        return mGreenDaoManager;
    }

    // 初始化Context对象
    public void init(Context context) {
        this.mContext = context;
    }

    // 判断数据库是否存在，如果不存在则创建
    public DaoMaster getDaoMaster() {
        if (null == mDaoMaster) {
            mHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
            mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        }
        return mDaoMaster;
    }

    // 完成对数据库的增删查找
    public DaoSession getDaoSession() {
        if (null == mDaoSession) {
            if (null == mDaoMaster) {
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    // 关闭数据库
    public void closeDataBase() {
        closeHelper();
        closeDaoSession();
    }

    public void closeDaoSession() {
        if (null != mDaoSession) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

    public void closeHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }
}
