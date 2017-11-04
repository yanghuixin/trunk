package com.witiot.cloudbox.views.member;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.CustInfoRes;
import com.witiot.cloudbox.model.CustInfoRqs;
import com.witiot.cloudbox.model.CustPointRequest;
import com.witiot.cloudbox.model.CustPointResponse;
import com.witiot.cloudbox.model.PostAvatarRes;
import com.witiot.cloudbox.model.PostAvatarRqs;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.ConstantBox;
import com.witiot.cloudbox.utils.ImageUtils;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.views.BaseFragment;
import com.witiot.cloudbox.views.box.BoxRecordActivity;
import com.witiot.cloudbox.views.box.BuyServiceActivity;
import com.witiot.cloudbox.views.device.DeviceActivity;
import com.witiot.cloudbox.views.internet.BuyFlowActivity;
import com.witiot.cloudbox.views.internet.FlowRecordActivity;
import com.witiot.cloudbox.views.login.LoginActivity;
import com.witiot.cloudbox.views.navigation.NavigationActivity;
import com.witiot.cloudbox.widget.TipDialog;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * 会员中心
 */
public class MemberFragment extends BaseFragment {


    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_sex)
    TextView userSex;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.birthday)
    TextView birthday;
    @BindView(R.id.job)
    TextView job;

    @BindView(R.id.btn_update)
    TextView btn_update;
    @BindView(R.id.btn_repair)
    TextView btn_repair;
    @BindView(R.id.btn_logout)
    TextView btn_logout;

    @BindView(R.id.title_ts)
    TextView titleTs;

    @BindView(R.id.num_days)
    TextView num_days;

    @BindView(R.id.buy_service)
    TextView buyService;
    @BindView(R.id.title_llye)
    TextView titleLlye;

    @BindView(R.id.num_flow)
    TextView num_flow;

    @BindView(R.id.buy_flow)
    TextView buyFlow;
    @BindView(R.id.service_record_tv)
    TextView serviceRecordTv;

    @BindView(R.id.flow_record_tv)
    TextView flowRecordTv;
    @BindView(R.id.title_pay)
    TextView titlePay;

    @BindView(R.id.num_amount)
    TextView num_amount;

    @BindView(R.id.buy_record)

    TextView buyRecord;
    @BindView(R.id.title_jf)
    TextView titleJf;

    @BindView(R.id.num_score)
    TextView num_score;
    @BindView(R.id.change_jf)
    TextView changeJf;
    @BindView(R.id.jf_record)
    TextView jfRecord;
    Unbinder unbinder;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;

    CustInfoRes.CustInfoBean custInfoBean;
    @BindView(R.id.logout)
    TextView logout;

    public MemberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        showProgress();
        CustInfoRqs rqs = new CustInfoRqs();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(getContext(), "tok", ""));
        rqs.setVer("1");
        CustInfoRqs.DatBean datBean = new CustInfoRqs.DatBean();
        datBean.setCustomerId((String) SPUtils.get(getContext(), "customerId", ""));
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(getContext(), gson.toJson(rqs), "getCustInfo", new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<CustInfoRes>() {
                    }.getType();
                    CustInfoRes res = gson.fromJson(result, typeToken);
                    if (res.getRet().equals("10000")) {
                        custInfoBean = res.getDat();
                        if (custInfoBean != null) {
                            initView();
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

    private void initView() {

        userName.setText(custInfoBean.getRows().getCustomerName());
        if (custInfoBean.getRows().getGendar() == 1) {
            userSex.setText("男");
            CommonUtils.setTextviewLeftDrawable(getContext(), userSex, R.mipmap.man);
        } else {
            userSex.setText("女");
            CommonUtils.setTextviewLeftDrawable(getContext(), userSex, R.mipmap.women);
        }
        phone.setText(custInfoBean.getRows().getCustomerId());
        if (StringUtils.stringIsNotEmpty(custInfoBean.getRows().getBirth())) {
            birthday.setText(custInfoBean.getRows().getBirth());
        } else {
            birthday.setText("<未填写>");
        }

        if (StringUtils.stringIsNotEmpty(custInfoBean.getRows().getProfession())) {
            job.setText(custInfoBean.getRows().getProfession());
        } else {
            job.setText("<未填写>");
        }

        if (StringUtils.stringIsNotEmpty( custInfoBean.getRows().getSurplusDays()+"" )) {
            num_days.setText(custInfoBean.getRows().getSurplusDays()+"");
        } else {
            num_days.setText("0");
        }

        if (StringUtils.stringIsNotEmpty(  custInfoBean.getRows().getSurplusFlow()+"" )) {
            num_flow.setText(custInfoBean.getRows().getSurplusFlow()+"");
        } else {
            num_flow.setText("0");
        }

        if (StringUtils.stringIsNotEmpty(custInfoBean.getRows().getTotalAmount()+"" )) {
            num_amount.setText(custInfoBean.getRows().getTotalAmount()+"" );
        } else {
            num_amount.setText("0");
        }

        if(StringUtils.stringIsNotEmpty(custInfoBean.getRows().getHeadimgurl())){
            avatar.setImageURI(UrlManage.IMG_BASE_URL+custInfoBean.getRows().getHeadimgurl());
        }

        // 积分从接口实时获取
        getUserPoint();

    }

    // 积分从接口实时获取
    private void getUserPoint() {

        String postUrl = "/getUserPoint";

        showProgress();

        CustPointRequest.DatBean datBean = new CustPointRequest.DatBean();
        datBean.setCustomerId((String) SPUtils.get(getContext(), "customerId", ""));

        CustPointRequest rqs = new CustPointRequest();
        rqs.setCmd("get");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(getContext(), "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(getContext(), gson.toJson(rqs), postUrl  , new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    //LogUtil.log( result );

                    Type typeToken = new TypeToken<CustPointResponse>() {
                    }.getType();

                    CustPointResponse res = gson.fromJson(result, typeToken);

                    if (res.getRet().equals("10000")) {
                        String dat = res.getDat();

                        if (StringUtils.stringIsNotEmpty( dat )) {
                            num_score.setText(dat );
                        } else {
                            num_amount.setText("0");
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_update, R.id.btn_logout, R.id.btn_repair,R.id.buy_service, R.id.buy_flow, R.id.flow_record_tv, R.id.service_record_tv, R.id.buy_record,
            R.id.change_jf, R.id.jf_record, R.id.avatar,R.id.logout})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_update:
                intent = new Intent(getContext(), PerfectInfoActivity.class);
                intent.putExtra("custInfoBean", custInfoBean);
                startActivity(intent);
                break;
            case R.id.btn_logout:
                showLogoutDialog();
                break;
            case R.id.logout:
                showLogoutDialog();
                break;
            case R.id.btn_repair:
                startActivity(new Intent(getContext(), DeviceActivity.class));
                break;
            case R.id.buy_service:
                startActivity(new Intent(getContext(), BuyServiceActivity.class));
                break;
            case R.id.service_record_tv:
                intent = new Intent(getContext(), BoxRecordActivity.class);
                intent.putExtra("custInfoBean", custInfoBean);
                startActivity(intent);
                break;
            case R.id.buy_flow:
                startActivity(new Intent(getContext(), BuyFlowActivity.class));

                break;
            case R.id.flow_record_tv:
                intent = new Intent(getContext(), FlowRecordActivity.class);
                intent.putExtra("custInfoBean", custInfoBean);
                startActivity(intent);
                break;
            case R.id.buy_record:
                startActivity(new Intent(getContext(),MyOrderActivity.class));
                break;
            case R.id.change_jf:
                Intent intent3 = new Intent(getContext(),ChangeJfActivity.class);
                intent3.putExtra("name",custInfoBean.getRows().getCustomerName());
                startActivity(intent3);
                break;
            case R.id.jf_record:
                startActivity(new Intent(getContext(),ChangejfRecordActivity.class));
                break;
            case R.id.avatar:
                selectAvatar();
                break;
        }
    }
    TipDialog tipDialog;
    private void showLogoutDialog() {
        tipDialog = new TipDialog(getContext(), false, new TipDialog.TipDialogButtonListener() {
            @Override
            public void onLeftBtnClicked() {
                tipDialog.dismiss();
                startActivity(new Intent(getContext(), LoginActivity.class));
                SPUtils.remove(getContext(),"tok");
                SPUtils.remove(getContext(),"customerId");
                ((NavigationActivity)getActivity()).finish();
            }

            @Override
            public void onRightBtnClicked() {
                tipDialog.dismiss();
            }

            @Override
            public void onCenterBtnClicked() {

            }
        });
        tipDialog.show();
        tipDialog.setCloseButtonVisibility(false);
        tipDialog.setCenterButtonVisibility(false);
        tipDialog.setMsg("确定退出登录？");
        tipDialog.setLeftButtonText("确定");
        tipDialog.setRightButtonText("取消");

    }

    private void selectAvatar() {
        // 配置选项
        ImgSelConfig config = new ImgSelConfig.Builder(getContext(), loader)
                // 是否多选
                .multiSelect(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(ContextCompat.getColor(getContext(), R.color.blue_title))
                // 返回图标ResId
                .backResId(R.drawable.ic_back)
                // 标题
                .title("选择头像")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(ContextCompat.getColor(getContext(), R.color.blue_title))
                // 裁剪大小。needCrop为true的时候配置
                .cropSize(1, 1, 750, 750)
                .needCrop(true)
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(1)
                .build();

// 跳转到图片选择器
        ImgSelActivity.startActivity(this, config, ConstantBox.REQUEST_CODE_IMAGE);
    }

    // 自定义图片加载器
    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            // TODO 在这边可以自定义图片加载库来加载ImageView，例如Glide、Picasso、ImageLoader等
            Glide.with(context).load(path).into(imageView);
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == ConstantBox.REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            if (pathList != null && pathList.size() > 0) {
//                Uri uri = Uri.parse("file://" + pathList.get(0));
//                avatar.setImageURI(uri);
                postAvatar(pathList.get(0));
            }
        }
    }

    private void postAvatar( String img) {
        final Gson gson = new Gson();
        PostAvatarRqs rqs = new PostAvatarRqs();
        rqs.setCmd("add");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(getContext(), "tok", ""));
        rqs.setVer("1");
        PostAvatarRqs.DatBean datBean = new PostAvatarRqs.DatBean();
        datBean.setCustomerId((String) SPUtils.get(getContext(), "customerId", ""));
        datBean.setDataImg(ImageUtils.bitmapToString(img));
        rqs.setDat(datBean);

        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(getContext(), gson.toJson(rqs), "uploadCustImage"  , new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    //LogUtil.log( result );

                    Type typeToken = new TypeToken<PostAvatarRes>() {
                    }.getType();

                    PostAvatarRes res = gson.fromJson(result, typeToken);

                    if (res.getRet().equals("10000")) {
                        if(StringUtils.stringIsNotEmpty(res.getDat())){
                            avatar.setImageURI(UrlManage.IMG_BASE_URL+res.getDat());
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
}
