package com.example.edz.myapplication.utile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.example.edz.myapplication.activity.Web2Activity;
import com.example.edz.myapplication.activity.WebActivity;

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
}
