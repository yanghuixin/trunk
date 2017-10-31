package com.witiot.cloudbox;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.pgyersdk.crash.PgyCrashManager;
import com.witiot.cloudbox.utils.ErrorProcessor;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.views.login.LoginActivity;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by lixin on 17/3/9.
 */
public class MyApplication extends Application {
    public static Context context;
    private DbManager.DaoConfig daoConfig;
    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }
    private int appCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //初始化蒲公英
        PgyCrashManager.register(this);
        
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();
                // 将错误日志记录在指定文件夹
                try {
//                    ErrorProcessor.saveErrorLog(ex);
                    PgyCrashManager.reportCaughtException(context , (Exception) ex);
                }catch (Exception e){
                    PgyCrashManager.reportCaughtException(context , e);
                    LogUtil.log("UncaughtExceptionHandler Exception",e.toString());
                }
                //重启应用
                protectApp();
            }
        });

        Fresco.initialize(this);

        x.Ext.init(this);//Xutils初始化
        daoConfig = new DbManager.DaoConfig()
                .setDbName("box_1")//创建数据库的名称
                .setDbVersion(1)//数据库版本号
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
//                        try {
//                            db.addColumn(LoginRes.UserBean.class,"interior_department");
//                            db.addColumn(LoginRes.UserBean.class,"interior_position");
//                            db.addColumn(LoginRes.UserBean.class,"floor");
//                            db.addColumn(LoginRes.UserBean.class,"jobNo");
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                        }
                        // db.dropTable(...);
                        // ...
                    }
                });//数据库更新操作
        /**
         * registerActivityLifecycleCallbacks是Application的一个接口，注册之后应用里的所有activity的生命周期都会被监控起来，我们可以在
         此接口方法里实现一些特殊的需求。比如统计用户对每个Activity使用情况，我们可以定义一个BaseActivity，
         在onStart()和onStop()人工插入统计方法。比如从桌面进应用的时候，检测某些状态，就可以在onActivityResumed方法里检测。
         */
        registerActivityLifecycleCallbacks(new MActivityLifecycleCallbacks());
    }


    protected void protectApp() {//重启应用
        new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
                AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 300, restartIntent);

                android.os.Process.killProcess(android.os.Process.myPid());

            }
        }.start();

    }


    public int getAppCount() {
        return appCount;
    }

    /**
     * ActivityLifecycleCallbacks是什么？
     * Application通过此接口提供了一套回调方法，用于让开发者对Activity的生命周期事件进行集中处理。
     * 为什么用ActivityLifecycleCallbacks？
     * 以往若需监测Activity的生命周期事件代码，你可能是这样做的，重写每一个Acivity的onResume()，然后作统计和处理：
     * ActivityLifecycleCallbacks接口回调可以简化这一繁琐过程，在一个类中作统一处理
     */
    private class MActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    //统计用户对每个Activity使用情况
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            appCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            appCount--;
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }


    public static Context getContext() {
        return context;
    }
}
