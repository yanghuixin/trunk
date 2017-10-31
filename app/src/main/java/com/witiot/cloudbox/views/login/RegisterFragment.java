package com.witiot.cloudbox.views.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.HttpUtil;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.AreaRequest;
import com.witiot.cloudbox.model.AreaResponse;
import com.witiot.cloudbox.model.BaseRes;
import com.witiot.cloudbox.model.RegisterRqs;
import com.witiot.cloudbox.model.SendMsgRqs;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TimeCount;
import com.witiot.cloudbox.views.BaseFragment;
import com.witiot.cloudbox.widget.ClearEditText;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment {


    @BindView(R.id.register_phone_et)
    ClearEditText registerPhoneEt;
    @BindView(R.id.register_code_et)
    ClearEditText registerCodeEt;
    @BindView(R.id.register_pw_et)
    ClearEditText registerPwEt;
    @BindView(R.id.register_comfirm_pw_et)
    ClearEditText registerComfirmPwEt;
    @BindView(R.id.protocol_checkbox)
    CheckBox protocolCheckbox;
    @BindView(R.id.protocol_tv)
    TextView protocolTv;
    @BindView(R.id.perfect_tv)
    TextView perfectTv;
    @BindView(R.id.btn_register)
    Button btnRegister;
    Unbinder unbinder;
    @BindView(R.id.get_code)
    TextView getCode;
    TimeCount timeCount ;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_register, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != timeCount) {
            timeCount.cancel();
            timeCount = null;
        }
    }


    private void register() {

        if (checkParam()) {
            RegisterRqs.DatBean datBean = new RegisterRqs.DatBean();
            datBean.setCustomerId(registerPhoneEt.getText().toString());
            datBean.setClientId(DeviceUtils.getAndroidID());
            datBean.setsmsCode(registerCodeEt.getText().toString());
            datBean.setCustomerPwd(HttpUtil.getMD5(registerPwEt.getText().toString().getBytes()));
            datBean.setDeviceToken(DeviceUtils.getAndroidID());

            RegisterRqs registerRqs = new RegisterRqs();
            registerRqs.setCmd("add");
            registerRqs.setSrc("3");
            registerRqs.setTok("");
            registerRqs.setVer("1");
            registerRqs.setDat(datBean);

            final Gson gson = new Gson();
            XRequest xRequest = new XRequest();
            xRequest.sendPostRequest(getContext(), gson.toJson(registerRqs), "register", new XRequestCallback() {
                @Override
                public void callback(boolean isSucceed, String result) {
                if(isSucceed){
                    Type typeToken = new TypeToken<BaseRes>() {
                    }.getType();
                    BaseRes res = gson.fromJson(result, typeToken);
                    if(res.getRet().equals("10000")){
                        toastShow("注册成功");
                        ((LoginActivity)getActivity()).setLogin();
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

        if (StringUtils.stringIsEmpty(registerCodeEt.getText().toString())) {
            registerCodeEt.setError("请输入请验证码");
            registerCodeEt.requestFocus();
            return false;
        }

        if (StringUtils.stringIsEmpty(registerPwEt.getText().toString())) {
            registerPwEt.setError("请设置密码");
            registerPwEt.requestFocus();
            return false;
        }

        if (registerPwEt.getText().toString().length() != 6) {
            registerPwEt.setError("请输入6位密码");
            registerPwEt.requestFocus();
            return false;
        }

        if (StringUtils.stringIsEmpty(registerComfirmPwEt.getText().toString())) {
            registerComfirmPwEt.setError("请确认密码");
            registerComfirmPwEt.requestFocus();
            return false;
        }

        if (!registerPwEt.getText().toString().equals(registerComfirmPwEt.getText().toString())) {
            toastShow("设置密码与确认密码不一致");
            registerComfirmPwEt.requestFocus();
            return false;
        }

        if(!protocolCheckbox.isChecked()){
            toastShow("请查看协议，接受请打钩");
            return false;
        }

        return true;
    }

    private boolean checkPhone() {
        if (StringUtils.stringIsEmpty(registerPhoneEt.getText().toString())) {
            registerPhoneEt.setError("请输入手机号");
            registerPhoneEt.requestFocus();
            return false;
        } else if (registerPhoneEt.getText().toString().length() != 11) {
            registerPhoneEt.setError("请输入正确的手机号");
            registerPhoneEt.requestFocus();
            return false;
        }
        return true;
    }


    @OnClick({R.id.get_code, R.id.protocol_tv, R.id.perfect_tv, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_code:
                getCode();
                break;
            case R.id.protocol_tv:
                break;
            case R.id.perfect_tv:
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void getCode() {
        if(checkPhone()){
            timeCount = new TimeCount(getContext(), 60000, 1, getCode, "获取验证码");
            timeCount.start();

            SendMsgRqs msgRqs = new SendMsgRqs();
            msgRqs.setCmd("get");
            msgRqs.setSrc("3");
            msgRqs.setTok("");
            msgRqs.setVer("1");

            SendMsgRqs.DatBean datBean = new SendMsgRqs.DatBean();
            datBean.setCustomerId(registerPhoneEt.getText().toString());
            datBean.setOperaType(1);  // 用户注册 1
            msgRqs.setDat(datBean);

            final Gson gson = new Gson();
            XRequest xRequest = new XRequest();
            xRequest.sendPostRequest(getContext(), gson.toJson(msgRqs), "sendMessage", new XRequestCallback() {
                @Override
                public void callback(boolean isSucceed, String result) {
                if(isSucceed){
                    Type typeToken = new TypeToken<BaseRes>() {
                    }.getType();
                    BaseRes res = gson.fromJson(result, typeToken);
                    if(res.getRet().equals("10000")){
                        toastShow("发送成功");
                    }else {
                        toastShow(res.getMsg());
                    }
                }else {
                    toastShow("网络连接异常");
                    timeCount.cancel();
                    timeCount = null;
                }
                }
            });
        }
    }

}
