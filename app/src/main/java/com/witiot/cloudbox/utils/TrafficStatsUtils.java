package com.witiot.cloudbox.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import com.yuyh.library.imgsel.common.Constant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;

/**
 * Created by liuyp on 2017/9/14.
 */

public class TrafficStatsUtils {

    public static Context context;
    public Context getContext() {
        return context;
    }
    public void setContext(Context context){
        this.context = context;
    }

    private static int uidBox = 0;
    public static int getUidBox() {
        if( uidBox == 0 ){
            uidBox = getUid(context);
        }
        return uidBox;
    }

    private static String APP_NET_START = "APP_NET_START";
    private static String APP_NET_TOTAL = "APP_NET_TOTAL";
    private static String APP_NET_TODAY = "APP_NET_TODAY";
    //private static String APP_DATE_START = "APP_DATE_START";
    private static String APP_DATE_END = "APP_DATE_END";


    // 在登录时候调用下面方法
    /**
     *
     * @Description: 流量监控 往cookie中写入登录前的流量
     * @author fengao
     */
    public static void startTrafficMonitor() {
        // 流量监控
        long totalTraffic = -1;
        totalTraffic = TrafficStatsUtils.getAppTrafficSize(context, getUidBox());
        LogUtil.log("startTrafficMonitor.totalTraffic = " + CommonUtils.byteToMBOrGB(totalTraffic) );

        //将初始流量保存到APP_CFG的cookie中 （没有取到默认-1）
        SPUtils.put(context, APP_NET_START, totalTraffic);
    }

    // 在退出系统的时候调用
    /**
     *
     * @Description: 处理网络流量
     * @author fengao
     */
    public static long stopTrafficMonitor(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //退出程序时间
        String appEndDate = dateFormat.format( TimeUtils.getNowDate());

        //进入程序的流量
        long totalStartTraffic =  (Long)SPUtils.get(context, APP_NET_START, 0l) ;

        //退出程序的流量
        long totalEndTraffic = getAppTrafficSize(context, getUidBox());

        // 流量增量
        long totalAddedTraffic = totalEndTraffic - totalStartTraffic;

        //cookie中記錄的总流量
        long totalTraffic =  (Long )SPUtils.get(context, APP_NET_TOTAL, 0l) ;

        //更新cookie中的总流量
        SPUtils.put(context, APP_NET_TOTAL, totalTraffic + totalAddedTraffic);

        //今日的流量
        long todayTraffic =  (Long )SPUtils.get(context, APP_NET_TODAY, 0l);

        //上一次退出系统的日期
        String lastEndDate =  (String )SPUtils.get(context, APP_DATE_END, "");

        if(lastEndDate.equals(appEndDate)){
            //更新今日的流量
            todayTraffic = todayTraffic + totalAddedTraffic;
        }else{
            if(lastEndDate.equals("")){//第一次进入流量监控
                todayTraffic = totalAddedTraffic;
            }else{
                String monthDate = lastEndDate.substring(0, 7);
                String monthTraffic = (String)SPUtils.get(context, monthDate, "");

                if(monthTraffic.equals("")){//第一次记录月份
                    monthTraffic = monthTraffic + todayTraffic;
                }
                monthTraffic = monthTraffic+","+todayTraffic;

                //更新cookie中对应月份的流量  2017-09:120,130,140
                SPUtils.put(context, monthDate, monthTraffic);

                todayTraffic = totalAddedTraffic;
            }
        }
        // 更新cookie中的今日流量！！！
        SPUtils.put(context, APP_NET_TODAY, todayTraffic);

        // 更新cookie中退出系统的时间
        SPUtils.put(context, APP_DATE_END, appEndDate);

        LogUtil.log("stopTrafficMonitor.todayTraffic = " + CommonUtils.byteToMBOrGB(todayTraffic) );
        return todayTraffic;
    }


    /**
     * 取出全部 app 的流量
     * @param context
     * @return
     */
    public static long getAppTrafficSize( Context context, int uid_box ) {

        long totalSize = 0;

        PackageManager pm = context.getPackageManager();

        //得到安装的所以应用
        List<PackageInfo> pinfos = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        //找出需要网络权限的应用，得到这个应用上传和下载的数据量
        for (PackageInfo info : pinfos) {
            String[] pers = info.requestedPermissions;

            if (pers != null && pers.length > 0) {
                for (String per : pers) {

                    //找到需要网络权限的应用
                    if ("android.permission.INTERNET".equals(per)) {
                        int uid = info.applicationInfo.uid;

                        // 不统计 本app 产生的流量
                        if (uid != uid_box) {

                            long rx = TrafficStats.getUidRxBytes(uid);
                            long tx = TrafficStats.getUidTxBytes(uid);

                            if (rx >= 0 || tx >= 0) {
                                totalSize += rx + tx;
                            } else {
                                // 通过uid查询文件夹中的数据
                                Long totalBytes = getTotalTraffic(uid);//  getTotalBytesManual(uid);
                                totalSize += totalBytes;

                                //LogUtil.log(" TrafficStatsUtils. packageName = " + info.packageName + " , bytes = " + CommonUtils.byteToMBOrGB(totalBytes));

                            }
                        }
                    }
                }
            }
        }

        LogUtil.log(" getAppTrafficSize. totalSize = " + CommonUtils.byteToMBOrGB(totalSize) );
        return totalSize;
    }

    /**
     *
     * @Description: 得到uid的总流量（上传+下载）
     * @author fengao
     * @param uid 程序的uid
     * @return uid的总流量   当设备不支持方法且没有权限访问/proc/uid_stat/uid时 返回-1
     */
    public static long getTotalTraffic(int uid){
        long txTraffic = (getTxTraffic(uid)==-1)?getTxTcpTraffic(uid):getTxTraffic(uid);
        if(txTraffic==-1){
            return -1;
        }
        long rxTraffic = (getRxTraffic(uid)==-1)?getRxTcpTraffic(uid):getRxTraffic(uid);
        if(rxTraffic==-1){
            return -1;
        }
        return txTraffic + rxTraffic;
    }

    /**
     *
     * @Description: 获取uid上传的流量(wifi+3g/4g)
     * @author fengao
     * @param uid 程序的uid
     * @return 上传的流量（tcp+udp）  返回-1 表示不支持得机型
     */
    public static long getTxTraffic(int uid) {
        return  TrafficStats.getUidTxBytes(uid);
    }

    /**
     *
     * @Description: 获取uid下載的流量(wifi+3g/4g)
     * @author fengao
     * @param uid 程序的uid
     * @return 下載的流量(tcp+udp) 返回-1表示不支持的机型
     */
    public static long getRxTraffic(int uid){
        return  TrafficStats.getUidRxBytes(uid);
    }

    /**
     *
     * @Description: 获取uid上传的流量(wifi+3g/4g)  通过读取/proc/uid_stat/uid/tcp_snd文件获取
     * @author fengao
     * @param uid 程序的uid
     * @return 上传的流量（tcp）  返回-1 表示出现异常
     */
    public static long getTxTcpTraffic(int uid){
        RandomAccessFile rafSnd = null;
        String sndPath = "/proc/uid_stat/" + uid + "/tcp_snd";
        long sndTcpTraffic;
        try {
            rafSnd = new RandomAccessFile(sndPath, "r");
            sndTcpTraffic = Long.parseLong(rafSnd.readLine());
        } catch (FileNotFoundException e) {
            sndTcpTraffic = -1;
        } catch (IOException e) {
            e.printStackTrace();
            sndTcpTraffic = -1;
        } finally {
            try {
                if (rafSnd != null){
                    rafSnd.close();
                }
            } catch (IOException e) {
                sndTcpTraffic = -1;
            }
        }
        return sndTcpTraffic;
    }


    /**
     *
     * @Description: 获取uid上传的流量(wifi+3g/4g) 通过读取/proc/uid_stat/uid/tcp_rcv文件获取
     * @author fengao
     * @param uid 程序的uid
     * @return 下载的流量（tcp）  返回-1 表示出现异常
     */
    public static long getRxTcpTraffic(int uid) {
        RandomAccessFile rafRcv = null; // 用于访问数据记录文件
        String rcvPath = "/proc/uid_stat/" + uid + "/tcp_rcv";
        long rcvTcpTraffic;
        try {
            rafRcv = new RandomAccessFile(rcvPath, "r");
            rcvTcpTraffic = Long.parseLong(rafRcv.readLine()); // 读取流量统计
        } catch (FileNotFoundException e) {
            rcvTcpTraffic = -1;
        } catch (IOException e) {
            rcvTcpTraffic = -1;
        } finally {
            try {
                if (rafRcv != null){
                    rafRcv.close();
                }
            } catch (IOException e) {
                rcvTcpTraffic = -1;
            }
        }
        return rcvTcpTraffic;
    }



    /**
     *
     * @Description: 取得程序的uid
     * @author fengao
     * @param context 上下文
     * @return 当前程序的uid  返回-1表示出现异常
     */
    public static int getUid(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo( context.getPackageName(),0);
            return applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
