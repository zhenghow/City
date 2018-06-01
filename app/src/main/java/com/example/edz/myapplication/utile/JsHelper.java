package com.example.edz.myapplication.utile;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.activity.GameLoadActivity;
import com.example.edz.myapplication.activity.LoginActivity;
import com.example.edz.myapplication.activity.MainActivity;
import com.example.edz.myapplication.activity.WebActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by EDZ on 2018/3/29.
 */

public class JsHelper {

    public static Activity context;
    private static Intent intent;


    public JsHelper(Activity context) {
        this.context = context;
    }

    //    String title, String url
    @JavascriptInterface
    public static void showActivity(String url, String title) {
        Log.e("JsHelper", "showActivity:url== " + url);

        intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public static void showSecondActivity(String url, String title) {
        Log.e("JsHelper", "showSecondActivity:url== " + url);

        intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    @JavascriptInterface
    public static void showThirdActivity(String url, String title) {
        Log.e("JsHelper", "showThirdActivity:url== " + url);

        intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    //咨讯    openBrowser
    @JavascriptInterface
    public static void openBrowser(String url) {
        Log.i("JsHelper", "openBrowser:url== " + url);

        Uri uri = Uri.parse(url);
        intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    //    邀请码
    @JavascriptInterface
    public static void onClickCopy(String code, String name) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //创建ClipData对象
        ClipData clipData = ClipData.newPlainText("邀请码",
                "我是" + name + ",邀请您加入City寻宝，邀请码：" + code + "，我的邀请次数有限，赶快加入哦～ http://www.ocity.io/download.html");

        //添加ClipData对象到剪切板中
//        我是多多，邀请您加入City寻宝，邀请码：GNEJ，我的邀请次数有限，赶快加入哦～ http://140.143.53.254/cityChain.apk
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
    }


    @JavascriptInterface
    public static void goBack() {
        context.finish();
    }

    /**
     * 返回code:10020
     */
    @JavascriptInterface
    public static void tokenError() {
        intent = new Intent(context, LoginActivity.class);
//        Toast.makeText(context,"sdsd",Toast.LENGTH_SHORT).show();
//        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(context, "loginToken");
//        sharedPreferencesHelper.put("token", null);
        intent.putExtra("type", "0");
        context.startActivity(intent);
    }

    /**
     * 游戏下载
     */
    @JavascriptInterface
    public static void downloadGame(String url, String title) {

        intent = new Intent(context, GameLoadActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    /**
     * 分享
     */
    @JavascriptInterface
    public static void share(String imgUrl, String title,String url) {

        if (UMShareAPI.get(context).isInstall(context, SHARE_MEDIA.WEIXIN)) {
            Log.i("JsHelper", "onViewClicked: " + UMShareAPI.get(context).isInstall(context, SHARE_MEDIA.WEIXIN));

            //图片分享
            UMImage image = new UMImage(context, imgUrl);//网络图片

            //链接分享
            UMWeb web = new UMWeb(url);
            web.setTitle(title);//标题
            web.setThumb(image);  //缩略图


            //不带面板分享
            new ShareAction(context)
                    .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                    .withMedia(web)
                    .setCallback(new UMShareListener() {//回调监听器
                        /**
                         * @descrption 分享开始的回调
                         * @param share_media 平台类型
                         */
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {

                        }

                        /**
                         * @descrption 分享成功的回调
                         * @param share_media 平台类型
                         */
                        @Override
                        public void onResult(SHARE_MEDIA share_media) {
                            Toast.makeText(context, "分享成功", Toast.LENGTH_LONG).show();
                        }

                        /**
                         * @descrption 分享失败的回调
                         * @param share_media 平台类型
                         * @param throwable 错误原因
                         */
                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                            Log.i("JsHelper", "分享失败== " + throwable.getMessage());
                        }

                        /**
                         * @descrption 分享取消的回调
                         * @param share_media 平台类型
                         */
                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {
                            Log.i("JsHelper", "onCancel: 取消                                          了");
                        }
                    })
                    .share();



        } else {
            Log.i("JsHelper", "onViewClicked: " + UMShareAPI.get(context).isInstall(context, SHARE_MEDIA.WEIXIN));
            Toast.makeText(context, "没有安装微信", Toast.LENGTH_SHORT).show();
        }

    }

}
