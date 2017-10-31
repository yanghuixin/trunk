package com.witiot.cloudbox.views.box;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.MonthPager;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.adapter.BoxSpecificAdapter;
import com.witiot.cloudbox.adapter.FlowSpecificAdapter;
import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.interfaces.RcOnItemClickListener;
import com.witiot.cloudbox.model.BaseRes;
import com.witiot.cloudbox.model.BillCalenderRes;
import com.witiot.cloudbox.model.BillCalenderRqs;
import com.witiot.cloudbox.model.BuyFlowRqs;
import com.witiot.cloudbox.model.FlowSpecifiRes;
import com.witiot.cloudbox.model.FlowSpecificRqs;
import com.witiot.cloudbox.model.OrderListRes;
import com.witiot.cloudbox.model.OrderListRqs;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.RecycleViewDivider;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.views.BaseActivity;
import com.witiot.cloudbox.views.member.MyOrderActivity;
import com.witiot.cloudbox.widget.BackgroundDarkPopupWindow;
import com.witiot.cloudbox.widget.CustomDayView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

public class BuyServiceActivity extends BaseActivity {

    List<LinearLayout> linearLayouts = new ArrayList<>();

    @BindView(R.id.close)
    TextView close;

    @BindView(R.id.pay_bt)
    LinearLayout payBt;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.box_buy_record)
    TextView boxBuyRecord;
    @BindView(R.id.unit_price_tv)
    TextView unitPriceTv;

    // liuyp
    @BindView(R.id.edt_begindate)
    TextView edt_begindate;
    @BindView(R.id.edt_buydays)
    EditText edt_buydays;

    @BindView(R.id.tv_orderId)
    TextView tv_orderId;
    @BindView(R.id.tv_orderMoney)
    TextView tv_orderMoney;
    @BindView(R.id.tv_beginDate)
    TextView tv_beginDate;
    @BindView(R.id.tv_endDate)
    TextView tv_endDate;
    @BindView(R.id.tv_buyDays)
    TextView tv_buyDays;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private LinearLayoutManager linearLayoutManager;
    BoxSpecificAdapter adapter;
    private List<FlowSpecifiRes.DatBean.RowsBean> rowsBeanList;
    Gson gson;
    double u_price = 0;//单价

    private ArrayList<com.ldf.calendar.view.Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private Context mContext;
    private CalendarDate currentDate;
    private boolean initiated = false;

    List<BillCalenderRes.DatBean.RowsBean> billCalenders;

    private List<OrderListRes.DatBean.OrderBean> orderList = new ArrayList<>();
    int orderType = 0 ;// 0云头柜，1流量
    long repeatTime = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_service);
        ButterKnife.bind(this);
        mContext = this;
        initUI();
        getData();

    }

    private void getData() {
        // paramlist
        FlowSpecificRqs.DatBean.Paramlist paramlist = new FlowSpecificRqs.DatBean.Paramlist();
//        serviceType=0  云头柜 freeDay 免费天数 price 为云头柜价格
//                serviceType=1 流量 使用的是Service开头的 servicePrice 流量价格
        paramlist.setHospitalId(CommonUtils.getHospitalId(this));
        paramlist.setStandardId("");
        paramlist.setStandardName("");
        paramlist.setStandardType("0");
        paramlist.setStatus("");

        // dat
        FlowSpecificRqs.DatBean datBean = new FlowSpecificRqs.DatBean();
        datBean.setParamlist(paramlist);

        // request
        FlowSpecificRqs rqs = new FlowSpecificRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);

        // post
        showProgress();
        gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "bill/hospital/service/listAllot", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<FlowSpecifiRes>() {
                    }.getType();

                    FlowSpecifiRes res = gson.fromJson(result, typeToken);

                    if (res.getRet().equals("10000")) {
                        rowsBeanList = res.getDat().getRows();
                        if (rowsBeanList.size() > 0) {
                            u_price = rowsBeanList.get(0).getPrice();

                            unitPriceTv.setText("每天" + u_price + "元");  // 单价
                            edt_begindate.setText(TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd")));  // 默认开始日期
                            edt_buydays.setText("7");       // 默认天数

                            // 绑定数据
                            adapter.setData(rowsBeanList);
                        }
                    } else {
                        toastShow(res.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }

                getCalenderList();
            }
        });
    }

    private void initUI() {

        recyclerview.setHasFixedSize(true);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.listview_divider);
        recyclerview.addItemDecoration(new RecycleViewDivider(
             mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.bg_grey)));
        adapter = new BoxSpecificAdapter(mContext);
        recyclerview.setAdapter(adapter);

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerview.setLayoutManager(linearLayoutManager);

        adapter.notifyDataSetChanged();

        edt_buydays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_buyDays.setText(editable.toString());

                if (StringUtils.stringIsNotEmpty( editable.toString())) {
                    tv_orderMoney.setText(String.valueOf(u_price * Integer.parseInt(editable.toString())));
                } else {
                    tv_orderMoney.setText("0");
                }

                if (StringUtils.stringIsNotEmpty( editable.toString())) {
                    Integer buyDays = Integer.parseInt(tv_buyDays.getText().toString()) - 1;  // 减一天
                    Calendar ca = Calendar.getInstance();
                    ca.setTime(TimeUtils.string2Date(tv_beginDate.getText().toString() + " 00:00:00"));
                    ca.add(Calendar.DATE, buyDays);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String endTime = TimeUtils.date2String(ca.getTime(), df);
                    tv_endDate.setText(endTime);
                }
            }
        });

        edt_begindate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (StringUtils.stringIsNotEmpty(editable.toString())) {
                    tv_beginDate.setText(editable.toString());

                    Integer buyDays = Integer.parseInt(tv_buyDays.getText().toString()) - 1;  // 减一天
                    Calendar ca =  Calendar.getInstance();
                    ca.setTime( TimeUtils.string2Date( tv_beginDate.getText().toString() + " 00:00:00" ) );
                    ca.add(Calendar.DATE, buyDays);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String endTime = TimeUtils.date2String(ca.getTime(), df);
                    tv_endDate.setText(endTime);
                } else {
                    tv_beginDate.setText(TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd")));

                    Integer buyDays = Integer.parseInt(tv_buyDays.getText().toString()) - 1;  // 减一天
                    Calendar ca =  Calendar.getInstance();
                    ca.setTime( TimeUtils.string2Date( tv_beginDate.getText().toString() + " 00:00:00"  ) );
                    ca.add(Calendar.DATE, buyDays);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String endTime = TimeUtils.date2String(ca.getTime(), df);
                    tv_endDate.setText(endTime);
                }
            }
        });
    }

    private void getCalenderList() {

        // paramlist
        BillCalenderRqs.DatBean.Paramlist paramlist = new BillCalenderRqs.DatBean.Paramlist();
        paramlist.setCustomerId((String) SPUtils.get(this, "customerId", ""));


        // dat
        BillCalenderRqs.DatBean datBean = new BillCalenderRqs.DatBean();
        datBean.setParamlist(paramlist);
        datBean.setPagesize("10000");
        // request
        BillCalenderRqs rqs = new BillCalenderRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);

        // post
        showProgress();
        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "bill/getBillCalenderList", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<BillCalenderRes>() { }.getType();

                    BillCalenderRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        billCalenders = res.getDat().getRows();

                        if (billCalenders != null) {
//                            initMarkData();
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

    @OnClick({R.id.close, R.id.pay_bt, R.id.edt_begindate,R.id.box_buy_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.pay_bt:
                subimtOrder();
                break;
            case R.id.edt_begindate:
                onYearMonthDayPicker(edt_begindate, "请选择开始日期");
                break;
            case R.id.box_buy_record:
                showDatepopup();
                break;
        }
    }

    BuyFlowRqs.DatBean paramlist;

    private void subimtOrder() {

        if(StringUtils.stringIsEmpty(edt_buydays.getText().toString())){
            edt_buydays.setError("请输入购买天数");
            edt_buydays.requestFocus();
            return;
        }

        // get control value
        String deviceId = DeviceUtils.getAndroidID();
        String customerId = (String) SPUtils.get(this, "customerId", "");
        String orderId = DeviceUtils.getAndroidID() + new SimpleDateFormat("yyyyMMddHHmmss").format(TimeUtils.getNowDate());//UUID.randomUUID().toString().replace("-", "");
        String orderType = "0"; // 0 云头柜  1 流量
        String status = "0";    // =0

        String amount = tv_orderMoney.getText().toString();
        amount = "1";//

        String beginTime = tv_beginDate.getText().toString() + " 00:00:00";
        Integer buyDays = Integer.parseInt(tv_buyDays.getText().toString()) - 1;  // 减一天

        Calendar ca =  Calendar.getInstance();
        ca.setTime( TimeUtils.string2Date(beginTime) );
        ca.add(Calendar.DATE, buyDays);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String endTime = TimeUtils.date2String(ca.getTime(), df) + " 23:59:59";
        // toastShow(endTime);

        // order detail
        List<BuyFlowRqs.DatBean.Detail> detailArr = new ArrayList<>();
        for (int i = 0; i < rowsBeanList.size(); i++) {
            BuyFlowRqs.DatBean.Detail detail = new BuyFlowRqs.DatBean.Detail();

            detail.setServiceId(rowsBeanList.get(i).getServiceId());            // 服务名称
            detail.setBuyCount( rowsBeanList.get(i).getAvailableCount() +"");   // 可用数量
            detail.setPrice(rowsBeanList.get(i).getServicePrice() + "");        // 价格，预留字段
            detail.setAmount(rowsBeanList.get(i).getServicePrice() + "");       // 金额，预留字段
            detailArr.add(detail);
        }

        // paramlist
        paramlist = new BuyFlowRqs.DatBean();
//        serviceType=0  云头柜 freeDay 免费天数 price 为云头柜价格
//        serviceType=1 流量 使用的是Service开头的 servicePrice 流量价格
        paramlist.setAmount(amount);
        paramlist.setDiscount("0");
        paramlist.setOrginalAmount(amount);
        paramlist.setBeginTime(beginTime); // TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd")));
        paramlist.setEndTime(endTime);      // TimeUtils.getToNumsDateStr(Integer.parseInt(zyDays.getText().toString())) + " 23:59:59");

        paramlist.setOrderId(orderId);
        paramlist.setOrderType(orderType);//0 云头柜  1 流量
        paramlist.setStatus(status);//支付状态
        paramlist.setCustomerId(customerId);
        paramlist.setDeviceId(deviceId);

        paramlist.setDetail(detailArr);  // detail

        // request package
        BuyFlowRqs rqs = new BuyFlowRqs();
        rqs.setCmd("add");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        rqs.setDat(paramlist);

        // post
        showProgress();
        gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "/bill/addorder", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<BaseRes>() {
                    }.getType();

                    BaseRes res = gson.fromJson(result, typeToken);

                    if (res.getRet().equals("10000")) {
                        getEwm();  // 生成二维码
                    } else {
                        toastShow(res.getMsg());
                    }
                } else {
                    toastShow("网络连接异常");
                }
            }
        });
    }

    // 生成二维码
    private void getEwm() {
        String serverip = UrlManage.HOST_URL;

        String pm = paramlist.getOrderId() + "|" + paramlist.getCustomerId() + "|" + paramlist.getAmount() + "|msg";  //订单编号，会员编号， 金额，备注（可为空）。

        String payurl = serverip + "wxpay_qrcode.jsp?WIDout_trade_no=" + paramlist.getOrderId() + "&WIDsubject=" +  Uri.encode("云头柜服务")
                + "&WIDtotal_fee=" + paramlist.getAmount() + "&WIDbody=" + Uri.encode(pm) +
                "&WIDshow_url=" + Uri.encode(serverip + "wxpay_notify_app.jsp");

        LogUtil.log("getEwm pm", pm);
        LogUtil.log("getEwm payurl", payurl);
        RequestParams params = new RequestParams(payurl);
        params.setConnectTimeout(60000);
        params.setReadTimeout(100000);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    LogUtil.log("getEwm onSuccess", result);
//                   Type typeToken = new TypeToken<WxPayInfo>() {
//                   }.getType();
//                   wxPayInfo = gson.fromJson(result,typeToken);
                    showpopup(result.trim());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.log("getEwm onError", ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    MonthPager monthPager;
    TextView currentMonthTv;
    private void showDatepopup() {

        View popupView = getLayoutInflater().inflate(R.layout.calendar_my, null);
        monthPager = (MonthPager) popupView.findViewById(R.id.calendar_view);
        currentMonthTv = (TextView) popupView.findViewById(R.id.current_month);

        //此处强行setViewHeight，毕竟你知道你的日历牌的高度
        monthPager.setViewheight(Utils.dpi2px(mContext, 270));
        initCurrentDate();
        initCalendarView();

        popup = new BackgroundDarkPopupWindow(
                popupView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.clear)));
        popup.setAnimationStyle(android.R.style.Animation_Dialog);
        popup.setDarkStyle(-1);
        popup.setDarkColor(Color.parseColor("#a0000000"));
        popup.resetDarkPosition();
        popup.darkFillScreen();
        popup.showAtLocation(payBt, Gravity.CENTER, 0, 0);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popup = null;
            }
        });
    }


    BackgroundDarkPopupWindow popup;
    TextView lookorder;
    private void showpopup(String url) {
        if (StringUtils.stringIsEmpty(url)) {
            toastShow("支付信息获取失败，请重试");
            return;
        }
        LogUtil.log("payInfo", url);
        View popupView = getLayoutInflater().inflate(R.layout.pay_ewm, null);
         lookorder = (TextView) popupView.findViewById(R.id.look_order);
        ImageView imageView = (ImageView) popupView.findViewById(R.id.ewm);
        imageView.setImageBitmap(CommonUtils.generateBitmap(url, 300, 300));
        popup = new BackgroundDarkPopupWindow(
                popupView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.clear)));
        popup.setAnimationStyle(android.R.style.Animation_Dialog);
        popup.setDarkStyle(-1);
        popup.setDarkColor(Color.parseColor("#a0000000"));
        popup.resetDarkPosition();
        popup.darkFillScreen();
        popup.showAtLocation(payBt, Gravity.CENTER, 0, 0);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popup = null;
                handlerTimer.sendEmptyMessage(1);
            }
        });
        lookorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
                // toastShow("功能开发中……");

            }
        });

        handlerTimer.sendEmptyMessage(0);

    }

    private Handler handlerTimer = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
                    handlerTimer.removeMessages(0);
                    // app的功能逻辑处理
                    getOrderList();

                    break;

                case 1:
                    // 直接移除，定时器停止
                    handlerTimer.removeMessages(0);
                    break;

                default:
                    break;
            }
        };
    };


    private void getOrderList() {
        showProgress();
        OrderListRqs rqs = new OrderListRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        OrderListRqs.DatBean datBean = new OrderListRqs.DatBean();
        OrderListRqs.DatBean.Paramlist paramlist = new OrderListRqs.DatBean.Paramlist();
        paramlist.setOrderType(orderType+"");
        paramlist.setCustomerId((String) SPUtils.get(this, "customerId", ""));
        paramlist.setOrderId(paramlist.getOrderId());
        datBean.setParamlist(paramlist);
        rqs.setDat(datBean);

        gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "bill/orderlist", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<OrderListRes>() {
                    }.getType();
                    OrderListRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        orderList = res.getDat().getRows();
                        if (orderList != null && orderList.size() >0 ) {
                            if(orderList.get(0).getStatus() == 2){
                                 if(popup != null){
                                     popup.dismiss();
                                     toastShow("支付成功");
                                     Intent intent = new Intent(BuyServiceActivity.this, MyOrderActivity.class);
                                     intent.putExtra("orderType",0);
                                     startActivity(intent);
                                     finish();
                                 }
                            }else {
                                // 再次发出msg，循环更新
                                handlerTimer.sendEmptyMessageDelayed(0,repeatTime );
                            }
                        }else {
                            // 再次发出msg，循环更新
                            handlerTimer.sendEmptyMessageDelayed(0, repeatTime);
                        }
                    } else {
                        toastShow(res.getMsg());
                        // 再次发出msg，循环更新
                        handlerTimer.sendEmptyMessageDelayed(0, repeatTime);
                    }
                } else {
                    toastShow("网络连接异常");
                    // 再次发出msg，循环更新
                    handlerTimer.sendEmptyMessageDelayed(0, repeatTime);
                }
            }
        });
    }


    // 选择日期
    public void onYearMonthDayPicker(final TextView textView, String title) {
        final DatePicker picker = new DatePicker(this);
        picker.setGravity(Gravity.CENTER);
        int h = getResources().getDimensionPixelOffset(R.dimen.perfect_info_pop_w);
        String today = TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd"));
        picker.setWidth(h);
        picker.setTopPadding(15);
        picker.setPadding(15);
        if (title.equals("请选择开始日期")) {
            picker.setRangeStart(Integer.parseInt(today.split("-")[0]), Integer.parseInt(today.split("-")[1]), Integer.parseInt(today.split("-")[2]));
            picker.setRangeEnd(2060, 1, 1);
        } else {
            picker.setRangeStart(2000, 1, 1);
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




    /**
     * 初始化currentDate
     *
     * @return void
     */
    private void initCurrentDate() {
        currentDate = new CalendarDate();
        if(currentMonthTv !=null ) {
            currentMonthTv.setText(currentDate.getYear()+ "年"+currentDate.getMonth()+"月");
        }
//        textViewYearDisplay.setText(currentDate.getYear() + "年");
//        textViewMonthDisplay.setText(currentDate.getMonth() + "");
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     *
     * @return void
     */
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(mContext, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                mContext,
                onSelectDateListener,
                CalendarAttr.CalendayType.MONTH,
                customDayView);
        initMarkData();
        initMonthPager();
    }

    /**
     * 初始化标记数据，HashMap的形式，可自定义
     *
     * @return void
     */
    private void initMarkData() {
        if (billCalenders == null) {
            return;
        }
        HashMap<String, String> markData = new HashMap<>();
//        markData.put("2017-8-9", "1");
//        markData.put("2017-7-9", "0");
//        markData.put("2017-6-9", "1");
//        markData.put("2017-6-10", "0");
        for (int i = 0; i < billCalenders.size(); i++) {
            markData.put(billCalenders.get(i).getServiceDay().replace("-0","-"), "0");
        }
        calendarAdapter.setMarkData(markData);
    }

    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshClickDate(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                monthPager.selectOtherMonth(offset);
            }
        };
    }

    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        if(currentMonthTv !=null ) {
            currentMonthTv.setText(date.getYear()+ "年"+date.getMonth()+"月");
        }
//        textViewYearDisplay.setText(date.getYear() + "年");
//        textViewMonthDisplay.setText(date.getMonth() + "");
    }

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     *
     * @return void
     */
    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) instanceof com.ldf.calendar.view.Calendar) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    if(currentMonthTv !=null ) {
                        currentMonthTv.setText(date.getYear()+ "年"+date.getMonth()+"月");
                    }
//                    textViewYearDisplay.setText(date.getYear() + "年");
//                    textViewMonthDisplay.setText(date.getMonth() + "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
