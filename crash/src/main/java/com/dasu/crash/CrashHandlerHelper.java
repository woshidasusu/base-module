package com.dasu.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Created by suxq on 2018/10/9.
 */

class CrashHandlerHelper {
    private static final String TAG = "CrashHandler";

    private static final String CRASH_LOG_SPLI = "crash_log_split:";

    int mCrashFileMax = 1024 * 1024;
    String mCrashFileName = "crash.log";
    String mLogPathDir;
    StringBuffer mDeviceInfos = new StringBuffer(800);
    DateFormat mDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    CrashHandlerHelper(Context context) {
        mLogPathDir = getDefaultDiskCachePath(context);
        collectDeviceInfo(context);
    }

    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                mDeviceInfos.append("\npackageName = ") ;
                mDeviceInfos.append(pi.packageName) ;
                mDeviceInfos.append("\nversionName = ");
                mDeviceInfos.append(pi.versionName);
                mDeviceInfos.append("\nversionCode = ");
                mDeviceInfos.append(pi.versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mDeviceInfos.append("\n");
                mDeviceInfos.append(field.getName());
                mDeviceInfos.append(" = ");
                if (field.get(null) instanceof String[]) {
                    mDeviceInfos.append(Arrays.toString((String[])field.get(null)));
                } else {
                    mDeviceInfos.append(field.get(null).toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * crash 日志文件默认保存在sd/{包名}/，如果sd不能使用，则在data/data/{包名}/files/
     * @param context
     * @return
     */
    private String getDefaultDiskCachePath(Context context) {
        boolean permission = (PackageManager.PERMISSION_GRANTED == context.getPackageManager()
                .checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", context.getPackageName()));

        if (isExternalStorageWritable() && permission) {
            String path =  Environment.getExternalStorageDirectory()+ File.separator
                    + context.getPackageName() + File.separator ;
            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdirs();
            }
        }
        return context.getFilesDir().toString() + File.separator;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) &&
                Environment.getExternalStorageDirectory().canWrite();
    }

    void saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        sb.append("\n");
        sb.append("crashTime = ");
        sb.append(mDateFormatter.format(System.currentTimeMillis()));
        String deviceInfo = mDeviceInfos.toString();
        Log.e("crash", deviceInfo);
        sb.append(deviceInfo);
        sb.append(CRASH_LOG_SPLI);
        try {
            String fileName = mCrashFileName;
            File dir = new File(mLogPathDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dstFile = new File(mLogPathDir, fileName);
            if (dstFile.exists() && dstFile.isFile()) {
                long count = dstFile.length();

                if (count > mCrashFileMax) {
                    dstFile.delete();
                }
            }
            FileOutputStream fos = new FileOutputStream(mLogPathDir + fileName, true);
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String inputStream2String(InputStream is) throws IOException {
        byte[] buf = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (int i; (i = is.read(buf)) != -1; ) {
            baos.write(buf, 0, i);
        }
        String data = baos.toString("UTF-8");
        baos.close();
        return data;
    }

    String[] getCrashLogs() {
        String fileName = mCrashFileName;

        File file = new File(mLogPathDir, fileName);
        FileInputStream inputStream = null;
        if (file.exists()) {
            try {
                inputStream = new FileInputStream(file);
                if (inputStream.available() > 0) {
                    String logContent = inputStream2String(inputStream);
                    if (!TextUtils.isEmpty(logContent)) {
                        return logContent.split(CRASH_LOG_SPLI);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    void clearCrashLogFile() {
        String fileName = mCrashFileName;
        try {
            File f = new File(mLogPathDir, fileName);
            FileWriter fw = new FileWriter(f);
            fw.write("");
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
