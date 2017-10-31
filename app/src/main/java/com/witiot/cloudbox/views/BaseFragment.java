package com.witiot.cloudbox.views;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.witiot.cloudbox.R;
import com.witiot.cloudbox.widget.MyProgressDialog;

import butterknife.ButterKnife;


public class BaseFragment extends Fragment {
    public Activity mActivity;
    public MyProgressDialog progressDialog;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        progressDialog = new MyProgressDialog(mActivity, R.style.MyProgressDialog);
    }

    public void toastShow(int resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void showProgress(){
//        if(progressDialog == null){
//            progressDialog = new MyProgressDialog(getActivity(),R.style.MyProgressDialog);
//            progressDialog.setCanceledOnTouchOutside(true);
//        }else{
//            progressDialog.show();
//        }
    }
    public void dismissProgress(){
//        try{
//            if(progressDialog != null){
//                progressDialog.dismiss();
//            }
//        }catch (Exception e){
//
//        }

    }
}
