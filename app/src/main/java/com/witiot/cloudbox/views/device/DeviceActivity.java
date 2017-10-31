package com.witiot.cloudbox.views.device;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.witiot.cloudbox.R;
import com.witiot.cloudbox.views.BaseActivity;
import com.witiot.cloudbox.views.login.LoginActivity;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceActivity extends BaseActivity {

    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.device_record)
    RadioButton deviceRecord;
    @BindView(R.id.device_report)
    RadioButton deviceReport;
    @BindView(R.id.device_register)
    RadioButton deviceRegister;

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.fragContent)
    FrameLayout fragContent;

    @BindView(R.id.sbwh_title)
    TextView sbwh_title;
    @BindView(R.id.device_bluetooth)
    RadioButton deviceBluetooth;

    private Context mContext;

    String tabType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.bind(this);
        mContext = this;

        initUI();

        // 报修记录
        switchFragment(new DeviceRecordFragment());
        permissionRqs();
    }

    private void permissionRqs() {
        AndPermission.with(DeviceActivity.this)
                .requestCode(200)
                .permission(Permission.PHONE,Permission.LOCATION)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
                        AlertDialog.newBuilder(DeviceActivity.this)
                                .setTitle("提醒")
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
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if(requestCode == 200) {
                // TODO ...

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
        transaction.replace(R.id.fragContent, to).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initUI() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.device_record:
                        tabType = "报修记录";
                        switchFragment(new DeviceRecordFragment());
                        break;
                    case R.id.device_report:
                        tabType = "在线报修";
                        switchFragment(new DeviceReportFragment());
                        break;
                    case R.id.device_register:
                        tabType = "设备注册";
                        switchFragment(new DeviceRegisterFragment());
                        break;
                    case R.id.device_bluetooth:
                        tabType = "设备注册";
                        switchFragment(new DeviceBluetoothFragment());
                        break;
                }
            }
        });
    }

    @OnClick(R.id.close)
    public void onViewClicked() {
        finish();
    }
}
