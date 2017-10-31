package com.witiot.cloudbox.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.witiot.cloudbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lixin on 17/3/30.
 */
public class TipDialog extends Dialog {
    @BindView(R.id.tvTip)
    TextView mTvTip;
    @BindView(R.id.btnLeft)
    TextView mBtnLeft;
    @BindView(R.id.btnRight)
    TextView mBtnRight;
    @BindView(R.id.btnCenter)
    TextView mBtnCenter;

    boolean isSingleBtnMode = true;
    @BindView(R.id.ivClose)
    ImageView mIvClose;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    private Context context;
    private TipDialogButtonListener tipDialogButtonListener = null;


    public interface TipDialogButtonListener {
        void onLeftBtnClicked();

        void onRightBtnClicked();

        void onCenterBtnClicked();

    }


    public TipDialog(Context context, boolean isSingleBtnMode, TipDialogButtonListener listener) {
        super(context, R.style.dialogStyleDim);
        this.isSingleBtnMode = isSingleBtnMode;
        this.tipDialogButtonListener = listener;
        this.context = context;
    }

    public TipDialog(Context context, int theme) {
        super(context, theme);
    }

    protected TipDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tip);
        ButterKnife.bind(this);
        initButton();
        this.setCanceledOnTouchOutside(false);
    }

    private void initButton() {

        if (isSingleBtnMode) {
            mBtnLeft.setVisibility(View.GONE);
            mBtnRight.setVisibility(View.GONE);
        }

        mBtnLeft.setOnClickListener(clickListener);
        mBtnRight.setOnClickListener(clickListener);
        mBtnCenter.setOnClickListener(clickListener);
        mIvClose.setOnClickListener(clickListener);
    }

    public void setMyTitle(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }

    public void setMsg(String msg) {
        mTvTip.setText(msg);
    }

    public void setLeftButtonText(String text) {
        mBtnLeft.setText(text);
    }

    public void setRightButtonText(String text) {
        mBtnRight.setText(text);
    }

    public void setCenterButtonText(String text) {
        mBtnCenter.setText(text);
    }

    public void setLeftButtonVisibility(boolean f) {
        if (f) {
            mBtnLeft.setVisibility(View.VISIBLE);
        } else {
            mBtnLeft.setVisibility(View.INVISIBLE);
        }
    }

    public void setCloseButtonVisibility(boolean f) {
        if (f) {
            mIvClose.setVisibility(View.VISIBLE);
        } else {
            mIvClose.setVisibility(View.INVISIBLE);
        }
    }


    public void setRigheButtonVisibility(boolean f) {
        if (f) {
            mBtnRight.setVisibility(View.VISIBLE);
        } else {
            mBtnRight.setVisibility(View.INVISIBLE);
        }
    }

    public void setCenterButtonVisibility(boolean f) {
        if (f) {
            mBtnCenter.setVisibility(View.VISIBLE);
        } else {
            mBtnCenter.setVisibility(View.INVISIBLE);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnLeft:
                    tipDialogButtonListener.onLeftBtnClicked();
                    break;
                case R.id.btnRight:
                    tipDialogButtonListener.onRightBtnClicked();
                    break;
                case R.id.btnCenter:
                    tipDialogButtonListener.onCenterBtnClicked();
                    break;
                case R.id.ivClose:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void show() {
        super.show();
        Window win = this.getWindow();
        win.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        int screenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getWidth();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = 650;
        lp.dimAmount = 0.35f;
        win.setAttributes(lp);
    }
}
