package com.example.edz.myapplication.utile;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.edz.myapplication.activity.LoginActivity;
import com.example.edz.myapplication.activity.Web2Activity;
import com.example.edz.myapplication.activity.WebActivity;
import com.example.edz.myapplication.activity.WebThirdActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;

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
        Log.e("JsHelper", "showActivity:url== " + url);

        intent = new Intent(context, Web2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    @JavascriptInterface
    public static void showThirdActivity(String url, String title) {
        Log.e("JsHelper", "showActivity:url== " + url);

        intent = new Intent(context, WebThirdActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    @JavascriptInterface
    public static void onClickCopy(String code,String name) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //创建ClipData对象
        ClipData clipData = ClipData.newPlainText("邀请码", "我是"+name+",邀请您加入City寻宝，邀请码："+code+"，我的邀请次数有限，赶快加入哦～ http://140.143.53.254/cityChain.apk");
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
     *
     * 游戏下载
     */
    @JavascriptInterface
    public static void downloadGame(String url) {

        OkGo.<File>get(url).tag(context)
                .execute(new FileCallback() {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);

//                        textView.setText("开始");
                    }

                    @Override
                    public void onSuccess(Response<File> response) {

//                        textView.setText("下载完成");
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        float f1 = (float) (Math.round(progress.fraction * 100)) / 100;

//                        textView.setText(progress.fraction * 100 + "%");
                    }
                });
    }
}
