package com.witiot.cloudbox.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.witiot.cloudbox.R;


/**
 * Created by lixin on 2017/4/15.
 */

public class MyProgressDialog extends ProgressDialog {

    public MyProgressDialog(Context context)
    {
        super(context);
    }

    public MyProgressDialog(Context context, int theme)
    {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private void init(Context context)
    {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.dialog_load);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show()
    {
        super.show();
    }
}
