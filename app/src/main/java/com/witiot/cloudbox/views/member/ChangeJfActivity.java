package com.witiot.cloudbox.views.member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.http.XRequest;
import com.witiot.cloudbox.http.XRequestCallback;
import com.witiot.cloudbox.model.BaseRes;
import com.witiot.cloudbox.model.CustPointResponse;
import com.witiot.cloudbox.model.PostAvatarRes;
import com.witiot.cloudbox.model.PostAvatarRqs;
import com.witiot.cloudbox.model.PostBillDataRqs;
import com.witiot.cloudbox.model.PostBillImgRqs;
import com.witiot.cloudbox.utils.CommonUtils;
import com.witiot.cloudbox.utils.ConstantBox;
import com.witiot.cloudbox.utils.DeviceUtils;
import com.witiot.cloudbox.utils.ImageUtils;
import com.witiot.cloudbox.utils.LogUtil;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.TimeUtils;
import com.witiot.cloudbox.views.BaseActivity;
import com.witiot.cloudbox.widget.BackgroundDarkPopupWindow;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.witiot.cloudbox.R.id.avatar;

public class ChangeJfActivity extends BaseActivity {

    @BindView(R.id.close)
    TextView close;

    @BindView(R.id.user_name_et)
    EditText userNameEt;
    @BindView(R.id.check_price_et)
    EditText checkPriceEt;
    @BindView(R.id.treatment_price_et)
    EditText treatmentPriceEt;
    @BindView(R.id.hospitalized_price_et)
    EditText hospitalizedPriceEt;
    @BindView(R.id.material_price_et)
    EditText materialPriceEt;
    @BindView(R.id.drugs_price_et)
    EditText drugsPriceEt;
    @BindView(R.id.bill_img)
    SimpleDraweeView billImg;
    @BindView(R.id.submit_data)
    TextView submitData;


    List<String> pathList = new ArrayList<>();

    PostBillDataRqs.DatBean dat;
    String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changejf);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        name = getIntent().getStringExtra("name");
        if(StringUtils.stringIsEmpty(name)){
            userNameEt.setText(name);
        }
    }

    @OnClick({R.id.close, R.id.bill_img, R.id.submit_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.bill_img:
                selectBillImg();
                break;
            case R.id.submit_data:
                submitData.setEnabled(false);
                submitData();
                break;
        }
    }

    private void submitData() {
        double totalFee = 0;
        if(pathList == null || pathList.size() == 0){
            toastShow("请选择票据图片");
            submitData.setEnabled(true);
            return;
        }

        dat = new PostBillDataRqs.DatBean();
        if(StringUtils.stringIsNotEmpty(userNameEt.getText().toString())){
            dat.setPatientName(userNameEt.getText().toString());
        }else {
            userNameEt.setError("请输入真实姓名");
            userNameEt.requestFocus();
            submitData.setEnabled(true);
            return;
        }

        if(StringUtils.stringIsNotEmpty(checkPriceEt.getText().toString())){
            dat.setInspectionFee(checkPriceEt.getText().toString());
            totalFee += Double.parseDouble(checkPriceEt.getText().toString());
        }else {
            checkPriceEt.setError("请输入检查费");
            checkPriceEt.requestFocus();
            submitData.setEnabled(true);
            return;
        }

        if(StringUtils.stringIsNotEmpty(treatmentPriceEt.getText().toString())){
            dat.setTreatmentFee(treatmentPriceEt.getText().toString());
            totalFee += Double.parseDouble(treatmentPriceEt.getText().toString());
        }else {
            treatmentPriceEt.setError("请输入治疗费");
            treatmentPriceEt.requestFocus();
            submitData.setEnabled(true);
            return;
        }

        if(StringUtils.stringIsNotEmpty(hospitalizedPriceEt.getText().toString())){
            dat.setHospitalFee(hospitalizedPriceEt.getText().toString());
            totalFee += Double.parseDouble(hospitalizedPriceEt.getText().toString());
        }else {
            hospitalizedPriceEt.setError("请输入住院费");
            hospitalizedPriceEt.requestFocus();
            submitData.setEnabled(true);
            return;
        }

        if(StringUtils.stringIsNotEmpty(materialPriceEt.getText().toString())){
            dat.setMaterialFee(materialPriceEt.getText().toString());
            totalFee += Double.parseDouble(materialPriceEt.getText().toString());
        }else {
            hospitalizedPriceEt.setError("请输入材料费");
            hospitalizedPriceEt.requestFocus();
            submitData.setEnabled(true);
            return;
        }

        if(StringUtils.stringIsNotEmpty(drugsPriceEt.getText().toString())){
            dat.setDrugFee(drugsPriceEt.getText().toString());
            totalFee += Double.parseDouble(drugsPriceEt.getText().toString());
        }else {
            drugsPriceEt.setError("请输入药费");
            drugsPriceEt.requestFocus();
            submitData.setEnabled(true);
            return;
        }

        dat.setTotalFee(totalFee+"");
        dat.setStatus("0");

        // 提交数据，包括图片和文本
        postBillImg(pathList.get(0));
    }

    private void postBillImg( String img) {

        // dat
        PostBillImgRqs.DatBean datBean = new PostBillImgRqs.DatBean();
        datBean.setSavePath("bill/"+(String) SPUtils.get(this, "customerId", "")+"/");
        datBean.setDataImg(ImageUtils.bitmapToString(img));

        // reqeust
        PostBillImgRqs rqs = new PostBillImgRqs();
        rqs.setCmd("add");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));
        rqs.setVer("1");
        rqs.setDat(datBean);

        // post
        showProgress();
        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "uploadImage"  , new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    //LogUtil.log( result );

                    Type typeToken = new TypeToken<PostAvatarRes>() { }.getType();

                    PostAvatarRes res = gson.fromJson(result, typeToken);

                    if (res.getRet().equals("10000")) {
//                        toastShow("票据上传成功");

                        // 再提交单据内容
                        submitText(res.getDat());
                    } else {
                        toastShow("票据上传失败，请重新提交");
                    }
                } else {
                    toastShow("票据上传失败，请重新提交");
                }
                submitData.setEnabled(true);
            }
        });
    }

    // 再提交单据内容
    private void submitText(String img) {
        String orderId = DeviceUtils.getAndroidID() + new SimpleDateFormat("yyyyMMddHHmmss").format( TimeUtils.getNowDate() ) ;//UUID.randomUUID().toString().replace("-", "");
        dat.setBillNo(orderId);
        dat.setCustomerId((String) SPUtils.get(this, "customerId", ""));
        dat.setDeviceId(DeviceUtils.getAndroidID());
        dat.setUploadPath(img);

        PostBillDataRqs rqs = new PostBillDataRqs();
        rqs.setDat(dat);
        rqs.setCmd("add");
        rqs.setSrc("3");
        rqs.setTok((String) SPUtils.get(this, "tok", ""));//Tok传入从缓存share里tok对应的值
        rqs.setVer("1");

        final Gson gson = new Gson();
        XRequest xRequest = new XRequest();
        xRequest.sendPostRequest(this, gson.toJson(rqs), "shopChargeBill/add"  , new XRequestCallback() {
            @Override
            public void callback(boolean isSucceed, String result) {
                dismissProgress();
                if (isSucceed) {
                    Type typeToken = new TypeToken<BaseRes>() {}.getType();

                    BaseRes res = gson.fromJson(result, typeToken);

                    if (res.getRet().equals("10000")) {
                        showpopup();
                    } else {
                        toastShow(res.getMsg());
                    }

                } else {
                    toastShow("网络连接异常");
                }
                submitData.setEnabled(true);
            }
        });

    }


    private void selectBillImg() {
        // 配置选项
        ImgSelConfig config = new ImgSelConfig.Builder(this, loader)
            // 是否多选
            .multiSelect(false)
            // “确定”按钮背景色
            .btnBgColor(Color.GRAY)
            // “确定”按钮文字颜色
            .btnTextColor(Color.BLUE)
            // 使用沉浸式状态栏
            .statusBarColor(ContextCompat.getColor(this, R.color.blue_title))
            // 返回图标ResId
            .backResId(R.drawable.ic_back)
            // 标题
            .title("选择收费单据照片")
            // 标题文字颜色
            .titleColor(Color.WHITE)
            // TitleBar背景色
            .titleBgColor(ContextCompat.getColor(this, R.color.blue_title))
            // 裁剪大小。needCrop为true的时候配置
            .cropSize(1, 1, 750, 750)
            .needCrop(false)
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
                this.pathList.clear();
                this.pathList.addAll(pathList);
                Uri uri = Uri.parse("file://" + pathList.get(0));
                billImg.setImageURI(uri);
            }
        }
    }


    BackgroundDarkPopupWindow popup;

    private void showpopup() {

        View popupView = getLayoutInflater().inflate(R.layout.pop_change_jf, null);
        TextView ok = (TextView) popupView.findViewById(R.id.ok_bt);

        popup = new BackgroundDarkPopupWindow(
                popupView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(false);
        popup.setOutsideTouchable(false);
        popup.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.clear)));
        popup.setAnimationStyle(android.R.style.Animation_Dialog);
        popup.setDarkStyle(-1);
        popup.setDarkColor(Color.parseColor("#a0000000"));
        popup.resetDarkPosition();
        popup.darkFillScreen();
        popup.showAtLocation(submitData.getRootView(), Gravity.CENTER, 0, 0);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popup = null;
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
                finish();
            }
        });
    }

}
