package com.witiot.cloudbox.utils.bluetooth;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;

public class BluetoothUtils {


	private static BluetoothSocket mmSocket = null;
	
	public static void setBluetoothSocket(BluetoothSocket socket) {
		mmSocket = socket;
	}
	
	public static BluetoothSocket getBluetoothSocket() {
		if(mmSocket != null) {
			return mmSocket;
		}
		return null;
	}

	public static void closeBluetoothSocket() {
		if(mmSocket != null) {
			try {
				mmSocket.close();
				mmSocket = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String bytesToHexString(byte[] bytes) {
		String result = "";
		for (int i = 0; i < bytes.length; i++) {
			String hexString = Integer.toHexString(bytes[i] & 0xFF);
			if (hexString.length() == 1) {
				hexString = '0' + hexString;
			}
			result += hexString.toUpperCase();
		}
		return result;
	}

    public static byte getByte(int value){
        if((value&0x80)==0){
            return (byte)value;
        }
        else{
            return (byte)((value&0xFF)-0x100);
        }
    }
}
