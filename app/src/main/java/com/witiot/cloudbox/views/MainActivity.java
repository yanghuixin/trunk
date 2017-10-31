package com.witiot.cloudbox.views;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.witiot.cloudbox.R;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.NetworkUtils;
import com.witiot.cloudbox.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.bt)
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Utils.init(this);
        hasPermissionToReadNetworkStats();

    }


    private boolean hasPermissionToReadNetworkStats() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }

        requestReadNetworkStats();
        return false;
    }

    // 打开“有权查看使用情况的应用”页面
    private void requestReadNetworkStats() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    int uid;

    @OnClick(R.id.bt)
    public void onViewClicked() {

          controllNetwork();

        querySummary();

    }

    private void controllNetwork() {

        LogUtil.log("getDataEnabled",NetworkUtils.getDataEnabled()+"");
        LogUtil.log("getNetworkType",NetworkUtils.getNetworkType()+"");
        LogUtil.log("getWifiEnabled",NetworkUtils.getWifiEnabled()+"");
//        LogUtil.log("isWifiAvailable",NetworkUtils.isWifiAvailable()+"");
//        LogUtil.log("isConnected",NetworkUtils.isConnected()+"");

        NetworkUtils.setWifiEnabled(!NetworkUtils.isWifiConnected());


    }

    private void querySummary() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ApplicationInfo appinfo = getApplicationInfo();
        List<ActivityManager.RunningAppProcessInfo> run = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningProcess : run) {
            if ((runningProcess.processName != null) && runningProcess.processName.equals(appinfo.processName)) {
                uid = runningProcess.uid;
                break;
            }
        }
        Log.i("test", "muid == "+uid);
        System.out.print("muid == "+uid);

        StringBuilder sb = new StringBuilder();
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) getSystemService(NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket = null;
// 获取到目前为止设备的Wi-Fi流量统计
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "", 0, System.currentTimeMillis());
                Log.i("Info", "Total: " + (bucket.getRxBytes() + bucket.getTxBytes()));
                sb.append("wifi 流量 ： "+ (bucket.getRxBytes() + bucket.getTxBytes()));



                // 获取subscriberId
                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String subId = tm.getSubscriberId();


//                com.sohu.tv  uid = 10060

                NetworkStats summaryStats;
                long summaryRx = 0;
                long summaryTx = 0;
                NetworkStats.Bucket summaryBucket = new NetworkStats.Bucket();
                long summaryTotal = 0;
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

        }


//        getUids();

        tv.setText(sb.toString()+"\n");
    }

    public List getUids() {
        List<Integer> uidList = new ArrayList<Integer>();
       PackageManager pm = getPackageManager();
        List<PackageInfo> packinfos = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
                        | PackageManager.GET_PERMISSIONS);
        for (PackageInfo info : packinfos) {
            String[] premissions = info.requestedPermissions;
            if (premissions != null && premissions.length > 0) {
                for (String premission : premissions) {
                    if ("android.permission.INTERNET".equals(premission)) {
                        // System.out.println(info.packageName+"访问网络");
                        int uid = info.applicationInfo.uid;
                        Log.i("test", "\n pkname = "+info.packageName+"  uid = " + uid);
                        // String name = pm.getNameForUid(uid);
                        // // textName.setText(name);
                        // Log.i("test", "name = "+name);
                        uidList.add(uid);
                    }
                }
            }
        }
        return uidList;
    }
}
