package com.dasu.volley.wrapper;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * A HttpStack implement witch can verify specified self-signed certification.
 * 有对https 做信任的请求
 */
class SelfSignSslHurlStack extends HurlStack {

    private SSLSocketFactory sslSocketFactory;
    private MyHostnameVerifier myHostnameVerifier;

    /**
     * Create a OkHttpStack with default OkHttpClient.
     */
    public SelfSignSslHurlStack() {
        try {
            SSLContext sc = null;
            sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
            sslSocketFactory = new NoSSLv3Compat.NoSSLv3Factory(sc.getSocketFactory()); // sc.getSocketFactory();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        myHostnameVerifier = new MyHostnameVerifier();
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {

        //客户端有导入签名的用这种
//        if ("https".equals(url.getProtocol()) && socketFactoryMap != null && socketFactoryMap.containsKey(url.getHost())) {
//            HttpsURLConnection connection = (HttpsURLConnection) new OkUrlFactory(okHttpClient).open(url);
//            connection.setSSLSocketFactory(socketFactoryMap.get(url.getHost()));
//            return connection;
//        }

        //无条件支持签名的用这种
        if ("https".equals(url.getProtocol())) {
            HttpsURLConnection connection = ((HttpsURLConnection) super.createConnection(url));
            try {
                connection.setSSLSocketFactory(sslSocketFactory);
                connection.setHostnameVerifier(myHostnameVerifier);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return connection;
        } else {
            return super.createConnection(url);
        }
    }


    /*************
     * 做所有都认证
     ***********/

    private class MyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }

    private class MyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    }

}
