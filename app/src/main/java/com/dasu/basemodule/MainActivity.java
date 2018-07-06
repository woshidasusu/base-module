package com.dasu.basemodule;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.dasu.update.OnUpdateListener;
import com.dasu.update.UpdateConfig;
import com.dasu.update.UpdateController;
import com.dasu.utils.LogUtils;

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
}
