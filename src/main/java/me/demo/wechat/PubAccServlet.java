package me.demo.wechat;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@WebServlet("/PubAccService")
public class PubAccServlet extends HttpServlet {
    private final String TOKEN = "mypubacc";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("starting signing");
        /**
         * fetch the 4 params weixin server passed
         */
        String signature = request.getParameter("signature");//微信加密签名signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        String timestamp = request.getParameter("timestamp");//时间戳
        String nonce = request.getParameter("nonce");//随机数
        String echostr = request.getParameter("echostr");//随机字符串

        if(StringUtils.isAnyBlank(signature,timestamp,nonce,echostr)){
            response.getWriter().write("Invalid access !");
            return;
        }



        //排序
        String sortString = sort(TOKEN, timestamp, nonce);
        //加密
        String mySignature = sha1(sortString);
        //校验签名
        if (mySignature != null && mySignature != "" && mySignature.equals(signature)) {
            System.out.println("signing succecced !");
            //如果检验成功输出echostr，微信服务器接收到此输出，才会确认检验完成。
            //response.getWriter().println(echostr);
            response.getWriter().write(echostr);
        } else {
            System.out.println("signing failed !");
        }

    }


    /**
     * 排序方法
     *
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }

        return sb.toString();
    }

    /**
     * 将字符串进行sha1加密
     *
     * @param str 需要加密的字符串
     * @return 加密后的内容
     */
    public String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}
