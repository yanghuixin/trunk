package com.yuyh.library.imgsel;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yuyh.library.imgsel.common.Callback;
import com.yuyh.library.imgsel.common.Constant;
import com.yuyh.library.imgsel.utils.FileUtils;
import com.yuyh.library.imgsel.utils.LogUtils;
import com.yuyh.library.imgsel.utils.StatusBarCompat;

import java.io.File;
import java.util.ArrayList;

/**
 * https://github.com/smuyyh/ImageSelector
 *
 * @author yuyh.
 * @date 2016/8/5.
 */
public class ImgSelActivity extends FragmentActivity implements View.OnClickListener, Callback {

    public static final String INTENT_RESULT = "result";
    private static final int IMAGE_CROP_CODE = 1;
    private static final int STORAGE_REQUEST_CODE = 1;

    private ImgSelConfig config;

    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private Button btnConfirm;
    private ImageView ivBack;
    private String cropImagePath;

    private ImgSelFragment fragment;

    private ArrayList<String> result = new ArrayList<>();

    public static void startActivity(Activity activity, ImgSelConfig config, int RequestCode) {
        Intent intent = new Intent(activity, ImgSelActivity.class);
        Constant.config = config;
        activity.startActivityForResult(intent, RequestCode);
    }

    public static void startActivity(Fragment fragment, ImgSelConfig config, int RequestCode) {
        Intent intent = new Intent(fragment.getActivity(), ImgSelActivity.class);
        Constant.config = config;
        fragment.startActivityForResult(intent, RequestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constant.config = null;//防止内存泄漏
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_sel);
        config = Constant.config;

        // Android 6.0 checkSelfPermission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        } else {
            fragment = ImgSelFragment.instance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fmImageList, fragment, null)
                    .commit();
        }

        initView();
        if (!FileUtils.isSdCardAvailable()) {
            Toast.makeText(this, getString(R.string.sd_disable), Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        rlTitleBar = (RelativeLayout) findViewById(R.id.rlTitleBar);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);

        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        if (config != null) {
            if (config.backResId != -1) {
                ivBack.setImageResource(config.backResId);
            }

            if (config.statusBarColor != -1) {
                StatusBarCompat.compat(this, config.statusBarColor);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                        && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                }
            }
            rlTitleBar.setBackgroundColor(config.titleBgColor);
            tvTitle.setTextColor(config.titleColor);
            tvTitle.setText(config.title);
            btnConfirm.setBackgroundColor(config.btnBgColor);
            btnConfirm.setTextColor(config.btnTextColor);

            if (config.multiSelect) {
                if (!config.rememberSelected) {
                    Constant.imageList.clear();
                }
                if(null != config.imageList){
                    Constant.imageList.clear();
                    Constant.imageList.addAll(config.imageList);
                }
                btnConfirm.setText(String.format(getString(R.string.confirm_format), config.btnText, Constant.imageList.size(), config.maxNum));
            } else {
                Constant.imageList.clear();
                btnConfirm.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnConfirm) {
            if (Constant.imageList != null && !Constant.imageList.isEmpty()) {
                exit();
            } else {
                Toast.makeText(this, getString(R.string.minnum), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.ivBack) {
            onBackPressed();
        }
    }

    @Override
    public void onSingleImageSelected(String path) {
        if (config.needCrop) {
            crop(path);
        } else {
            Constant.imageList.add(path);
            exit();
        }
    }

    @Override
    public void onImageSelected(String path) {
        btnConfirm.setText(String.format(getString(R.string.confirm_format), config.btnText, Constant.imageList.size(), config.maxNum));
    }

    @Override
    public void onImageUnselected(String path) {
        btnConfirm.setText(String.format(getString(R.string.confirm_format), config.btnText, Constant.imageList.size(), config.maxNum));
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            if (config.needCrop) {
                crop(imageFile.getAbsolutePath());
            } else {
                Constant.imageList.add(imageFile.getAbsolutePath());
                config.multiSelect = false; // 多选点击拍照，强制更改为单选
                exit();
            }
        }
    }


    @Override
    public void onPreviewChanged(int select, int sum, boolean visible) {
        if (visible) {
            tvTitle.setText(select + "/" + sum);
        } else {
            tvTitle.setText(config.title);
        }
    }

    private void crop(String imagePath) {
        System.out.println("imagePath = " + imagePath);
        File file = new File(FileUtils.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg");
        cropImagePath = file.getAbsolutePath();
        System.out.println("crop cropimagePath = "+cropImagePath);

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(false);
        options.setStatusBarColor(getColor(this,R.color.red_button));
        options.setToolbarColor(getColor(this,R.color.red_button));
        UCrop uCrop = UCrop.of(Uri.fromFile(new File(imagePath)), Uri.fromFile(file));
        uCrop.withAspectRatio(1, 1).withMaxResultSize(500, 500).withOptions(options);

        uCrop.start(this);


//        Intent intent = new Intent("com.android.camera.action.CROP");
//        Uri fromUri = getImageContentUri(new File(imagePath));
//        System.out.println("fromUri = "+fromUri);
//        intent.setDataAndType(fromUri, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", config.aspectX);
//        intent.putExtra("aspectY", config.aspectY);
//        intent.putExtra("outputX", config.outputX);
//        intent.putExtra("outputY", config.outputY);
//        intent.putExtra("scale", true);
//        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true);
//        startActivityForResult(intent, IMAGE_CROP_CODE);
    }

    public   int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == IMAGE_CROP_CODE && resultCode == RESULT_OK) {
//            Constant.imageList.add(cropImagePath);
//            System.out.println("crop cropimagePath = "+cropImagePath);
//            config.multiSelect = false; // 多选点击拍照，强制更改为单选
//            exit();
//        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
//            cropImagePath = getRealPathFromURI(this,resultUri);
            Constant.imageList.add(cropImagePath);
            System.out.println("onActivityResult cropimagePath = "+cropImagePath);
            config.multiSelect = false; // 多选点击拍照，强制更改为单选
            exit();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void exit() {
        Intent intent = new Intent();
        result.clear();
        result.addAll(Constant.imageList);
        System.out.println("result.size() = "+result.size());
        intent.putStringArrayListExtra(INTENT_RESULT, result);
        setResult(RESULT_OK, intent);

        if (!config.multiSelect) {
            Constant.imageList.clear();
        }

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_REQUEST_CODE:
                if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fmImageList, ImgSelFragment.instance(), null)
                            .commitAllowingStateLoss();
                } else {
                    showMissingPermissionDialog();
//                    Toast.makeText(this, getString(R.string.permission_storage_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ImgSelActivity.this);
        builder.setTitle("帮助");
        builder.setMessage("当前应用缺少必要权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限。\\n\\n最后点击两次后退按钮，即可返回。");

        // 拒绝, 退出应用
        builder.setNegativeButton("暂不设置", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:com.maoye.xhm"));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (fragment == null || !fragment.hidePreview()) {
//            Constant.imageList.clear();
            exit();
            super.onBackPressed();
        }
    }
}
