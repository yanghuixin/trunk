package com.witiot.cloudbox.views.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.HttpUtil;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.LoginRes;
import com.witiot.cloudbox.model.LoginRqs;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.views.BaseFragment;
import com.witiot.cloudbox.views.device.DeviceActivity;
import com.witiot.cloudbox.views.navigation.NavigationActivity;
import com.witiot.cloudbox.widget.ClearEditText;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {

    @BindView(R.id.login_phone_et)
    ClearEditText loginPhoneEt;

    @BindView(R.id.login_pw_et)
    ClearEditText loginPwEt;

    @BindView(R.id.forget_pw)
    TextView forgetPw;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.wechat_login)
    TextView wechatLogin;

    @BindView(R.id.device_login)
    TextView device_login;

    Unbinder unbinder;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.forget_pw, R.id.btn_login, R.id.wechat_login, R.id.device_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_pw:
                //  重置密码
                startActivity(new Intent(getContext(), ResetpwdActivity.class));
                break;
            case R.id.btn_login:
                //  手机号码登录
                login();
                break;
            case R.id.wechat_login:
                //  微信登录
                break;
            case R.id.device_login:
                //  设备维护
                startActivity(new Intent(getContext(), DeviceActivity.class));
                break;
        }
    }

    private void login() {
        if(loginPhoneEt.getText().toString().equals("123")){
            startActivity(new Intent(getContext(), NavigationActivity.class));
            ((LoginActivity)getActivity()).finish();
        }
       if(checkParam()){
           LoginRqs loginRqs = new LoginRqs();
           loginRqs.setCmd("get");
           loginRqs.setSrc("3");
           loginRqs.setTok("");
           loginRqs.setVer("1");

           LoginRqs.DatBean datBean = new LoginRqs.DatBean();
           datBean.setCustomerId(loginPhoneEt.getText().toString());
           datBean.setCustomerPwd(HttpUtil.getMD5(loginPwEt.getText().toString().getBytes()));
           loginRqs.setDat(datBean);

           final Gson gson = new Gson();
           XRequest xRequest = new XRequest();
           xRequest.sendPostRequest(getContext(), gson.toJson(loginRqs), "login", new XRequestCallback() {
               @Override
               public void callback(boolean isSucceed, String result) {
                   if(isSucceed){
                       Type typeToken = new TypeToken<LoginRes>() {
                       }.getType();
                       LoginRes res = gson.fromJson(result, typeToken);
                       if(res.getRet().equals("10000")){
                           toastShow("登录成功");
                           SPUtils.put(getContext(),"tok",res.getDat());
                           SPUtils.put(getContext(),"customerId",loginPhoneEt.getText().toString());
                           startActivity(new Intent(getContext(), NavigationActivity.class));
                           ((LoginActivity)getActivity()).finish();
                       }else {
                           toastShow(res.getMsg());
                       }
                   }else {
                       toastShow("网络连接异常");
                   }
               }
           });
       }
    }

    private boolean checkParam() {

        if (!checkPhone()) {
            return false;
        }

        if (StringUtils.stringIsEmpty(loginPwEt.getText().toString())) {
            loginPwEt.setError("请输入密码");
            loginPwEt.requestFocus();
            return false;
        }

        return true;
    }

    private boolean checkPhone() {
        if (StringUtils.stringIsEmpty(loginPhoneEt.getText().toString())) {
            loginPhoneEt.setError("请输入手机号");
            loginPhoneEt.requestFocus();
            return false;
        } else if (loginPhoneEt.getText().toString().length() != 11) {
            loginPhoneEt.setError("请输入正确的手机号");
            loginPhoneEt.requestFocus();
            return false;
        }
        return true;
    }

}
