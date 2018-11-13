package me.demo.wechat.util;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * 访问网络用到的工具类
 */
public class NetWorkHelper {

    X509TrustManager xtm = new X509TrustManager() {

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }


        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {

        }


        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {

        }
    };


    /**
     * 发起Https请求
     *
     * @param reqUrl        请求的URL地址
     * @param requestMethod
     * @return 响应后的字符串
     */
    public String getHttpsResponse(String reqUrl, String requestMethod) {
        URL url;
        InputStream is;
        String resultData = "";
        try {
            url = new URL(reqUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            TrustManager[] tm = {xtm};

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);

            con.setSSLSocketFactory(ctx.getSocketFactory());
            con.setHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });


            con.setDoInput(true); //允许输入流，即允许下载

            //在android中必须将此项设置为false
            con.setDoOutput(false); //允许输出流，即允许上传
            con.setUseCaches(false); //不使用缓冲
            if (null != requestMethod && !requestMethod.equals("")) {
                con.setRequestMethod(requestMethod); //使用指定的方式
            } else {
                con.setRequestMethod("GET"); //使用get请求
            }
            is = con.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine;
            while ((inputLine = bufferReader.readLine()) != null) {
                resultData += inputLine + "\n";
            }
            System.out.println(resultData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultData;
    }

    public String getHttpsResponse(String reqUrl, String requestMethod, Map<String, String> params) throws UnsupportedEncodingException {
        String parameters = "";
        boolean hasParams = false;
        for(String key : params.keySet()){
            String value = URLEncoder.encode(params.get(key), "UTF-8");
            parameters += key +"="+ value +"&";
            hasParams = true;
        }
        if(hasParams){
            parameters = parameters.substring(0, parameters.length()-1);
        }


        reqUrl += "?"+ parameters;

        String result=getHttpsResponse(reqUrl,requestMethod);
        return result;
    }



}
