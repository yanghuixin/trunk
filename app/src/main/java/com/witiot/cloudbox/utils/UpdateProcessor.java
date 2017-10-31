package com.witiot.cloudbox.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;


import com.witiot.cloudbox.http.UrlManage;
import com.witiot.cloudbox.model.UpdateVersionRes;
import com.witiot.cloudbox.views.login.LoginActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import top.wuhaojie.installerlibrary.AutoInstaller;

public class UpdateProcessor {
    ProgressDialog pd = null;
    String UPDATE_SERVERAPK = "ytg.apk";
    Context con;
    String url;
    String desc;
    String location_path;
    UpdateVersionRes.DatBean.UpdateBean updateBean;
    public UpdateProcessor(Context con, UpdateVersionRes appUpdateRes) {
        super();
        this.con = con;
        doNewVersionUpdate(this.con, appUpdateRes);
    }

    /**
     * 更新版本
     */
    public void doNewVersionUpdate(final Context con, final UpdateVersionRes appUpdateRes) {
//        StringBuffer sb = new StringBuffer();
        updateBean = appUpdateRes.getDat().getRows().get(0);
        final String url = updateBean.getUpdateUrl();
        String temp[] = url.split("/");

        for (int i = 0; i < temp.length; i++) {
            if (temp[i].contains("apk")) {
                UPDATE_SERVERAPK = temp[i].replace("/", "");
            }
        }
//        sb.append("当前版本：");
//        sb.append(",发现版本：");
//        sb.append(",是否更新");
        Dialog dialog = new AlertDialog.Builder(con)
                .setTitle("版本更新")
                .setMessage(updateBean.getContent())
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        pd = new ProgressDialog(con);
                        pd.setTitle("正在下载");
                        pd.setMessage("请稍候...");
                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pd.setCancelable(false);
                        pd.setCanceledOnTouchOutside(false);
                        downFile(url);
                    }
                })
//                .setNegativeButton("暂不更新",
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                dialog.dismiss();
//                                if (appUpdateRes.data.mustupdate.equals("1")) {
//                                    System.exit(0);
//                                }
//                            }
//                        }
//                )
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        // 显示更新框
        dialog.show();



    }

    /**
     * 下载apk
     */
    public void downFile(final String url) {
        LogUtil.log("downFile url",url);
        pd.show();
        location_path = Environment.getExternalStorageDirectory() +"/"+ UPDATE_SERVERAPK;
        LogUtil.log("location_path",location_path);
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(location_path);

        x.http().get(requestParams, new Callback.ProgressCallback<File>(){

            @Override
            public void onSuccess(File result) {
                down();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.log("downFile",ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                pd.setProgress((int) (((float) current / total) * 100));
            }
        });


//        httpUtils.download(url, location_path, false, true, new RequestCallBack<File>() {
//            @Override
//            public void onSuccess(ResponseInfo<File> responseInfo) {
//                Message message = handler.obtainMessage();
//                handler.sendMessage(message);
//            }
//
//            @Override
//            public void onLoading(long total, long current, boolean isUploading) {
//                super.onLoading(total, current, isUploading);
//                LogUtil.log("current/total", current + "/"+total);
//                pd.setProgress((int) (((float) current / total) * 100));
//
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                 LogUtil.log("s",s);
//                LogUtil.log("HttpException",e.toString());
//
//            }
//
//        });

//        new Thread() {
//            public void run() {
//                HttpClient client = new DefaultHttpClient();
//                HttpGet get = new HttpGet(url);
//                HttpResponse response;
//
//                try {
//                    response = client.execute(get);
//                    HttpEntity entity = response.getEntity();
//                    long length = entity.getContentLength();
//                    InputStream is = entity.getContent();
//                    FileOutputStream fileOutputStream = null;
//                    if (is != null) {
//                        File file = new File(
//                                Environment.getExternalStorageDirectory(),
//                                UPDATE_SERVERAPK);
//                        fileOutputStream = new FileOutputStream(file);
//                        byte[] b = new byte[1024];
//                        int charb = -1;
//                        int count = 0;
//                        while ((charb = is.read(b)) != -1) {
//                            fileOutputStream.write(b, 0, charb);
//                            count += charb;
//                            pd.setProgress((int) (((float) count / length) * 100));
//                        }
//                    }
//                    fileOutputStream.flush();
//                    if (fileOutputStream != null) {
//                        fileOutputStream.close();
//                    }
//                    down();
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            pd.cancel();
            update();
        }
    };

    /**
     * 下载完成，通过handler将下载对话框取消
     */
    public void down() {
        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        }.start();
    }

    /**
     * 安装应用
     */
    public void update() {
//        AutoInstaller.getDefault(con).install(location_path);
        File file = new File(Environment.getExternalStorageDirectory(), UPDATE_SERVERAPK);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkUri;
        // Android 7.0 以上不支持 file://协议 需要通过 FileProvider 访问 sd卡 下面的文件，所以 Uri 需要通过 FileProvider 构造，协议为 content://
        if (Build.VERSION.SDK_INT >= 24) {
            // content:// 协议  files_root_down
            apkUri = FileProvider.getUriForFile(con, "com.witiot.cloudbox.fileProvider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            // file:// 协议
            apkUri = Uri.fromFile(file);
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        con.startActivity(intent);
        System.exit(0);
    }
}
