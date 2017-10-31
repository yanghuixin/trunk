package com.witiot.cloudbox.views.navigation;

import android.app.AppOpsManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.AdListRes;
import com.witiot.cloudbox.model.AdListRqs;
import com.witiot.cloudbox.model.DeviceAllotRequest;
import com.witiot.cloudbox.model.DeviceAllotResponse;
import com.witiot.cloudbox.service.MyBletoothService;
import com.witiot.cloudbox.service.PostFlowService;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.utils.Utils;
import com.witiot.cloudbox.views.BaseActivity;
import com.witiot.cloudbox.views.assistant.AssistantFragment;
import com.witiot.cloudbox.views.box.BoxFragment;
import com.witiot.cloudbox.views.internet.InternetFragment;
import com.witiot.cloudbox.views.mall.MallActivity;
import com.witiot.cloudbox.views.member.MemberFragment;
import com.witiot.cloudbox.views.yjzx.YjzxFragment;
import com.witiot.cloudbox.views.secure.SecureFragment;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
//导航Activity
public class NavigationActivity extends BaseActivity {
    
    @BindView(R.id.rb_internet)
    RadioButton rbInternet;
    @BindView(R.id.rb_box)
    RadioButton rbBox;
    @BindView(R.id.rb_mall)
    RadioButton rbMall;
    @BindView(R.id.rb_assistant)
    RadioButton rbAssistant;
    @BindView(R.id.rb_yjzx)
    RadioButton rbYjzx;
    @BindView(R.id.rb_ywx)
    RadioButton rbYwx;
    @BindView(R.id.rb_member)
    RadioButton rbMember;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.navigattion_content)
    FrameLayout navigattionContent;
    BroadcastReceiver mReceiverBluetooth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        initUI();
        Utils.init(this);

        Intent intent = new Intent(NavigationActivity.this, PostFlowService.class);
        startService(intent);

        Intent intent1 = new Intent(NavigationActivity.this, MyBletoothService.class);
        startService(intent1);

        // 启动 开机广告
        Intent intent2 = new Intent(NavigationActivity.this, SplashActivity.class);
        startActivity (intent2);

        AndPermission.with(NavigationActivity.this)
            .requestCode(200)
            .permission(Permission.PHONE)
            .rationale(new RationaleListener() {
                @Override
                public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
                AlertDialog.newBuilder(NavigationActivity.this)
                    .setTitle("友好提醒")
                    .setCancelable(false)
                    .setMessage("软件需要你授予相关权限才能使用，请允许，谢谢！")
                    .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            rationale.resume();
                        }
                    })
//                                .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        rationale.cancel();
//                                    }
//                                })
                    .show();
                }
            })
            .callback(listener)
            .start();

        listenerBluetooth();
    }


    private void listenerBluetooth() {
        mReceiverBluetooth = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                toastShow("蓝牙状态改变广播");

            if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {

//                    toastShow("设备正在断开连接。。。");
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
//                    toastShow( "设备连接已断开！！！");

            }
            else if(action.equals(BluetoothAdapter.STATE_OFF))
            {

            }
            }
        };

        IntentFilter filter1, filter2, filter3;
        filter1 = new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter2 = new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter3 = new IntentFilter("android.bluetooth.BluetoothAdapter.STATE_OFF");
        registerReceiver(mReceiverBluetooth, filter1);
        registerReceiver(mReceiverBluetooth, filter2);
        registerReceiver(mReceiverBluetooth, filter3);
        initBluetooth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReceiverBluetooth != null){
            unregisterReceiver(mReceiverBluetooth);
        }
    }

    private void initBluetooth() {

    }


    private void initUI() {
// TODO: 2017/10/31 看到此处 
        switchFragment(new InternetFragment());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                Fragment to = null;
                switch (i){
                    case R.id.rb_internet:
                        to = new InternetFragment();
                        break;
                    case R.id.rb_box:
                        to = new BoxFragment();
                        break;
                    case R.id.rb_mall:
                        //to = new MallFragment();
                        to = null;
                        startActivity(new Intent(NavigationActivity.this, MallActivity.class));
                        break;
                    case R.id.rb_assistant:
                        to = new AssistantFragment();
                        break;
                    case R.id.rb_yjzx:
                        to = new YjzxFragment();
                        break;
                    case R.id.rb_ywx:
                        to = new SecureFragment();
                        break;
//                    case R.id.rb_setting:
//                        to = new SettingFragment();
//                        break;
                    case R.id.rb_member:
                        to = new MemberFragment();
                        break;
                }
                if(to != null) {
                    switchFragment(to);//replace传入的fragment（to）
                }else {
                    rbInternet.performClick();//默认第一个button被点击
                }
            }
        });

//        rbInternet.performClick();
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if(requestCode == 200) {
                // TODO ...
                Intent intent = new Intent(NavigationActivity.this, PostFlowService.class);
                startService(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if(requestCode == 200) {
                // TODO ...
//                finish();
            }
        }
    };

    private void switchFragment(Fragment to) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.navigattion_content, to).commit();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if( hasPermissionToReadNetworkStats() ) {

        }


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



}
