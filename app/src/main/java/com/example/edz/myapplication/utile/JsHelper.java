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
    public static void onClickCopy(String code) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //创建ClipData对象
        ClipData clipData = ClipData.newPlainText("邀请码", code);
        //添加ClipData对象到剪切板中
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


}
