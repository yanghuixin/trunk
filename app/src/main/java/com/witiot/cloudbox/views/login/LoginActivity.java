package com.witiot.cloudbox.views.login;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.witiot.cloudbox.model.DeviceAllotRequest;
import com.witiot.cloudbox.model.DeviceAllotResponse;
import com.witiot.cloudbox.model.NewVersionRequest;
import com.witiot.cloudbox.model.UpdateVersionRes;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.UpdateProcessor;
import com.witiot.cloudbox.views.BaseActivity;
import com.witiot.cloudbox.views.navigation.NavigationActivity;
import com.witiot.cloudbox.widget.TipDialog;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_tab)
    RadioButton loginType;
    @BindView(R.id.register_tab)
    RadioButton registerType;
    @BindView(R.id.tab_group)
    RadioGroup tabGroup;
    @BindView(R.id.fgContent)
    FrameLayout fgContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initUI();

        switchFragment(new com.witiot.cloudbox.views.login.LoginFragment());

        // 改为先取医院编号
//        if(StringUtils.stringIsNotEmpty((String) SPUtils.get(this,"tok",""))){
//            startActivity(new Intent(this, NavigationActivity.class));
//            finish();
//        }

        getNewVersion();

        permissionRqs();
    }
    private void getNewVersion() {
        //NewVersionReques自己建的实体类（创建一个对象只是方便转换成最终上传的 json 字符串
        //），所有set方法都是传给后台的参数
        //OKHttp是把map转 json，这个只是改成把自建的对象转json而已
        //post 请求就是把一个完整的 json post到服务器，get 请求才是拼接
        NewVersionRequest.DatBean.Paramlist paramlist = new NewVersionRequest.DatBean.Paramlist();
        paramlist.setVersionNumber(CommonUtils.getAppVersionName(this).replace(".",""));
        // dat
        NewVersionRequest.DatBean datBean = new NewVersionRequest.DatBean();
        datBean.setParamlist(paramlist);
        // request
        NewVersionRequest rqs = new NewVersionRequest();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok("");//Tok传入空或从缓存share里tok对应的值 rqs.setTok((String) SPUtils.get(this, "tok", ""));//Tok传入从缓存share里tok对应的值
        rqs.setVer("1");
        rqs.setDat(datBean);
        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "version/updateVersion", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {// result后台返回的json数据，解析出来就好
                if(!isSucceed){
                    toastShow(result);
                    return;
                }
                Type typeToken = new TypeToken<UpdateVersionRes>() {}.getType();
                UpdateVersionRes res = gson.fromJson(result, typeToken);
                if (res.getRet().equals("10000")) {
                     if(res.getDat() != null && res.getDat().getRows()!= null && res.getDat().getRows().size() > 0){
                         UpdateProcessor updateProcessor = new UpdateProcessor(LoginActivity.this,res);
                     }else {
                         // 读取医院编号，并判断 是否已经登录
                         getHospitalId();
                     }
                }else {
                    // 读取医院编号，并判断 是否已经登录
                    getHospitalId();
                }

            }
        });
    }
    TipDialog tipDialog;
    private void update() {
        tipDialog = new TipDialog(this, true, new TipDialog.TipDialogButtonListener() {
            @Override
            public void onLeftBtnClicked() {

            }

            @Override
            public void onRightBtnClicked() {

            }

            @Override
            public void onCenterBtnClicked() {
                getApk();
            }
        });
        tipDialog.show();
        tipDialog.setCanceledOnTouchOutside(false);
        tipDialog.setCancelable(false);
        tipDialog.setLeftButtonVisibility(false);
        tipDialog.setRigheButtonVisibility(false);
        tipDialog.setCloseButtonVisibility(false);
        tipDialog.setMyTitle("检测到新版本");
        tipDialog.setCenterButtonText("立即更新");
        tipDialog.setMsg("");
    }

    private void getApk() {

    }


    private void permissionRqs() {
        AndPermission.with(LoginActivity.this)
                .requestCode(200)
                .permission(Permission.PHONE,Permission.LOCATION,Permission.STORAGE)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
                        AlertDialog.newBuilder(LoginActivity.this)
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


    private void initUI() {
        tabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.login_tab:
                        switchFragment(new LoginFragment());
                        break;
                    case R.id.register_tab:
                        switchFragment(new RegisterFragment());
                        break;
                }
            }
        });
    }

    public void setLogin(){
        loginType.performClick();
    }

    private void switchFragment(Fragment to) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
//        if (mContent == null) {
//            transaction.add(R.id.fgContent, to).commit();
//            mContent = to;
//        }else {
        transaction.replace(R.id.fgContent, to).commit();


    }


    @Override
    protected void onResume() {
        super.onResume();
        if(hasPermissionToReadNetworkStats()){

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
        toastShow("请开启云头柜权限\n点击 \"云头柜\" --> 点击右侧开关即可");
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    /**
     * 读取 设备所属的 医院编号
     */
    private void getHospitalId() {

        // paramlist
        DeviceAllotRequest.DatBean.Paramlist paramlist = new DeviceAllotRequest.DatBean.Paramlist();
        paramlist.setDeviceId(DeviceUtils.getAndroidID());  //
        paramlist.setDeviceStatus(1);   // 可用的

        // dat
        DeviceAllotRequest.DatBean datBean = new DeviceAllotRequest.DatBean();
        datBean.setPageindex("1");
        datBean.setPagesize("10");   // 最多6条记录
        datBean.setParamlist(paramlist);

        // request
        DeviceAllotRequest rqs = new DeviceAllotRequest();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok("");
        rqs.setVer("1");
        rqs.setDat(datBean);

        showProgress();

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "device/getAllotList", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<DeviceAllotResponse>() {}.getType();
                    DeviceAllotResponse res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {

                        if(res.getDat().getRows()!=null && res.getDat().getRows().size() >0 ) {
                            String hid = res.getDat().getRows().get(0).getHospitalId();
                            String hname = res.getDat().getRows().get(0).getHospitalId();

                            // 保存为本地数据
                            CommonUtils.setHospitalId(getApplicationContext(), hid);
                            CommonUtils.setHospitalName(getApplicationContext(), hname);

                            LogUtil.log("医院编号：" + (String)CommonUtils.getHospitalId(getApplicationContext()));
                        }
                    } else {
                        toastShow(res.getMsg());
                        //initAdvert();
                    }
                } else {
                    toastShow("网络连接异常，无法访问服务器。");
                }
                // 判断是否已经登录 tok
                if(StringUtils.stringIsNotEmpty((String) SPUtils.get(getApplicationContext(),"tok",""))){
                    startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                    finish();
                }
            }
        });
    }
}
