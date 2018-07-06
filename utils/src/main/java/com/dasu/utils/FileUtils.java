package com.dasu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by suxq on 2018/5/5.
 */

public class FileUtils {
    /**
     * 复制文件，默认删除原文件
     *
     * @param sourceFile
     * @param destFile
     */
    public static boolean copyFile(File sourceFile, File destFile) {
        return copyFile(sourceFile, destFile, true);
    }

    /**
     * 复制文件
     * @param sourceFile 原文件
     * @param destFile 目标文件
     * @param isDeleteSource 是否删除原文件
     * @return 是否成功
     */
    public static boolean copyFile(File sourceFile, File destFile, boolean isDeleteSource) {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            int byteRead = 0;
            if (sourceFile.exists()) {
                is = new FileInputStream(sourceFile);
                os = new FileOutputStream(destFile);
                byte[] buffer = new byte[1024];
                while ((byteRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, byteRead);
                }
                if (isDeleteSource) {
                    sourceFile.delete();
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            if (destFile.exists()) {
                destFile.delete();
            }
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param file
     * @throws IOException
     */
    public static void deleteFiles(File file) throws IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFiles(f);
            }
        } else {
            file.delete();
        }
    }

    /**
     * 从输入流中读取数据并返回字符串
     *
     * @param inputStream
     * @return
     */
    public static String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
}
