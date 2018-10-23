package com.dasu.volley.wrapper;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dasu.volley.VolleyListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dasu on 2018/10/22.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 */
public class ImageFileRequest extends Request{
    private String mFilePath;
    private VolleyListener mVolleyListener;

    public ImageFileRequest(String url) {
        super(url, null);
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public void download(VolleyListener<? extends String> listener) {
        mVolleyListener = listener;
        VolleyQueueSingleton.getInstance().add(this);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        boolean result = true;
        byte[] data = response.data;
        File file = new File(mFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }

        if (!result) {
            return Response.error(new ParseError(response));
        } else {
            return Response.success(mFilePath, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

    @Override
    protected void deliverResponse(Object response) {
        if (mVolleyListener != null) {
            mVolleyListener.onSuccess(mFilePath);
        }
    }
}
