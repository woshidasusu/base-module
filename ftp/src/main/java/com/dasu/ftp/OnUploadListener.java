package com.dasu.ftp;

/**
 * Created by suxq on 2018/9/11.
 */

public interface OnUploadListener {
    /**
     * 上传成功
     */
    void onSuccess();

    /**
     * 上传失败，具体原因见 code 和 description
     * @param code 错误码，具体见{@link FtpController}
     * @param description 错误信息
     */
    void onError(int code, Exception description);
}
