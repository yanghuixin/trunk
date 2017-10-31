package com.witiot.cloudbox.service;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.ApplicationBean;
import com.witiot.cloudbox.model.BaseRes;
import com.witiot.cloudbox.model.PostFlowRqs;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.utils.TrafficStatsUtils;
import com.witiot.cloudbox.utils.Utils;
import com.witiot.cloudbox.views.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostFlowService extends Service {

    int uid_box;
    List<Integer> uidList = new ArrayList<Integer>();
    DecimalFormat df ;

    private List<ApplicationBean> appList;

    public PostFlowService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
        df = new DecimalFormat("#.###");

        //appList = new ArrayList<>();
        //getAppTrafficStatList();
        //LogUtil.log( "appList = " + appList.toString() );

        // 提交流量
        //long total = getAppTrafficSize();
        //postData(total);

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ApplicationInfo appinfo = getApplicationInfo();

        // 获取本APP的 uid
        List<ActivityManager.RunningAppProcessInfo> run = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningProcess : run) {
            if ((runningProcess.processName != null) && runningProcess.processName.equals(appinfo.processName)) {
                uid_box = runningProcess.uid;
                break;
            }
        }
        LogUtil.log("PostFlowService uid_box=",uid_box);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 获取所有的uid
        //getUidList();

//        long total = 0;
//        for (int i = 0; i < uidList.size(); i++) {
//            total = total + querySummary(uidList.get(i));
//        }
//        LogUtil.log("PostFlowService total=",total+"");

        // 提交流量
        //postData(total);

        TrafficStatsUtils.context = getApplicationContext();
        TrafficStatsUtils.startTrafficMonitor();

        LogUtil.log("启动定时器");
        flowHandler.postDelayed(flowRunnable, 5000);

        return super.onStartCommand(intent, flags, startId);
    }

    private void postData(long total) {
        // dat
        PostFlowRqs.DatBean datBean = new PostFlowRqs.DatBean();
        datBean.setCustomerId((String) SPUtils.get(this, "customerId", ""));
        datBean.setDeviceId(DeviceUtils.getAndroidID());
        datBean.setFlowSize(df.format(CommonUtils.byteToMB(total)));
        datBean.setPostTime(TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd")));

        // request
        PostFlowRqs rqs = new PostFlowRqs();
        rqs.setCmd("add");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);

        // post
        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "bill/postBillFlow", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                if (isSucceed) {
                    Type typeToken = new TypeToken<BaseRes>() { }.getType();

                    BaseRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        LogUtil.log("PostFlowService result= ","流量提交成功");
                    } else {
                        LogUtil.log("PostFlowService result= ","流量提交失败");
                    }
                }
                else {

                }
            }
        });
    }


    private long querySummary(int uid) {

//      StringBuilder sb = new StringBuilder();
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) getSystemService(NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket = null;

        // 获取到目前为止设备的Wi-Fi流量统计
        long summaryTotal = 0;
        try {
//           bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "", 0, System.currentTimeMillis());
//           Log.i("Info", "Total: " + (bucket.getRxBytes() + bucket.getTxBytes()));
//           sb.append("wifi 流量 ： "+ (bucket.getRxBytes() + bucket.getTxBytes()));

            // 获取subscriberId
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String subId = tm.getSubscriberId();
            NetworkStats summaryStats;
            long summaryRx = 0;
            long summaryTx = 0;

            NetworkStats.Bucket summaryBucket = new NetworkStats.Bucket();
            summaryTotal = 0;
//                summaryStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE,subId
//                        , CommonUtils.getTimesMonthMorning(), System.currentTimeMillis(),10060);
            summaryStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, subId, CommonUtils.getTimesMonthMorning(), System.currentTimeMillis());
            do {
                summaryStats.getNextBucket(summaryBucket);
                int summaryUid = summaryBucket.getUid();
                if (uid == summaryUid) {
                    summaryRx += summaryBucket.getRxBytes();
                    summaryTx += summaryBucket.getTxBytes();
                }
                Log.i(MainActivity.class.getSimpleName(), "uid:" + summaryBucket.getUid() + " rx:" + summaryBucket.getRxBytes() +
                        " tx:" + summaryBucket.getTxBytes());
                summaryTotal += summaryBucket.getRxBytes() + summaryBucket.getTxBytes();
            } while (summaryStats.hasNextBucket());


        } catch (Exception e) {
            e.printStackTrace();
        }

        return summaryTotal;
//        getUids();
//        tv.setText(sb.toString()+"\n");
    }


    public List getUidList() {
        PackageManager pm = getPackageManager();
        List<PackageInfo> packinfos = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
                        | PackageManager.GET_PERMISSIONS);
        for (PackageInfo info : packinfos) {
            String[] premissions = info.requestedPermissions;
            if (premissions != null && premissions.length > 0) {
                for (String premission : premissions) {
                    if ("android.permission.INTERNET".equals(premission)) {
                        //System.out.println(info.packageName+"访问网络");

                        int uid = info.applicationInfo.uid;
                        LogUtil.log("getUidList: ", "\n pkname = "+info.packageName+"  uid = " + uid);
                        // String name = pm.getNameForUid(uid);
                        // textName.setText(name);
                        // Log.i("test", "name = "+ name  );

                        if(uid != uid_box) {
                            uidList.add(uid);
                        }
                    }
                }
            }
        }
        return uidList;
    }


    @Override
    public void onDestroy() {
        TrafficStatsUtils.stopTrafficMonitor();
        flowHandler.removeCallbacks(flowRunnable);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public long getAppTrafficSize() {

        long totalSize = 0;

        PackageManager pm = getPackageManager();

        //得到安装的所以应用
        List<PackageInfo> pinfos = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        //找出需要网络权限的应用，得到这个应用上传和下载的数据量
        for (PackageInfo info : pinfos) {
            String[] pers = info.requestedPermissions;

            if (pers != null && pers.length > 0) {
                for (String per : pers)

                    //找到需要网络权限的应用
                    if ("android.permission.INTERNET".equals(per)) {
                        int uid = info.applicationInfo.uid;

                        // 不统计 本app 产生的流量
                        if(uid != uid_box) {

                            long rx = TrafficStats.getUidRxBytes(uid);
                            long tx = TrafficStats.getUidTxBytes(uid);

                            if( rx>=0 || tx >=0) {
                                totalSize += rx + tx;
                            }
                            else {
                                // 通过uid查询文件夹中的数据
                                Long totalBytes = getTotalBytesManual(uid);
                                totalSize += totalBytes;

                                LogUtil.log(" getAppTrafficSize. packageName = "+ info.packageName + " , bytes = " + CommonUtils.byteToMBOrGB(totalBytes ));

                            }
                        }
                    }
            }
        }

        LogUtil.log(" getAppTrafficSize. totalSize= " + CommonUtils.byteToMBOrGB(totalSize) );
        return totalSize;
    }

    /**
     * Android7.0系统的手机上运行，一直获取不到流量的使用数据：
     * 通过uid查询文件夹中的数据
     * @param localUid
     * @return
     */
    private Long getTotalBytesManual(int localUid) {
//        Log.e("BytesManual*****", "localUid:" + localUid);
        File dir = new File("/proc/uid_stat/");
        String[] children = dir.list();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < children.length; i++) {
            stringBuffer.append(children[i]);
            stringBuffer.append("   ");
        }
//        Log.e("children*****", children.length + "");
//        Log.e("children22*****", stringBuffer.toString());
        if (!Arrays.asList(children).contains(String.valueOf(localUid))) {
            return 0L;
        }
        File uidFileDir = new File("/proc/uid_stat/" + String.valueOf(localUid));
        File uidActualFileReceived = new File(uidFileDir, "tcp_rcv");
        File uidActualFileSent = new File(uidFileDir, "tcp_snd");
        String textReceived = "0";
        String textSent = "0";
        try {
            BufferedReader brReceived = new BufferedReader(new FileReader(uidActualFileReceived));
            BufferedReader brSent = new BufferedReader(new FileReader(uidActualFileSent));
            String receivedLine;
            String sentLine;

            if ((receivedLine = brReceived.readLine()) != null) {
                textReceived = receivedLine;
//                Log.e("receivedLine*****", "receivedLine:" + receivedLine);
            }
            if ((sentLine = brSent.readLine()) != null) {
                textSent = sentLine;
//                Log.e("sentLine*****", "sentLine:" + sentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e("IOException*****", e.toString());
        }
//        Log.e("BytesManualEnd*****", "localUid:" + localUid);
        return Long.valueOf(textReceived).longValue() + Long.valueOf(textSent).longValue();
    }

    // 定时器提交流量数据，5秒提交
    android.os.Handler flowHandler =  new android.os.Handler();

    Runnable flowRunnable = new Runnable() {
        @Override
        public void run() {
            // 提交流量
            //long total = getAppTrafficSize();
            // postData(total);
            long total = TrafficStatsUtils.stopTrafficMonitor();
            postData(total);

            //flowHandler.postDelayed(this,  60 * 1000);
        }
    };

    // 2. 启动计时器
    //handler.postDelayed(runnable, 2000);//每两秒执行一次runnable.
    //3. 停止计时器
    //handler.removeCallbacks(runnable);


    // 废弃
    public void getAppTrafficStatList() {
        PackageManager pm = getPackageManager();

        //得到安装的所以应用
        List<PackageInfo> pinfos = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        //找出需要网络权限的应用，得到这个应用上传和下载的数据量
        for (PackageInfo info : pinfos) {
            String[] pers = info.requestedPermissions;

            if (pers != null && pers.length > 0) {
                for (String per : pers)

                    //找到需要网络权限的应用
                    if ("android.permission.INTERNET".equals(per)) {
                        int uid = info.applicationInfo.uid;
                        String lable = (String) info.applicationInfo.loadLabel(pm);

                        Drawable icon = null;
                        try {
                            icon = pm.getApplicationIcon(info.packageName);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        long rx = TrafficStats.getUidRxBytes(uid);
                        long tx = TrafficStats.getUidTxBytes(uid);

                        ApplicationBean app = new ApplicationBean();
                        app.setPkgName(info.packageName);
                        app.setAppName(lable);
                        app.setAppIcon(icon);
                        app.setRx(rx);
                        app.setTx(tx);

                        LogUtil.log(" getAppTrafficStatList. Name= " + app.getAppNme() + ", 接收rx= " + app.getRx() + ", 上传tx= " + app.getTx() );

                        appList.add(app);
                    }
            }
        }
    }

}
