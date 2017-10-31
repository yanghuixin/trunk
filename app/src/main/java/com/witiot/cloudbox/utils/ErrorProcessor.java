package com.witiot.cloudbox.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.witiot.cloudbox.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ErrorProcessor {
    public static void saveErrorLog(Throwable ex) {

        // 生成错误信息String
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String result = info.toString();
        printWriter.close();

        // 生成VersionName_VersionCode_timestamp的文件名
        String fileName = generateFileName();

        // 检测是否挂在SD卡
        String sdStatus = Environment.getExternalStorageState();

        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("ErrorProcessor",
                    "SD card is not avaiable/writeable right now.");
            return;
        }
        try {
            // 存储路径
            String pathName = Environment.getExternalStorageDirectory().toString() + "/YTG/Log/";

            File path = new File(pathName);

            File file = new File(pathName + fileName);

            if (!path.exists()) {
                Log.d("ErrorProcessor", "Create the path:" + pathName);
                path.mkdir();
            }

            if (!file.exists()) {
                Log.d("ErrorProcessor", "Create the file:" + fileName);
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(file);
            byte[] buf = result.getBytes();
            stream.write(buf);
            stream.close();
        } catch (Exception e) {
            Log.e("ErrorProcessor", "Error on writeFilToSD.");
            e.printStackTrace();
        }
    }

    /**
     * 生成格式为VersionName_VersionCode_timeStamp的log文件
     *
     * @return
     */
    private static String generateFileName() {
        String result = "";

        PackageInfo pInfo = null;
        try {
            PackageManager pm = MyApplication.getContext().getPackageManager();
            pInfo = pm.getPackageInfo(MyApplication.getContext().getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {

        }

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhss");

        String timeStamp = sdf.format(cal.getTime());

        result = pInfo.versionName + "_" + pInfo.versionCode + "_" + timeStamp
                + ".txt";

        return result;
    }
}
