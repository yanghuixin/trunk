package com.witiot.cloudbox.views.member;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.BaseRes;
import com.witiot.cloudbox.model.CustInfoRes;
import com.witiot.cloudbox.model.PerfectInfoRqs;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.views.BaseActivity;
import com.witiot.cloudbox.widget.BackgroundDarkPopupWindow;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

public class PerfectInfoActivity extends BaseActivity {

    @BindView(R.id.close)
    TextView close;

    @BindView(R.id.submit_data)
    TextView submitData;

    @BindView(R.id.user_name_et)
    EditText userNameEt;
    @BindView(R.id.gendar_sp)
    TextView gendarSp;
    @BindView(R.id.birth_sp)
    TextView birthSp;
    @BindView(R.id.marriage_sp)
    TextView marriageSp;
    @BindView(R.id.job_et)
    EditText jobEt;

    @BindView(R.id.health_sp)
    TextView healthSp;
    @BindView(R.id.hospital_date_sp)
    TextView hospitalDateSp;
    @BindView(R.id.hospital_days_et)
    EditText hospitalDaysEt;
    @BindView(R.id.surgery_et)
    EditText surgeryEt;
    @BindView(R.id.surgery_time_sp)
    TextView surgeryTimeSp;

    @BindView(R.id.history_et)
    EditText historyEt;
    @BindView(R.id.now_et)
    EditText nowEt;
    @BindView(R.id.main_tell_et)
    EditText mainTellEt;
    @BindView(R.id.zhenduan_et)
    EditText zhenduanEt;

    final int GENDAR_SP = 0;
    final int MARRIAGE_SP = 1;
    final int HEALTH_SP = 2;
    final int HOSPITAL_DATE_SP = 3;
    final int SURGERY_TIME_SP = 4;
    final int BIRTH_SP = 5;

    PerfectInfoRqs perfectInfoRqs = new PerfectInfoRqs();
    PerfectInfoRqs.DatBean datBean = new PerfectInfoRqs.DatBean();
    CustInfoRes.CustInfoBean custInfoBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_perfect_info);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        custInfoBean = (CustInfoRes.CustInfoBean) getIntent().getSerializableExtra("custInfoBean");
        if(custInfoBean != null && StringUtils.stringIsNotEmpty(custInfoBean.getRows().getCustomerName())){
            userNameEt.setText(custInfoBean.getRows().getCustomerName());
            if(custInfoBean.getRows().getGendar() == 1) {
                gendarSp.setText("男");
            }
            else {
                gendarSp.setText("女");
            }

            birthSp.setText(custInfoBean.getRows().getBirth());  // 日期
            marriageSp.setText(custInfoBean.getRows().getMarriage());
            jobEt.setText(custInfoBean.getRows().getProfession());
            healthSp.setText(custInfoBean.getRows().getInsurance());
            hospitalDateSp.setText(custInfoBean.getRows().getHospitalDate());  // 日期
            hospitalDaysEt.setText(custInfoBean.getRows().getHospitalDays()+"");
            historyEt.setText(custInfoBean.getRows().getPastIllness());
            nowEt.setText(custInfoBean.getRows().getPresentIllness());
            mainTellEt.setText(custInfoBean.getRows().getComplaint());
            zhenduanEt.setText(custInfoBean.getRows().getDiagnosis());
            surgeryEt.setText(custInfoBean.getRows().getSurgery());
            surgeryTimeSp.setText(custInfoBean.getRows().getSurgeryTime());  // 日期
        }
    }

    List<String> datas;
    @OnClick({R.id.close, R.id.gendar_sp, R.id.birth_sp, R.id.marriage_sp, R.id.health_sp, R.id.hospital_date_sp,  R.id.surgery_time_sp,R.id.submit_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.gendar_sp:
                datas = new ArrayList<>();
                datas.add("男");
                datas.add("女");
                showpopup(datas,GENDAR_SP, gendarSp);
                break;
            case R.id.birth_sp:
                onYearMonthDayPicker(birthSp,"请选择出生日期");
                break;
            case R.id.marriage_sp:
                datas = new ArrayList<>();
                datas.add("未婚");
                datas.add("已婚");
                showpopup(datas,MARRIAGE_SP,marriageSp);
                break;
            case R.id.health_sp:
                datas = new ArrayList<>();
                datas.add("新农合");
                datas.add("自费");
                datas.add("城镇");
                showpopup(datas,HEALTH_SP,healthSp);
                break;
            case R.id.hospital_date_sp:
                onYearMonthDayPicker(hospitalDateSp,"请选择住院日期");
                break;
            case R.id.surgery_time_sp:
                onYearMonthDayPicker(surgeryTimeSp,"请选择手术日期");
                break;
            case R.id.submit_data:
                submitData();
                break;
        }
    }

    private void submitData() {
        if(checkParams()){
            showProgress();
            perfectInfoRqs.setCmd("add");
            perfectInfoRqs.setSrc("3");
            perfectInfoRqs.setTok((String) SPUtils.get(this,"tok",""));
            perfectInfoRqs.setVer("1");

            final Gson gson = new Gson();
            XRequest xRequest = new XRequest();
            xRequest.sendPostRequest(this, gson.toJson(perfectInfoRqs), "postCustInfo", new XRequestCallback() {
                @Override
                public void callback(boolean isSucceed, String result) {
                    dismissProgress();
                    if(isSucceed){
                        Type typeToken = new TypeToken<BaseRes>() {
                        }.getType();
                        BaseRes res = gson.fromJson(result, typeToken);
                        if(res.getRet().equals("10000")){
                             toastShow("个人资料更新成功");
                             finish();
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

    private boolean checkParams() {

        if(StringUtils.stringIsEmpty(userNameEt.getText().toString())){
            userNameEt.setError("请输入姓名");
            userNameEt.requestFocus();
            return false;
        }else {
            datBean.setCustomerName(userNameEt.getText().toString());
        }

        if(StringUtils.stringIsEmpty(gendarSp.getText().toString())){
            toastShow("请选择性别");
            gendarSp.setError("请选择性别");
            return false;
        }else {
            gendarSp.setError(null, null);
            if(gendarSp.getText().toString().equals("男")){
                datBean.setGendar("1");
            }
            else{
                datBean.setGendar("2");
            }
        }

        if(StringUtils.stringIsEmpty( birthSp.getText().toString())){
            toastShow("请选择出生日期");
            birthSp.setError("请选择出生日期");
            return false;
        }else {
            birthSp.setError(null, null);
            //toastShow(birthSp.getText().toString() + " 00:00:00");
            datBean.setBirth(birthSp.getText().toString() );
        }

        if(StringUtils.stringIsEmpty(marriageSp.getText().toString())){
            toastShow("请选择婚姻状况");
            marriageSp.setError("请选择婚姻状况");
            return false;
        }else {
            marriageSp.setError(null, null);
            datBean.setMarriage(marriageSp.getText().toString());
        }

//        if(StringUtils.stringIsEmpty(jobEt.getText().toString())){
//            jobEt.setError("请输入您的职业");
//            jobEt.requestFocus();
//            return false;
//        }else {
//            datBean.setProfession(jobEt.getText().toString());
//        }
        datBean.setProfession(jobEt.getText().toString());

        if(StringUtils.stringIsEmpty(healthSp.getText().toString())){
            toastShow("请选择医保类型");
            healthSp.setError("请选择婚姻状况");
            return false;
        }else {
            healthSp.setError(null, null);
            datBean.setInsurance(healthSp.getText().toString());
        }

        if(StringUtils.stringIsEmpty(hospitalDateSp.getText().toString())){
            toastShow("请选择住院日期");
            hospitalDateSp.setError("请选择婚姻状况");
            return false;
        }else {
            hospitalDateSp.setError(null, null);
            datBean.setHospitalDate(hospitalDateSp.getText().toString());
        }

        if(StringUtils.stringIsEmpty(hospitalDaysEt.getText().toString())){
            hospitalDaysEt.setError("请输入住院天数");
            hospitalDaysEt.requestFocus();
            return false;
        }else {
            datBean.setHospitalDays(hospitalDaysEt.getText().toString());
        }

//        if(StringUtils.stringIsEmpty(surgeryEt.getText().toString())){
//            //toastShow("请输入手术名称");
//            surgeryEt.setError("请输入手术名称");
//            surgeryEt.requestFocus();
//            return false;
//        }else {
//            datBean.setSurgery(surgeryEt.getText().toString());
//        }
        datBean.setSurgery(surgeryEt.getText().toString());

        if(StringUtils.stringIsEmpty(nowEt.getText().toString())){
            nowEt.setError("请输入现病史");
            nowEt.requestFocus();
            return false;
        }else {
            datBean.setPresentIllness(nowEt.getText().toString());
        }

//        if(StringUtils.stringIsEmpty(surgeryTimeSp.getText().toString())){
//            toastShow("请选择手术日期");
//            return false;
//        }else {
//            datBean.setSurgeryTime(surgeryTimeSp.getText().toString());
//        }
        datBean.setSurgeryTime(surgeryTimeSp.getText().toString());

//        if(StringUtils.stringIsEmpty(historyEt.getText().toString())){
//            historyEt.setError("请输入既往史");
//            historyEt.requestFocus();
//            return false;
//        }else {
//            datBean.setPastIllness(historyEt.getText().toString());
//        }
        datBean.setPastIllness(historyEt.getText().toString());



//        if(StringUtils.stringIsEmpty(mainTellEt.getText().toString())){
//            mainTellEt.setError("请输入主诉");
//            mainTellEt.requestFocus();
//            return false;
//        }else {
//            datBean.setComplaint(mainTellEt.getText().toString());
//        }
        datBean.setComplaint(mainTellEt.getText().toString());

//        if(StringUtils.stringIsEmpty(zhenduanEt.getText().toString())){
//            zhenduanEt.setError("请输入诊断");
//            zhenduanEt.requestFocus();
//            return false;
//        }else {
//            datBean.setDiagnosis(zhenduanEt.getText().toString());
//        }
        datBean.setDiagnosis(zhenduanEt.getText().toString());

        datBean.setCustomerId((String) SPUtils.get(this,"customerId",""));

        perfectInfoRqs.setDat(datBean);
        return true;
    }


    public void onYearMonthDayPicker( final TextView textView,String title) {
        final DatePicker picker = new DatePicker(this);
        picker.setGravity(Gravity.CENTER);
        int h = getResources().getDimensionPixelOffset(R.dimen.perfect_info_pop_w);
        String today = TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd"));
        picker.setWidth(h);
        picker.setTopPadding(15);
        picker.setPadding(15);
        if(title.equals("选择手术时间")){
            picker.setRangeStart(Integer.parseInt(today.split("-")[0]), Integer.parseInt(today.split("-")[1]), Integer.parseInt(today.split("-")[2]));
            picker.setRangeEnd(2060,1,1);
        }else{
            picker.setRangeStart(1900, 1, 1);
            picker.setRangeEnd(Integer.parseInt(today.split("-")[0]), Integer.parseInt(today.split("-")[1]), Integer.parseInt(today.split("-")[2]));
        }

        picker.setSelectedItem(Integer.parseInt(today.split("-")[0]), Integer.parseInt(today.split("-")[1]), Integer.parseInt(today.split("-")[2]));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
//                showToast(year + "-" + month + "-" + day);
                textView.setText(year + "-" + month + "-" + day);
            }
        });
        picker.setTitleText(title);

        picker.show();
    }


    BackgroundDarkPopupWindow popup;
    private void showpopup(final List<String> datas, final int type,View rootview) {
        View popupView = getLayoutInflater().inflate(R.layout.pop_listview, null);
        ListView listview = (ListView) popupView.findViewById(R.id.list_pop);
        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas));
        popup = new BackgroundDarkPopupWindow(
                popupView, getResources().getDimensionPixelSize(R.dimen.perfect_info_pop_w),
                WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.white)));
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
                    case HEALTH_SP:
                        healthSp.setText(datas.get(i));
                        break;
                    case GENDAR_SP:
                        gendarSp.setText(datas.get(i));
                        break;
                    case MARRIAGE_SP:
                        marriageSp.setText(datas.get(i));
                        break;
                    case SURGERY_TIME_SP:
                        surgeryTimeSp.setText(datas.get(i));
                        break;
                    case BIRTH_SP:
                        birthSp.setText(datas.get(i));
                        break;
                    case HOSPITAL_DATE_SP:
                        hospitalDateSp.setText(datas.get(i));
                        break;
                }
                popup.dismiss();
            }
        });
    }

}
