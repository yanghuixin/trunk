package com.witiot.cloudbox.utils.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.witiot.cloudbox.MyApplication;
import com.witiot.cloudbox.R;
import com.witiot.cloudbox.utils.SPUtils;
import com.witiot.cloudbox.views.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DevicesListActivity extends BaseActivity {

    @BindView(R.id.close)
    TextView close;
    private ProgressBar progressbarSearchDevices;

    private BluetoothAdapter mBluetoothAdapter;
    private List<String> mDevicesArray = new ArrayList<String>();
    private DevicesListAdapter<String> devicesListAdapter;
    private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.listview_devices);
        ButterKnife.bind(this);

        ListView listView = (ListView) findViewById(R.id.listview_devices);
        progressbarSearchDevices = (ProgressBar) findViewById(R.id.progressbar_search_devices);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 将已配对的设备添加到列表中
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mDevicesArray.add(device.getName() + "\n" + device.getAddress());
                deviceList.add(device);
            }
        }


        // 注册广播接收器，以获取蓝牙设备搜索结果
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        // 搜索蓝牙设备
        mBluetoothAdapter.startDiscovery();
        progressbarSearchDevices.setVisibility(View.VISIBLE);

        // 为ListView控件设置适配器
        devicesListAdapter = new DevicesListAdapter<String>(getApplicationContext(), mDevicesArray);
        listView.setAdapter(devicesListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = deviceList.get(position);
                BluetoothSocket socket = null;
                try {
                    // 蓝牙串口服务对应的UUID。如使用的是其它蓝牙服务，需更改下面的字符串
                    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                    socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (Exception e) {
                    toastShow("获取Socket失败，请重试");
                    Log.d("log BluetoothSocket", "获取Socket失败");
                    Toast.makeText(getApplicationContext(), "获取Socket失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                mBluetoothAdapter.cancelDiscovery();
                try {
                    // Connect the device through the socket. This will block
                    // until it succeeds or throws an exception
                    socket.connect();
                    Log.d("log BluetoothSocket", "连接成功");
                    Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
                    BluetoothUtils.setBluetoothSocket(socket);
                    progressbarSearchDevices.setVisibility(View.INVISIBLE);
                    SPUtils.put(MyApplication.getContext(),"bluetoothName",device.getName());
                    SPUtils.put(MyApplication.getContext(),"bluetoothAddress",device.getAddress());
                    toastShow("连接成功");
                    // 连接成功，返回主界面
                    finish();
                } catch (IOException connectException) {
                    // Unable to connect; close the socket and get out
                    Log.d("log BluetoothSocket", "连接失败");
                    toastShow("连接失败，请重试");
                    try {
                        socket.close();
                    } catch (IOException closeException) {
                    }
                    return;
                }


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Iterator<BluetoothDevice> iterator = deviceList.iterator();
                boolean exist = false;
                while (iterator.hasNext()) {
                    if (((BluetoothDevice) iterator.next()).getAddress().equals(device.getAddress())) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    deviceList.add(device);
                    // Add the name and address to an array adapter to show in a ListView
                    mDevicesArray.add(device.getName() + "\n" + device.getAddress());
                    Toast.makeText(getApplicationContext(), device.getName() + "\n" + device.getAddress(), Toast.LENGTH_SHORT).show();
                    devicesListAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @OnClick(R.id.close)
    public void onViewClicked() {
        finish();
    }

    class DevicesListAdapter<T> extends BaseAdapter {

        Context context;
        List<T> list;
        private LayoutInflater inflater;

        public DevicesListAdapter(Context context, List list) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public T getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = inflater.inflate(R.layout.item_listview_devices, null);
                holder.deviceName = (TextView) convertView.findViewById(R.id.item_device_name);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.deviceName.setText(list.get(position).toString());
            return convertView;
        }

        protected class Holder {
            TextView deviceName;
        }

    }
}
