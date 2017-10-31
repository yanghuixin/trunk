package com.witiot.cloudbox.utils;


import com.witiot.cloudbox.MyApplication;
import com.witiot.cloudbox.model.UserBean;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by lixin on 17/3/11.
 */
public class ConstantBox {

    public static UserBean userBean;
    public  static UserBean getUserBean(){
        if(userBean == null){
            try {
                DbManager db = x.getDb(((MyApplication) MyApplication.context.getApplicationContext()).getDaoConfig());
                userBean = db.selector(UserBean.class).findFirst();
            }catch (Exception e){
                userBean = null;
            }
        }
        return userBean;
    }

    public static void setUserBeanNull(UserBean userBean) {
        DbManager db = x.getDb(((MyApplication)MyApplication.context.getApplicationContext()).getDaoConfig());
        try {
            db.delete(UserBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ConstantBox.userBean = userBean;
    }

    public static boolean netWorkIsOK  = true;
    public static final int STATUS_FORCE_KILLED=-1; //应用放在后台被强杀了
    public static final int STATUS_KICK_OUT=1;//TOKEN失效或者被踢下线
    public static final int STATUS_NORMAL=2;  //APP正常态


    public final static int IMG_ADD = 0;
    public final static int IMG_NORMAL = 1;
    public static final int REQUEST_CODE_IMAGE = 0;

    public static boolean splashAcIsFinish = false;
}
