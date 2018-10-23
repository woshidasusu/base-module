package com.dasu.basemodule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.dasu.crash.CrashHandler;
import com.dasu.ftp.FtpController;
import com.dasu.ftp.OnUploadListener;
import com.dasu.log.LogUtils;
import com.dasu.update.OnUpdateListener;
import com.dasu.update.UpdateConfig;
import com.dasu.update.UpdateController;
import com.dasu.utils.MacUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.inject(this);
        String mac = MacUtils.getMac();
        LogUtils.d("!!!!!!", "mac: " + mac);
    }

    @OnClick(R.id.btn_main_update)
    public void onUpdateBtnClick() {
        UpdateController.checkUpdate(this, new UpdateConfig() {
            @Override
            public boolean checkVersion(Context context) {
                this.setAutoDownload(true)
                        .setApkUrl("https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk")
                        .setApkFilePath(Environment.getExternalStorageDirectory() + "/com.dasu.basemodule/qq.apk")
//                        .setApkFilePath(mContext.getFilesDir() + "/com.dasu.basemodule/qq.apk")
                        .setDebugLogEnable(true);
                return true;
            }
        }, new OnUpdateListener() {
            @Override
            public void onDownloading(int progress) {
                LogUtils.d("!!!!!!!!!!", "progress: " + progress);
            }

            @Override
            public void onDownloadFinish(boolean isSucceed, String apkPath) {
                LogUtils.d("!!!!!!!!!!", "onDownloadFinish: isSucceed=" + isSucceed + ", apkPath=" + apkPath);
            }

            @Override
            public void onPreUpdate(Object newVersionInfo) {
                LogUtils.d("!!!!!!!!!!", "onPreUpdate");
            }
        });
    }

    @OnClick(R.id.btn_main_crash)
    public void onCrashBtnClick() {
        Intent intent = new Intent(this, CrashActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_main_ftp)
    public void onFtpBtnClick() {
        String filePath = CrashHandler.getInstance().getCrashFilePath();
        String ftpUrl = "";
        String destPath = "";
        FtpController.upload(this, filePath, ftpUrl, destPath, new OnUploadListener() {
            @Override
            public void onSuccess() {
                LogUtils.d("!!!!!!!!!!!", "ftp upload success");
            }

            @Override
            public void onError(int code, Exception description) {
                LogUtils.e("!!!!!!!!!!!", "ftp upload error: " + description.getMessage());
            }
        });
    }

    @OnClick(R.id.btn_main_blur)
    public void onBlurBtnClick(){
        Intent intent = new Intent(this, BlurActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_main_image)
    public void onBlankjBtnClick(){
        Intent intent = new Intent(this, ImageActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtils.d("!!!!!!!!!!", "keyevent");
        return super.dispatchKeyEvent(event);
    }

    @OnClick(R.id.btn_main_fresco)
    public void onFrescoBtnClick() {
        Intent intent = new Intent(this, FrescoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_main_volley)
    public void onVolleyBtnClick() {
        Intent intent = new Intent(this, VolleyActivity.class);
        startActivity(intent);
    }
}
