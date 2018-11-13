package me.demo.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.demo.wechat.common.AccessTokenAccessor;
import me.demo.wechat.entry.AccessToken;
import me.demo.wechat.util.NetWorkHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
    * 用于获取accessToken的Servlet
    * Created by xdp on   /1/ .
    */
  @WebServlet(
                  name = "AccessTokenService",
                  urlPatterns = {"/AccessTokenService"},
                  loadOnStartup = 1,
                  initParams = {
                          @WebInitParam(name = "appId", value = "wx33895b4b9d85841a"),
                          @WebInitParam(name = "appSecret", value = "da77409f548c8b25ef1693d87ab4f517")
                  })
  public class AccessTokenService extends HttpServlet {
 
              @Override
      public void init() throws ServletException {
                  System.out.println(" starting WebServlet");
                  super.init();

                  final String appId = getInitParameter("appId");
                  final String appSecret = getInitParameter("appSecret");

                  //开启一个新的线程
                  new Thread(new Runnable() {
              @Override
              public void run() {
                                  while (true) {
                                          try {
                                                  //获取accessToken
                                                   AccessTokenAccessor.accessToken = getAccessToken(appId, appSecret);
                                                  //获取成功
                                                  if ( AccessTokenAccessor.accessToken != null) {
                                                          //获取到access_token 休眠  秒,大约2个小时左右
                                                          Thread.sleep( 7000  *  1000 );
                                                          //Thread.sleep(  *   );// 秒钟获取一次
                                                      } else {
                                                          //获取失败
                                                          Thread.sleep(  1000 * 3000); //获取的access_token为空 休眠3秒
                                                      }
                                              } catch (Exception e) {
                                                  System.out.println("发生异常：" + e.getMessage());
                                                  e.printStackTrace();
                                                  try {
                                                          Thread.sleep( 1000  * 10 ); //发生异常休眠1秒
                                                      } catch (Exception e1) {

                                                      }
                                              }
                                      }

                              }
          }).start();
              }
 
              /**
        * 获取access_token
        *
        * @return AccessToken
        */
              private AccessToken getAccessToken(String appId, String appSecret) {
                  NetWorkHelper netHelper = new NetWorkHelper();
                  /**
                    * 接口地址为https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET，其中grant_type固定写为client_credential即可。
                    */
                  String Url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appId, appSecret);
                  //此请求为https的get请求，返回的数据格式为{"access_token":"ACCESS_TOKEN","expires_in":  }
                  String result = netHelper.getHttpsResponse(Url, "");
                  System.out.println("retrieved access_token="+result);
                  //使用FastJson将Json字符串解析成Json对象
                  JSONObject json = JSON.parseObject(result);
                  AccessToken token = new AccessToken();
                  token.setAccessToken(json.getString("access_token"));
                  token.setExpiresin(json.getInteger("expires_in"));
                  return token;
              }
  }
