package com.witiot.cloudbox.views.device;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.witiot.cloudbox.MyApplication;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.bluetooth.BluetoothUtils;
import com.witiot.cloudbox.utils.bluetooth.ConnectedThread;
import com.witiot.cloudbox.utils.bluetooth.DevicesListActivity;
import com.witiot.cloudbox.views.BaseFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceBluetoothFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.bluetooth_status)
    TextView bluetoothStatus;
    @BindView(R.id.bluetooth_link)
    Button bluetoothLink;

    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();

    public DeviceBluetoothFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_bluetooth, container, false);
        unbinder = ButterKnife.bind(this, view);
        // 获取蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initBluetooth();
    }

    private void initBluetooth() {
        //请求开启蓝牙
        if (mBluetoothAdapter== null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
            return;
        }
            // 将已配对的设备添加到列表中
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    deviceList.add(device);
                }
            }

        if(BluetoothUtils.getBluetoothSocket() == null){
            bluetoothStatus.setText("蓝牙连接未连接");
            if(deviceList.size() > 0) {
                for (int i = 0; i < deviceList.size(); i++) {
                    BluetoothDevice device = deviceList.get(i);
                    BluetoothSocket socket = null;
                    try {
                        // 蓝牙串口服务对应的UUID。如使用的是其它蓝牙服务，需更改下面的字符串
                        UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                        socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    } catch (Exception e) {
                        Log.d("log BluetoothSocket", "获取Socket失败");
                        bluetoothStatus.setText("蓝牙连接失败");
                        Toast.makeText(getContext(), "获取Socket失败", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        // Connect the device through the socket. This will block
                        // until it succeeds or throws an exception
                        socket.connect();
                        Log.d("log BluetoothSocket", "连接成功");

                        bluetoothStatus.setText("蓝牙连接成功  \n设备蓝牙名称："+device.getName()+"\n地址："+device.getAddress());
                        SPUtils.put(MyApplication.getContext(),"bluetoothName",device.getName());
                        SPUtils.put(MyApplication.getContext(),"bluetoothAddress",device.getAddress());
                        Toast.makeText(getContext(), "连接成功", Toast.LENGTH_SHORT).show();
                        BluetoothUtils.setBluetoothSocket(socket);
                        // 连接成功
                    } catch (IOException connectException) {
                        // Unable to connect; close the socket and get out
                        Log.d("log BluetoothSocket", "连接失败");
                        bluetoothStatus.setText("蓝牙连接失败");
                        try {
                            socket.close();
                        } catch (IOException closeException) {
                        }
                        return;
                    }
                }
            }
        }else {
            bluetoothStatus.setText("蓝牙连接成功");
        }
    }


//    //已连接蓝牙设备，则接收数据，并显示到接收区文本框
//    Handler handlerBluetooth = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case ConnectedThread.MESSAGE_READ:
//                    byte[] buffer = (byte[]) msg.obj;
//                    int length = msg.arg1;
//                    for (int i = 0; i < length; i++) {
//                        char c = (char) buffer[i];
////                        edtReceivedMessage.getText().append(c);
//                    }
//                    int leng = buffer.length;
////                    edtReceivedMessage.setText("收到指令"+leng);
//                    if(buffer != null ){
//                        if(buffer[0] == 0x03 && buffer[1] == 0x01){
////                                 1256
////                            edtReceivedMessage.setText("开锁指令");
//                            if(buffer[2] == 0x05 && buffer[3] == 0x06){
//                                byte[] bytes1 = new byte[6];
//                                bytes1[0] = 0x03;
//                                bytes1[1] = 0x03;
//                                bytes1[2] = 0x00;
//                                bytes1[3] = 0x00;
//                                bytes1[4] = 0x00;
//                                bytes1[5] = 0x00;
//                                mConnectedThread.write(bytes1);//回应码
//
//                                byte[] bytes = new byte[2];
//                                bytes[0] = 0x03;
//                                bytes[1] = 0x01;
//                                mConnectedThread.write(bytes);//开密码锁
//
////                                edtReceivedMessage.setText(edtReceivedMessage.getText()+" 发送密码到单片机校验");
//                            }else {
////                                edtReceivedMessage.setText(edtReceivedMessage.getText()+"  密码错误");
//                            }
//                        }
//                    }
//
//                    break;
//            }
//
//        }
//    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bluetooth_link)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), DevicesListActivity.class));

    }
}

