package com.witiot.cloudbox.views.device;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.HttpUtil;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.DeviceReportRqs;
import com.witiot.cloudbox.model.LoginRes;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TimeUtils;
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
public class DeviceReportFragment extends BaseFragment {

    @BindView(R.id.edt_reportContent)
    EditText edt_reportContent;

    @BindView(R.id.edt_reportMan)
    ClearEditText edt_reportMan;

    @BindView(R.id.edt_reportMobile)
    ClearEditText edt_reportMobile;

    @BindView(R.id.btn_device_report)
    Button btn_device_report;

    Unbinder unbinder;

    public DeviceReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_report, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({ R.id.btn_device_report })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_device_report:
                //  设备报修
                device_report();
                break;
        }
    }


    private void device_report() {

        if(checkParam()){  // 输入验证成功

            DeviceReportRqs registerRqs = new DeviceReportRqs();
            registerRqs.setCmd("get");
            registerRqs.setSrc("3");
            registerRqs.setTok("");
            registerRqs.setVer("1");

            DeviceReportRqs.DatBean datBean = new DeviceReportRqs.DatBean();

            datBean.setDeviceId(DeviceUtils.getAndroidID());     // 设备编号

            datBean.setReportContent(  edt_reportContent.getText().toString() );
            datBean.setReportMan( edt_reportMan.getText().toString() );
            datBean.setReportMobile( edt_reportMobile.getText().toString() );
            datBean.setReportTime(TimeUtils.date2String(TimeUtils.getNowDate()) ); //               "2017-08-29 12:12:12");
            datBean.setStatus("0");  // 状态

            registerRqs.setDat(datBean);

            final Gson gson = new Gson();
            XRequest xRequest = new XRequest();
            xRequest.sendPostRequest(getContext(), gson.toJson(registerRqs), "/device/repair", new XRequestCallback() {
                @Override
                public void callback(boolean isSucceed, String result) {
                    if(isSucceed){
                        Type typeToken = new TypeToken<LoginRes>() {   }.getType();
                        LoginRes res = gson.fromJson(result, typeToken);

                        if(res.getRet().equals("10000")){
                            toastShow( "设备报修成功，请等待维护人员现场处理." );
                            ((DeviceActivity)getActivity()).finish();
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

        if (StringUtils.stringIsEmpty( edt_reportContent.getText().toString())) {
            edt_reportContent.setError("请输入设备故障描述");
            edt_reportContent.requestFocus();
            return false;
        }

        if (StringUtils.stringIsEmpty(edt_reportMan.getText().toString())) {
            edt_reportMan.setError("请输入联系人姓名");
            edt_reportMan.requestFocus();
            return false;
        }

        if (!checkPhone()) {
            return false;
        }

        return true;
    }


    private boolean checkPhone() {
        if (StringUtils.stringIsEmpty( edt_reportMobile.getText().toString())) {
            edt_reportMobile.setError("请输入联系人手机号码");
            edt_reportMobile.requestFocus();
            return false;
        } else if (edt_reportMobile.getText().toString().length() != 11) {
            edt_reportMobile.setError("请输入正确的手机号");
            edt_reportMobile.requestFocus();
            return false;
        }
        return true;
    }

}

