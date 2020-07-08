package com.starstudio.frame.net.extend.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
public class HttpsTrustManager implements X509TrustManager {

    private static TrustManager[] trustManagers;
    private static final X509Certificate[] ACCEPTEDISSUERS = new X509Certificate[]{};

    @Override
    public void checkClientTrusted(
            X509Certificate[] x509Certificates, String s)
            throws java.security.cert.CertificateException {

    }

    @Override
    public void checkServerTrusted(
            X509Certificate[] x509Certificates, String s)
            throws java.security.cert.CertificateException {

    }

    public boolean isClientTrusted(X509Certificate[] chain) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return true;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return ACCEPTEDISSUERS;
    }

    public static void allowAllSSL() {
//        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//
//			@Override
//			public boolean verify(String arg0, SSLSession arg1) {
//
//				return true;
//			}
//
//        });

        try {
            SSLContext sslcontext = SSLContext.getInstance("TLSv1");
            sslcontext.init(null, null, null);
            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            SSLEngine engine = sslContext.createSSLEngine();

            HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

//        SSLContext context = null;
//        if (trustManagers == null) {
//            trustManagers = new TrustManager[]{new HttpsTrustManager()};
//        }
//
//        try {
//            context = SSLContext.getInstance("TLS");
//            context.init(null, trustManagers, new SecureRandom());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//
//        HttpsURLConnection.setDefaultSSLSocketFactory(context
//                .getSocketFactory());
    }

}
