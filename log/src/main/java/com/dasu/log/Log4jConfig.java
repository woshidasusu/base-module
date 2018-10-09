package com.dasu.log;

import android.content.Context;
import android.text.TextUtils;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by suxq on 2018/9/12.
 *
 * log4j 配置，目前只配置写文件也只允许配置一个输出
 */

class Log4jConfig {
    /**
     *    ### log日志的格式
     *
     *    ### 输出格式解释：
     *    ### %d{yyyy-MM-dd HH:mm:ss}: 时间，大括号内是时间格式
     *    ### %n: 换行
     *    ### %p: 日志级别，这里%5p是指定的5个字符的日志名称，为的是格式整齐
     *    ### %m: 日志信息
     *    ### %r: Logger初始化后经过的ms数，可用于区分不同次应用启动时日志的分界
     *
     *    ### 输出示例：
     *    ### 2018-09-13 15:02:01 running(15297ms) [DEBUG]/MainActivity: TimerTask, time: 278753449
     */
    private String mFilePattern = "%-d{yyyy-MM-dd HH:mm:ss} running(%rms) [%5p]/%m%n";
    private Level mLevel = Level.ALL;
    private String mFileName = "android-log4j.log";
    private int mMaxBackupSize = 5;
    private long mMaxFileSize = 1024 * 1024 * 3; //3MB
    private boolean mImmediateFlush = true; //false好像会出现数据重复问题，暂时不开启该接口

    public Log4jConfig(String fileName) {
        this.mFileName = fileName;
    }

    public void configure(final Logger root) {
        root.removeAllAppenders();
        configureFileAppender(root);
        root.setLevel(mLevel);
    }

    private void configureFileAppender(final Logger root) {
        final RollingFileAppender rollingFileAppender;
        final Layout fileLayout = new PatternLayout(mFilePattern);

        try {
            rollingFileAppender = new RollingFileAppender(fileLayout, mFileName);
        } catch (final IOException e) {
            throw new RuntimeException("Exception configuring log system", e);
        }

        rollingFileAppender.setMaxBackupIndex(mMaxBackupSize);
        rollingFileAppender.setMaximumFileSize(mMaxFileSize);
        rollingFileAppender.setImmediateFlush(mImmediateFlush);
        rollingFileAppender.setEncoding("UTF-8");

        root.addAppender(rollingFileAppender);

    }

    public Log4jConfig setLevel(final Level level) {
        this.mLevel = level;
        return this;
    }

    public Log4jConfig setFilePattern(final String filePattern) {
        this.mFilePattern = filePattern;
        return this;
    }

    public Log4jConfig setFileName(final String fileName) {
        this.mFileName = fileName;
        return this;
    }

    public Log4jConfig setMaxBackupSize(final int maxBackupSize) {
        this.mMaxBackupSize = maxBackupSize;
        return this;
    }

    public Log4jConfig setMaxFileSize(final long maxFileSize) {
        this.mMaxFileSize = maxFileSize * 1024 * 1024;
        return this;
    }

    String mergeLog4jFiles(Context context, String dirPath, String fileName) {
        if (TextUtils.isEmpty(fileName) || TextUtils.isEmpty(dirPath)) {
            return fileName;
        }

        if (!dirPath.endsWith(File.separator)) {
            dirPath += File.separator;
        }

        File dir = new File(dirPath);
        File lastFile = new File(dirPath, fileName);
        if (dir.exists() && lastFile.exists() && dir.isDirectory()) {
            List<File> fileList = new ArrayList<>();
            for (File f : dir.listFiles()) {
                if (f.getName().startsWith(fileName) && !f.getName().equals(fileName)) {
                    fileList.add(f);
                    if (LogConfig.sIsPrintDebugLog) {
                        LogUtils.d(LogConstValue.LOG_TAG, "mergeLog4jFiles, find a wait merge file:" + f.getName());
                    }
                }
            }

            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    char lastChar1 = f1.getName().charAt(f1.getName().length() - 1);
                    char lastChar2 = f2.getName().charAt(f2.getName().length() - 1);
                    if (lastChar1 < lastChar2) {
                        return 1;
                    } else if (lastChar1 == lastChar2) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
            fileList.add(lastFile);

            StringBuilder stringBuilder = new StringBuilder(dirPath);
            stringBuilder.append(fileName.substring(0, fileName.indexOf(".")));
            stringBuilder.append("_");
            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getTimeInstance();
            simpleDateFormat.applyPattern("yyyyMMdd_HHmmss");
            String timeStr = simpleDateFormat.format(System.currentTimeMillis());
            stringBuilder.append(timeStr);
            stringBuilder.append(".log");

            String tmpPath = stringBuilder.toString();
            int byteRead = 0;
            FileOutputStream os = null;
            FileInputStream is = null;
            try {
                os = new FileOutputStream(tmpPath);
                byte[] buffer = new byte[1024];
                for (int i = 0; i < fileList.size(); i++) {
                    if (LogConfig.sIsPrintDebugLog) {
                        LogUtils.d(LogConstValue.LOG_TAG, "mergeLog4jFiles, copy the " + i + "th file:" + fileList.get(i).getName());
                    }
                    is = new FileInputStream(fileList.get(i));
                    while ((byteRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, byteRead);
                    }
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e(LogConstValue.LOG_TAG, "mergeLog4jFiles error, msg:" + e.getMessage());
                new File(tmpPath).delete();
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tmpPath;
        }

        return dirPath + fileName;
    }
}
