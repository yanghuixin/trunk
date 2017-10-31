package com.witiot.cloudbox.views.setting;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.witiot.cloudbox.R;
import com.witiot.cloudbox.views.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SettingFragment extends BaseFragment {



    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }



}
