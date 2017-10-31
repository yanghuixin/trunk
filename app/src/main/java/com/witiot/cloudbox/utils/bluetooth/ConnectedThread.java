package com.witiot.cloudbox.utils.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ConnectedThread extends Thread {

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Handler mHandler;
    public final static int BLUETOOTH_CONECT_FAIL = 110;
    public final static int MESSAGE_READ = 1;
    boolean mWorking = true;
    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mmSocket = socket;
        mHandler = handler;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            mHandler.sendEmptyMessage(BLUETOOTH_CONECT_FAIL);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {

        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (mWorking) {
            byte[] buffer = new byte[100];
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                // Send the obtained bytes to the UI activity
                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
                mHandler.sendEmptyMessage(BLUETOOTH_CONECT_FAIL);
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (Exception e) {
            mHandler.sendEmptyMessage(BLUETOOTH_CONECT_FAIL);
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        mWorking = false;
        try {
            mmSocket.close();
        } catch (Exception e) {

        }
    }

}
