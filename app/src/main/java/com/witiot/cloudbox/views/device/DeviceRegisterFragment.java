package com.witiot.cloudbox.views.device;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.HttpUtil;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.AreaRequest;
import com.witiot.cloudbox.model.AreaResponse;
import com.witiot.cloudbox.model.DeviceRegisterRqs;
import com.witiot.cloudbox.model.HospitalRequest;
import com.witiot.cloudbox.model.HospitalResponse;
import com.witiot.cloudbox.model.LoginRes;
import com.witiot.cloudbox.model.LoginRqs;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.views.BaseFragment;
import com.witiot.cloudbox.views.login.LoginActivity;
import com.witiot.cloudbox.views.login.ResetpwdActivity;
import com.witiot.cloudbox.views.navigation.NavigationActivity;
import com.witiot.cloudbox.widget.BackgroundDarkPopupWindow;
import com.witiot.cloudbox.widget.ClearEditText;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceRegisterFragment extends BaseFragment {

    @BindView(R.id.edt_province)
    TextView edt_province;

    @BindView(R.id.edt_city)
    TextView edt_city;

    @BindView(R.id.edt_hospital)
    TextView edt_hospital;

    @BindView(R.id.edt_depart)
    TextView edt_depart;

    @BindView(R.id.edt_floorNum)
    TextView edt_floorNum;

    @BindView(R.id.edt_roomNum)
    TextView edt_roomNum;

    @BindView(R.id.edt_roomType)
    TextView edt_roomType;

    @BindView(R.id.edt_device)
    TextView edt_device;

    @BindView(R.id.btn_device_register)
    Button btn_device_register;

    Unbinder unbinder;

    final int Province_SP = 0;
    final int City_SP = 1;
    final int Hospital_SP = 2;
    final int Depart_SP = 3;
    final int FloorNum_SP = 4;
    final int RoomNum_SP = 5;
    final int RoomType_SP = 6;

    HospitalResponse.DatBean hospBean = new HospitalResponse.DatBean();
    String areaKind = "1";
    AreaResponse.DatBean provinceData = new AreaResponse.DatBean();
    AreaResponse.DatBean areaData = new AreaResponse.DatBean();
    String hospitalId = "";
    String departId = "";

    public DeviceRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_register, container, false);
        unbinder = ButterKnife.bind(this, view);

        edt_device.setText( DeviceUtils.getAndroidID());  // 默认本机设备编号

        areaKind = "1";
        getAreaList(areaKind, "", "");

        //getHospitalList();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    List<String> datas;
    @OnClick({R.id.btn_device_register, R.id.edt_province, R.id.edt_city, R.id.edt_hospital, R.id.edt_depart, R.id.edt_floorNum, R.id.edt_roomNum, R.id.edt_roomType })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_device_register:
                //  设备注册
                device_register();
                break;
            case R.id.edt_province:
                datas = new ArrayList<>();
//                datas.add("北京市");

                for(int i=0; i< provinceData.getRows().size(); i++ ){
                    datas.add( provinceData.getRows().get(i).getProvince() );
                }

                edt_city.setText("");
                edt_hospital.setText("");

                showpopup(datas, Province_SP, edt_province);
                break;
            case R.id.edt_city:
                datas = new ArrayList<>();
                //datas.add("北京市");

                for(int i=0; i< areaData.getRows().size(); i++ ){
                    datas.add( areaData.getRows().get(i).getCity() );
                }

                edt_hospital.setText("");

                showpopup(datas, City_SP, edt_city);
                break;
            case R.id.edt_hospital:
                datas = new ArrayList<>();
                //datas.add("协和医院");
                //datas.add("解放军总医院");

                for(int i=0; i< hospBean.getRows().size(); i++ ){
                    datas.add( hospBean.getRows().get(i).getAbbrName() );
                }

                showpopup(datas, Hospital_SP, edt_hospital);
                break;
            case R.id.edt_depart:
                datas = new ArrayList<>();
                datas.add("神经科");
                datas.add("妇产科");
                datas.add("外科");
                datas.add("内科");
                showpopup(datas, Depart_SP, edt_depart);
                break;

            case R.id.edt_floorNum:
                datas = new ArrayList<>();
                datas.add("1层");
                datas.add("2层");
                datas.add("3层");
                showpopup(datas, FloorNum_SP, edt_floorNum);
                break;

            case R.id.edt_roomNum:
                datas = new ArrayList<>();
                datas.add("101");
                datas.add("102");
                datas.add("201");
                datas.add("202");
                datas.add("301");
                datas.add("302");
                showpopup(datas, RoomNum_SP, edt_roomNum);
                break;

            case R.id.edt_roomType:
                datas = new ArrayList<>();
                datas.add("单人间");
                datas.add("多人间");
                showpopup(datas, RoomType_SP, edt_roomType);
                break;
        }
    }


    private void device_register() {

       if(checkParam()){
           DeviceRegisterRqs registerRqs = new DeviceRegisterRqs();
           registerRqs.setCmd("get");
           registerRqs.setSrc("3");
           registerRqs.setTok("");
           registerRqs.setVer("1");

           DeviceRegisterRqs.DatBean datBean = new DeviceRegisterRqs.DatBean();

           datBean.setDeviceId(DeviceUtils.getAndroidID());     // 设备编号
           datBean.setHospitalId( hospitalId );
           datBean.setDepartId( departId );
           datBean.setFloorNum( edt_floorNum.getText().toString());
           datBean.setRoomNum( edt_roomNum.getText().toString());
           datBean.setRoomType( edt_roomType.getText().toString() );

           datBean.setStatus("1");  // 状态

           registerRqs.setDat(datBean);

           final Gson gson = new Gson();
           XRequest xRequest = new XRequest();
           xRequest.sendPostRequest(getContext(), gson.toJson(registerRqs), "/device/registerDevice", new XRequestCallback() {
               @Override
               public void callback(boolean isSucceed, String result) {
               if(isSucceed){
                   Type typeToken = new TypeToken<LoginRes>() {   }.getType();
                   LoginRes res = gson.fromJson(result, typeToken);

                   if(res.getRet().equals("10000")){
                       toastShow( res.getMsg() );
                        CommonUtils.setHospitalId(getContext(),hospitalId);
                       //SPUtils.put(getContext(),"tok",res.getDat());
                       //SPUtils.put(getContext(),"customerId",loginPhoneEt.getText().toString());
                       //startActivity(new Intent(getContext(), NavigationActivity.class));
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
        //LogUtil.log(hospitalId);
        if (StringUtils.stringIsEmpty( edt_province.getText().toString())) {
            edt_province.setError("请选择省份");
            edt_province.requestFocus();
            return false;
        }
        else {
            edt_province.setError(null, null);
        }

        if (StringUtils.stringIsEmpty( edt_city.getText().toString())) {
            edt_city.setError("请选择城市");
            edt_city.requestFocus();
            return false;
        }
        else {
            edt_city.setError(null, null);
        }

        if (StringUtils.stringIsEmpty( edt_hospital.getText().toString())) {
            edt_hospital.setError("请选择医院");
            edt_hospital.requestFocus();
            return false;
        }
        else {
            edt_hospital.setError(null, null);
        }

        if (StringUtils.stringIsEmpty( edt_depart.getText().toString())) {
            edt_depart.setError("请选择科室");
            edt_depart.requestFocus();
            return false;
        }
        else {
            edt_depart.setError(null, null);
        }

        if (StringUtils.stringIsEmpty( edt_floorNum.getText().toString())) {
            edt_floorNum.setError("请选择楼层");
            edt_floorNum.requestFocus();
            return false;
        }
        else {
            edt_floorNum.setError(null, null);
        }

        if (StringUtils.stringIsEmpty( edt_roomNum.getText().toString())) {
            edt_roomNum.setError("请选择房号");
            edt_roomNum.requestFocus();
            return false;
        }
        else {
            edt_roomNum.setError(null, null);
        }

        if (StringUtils.stringIsEmpty( edt_roomType.getText().toString())) {
            edt_roomType.setError("请选择房型");
            edt_roomType.requestFocus();
            return false;
        }
        else {
            edt_roomType.setError(null, null);
        }

        return true;
    }



    BackgroundDarkPopupWindow popup;
    private void showpopup(final List<String> datas, final int type,View rootview) {
        //View popupView = getLayoutInflater().inflate(R.layout.pop_listview, null);
        View popupView =  this.getActivity().getLayoutInflater( ).inflate(R.layout.pop_listview, null);
        ListView listview = (ListView) popupView.findViewById(R.id.list_pop);
        listview.setAdapter(new ArrayAdapter<String>( this.getContext() , android.R.layout.simple_list_item_1, datas));
        popup = new BackgroundDarkPopupWindow(
                popupView, getResources().getDimensionPixelSize(R.dimen.perfect_info_pop_w),
                WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this.getContext(), R.color.white)));
        popup.setAnimationStyle(android.R.style.Animation_Dialog);
        popup.setDarkStyle(-1);
        popup.setDarkColor(Color.parseColor("#a0000000"));
        popup.resetDarkPosition();
        popup.darkFillScreen();
        popup.showAsDropDown(rootview, 0, 0);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popup = null;

            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (type){
                    case Province_SP:
                        edt_province.setText(datas.get(i));

                        areaKind = "2";
                        getAreaList(areaKind, edt_province.getText().toString(), "");

                        break;
                    case City_SP:
                        edt_city.setText(datas.get(i));

                        // 按省、市读取医院信息
                        getHospitalList( edt_province.getText().toString(), edt_city.getText().toString() );

                        break;
                    case Hospital_SP:
                        edt_hospital.setText(datas.get(i));
                        hospitalId = hospBean.getRows().get(i).getHospitalId();
                        break;
                    case Depart_SP:
                        edt_depart.setText(datas.get(i));
                        departId = edt_depart.getText().toString();
                        break;
                    case FloorNum_SP:
                        edt_floorNum.setText(datas.get(i));
                        break;
                    case RoomNum_SP:
                        edt_roomNum.setText(datas.get(i));
                        break;
                    case RoomType_SP:
                        edt_roomType.setText(datas.get(i));
                        break;
                }
                popup.dismiss();
            }
        });
    }


    /**
     * 读取 省份数据
     * @param kind
     * @param province
     * @param city
     */
    public void getAreaList(String kind, String province, String city) {

        // paramlist
        AreaRequest.DatBean.Paramlist paramlist = new AreaRequest.DatBean.Paramlist();
        paramlist.setKind(kind);
        if(kind=="2") {
            paramlist.setProvince(province);
        }
        else if(kind=="3") {
            paramlist.setCity(city);
            paramlist.setProvince(province);
        }

        // dat
        AreaRequest.DatBean datBean = new AreaRequest.DatBean();
        datBean.setParamlist(paramlist);

        // request
        AreaRequest rqs = new AreaRequest();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok("");
        rqs.setVer("1");
        rqs.setDat(datBean);

        showProgress();

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this.getContext(), gson.toJson(rqs), "area/list", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
            dismissProgress();
            if (isSucceed) {
                Type typeToken = new TypeToken<AreaResponse>() {}.getType();

                AreaResponse res = gson.fromJson(result, typeToken);
                if (res.getRet().equals("10000")) {

                    if(areaKind=="1") {
                        provinceData = res.getDat();         // 省份数据
                    }
                    else if (areaKind=="2"){
                        areaData = res.getDat();
                    }
                    else if (areaKind=="3"){
                        //areaData = res.getDat();
                    }

                    // if(res.getDat().getRows()!=null && res.getDat().getRows().size() >0 ) {    }
                } else {
                    toastShow(res.getMsg());
                    //initAdvert();
                }
            } else {
                toastShow("网络连接异常，无法访问服务器。");
            }
            }
        });
    }

    /**
     * 读取医院信息
     * @param province
     * @param city
     */
    private void getHospitalList(String province, String city) {

        // paramlist
        HospitalRequest.DatBean.Paramlist pl = new HospitalRequest.DatBean.Paramlist();
        pl.setStatus("1");
        pl.setProvince(province);
        pl.setCity(city);

        // dat
        final HospitalRequest.DatBean rqsBean = new HospitalRequest.DatBean();
        rqsBean.setPageindex("1");
        rqsBean.setPagesize("1000");
        rqsBean.setParamlist(pl);

        // request
        HospitalRequest rqs = new HospitalRequest();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(getContext(), "tok", ""));
        rqs.setVer("1");
        rqs.setDat(rqsBean);

        // post
        showProgress();
        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(getContext(), gson.toJson(rqs), "/hospital/list", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
            dismissProgress();
            if (isSucceed) {
                Type typeToken = new TypeToken<HospitalResponse>() {
                }.getType();

                HospitalResponse hospResponse = gson.fromJson(result, typeToken);
                if (hospResponse.getRet().equals("10000")) {
                    hospBean = hospResponse.getDat();
                    if (hospBean != null) {
                        // initView();
                    }
                } else {
                    toastShow(hospResponse.getMsg());
                }
            } else {
                toastShow("网络连接异常");
            }
            }
        });
    }

}

