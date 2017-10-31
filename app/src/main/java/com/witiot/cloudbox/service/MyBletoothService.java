package com.witiot.cloudbox.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


import com.witiot.cloudbox.MyApplication;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.model.BluetoothResEvent;
import com.witiot.cloudbox.model.BluetoothRqsEvent;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.utils.StringUtils;
import com.witiot.cloudbox.utils.bluetooth.BluetoothUtils;
import com.witiot.cloudbox.utils.bluetooth.ConnectedThread;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MyBletoothService extends Service {
    private ConnectedThread mConnectedThread;
    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
    public String bluetoothAddress;
    public String bluetoothName;

    private static final long HEART_BEAT_RATE =  2000;//目前心跳检测频率为30s
    private Handler handlerTimer;

    public MyBletoothService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        handlerTimer = new Handler();
        System.out.println("MyBletoothService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        handlerTimer.removeCallbacks(heartBeatRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("MyBletoothService onStartCommand");
        try {
            handlerTimer.removeCallbacks(heartBeatRunnable);
            if (mConnectedThread != null) {
                mConnectedThread.cancel();
                mConnectedThread = null;
            }
            BluetoothUtils.closeBluetoothSocket();
        }catch (Exception E){

        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                initBluetooth();
            }
        }).start();


        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void BluetoothRqs(BluetoothRqsEvent messageEvent) {
        System.out.println("MyBletoothService BluetoothRqs");
        handlerTimer.removeCallbacks(heartBeatRunnable);
        byte[] bytes;
        switch (messageEvent.getViewId()) {
            case 120:
                break;
            case R.id.switch_pw_open:
                bytes = new byte[6];
                bytes[0] = 0x03;
                bytes[1] = 0x01;
                bytes[2] = 0x00;
                bytes[3] = 0x00;
                bytes[4] = 0x00;
                bytes[5] = 0x00;
                mConnectedThread.write(bytes);
                break;
            case R.id.switch_pw_close:
                bytes = new byte[6];
                bytes[0] = 0x03;
                bytes[1] = 0x00;
                bytes[2] = 0x00;
                bytes[3] = 0x00;
                bytes[4] = 0x00;
                bytes[5] = 0x00;
                sendDataToDevice(bytes);
                break;
            case R.id.switch_air_open:
                bytes = new byte[6];
                bytes[0] = 0x01;
                bytes[1] = 0x01;
                bytes[2] = 0x00;
                bytes[3] = 0x00;
                bytes[4] = 0x00;
                bytes[5] = 0x00;
                sendDataToDevice(bytes);
                break;
            case R.id.switch_air_close:
                bytes = new byte[6];
                bytes[0] = 0x01;
                bytes[1] = 0x00;
                bytes[2] = 0x00;
                bytes[3] = 0x00;
                bytes[4] = 0x00;
                bytes[5] = 0x00;
                sendDataToDevice(bytes);
                break;

            case R.id.switch_xdg_open:
                bytes = new byte[6];
                bytes[0] = 0x02;
                bytes[1] = 0x01;
                bytes[2] = 0x00;
                bytes[3] = 0x00;
                bytes[4] = 0x00;
                bytes[5] = 0x00;
                sendDataToDevice(bytes);
                break;

            case R.id.switch_xdg_close:
                bytes = new byte[6];
                bytes[0] = 0x02;
                bytes[1] = 0x00;
                bytes[2] = 0x00;
                bytes[3] = 0x00;
                bytes[4] = 0x00;
                bytes[5] = 0x00;
                sendDataToDevice(bytes);
                break;

            case R.id.switch_jsq_open:
                bytes = new byte[6];
                bytes[0] = 0x00;
                bytes[1] = 0x01;
                bytes[2] = 0x00;
                bytes[3] = 0x00;
                bytes[4] = 0x00;
                bytes[5] = 0x00;
                sendDataToDevice(bytes);
                break;

            case R.id.switch_jsq_close:
                bytes = new byte[6];
                bytes[0] = 0x00;
                bytes[1] = 0x00;
                bytes[2] = 0x00;
                bytes[3] = 0x00;
                bytes[4] = 0x00;
                bytes[5] = 0x00;
                sendDataToDevice(bytes);
                break;
            case R.id.connect_bluetooth_bt:
//                if (mConnectedThread != null) {
//                    mConnectedThread.cancel();
//                    mConnectedThread = null;
//                }
//                BluetoothUtils.closeBluetoothSocket();
//                repeatCount = 0;
//                connectBlutooth();
                break;
        }

        handlerTimer.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);

    }

    private void sendDataToDevice(byte[] bytes) {
        if (mConnectedThread == null) {
            connectBlutooth();
            return;
        }
        mConnectedThread.write(bytes);
    }


    //已连接蓝牙设备，则接收数据，并显示到接收区文本框
    Handler handlerReceive = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConnectedThread.BLUETOOTH_CONECT_FAIL:
                    if (mConnectedThread != null) {
                        mConnectedThread.cancel();
                        mConnectedThread = null;
                    }
                    BluetoothUtils.closeBluetoothSocket();
                    connectBlutooth();
                    break;
                case ConnectedThread.MESSAGE_READ:

                    final byte[] buffer = (byte[]) msg.obj;
                    int length = msg.arg1;
//                    int leng = buffer.length;
                    String receiveMsg = null;
                    try {
                        byte[] receive = new byte[6];
                        for (int i = 0; i < length; i++) {
                            receive[i] = buffer[i];
                        }
                         receiveMsg = BluetoothUtils.bytesToHexString(receive);
//                        controlName.setVisibility(View.VISIBLE);
//                        controlName.setText(" - " + BluetoothUtils.bytesToHexString(receive).replace("A", "0") + " - ");
                    } catch (Exception e) {

                    }

//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
                            if (buffer != null) {
                                BluetoothResEvent messageEvent = new BluetoothResEvent();
                                // 00 03 净水器 响应码
                                if (buffer[0] == 0x00 && buffer[1] == 0x03) {
                                    // 01 净水器 开启
                                    if (buffer[2] == 0x01) {
                                        messageEvent.setEventName("jsq");
                                        messageEvent.setEventType(1);
                                        EventBus.getDefault().post(messageEvent);
                                    }
                                    // 00 净水器 关闭
                                    else if (buffer[2] == 0x00) {
                                        messageEvent.setEventName("jsq");
                                        messageEvent.setEventType(0);
                                        EventBus.getDefault().post(messageEvent);
                                    }
                                }

                                // 01 03 空气净化器 响应码
                                else if (buffer[0] == 0x01 && buffer[1] == 0x03) {
                                    // 01 空气净化器 开启成功
                                    if (buffer[2] == 0x01) {
                                        messageEvent.setEventName("jhq");
                                        messageEvent.setEventType(1);
                                        EventBus.getDefault().post(messageEvent);
                                    }
                                    // 00 空气净化器 关闭成功
                                    else if (buffer[2] == 0x00) {
                                        messageEvent.setEventName("jhq");
                                        messageEvent.setEventType(0);
                                        EventBus.getDefault().post(messageEvent);
                                    }
                                }

                                // 02 03 消毒柜 响应码
                                else if (buffer[0] == 0x02 && buffer[1] == 0x03) {
                                    // 01 消毒柜 开启成功
                                    if (buffer[2] == 0x01) {
                                        messageEvent.setEventName("xdg");
                                        messageEvent.setEventType(1);
                                        EventBus.getDefault().post(messageEvent);
                                    }
                                    // 00 消毒柜 关闭成功
                                    else if (buffer[2] == 0x00) {
                                        messageEvent.setEventName("xdg");
                                        messageEvent.setEventType(0);
                                        EventBus.getDefault().post(messageEvent);
                                    }
                                }

                                // 03 03 密码锁 响应码
                                else if (buffer[0] == 0x03 && buffer[1] == 0x03) {
                                    // 0303 01 开锁
                                    if (buffer[2] == 0x01) {
                                        messageEvent.setEventName("mms");
                                        messageEvent.setEventType(1);
                                        EventBus.getDefault().post(messageEvent);
                                    }
                                    // 0303 00 关锁
                                    else if (buffer[2] == 0x00) {
                                        //密码锁 关闭成功
                                        messageEvent.setEventName("mms");
                                        messageEvent.setEventType(0);
                                        EventBus.getDefault().post(messageEvent);
                                    }
                                }

                                // 03 01 接收来自 单片机 的开锁指令
                                else if (buffer[0] == 0x03 && buffer[1] == 0x01) {
                                    byte[] pw12 = new byte[2];
                                    pw12[0] = buffer[2];
                                    pw12[1] = buffer[3];
                                    String pw = BluetoothUtils.bytesToHexString(pw12).replace("A", "0");


                                    if (pw.equals("0506") || pw.equals("1006") || pw.equals("1256") || pw.equals("8080")) {
                                        // 模拟密码： 0506, 8080, 1256, 1006
                                        byte[] bytes = new byte[6];
                                        bytes[0] = 0x03;
                                        bytes[1] = 0x01;
                                        bytes[2] = 0x00;
                                        bytes[3] = 0x00;
                                        bytes[4] = 0x00;
                                        bytes[5] = 0x00;
                                        mConnectedThread.write(bytes);//开密码锁
                                        messageEvent.setEventName("open_mms");
                                        messageEvent.setEventType(1);
                                        EventBus.getDefault().post(messageEvent);
                                    } else {

                                    }
                                }else if(receiveMsg != null && receiveMsg.contains("")){
                                    messageEvent.setEventName("heart");
                                    messageEvent.setEventType(1);
                                    EventBus.getDefault().post(messageEvent);
                                }

                            }
//                        }
//                    });

                    break;
            }
        }
    };

    private void initBluetooth() {
        System.out.println("MyBletoothService initBluetooth");
        // 获取蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            return;
        }
        // 将已配对的设备添加到列表中
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            if(deviceList != null){
                deviceList.clear();
            }else {
                deviceList = new ArrayList<>();
            }
            for (BluetoothDevice device : pairedDevices) {
                deviceList.add(device);
            }
        }

        if(BluetoothUtils.getBluetoothSocket() == null){
            connectBlutooth();
        }else {
            mConnectedThread = new ConnectedThread(BluetoothUtils.getBluetoothSocket(), handlerReceive);
            mConnectedThread.start();
            sendHeartBeat();
        }
    }


    private void connectBlutooth() {
        System.out.println("MyBletoothService connectBlutooth");
        bluetoothAddress = (String) SPUtils.get(MyApplication.getContext(), "bluetoothAddress", "");
        bluetoothName = (String) SPUtils.get(MyApplication.getContext(), "bluetoothName", "");
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (deviceList.size() > 0) {
                    for (int i = 0; i < deviceList.size(); i++) {
                        BluetoothDevice device = deviceList.get(i);
                        BluetoothSocket socket = null;
//                        if (StringUtils.stringIsEmpty(bluetoothAddress)
//                                || !device.getAddress().equals(bluetoothAddress)) {
//                            break;
//                        }
                        try {
                            if(BluetoothUtils.getBluetoothSocket() != null){

                                return;
                            }
                            // 蓝牙串口服务对应的UUID。如使用的是其它蓝牙服务，需更改下面的字符串
                            UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                        } catch (Exception e) {
                            Log.d("log BluetoothSocket", "获取Socket失败");
                            bluetoothLinkHandler.sendEmptyMessage(0);
//                        Toast.makeText(getContext(), "获取Socket失败", Toast.LENGTH_SHORT).show();
//                        return;
                        }

                        try {
                            // Connect the device through the socket. This will block
                            // until it succeeds or throws an exception
                            socket.connect();
                            Log.d("log BluetoothSocket", "连接成功");
//                        Toast.makeText(getContext(), "连接成功", Toast.LENGTH_SHORT).show();
                            BluetoothUtils.setBluetoothSocket(socket);
                            bluetoothLinkHandler.sendEmptyMessage(1);
                            // 连接成功
                            break;
                        } catch (IOException connectException) {
                            // Unable to connect; close the socket and get out
                            Log.d("log BluetoothSocket", "连接失败");
                            bluetoothLinkHandler.sendEmptyMessage(0);
                            try {
                                socket.close();
                            } catch (IOException closeException) {
                            }
//                        return;
                        }
                    }
                }else {
                    BluetoothResEvent messageEvent = new BluetoothResEvent();
                    messageEvent.setEventName("bluetoothLink");
                    messageEvent.setEventType(-1);
                    EventBus.getDefault().post(messageEvent);
                }
            }
        }.start();
    }

    Handler bluetoothLinkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BluetoothResEvent messageEvent = new BluetoothResEvent();
            if (msg.what == 1) {
                mConnectedThread = new ConnectedThread(BluetoothUtils.getBluetoothSocket(), handlerReceive);
                mConnectedThread.start();
                sendHeartBeat();
            } else {
                if(BluetoothUtils.getBluetoothSocket() != null){
                    return;
                }
                messageEvent.setEventName("bluetoothLink");
                messageEvent.setEventType(0);
                EventBus.getDefault().post(messageEvent);
//                repeatCount ++;
//                if(repeatCount < 3){
//                    connectBlutooth();
//                }
            }
        }
    };

    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            // excute task
            // app的功能逻辑处理
            if(mConnectedThread == null){
                BluetoothResEvent messageEvent = new BluetoothResEvent();
                handlerTimer.removeCallbacks(heartBeatRunnable);
                messageEvent.setEventName("bluetoothLink");
                messageEvent.setEventType(0);
                EventBus.getDefault().post(messageEvent);
                return;
            }
            byte[] bytes = new byte[6];
            bytes[0] = BluetoothUtils.getByte(0xff);
            bytes[1] = 0x01;
            bytes[2] = 0x00;
            bytes[3] = 0x00;
            bytes[4] = 0x00;
            bytes[5] = 0x00;
            mConnectedThread.write(bytes);
            handlerTimer.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    //初始化成功后，就准备发送心跳包
    public void sendHeartBeat() {
        BluetoothResEvent messageEvent = new BluetoothResEvent();
        messageEvent.setEventName("bluetoothLink");
        messageEvent.setEventType(1);
        EventBus.getDefault().post(messageEvent);
        handlerTimer.removeCallbacks(heartBeatRunnable);
        handlerTimer.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
