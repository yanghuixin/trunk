package com.witiot.cloudbox.views.box;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.MyApplication;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.BaseRequest;
import com.witiot.cloudbox.model.BaseRes;
import com.witiot.cloudbox.model.BillCalenderPostRqs;
import com.witiot.cloudbox.model.BillCalenderRes;
import com.witiot.cloudbox.model.BillCalenderRqs;
import com.witiot.cloudbox.model.BluetoothResEvent;
import com.witiot.cloudbox.model.BluetoothRqsEvent;
import com.witiot.cloudbox.model.CustLogResponse;
import com.witiot.cloudbox.service.MyBletoothService;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.SpanUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.views.BaseFragment;
import com.witiot.cloudbox.views.navigation.NavigationActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * 云头柜开关
 */
public class BoxFragment extends BaseFragment {

    @BindView(R.id.box_record_tv)
    TextView boxRecordTv;

    @BindView(R.id.pw_tip)
    TextView pwTip;
    @BindView(R.id.air_tip)
    TextView airTip;
    @BindView(R.id.xdg_tip)
    TextView xdgTip;
    @BindView(R.id.jsq_tip)
    TextView jsqTip;

    Unbinder unbinder;
    @BindView(R.id.time_tv)
    TextView timeTv;

    @BindView(R.id.box_buy_service)
    TextView boxBuyService;
    @BindView(R.id.help)
    TextView help;

    @BindView(R.id.pw_iv)
    ImageView pwIv;
    @BindView(R.id.air_iv)
    ImageView airIv;
    @BindView(R.id.xdg_iv)
    ImageView xdgIv;
    @BindView(R.id.jsq_iv)

    ImageView jsqIv;

    @BindView(R.id.switch_pw_open)
    Button switchPwOpen;
    @BindView(R.id.switch_pw_close)
    Button switchPwClose;

    @BindView(R.id.switch_air_open)
    Button switchAirOpen;
    @BindView(R.id.switch_air_close)
    Button switchAirClose;

    @BindView(R.id.switch_xdg_open)
    Button switchXdgOpen;
    @BindView(R.id.switch_xdg_close)
    Button switchXdgClose;

    @BindView(R.id.switch_jsq_open)
    Button switchJsqOpen;
    @BindView(R.id.switch_jsq_close)
    Button switchJsqClose;

    @BindView(R.id.time_pw)
    Chronometer timePw;
    @BindView(R.id.time_air)
    Chronometer timeAir;
    @BindView(R.id.time_xdg)
    Chronometer timeXdg;
    @BindView(R.id.time_jsq)
    Chronometer timeJsq;

    @BindView(R.id.ytg_day)
    TextView ytg_day;
    @BindView(R.id.control_name)
    TextView controlName;
    @BindView(R.id.pw_et)
    EditText pwEt;


    private final long pw_total_time = (long) (60);//秒
    private final long jsq_total_time = (long) (60);//秒

    @BindView(R.id.connect_bluetooth_bt)
    TextView connectBluetoothBt;

    @BindView(R.id.reLinkBt)
    TextView reLinkBt;

    @BindView(R.id.reLinkRl)
    RelativeLayout reLinkRl;

    @BindView(R.id.btn_buy_service)
    TextView btnBuyService;

    @BindView(R.id.rl_nodata)
    RelativeLayout rlNodata;

    private long jsq_start_time = 0;

    private final long xdg_total_time = (long) (2. * 60);//秒
    private long xdg_start_time = 0;

    private long jhq_start_time = 0;

    View view;
    List<BillCalenderRes.DatBean.RowsBean> billCalenders;

    public BoxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_box, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        SimpleDateFormat dt = new SimpleDateFormat("yyyy年MM月dd日");
        ytg_day.setText(dt.format(TimeUtils.getNowDate()));
        getData();
        initUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getData();
    }


    /**
     * 读取当日的开关数据
     */
    private void getData() {
        BluetoothRqsEvent messageEvent = new BluetoothRqsEvent();
        messageEvent.setViewId(120);
        EventBus.getDefault().post(messageEvent);
        // paramlist
        BillCalenderRqs.DatBean.Paramlist paramlist = new BillCalenderRqs.DatBean.Paramlist();
        paramlist.setCustomerId((String) SPUtils.get(getContext(), "customerId", ""));
        paramlist.setServiceDay(TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd")));  // 当天！！！

        // dat
        BillCalenderRqs.DatBean datBean = new BillCalenderRqs.DatBean();
        datBean.setParamlist(paramlist);
        datBean.setOrderby("seqIndex ");

        // request
        BillCalenderRqs rqs = new BillCalenderRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(getContext(), "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);

        // post
        showProgress();
        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(getContext(), gson.toJson(rqs), "bill/getBillCalenderList", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();

                if (isSucceed) {
                    Type typeToken = new TypeToken<BillCalenderRes>() {
                    }.getType();

                    BillCalenderRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        billCalenders = res.getDat().getRows();
                        if (billCalenders != null && billCalenders.size()>0) {
                            updateViewCount("", 0);

                            rlNodata.setVisibility(View.GONE);  // 不显示
                        }
                        else {
                            rlNodata.setVisibility(View.VISIBLE); // 显示
                        }
                    } else {
                        toastShow(res.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
            }
        });
    }


    private void initUI() {

        timeAir.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                // 如果开始计时到现在超过了startime秒
//                if (SystemClock.elapsedRealtime()
//                        - chronometer.getBase() > jsq_total_time * 1000) {
//                    chronometer.stop();
//                    timeAir.setVisibility(View.INVISIBLE);
//                    timeAir.stop();
//                    switchAirOpen.setEnabled(true);
//                    switchAirOpen.setText("启动");
//                    switchAirClose.setEnabled(false);
//                    SPUtils.remove(getContext(), "jhq_start_time");
//                    updateViewCount("", 0);
//                }
            }
        });

        jhq_start_time = (long) SPUtils.get(getContext(), "jhq_start_time", (long) 0);
        // 净化器
        if ((boolean) SPUtils.get(getContext(), "jhq_is_open", false)) {
            switchAirOpen.setText("已开启");
            switchAirOpen.setEnabled(false);
            switchAirClose.setEnabled(true);
            timeAir.setVisibility(View.VISIBLE);
            timeAir.setBase(jhq_start_time);
            timeAir.start();
            SPUtils.put(getContext(), "jhq_is_open", true);
        }

        // 消毒柜
        timeXdg.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                // 如果开始计时到现在超过了startime秒
                if (SystemClock.elapsedRealtime()
                        - chronometer.getBase() > xdg_total_time * 1000) {
                    chronometer.stop();
                    timeXdg.setVisibility(View.INVISIBLE);
                    timeXdg.stop();
                    switchXdgOpen.setEnabled(true);
                    switchXdgOpen.setText("启动");
                    switchXdgClose.setEnabled(false);
                    SPUtils.remove(getContext(), "xdg_start_time");
                    updateViewCount("", 0);
                }
            }
        });

        xdg_start_time = (long) SPUtils.get(getContext(), "xdg_start_time", (long) 0);
        if (xdg_start_time > 0) {
            switchXdgOpen.setText("已开启");
            switchXdgOpen.setEnabled(false);
            switchXdgClose.setEnabled(true);
            timeXdg.setVisibility(View.VISIBLE);
            timeXdg.setBase(xdg_start_time);
            timeXdg.start();
        }

        timeJsq.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                // 如果开始计时到现在超过了startime秒
                if (SystemClock.elapsedRealtime()
                        - chronometer.getBase() > jsq_total_time * 1000) {
                    chronometer.stop();
                    timeJsq.setVisibility(View.INVISIBLE);
                    timeJsq.stop();
                    switchJsqOpen.setEnabled(true);
                    switchJsqOpen.setText("启动");
                    switchJsqClose.setEnabled(false);
                    SPUtils.remove(getContext(), "jsq_start_time");
                    updateViewCount("", 0);
                }
            }
        });

        // 净水器
        jsq_start_time = (long) SPUtils.get(getContext(), "jsq_start_time", (long) 0);
        if (jsq_start_time > 0) {
            switchJsqOpen.setText("已开启");
            switchJsqOpen.setEnabled(false);
            switchJsqClose.setEnabled(true);
            timeJsq.setVisibility(View.VISIBLE);
            timeJsq.setBase(jsq_start_time);
            timeJsq.start();
        }

        timePw.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (SystemClock.elapsedRealtime()
                        - chronometer.getBase() > pw_total_time * 1000) {
                    chronometer.stop();
                    timePw.stop();
                    switchPwOpen.setEnabled(true);
                    switchPwOpen.setText("开锁");
                    switchPwClose.setEnabled(false);
                    SPUtils.remove(getContext(), "pw_start_time");
                }
            }
        });
        long pw_start_time = (long) SPUtils.get(getContext(), "pw_start_time", (long) 0);
        if (pw_start_time > 0) {
            switchPwOpen.setText("已开锁");
            switchPwOpen.setEnabled(false);
            switchPwClose.setEnabled(true);
            timePw.setBase(pw_start_time);
            timePw.start();
        }

        initControlStatus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timeJsq != null) {
            timeJsq.stop();
        }

        if (timeXdg != null) {
            timeXdg.stop();
        }

        if (timePw != null) {
            timePw.stop();
        }

        if (timeAir != null) {
            timeAir.stop();
        }

        if (unbinder != null) {
            unbinder.unbind();
        }

        EventBus.getDefault().unregister(this);

    }


    @OnClick({R.id.reLinkRl,R.id.reLinkBt,R.id.connect_bluetooth_bt, R.id.box_record_tv, R.id.box_buy_service, R.id.help,
            R.id.switch_pw_open, R.id.switch_pw_close, R.id.switch_air_open, R.id.switch_air_close,
            R.id.switch_xdg_open, R.id.switch_xdg_close, R.id.switch_jsq_open, R.id.switch_jsq_close, R.id.rl_nodata, R.id.btn_buy_service})
    public void onViewClicked(View view) {
        BluetoothRqsEvent messageEvent = new BluetoothRqsEvent();
        switch (view.getId()) {
            case R.id.switch_pw_open:
                if (checkPwParams()) {
                    return;
                }
                messageEvent.setEventName("");
                messageEvent.setViewId(R.id.switch_pw_open);
                messageEvent.setEventType(1);
                EventBus.getDefault().post(messageEvent);
                pwEt.setText("");
                setControlStatus("开启密码锁");
                break;
            case R.id.switch_pw_close:
                messageEvent.setEventName("");
                messageEvent.setViewId(R.id.switch_pw_close);
                messageEvent.setEventType(0);
                EventBus.getDefault().post(messageEvent);
                break;
            case R.id.switch_air_open:
                messageEvent.setEventName("");
                messageEvent.setViewId(R.id.switch_air_open);
                messageEvent.setEventType(1);
                EventBus.getDefault().post(messageEvent);
                break;
            case R.id.switch_air_close:
                messageEvent.setEventName("");
                messageEvent.setViewId(R.id.switch_air_close);
                messageEvent.setEventType(0);
                EventBus.getDefault().post(messageEvent);
                break;
            case R.id.switch_xdg_open:
                messageEvent.setEventName("");
                messageEvent.setViewId(R.id.switch_xdg_open);
                messageEvent.setEventType(1);
                EventBus.getDefault().post(messageEvent);
                break;
            case R.id.switch_xdg_close:
                messageEvent.setEventName("");
                messageEvent.setViewId(R.id.switch_xdg_close);
                messageEvent.setEventType(0);
                EventBus.getDefault().post(messageEvent);
                break;
            case R.id.switch_jsq_open:
                messageEvent.setEventName("");
                messageEvent.setViewId(R.id.switch_jsq_open);
                messageEvent.setEventType(1);
                EventBus.getDefault().post(messageEvent);
                break;
            case R.id.switch_jsq_close:
                messageEvent.setEventName("");
                messageEvent.setViewId(R.id.switch_jsq_close);
                messageEvent.setEventType(1);
                EventBus.getDefault().post(messageEvent);
                break;
            case R.id.box_buy_service:
                startActivity(new Intent(getActivity(), BuyServiceActivity.class));
                break;
            case R.id.btn_buy_service:
                startActivity(new Intent(getActivity(), BuyServiceActivity.class));
                //
                break;
            case R.id.help:
                break;
            case R.id.box_record_tv:
                startActivity(new Intent(getActivity(), BoxRecordActivity.class));
                break;
            case R.id.connect_bluetooth_bt:
                toastShow("正在重试连接，请稍候……");
                showProgress();
                Intent intent2 = new Intent(getContext(), MyBletoothService.class);
                getContext().startService(intent2);
                break;
            case R.id.reLinkBt:
                toastShow("正在重试连接，请稍候……");
                showProgress();
                Intent intent1 = new Intent(getContext(), MyBletoothService.class);
                getContext().startService(intent1);
                break;
            case R.id.reLinkRl:
                break;
        }
    }

    private boolean checkPwParams() {
        if (StringUtils.stringIsEmpty(pwEt.getText().toString())) {
            toastShow("请输入密码后点击开锁");
            pwEt.setError("请输入密码");
            pwEt.requestFocus();
            return true;
        }
        if (pwEt.getText().toString().length() != 4) {
            toastShow("请输入4位密码");
            pwEt.setError("请输入4位密码");
            pwEt.requestFocus();
            return true;
        }
        if (!pwEt.getText().toString().equals("0506")) {
            toastShow("密码有误");
            pwEt.setError("密码有误");
            pwEt.requestFocus();
            return true;
        }
        return false;
    }

    int heartCount = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void BluetoothRes(BluetoothResEvent messageEvent) {
        if (messageEvent.getEventName().equals("bluetoothLink")) {
            dismissProgress();
            if (messageEvent.getEventType() == 0) {
                toastShow("设备连接失败");
                connectBluetoothBt.setText("重新连接设备");
                reLinkRl.setVisibility(View.VISIBLE);
                connectBluetoothBt.setEnabled(true);
            }else if(messageEvent.getEventType() == -1){
                connectBluetoothBt.setText("设备蓝牙未配对");
                reLinkRl.setVisibility(View.VISIBLE);
                connectBluetoothBt.setEnabled(false);
            }else {
                reLinkRl.setVisibility(View.GONE);
                connectBluetoothBt.setText("重新连接设备");
                connectBluetoothBt.setEnabled(true);
            }
        } else if (messageEvent.getEventName().equals("heart")) {
            reLinkRl.setVisibility(View.GONE);
            heartCount++;
            help.setText("心跳" + heartCount);
        } else {
//            edtReceivedMessage.setText(messageEvent.getEventName()+"  type = "+messageEvent.getEventType());
            changeStatus(messageEvent);
        }

    }

    private void changeStatus(BluetoothResEvent messageEvent) {
        String name = messageEvent.getEventName();
        switch (name) {
            case "jsq":
                jsqStatus(messageEvent);
                break;
            case "jhq":
                jhqStatus(messageEvent);
                break;
            case "xdg":
                xdgStatus(messageEvent);
                break;
            case "mms":
                mmsStatus(messageEvent);
                break;
        }
    }

    private void mmsStatus(BluetoothResEvent messageEvent) {
        if (messageEvent.getEventType() == 1) {
            switchPwOpen.setText("已开锁");
            switchPwOpen.setEnabled(false);
            switchPwClose.setEnabled(true);
            long temp = SystemClock.elapsedRealtime();
            timePw.setBase(temp);
            timePw.start();
            SPUtils.put(mActivity, "pw_start_time", temp);
            setControlStatus("开启密码锁");
            updateViewCount("mms", 1);             // 控制UI显示
            postCalender("mms", 1);                // 提交日历表
            postCustLog("mms", "开启密码锁");
        } else {
            switchPwOpen.setText("开锁");
            switchPwOpen.setEnabled(true);
            switchPwClose.setEnabled(false);
            timePw.setVisibility(View.INVISIBLE);
            timePw.stop();
            setControlStatus("关闭密码锁");
            postCustLog("mms", "关闭密码锁");   // 提交操作日志
        }
    }

    private void xdgStatus(BluetoothResEvent messageEvent) {
        if (messageEvent.getEventType() == 1) {
            switchXdgOpen.setText("已开启");
            switchXdgOpen.setEnabled(false);
            switchXdgClose.setEnabled(true);
            timeXdg.setVisibility(View.VISIBLE);
            long temp = SystemClock.elapsedRealtime();
            timeXdg.setBase(temp);
            timeXdg.start();
            SPUtils.put(mActivity, "xdg_start_time", temp);
            setControlStatus("开启消毒柜");
            updateViewCount("xdg", 1);             // 控制UI显示
            postCalender("xdg", 1);                // 提交日历表
            postCustLog("xdg", "开启消毒柜");   // 提交操作日志
        } else {
            switchXdgOpen.setText("启动");
            switchXdgOpen.setEnabled(true);
            switchXdgClose.setEnabled(false);
            timeXdg.setVisibility(View.INVISIBLE);
            timeXdg.stop();
            SPUtils.remove(mActivity, "xdg_start_time");
            setControlStatus("关闭消毒柜");
            postCustLog("xdg", "关闭消毒柜");   // 提交操作日志
        }
    }

    private void jsqStatus(BluetoothResEvent messageEvent) {
        if (messageEvent.getEventType() == 1) {
            switchJsqOpen.setText("已开启");
            switchJsqOpen.setEnabled(false);
            switchJsqClose.setEnabled(true);
            timeJsq.setVisibility(View.VISIBLE);  //显示计时器
            long temp = SystemClock.elapsedRealtime();
            timeJsq.setBase(temp);
            timeJsq.start();
            SPUtils.put(mActivity, "jsq_start_time", temp);
            setControlStatus("开启净水器");
            updateViewCount("jsq", 1);             // 控制UI显示
            postCalender("jsq", 1);                // 提交日历表
            postCustLog("jsq", "开启净水器");   // 提交操作日志
        } else {
            switchJsqOpen.setText("启动");
            switchJsqOpen.setEnabled(true);
            switchJsqClose.setEnabled(false);
            SPUtils.put(mActivity, "jsq_is_open", false);
            timeJsq.setVisibility(View.INVISIBLE);  // 隐藏
            timeJsq.stop();
            setControlStatus("关闭净水器");
            SPUtils.remove(mActivity, "jsq_start_time");
            postCustLog("jsq", "关闭净水器");   // 提交操作日志
        }
    }

    private void jhqStatus(BluetoothResEvent messageEvent) {
        if (messageEvent.getEventType() == 1) {
            switchAirOpen.setText("已开启");
            switchAirOpen.setEnabled(false);
            switchAirClose.setEnabled(true);

            timeAir.setVisibility(View.VISIBLE);  //显示计时器
            long temp = SystemClock.elapsedRealtime();
            timeAir.setBase(temp);
            timeAir.start();

            SPUtils.put(mActivity, "jhq_start_time", temp);
            SPUtils.put(mActivity, "jhq_is_open", true);
            setControlStatus("开启空气净化器");
            postCustLog("jhq", "开启空气净化器");   // 提交操作日志
        } else {
            switchAirOpen.setText("启动");
            switchAirOpen.setEnabled(true);
            switchAirClose.setEnabled(false);
            long totalTime = SystemClock.elapsedRealtime() - timeAir.getBase();
            timeAir.setVisibility(View.INVISIBLE);  // 隐藏
            timeAir.stop();
            SPUtils.put(mActivity, "jhq_is_open", false);
            setControlStatus("关闭空气净化器");
            SPUtils.remove(mActivity, "jhq_start_time");
            // 使用分钟
            int useCount = (int) (totalTime / 60000);
            postCalender("jhq", useCount);                // 提交日历表
            updateViewCount("jhq", useCount);             // 控制UI显示
            postCustLog("jhq", "关闭空气净化器");   // 提交操作日志
        }
    }

    private void setControlStatus(String name) {
        toastShow(name);
        timeTv.setVisibility(View.VISIBLE);
        controlName.setVisibility(View.VISIBLE);
        String time = TimeUtils.getNowString(new SimpleDateFormat("HH:mm:ss"));
        timeTv.setText(time);
        controlName.setText(name);
        SPUtils.put(getActivity(), "ControlStatus", name);
        SPUtils.put(getActivity(), "ControlStatusTime", time);
    }

    private void initControlStatus() {
        String name = (String) SPUtils.get(getActivity(), "ControlStatus", "");
        if (StringUtils.stringIsNotEmpty(name)) {
            timeTv.setText((String) SPUtils.get(getActivity(), "ControlStatusTime", ""));
            controlName.setText((String) SPUtils.get(getActivity(), "ControlStatus", ""));
        } else {
            timeTv.setVisibility(View.INVISIBLE);
            controlName.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 根据 获取的数据 更新 UI
     *
     * @param deviceType
     */
    private void updateViewCount(String deviceType, int useCount) {
        if(billCalenders == null){
            return;
        }
        for (int i = 0; i < billCalenders.size(); i++) {

            // 查找 对应的设备
//            if (billCalenders.get(i).getDeviceType().equals(deviceType)) {

            // 密码锁
            if (billCalenders.get(i).getDeviceType().equals("mms")) {
                // 累加 使用次数，如果是空气净化器，则是分钟
                if (deviceType.equals("mms")) {
                    int actualCount = billCalenders.get(i).getActualCount() + useCount;
                    billCalenders.get(i).setActualCount(actualCount);
                }
                pwTip.setText(new SpanUtils().append("已用").append(billCalenders.get(i).getActualCount() + "")
                        .setForegroundColor(ContextCompat.getColor(MyApplication.getContext(), R.color.orange_text))
                        .append("次/不限次").create()
                );
            }
            // 空气净化器
            else if (billCalenders.get(i).getDeviceType().equals("jhq")) {
                // 累加 使用次数，如果是空气净化器，则是分钟
                if (deviceType.equals("jhq")) {
                    int actualCount = billCalenders.get(i).getActualCount() + useCount;
                    billCalenders.get(i).setActualCount(actualCount);
                }
                airTip.setText(new SpanUtils().append("已用").append(billCalenders.get(i).getActualCount() + "")
                        .setForegroundColor(ContextCompat.getColor(MyApplication.getContext(), R.color.orange_text))
                        .append("分钟/不限时").create()
                );
            }
            // 消毒柜
            else if (billCalenders.get(i).getDeviceType().equals("xdg")) {
                // 累加 使用次数，如果是空气净化器，则是分钟
                if (deviceType.equals("xdg")) {
                    int actualCount = billCalenders.get(i).getActualCount() + useCount;
                    billCalenders.get(i).setActualCount(actualCount);
                }
                if (billCalenders.get(i).getActualCount() >= billCalenders.get(i).getAvailableCount()) {
                    switchXdgOpen.setEnabled(false);
                }
                xdgTip.setText(new SpanUtils().append("已用").append(billCalenders.get(i).getActualCount() + "")
                        .setForegroundColor(ContextCompat.getColor(MyApplication.getContext(), R.color.orange_text))
                        .append("次/共").append(billCalenders.get(i).getAvailableCount() + "").append("次").create()
                );
            }
            // 净水器
            else if (billCalenders.get(i).getDeviceType().equals("jsq")) {
                // 累加 使用次数，如果是空气净化器，则是分钟
                if (deviceType.equals("jsq")) {
                    int actualCount = billCalenders.get(i).getActualCount() + useCount;
                    billCalenders.get(i).setActualCount(actualCount);
                }
                if (billCalenders.get(i).getActualCount() >= billCalenders.get(i).getAvailableCount()) {
                    switchJsqOpen.setEnabled(false);
                }
                jsqTip.setText(new SpanUtils().append("已用").append(billCalenders.get(i).getActualCount() + "")
                        .setForegroundColor(ContextCompat.getColor(MyApplication.getContext(), R.color.orange_text))
                        .append("次/共").append(billCalenders.get(i).getAvailableCount() + "").append("次").create()
                );
            }

        }

//        }
    }

    /**
     * 提交使用日历，主要是次数
     *
     * @param deviceType
     * @param useCount
     */
    private void postCalender(String deviceType, int useCount) {

        //initView(deviceType);   // 控制UI显示

        BillCalenderRes.DatBean.RowsBean bean = null;
        for (int i = 0; i < billCalenders.size(); i++) {
            // 根据设备类型判断
            if (billCalenders.get(i).getDeviceType().equals(deviceType)) {
                bean = billCalenders.get(i);
                break;
            }
        }

        if (bean != null) {
            // dat
            BillCalenderPostRqs.DatBean datBean = new BillCalenderPostRqs.DatBean();
            datBean.setCustomerId((String) SPUtils.get(getContext(), "customerId", ""));
            datBean.setServiceId(bean.getServiceId());    // 哪一个 serviceId
            datBean.setServiceDay(bean.getServiceDay());    // 哪一天
            datBean.setUseCount(useCount);

            // request
            BillCalenderPostRqs rqs = new BillCalenderPostRqs();
            rqs.setCmd("get");
            rqs.setSrc("3");
            rqs.setTok((String) SPUtils.get(getContext(), "tok", ""));
            rqs.setVer("1");
            rqs.setDat(datBean);

            showProgress();
            final Gson gson = new Gson();
            XRequest xRequest = new XRequest();
            xRequest.sendPostRequest(getContext(), gson.toJson(rqs), "bill/postCalender", new XRequestCallback() {
                @Override
                public void callback(boolean isSucceed, String result) {
                    dismissProgress();
                    if (isSucceed) {
                        Type typeToken = new TypeToken<BaseRes>() {
                        }.getType();
                        BaseRes res = gson.fromJson(result, typeToken);
                        if (res.getRet().equals("10000")) {

                        } else {
                            toastShow(res.getMsg());
                        }
                    } else {
                        toastShow("网络连接异常");
                    }
                }
            });
        }
    }


    /**
     * 提交使用日志，操作日志，便于查询
     *
     * @param deviceType
     * @param operContent
     */
    private void postCustLog(String deviceType, String operContent) {
        int operType = 4;
        String operPage = deviceType;
        postCustLog(operType, operPage, operContent);
    }

    private void postCustLog(int operType, String operPage, String operContent) {

        // dat
        CustLogResponse.DatBean.CustLogBean datBean = new CustLogResponse.DatBean.CustLogBean();
        datBean.setCustomerId((String) SPUtils.get(getContext(), "customerId", ""));
        datBean.setDeviceId(DeviceUtils.getAndroidID());
        datBean.setOperType(operType);
        datBean.setOperPage(operPage);
        datBean.setOperContent(operContent);

        // request
        BaseRequest rqs = new BaseRequest();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(getContext(), "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);  // 使用 CustLogResponse !!! .DatBean.

        showProgress();
        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(getContext(), gson.toJson(rqs), "postCustLog", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<BaseRes>() {
                    }.getType();

                    BaseRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {

                    } else {
                        toastShow(res.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
            }
        });

    }


}
