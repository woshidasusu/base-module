package com.dasu.fresco;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dasu on 2018/7/5.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 * 读取 assert 目录下的 build.properties 文件，该文件可由宿主 app 的 build.gradle 脚本中添加如下代码生成：
 *
 * <pre>
 def assertDir = sourceSets.main.assets.srcDirs[0]
 applicationVariants.all { variant ->
    variant.mergeAssets.dependsOn{
        tasks.create(name: "generate${variant.name.capitalize()}BuildProperties") {
            doFirst{
                if (!file(assertDir).exists()) {
                    file(assertDir).mkdirs()
                }
                def config = "";
                def fileName = "${assertDir}/build.properties"
                if (file(fileName).exists()) {
                    file(fileName).delete();
                }
                 android.defaultConfig.buildConfigFields.each {field ->
                    config += field.key + "=" + field.value.value.replaceAll("\"","")
                    config += "\n"
                 }
                variant.productFlavors.each {
                    if (it.name.equals(variant.flavorName)) {
                        variant.buildType.buildConfigFields.each {field ->
                            config += field.key + "=" + field.value.value.replaceAll("\"","")
                            config += "\n"
                        }
                        it.buildConfigFields.each {field ->
                            config += field.key + "=" + field.value.value.replaceAll("\"","")
                            config += "\n"
                        }

                    }
                }
                 println "=======BuildConfigFields======="
                 println config
                 file(fileName).withPrintWriter { printWriter ->
                     printWriter.println(config)
                 }
            }
        }
    }
 }
 * </pre>
 *
 */

@Deprecated
class PropertiesFile {
    private Map<String, String> mProperties = new HashMap<>();

    public PropertiesFile(Context context, String file) {
        Properties prop = new Properties();
        AssetManager am = context.getAssets();
        InputStream input = null;
        try {
            input = am.open(file);
            InputStream in = new BufferedInputStream(input);
            prop.load(in);
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                String value = prop.getProperty(key);
                mProperties.put(key, value);
                Log.d("DFresco", key + "=" + value);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key, String defaultValue) {
        String value = mProperties.get(key);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }
}
