package me.demo.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.demo.wechat.util.NetWorkHelper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/routeService")
public class WXRouterService extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String code = request.getParameter("code");//获取code
        Map params = new HashMap();
        params.put("secret", "da77409f548c8b25ef1693d87ab4f517");
        params.put("appid", "wx33895b4b9d85841a");
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        String result = new NetWorkHelper().getHttpsResponse(
                "https://api.weixin.qq.com/sns/oauth2/access_token","GET", params);
        System.out.println("raw response for openID: "+result);
        JSONObject json = JSON.parseObject(result);
        String openid = json.get("openid").toString();
        System.out.println("retreived openid:"+openid);
        if(StringUtils.isNotBlank(openid))response.sendRedirect("https://portal0012.globalview.adp.com/gvservice/home/msft");
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }


}
