package com.example.edz.myapplication.utile;

/**
 * Created by EDZ on 2018/3/27.
 */

public class Urls {

//    public static final String  URL="http://citychain.niwoa.com";

//    public static final String URL = "http://192.168.0.115";
//    public static final String URL = "http://192.168.1.107";
//    public static final String URL = "http://140.143.53.254:83";//测试服务器
    public static final String URL = "http://140.143.53.254";//正式服务器

    //android
    public static final String URL_User = URL + "/user/";
    public static final String Url_Login = URL_User + "login";
    public static final String Url_Register = URL_User + "register";
    public static final String Url_smscode = URL_User + "smsCode";
    public static final String Url_Setting = URL_User + "setting";
    public static final String Url_Version = URL_User + "version";

    //webView
    public static final String Url_web = URL + "/h5/";
    public static final String Url_webBase = Url_web + "base?";
    public static final String Url_webHome = Url_web + "home?";
    public static final String Url_webProtocol = Url_web + "protocol";


    public static final String Url_webLoad = Url_web + "choose";


}
