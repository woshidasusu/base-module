package com.dasu.basemodule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.dasu.crash.CrashHandler;
import com.dasu.ftp.FtpController;
import com.dasu.ftp.OnUploadListener;
import com.dasu.log.LogUtils;
import com.dasu.update.OnUpdateListener;
import com.dasu.update.UpdateConfig;
import com.dasu.update.UpdateController;

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
        String ftpUrl = "ftp://ftpvideo:Ftpvideo202@123.103.23.202:2121/advert/";
        String destPath = "C4:4E:AC:0A:59:FF";
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
}
