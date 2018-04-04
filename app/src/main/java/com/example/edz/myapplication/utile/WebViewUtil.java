package com.example.edz.myapplication.utile;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by EDZ on 2018/4/2.
 */

public class WebViewUtil {




    public static void init(final WebView webView, final FrameLayout loadingLayout,final LinearLayout webError) {

        final Handler mhandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1: {
                        webViewGoBack();
                    }
                    break;
                }
            }
            private void webViewGoBack() {
                webView.goBack();
            }

        };


        WebSettings webSettings = webView.getSettings();
        //支持App内部JavaScript交互
        webSettings.setJavaScriptEnabled(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //屏幕回退
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                    mhandler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }
        });

        //禁止调用外部浏览器
        webView.setWebViewClient(new WebViewClient() {
            //网络连接失败处理//6.0以下执行
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl); //6.0以下执行
//                Log.i(TAG, "onReceivedError: ------->errorCode" + errorCode + ":" + description); //网络未连接
                view.loadUrl("about:blank");// 避免出现默认的错误界面
                loadingLayout.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                webError.setVisibility(View.VISIBLE);
            }


            //网络连接失败处理//6.0以上执行
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                view.loadUrl("about:blank");// 避免出现默认的错误界面
                loadingLayout.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                webError.setVisibility(View.VISIBLE);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                Log.i(TAG, "onProgressChanged:----------->" + newProgress);
                if (newProgress == 100) {
                    webView.setVisibility(View.VISIBLE);
                    loadingLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                Log.i(TAG, "onReceivedTitle:title ------>" + title);
                loadingLayout.setVisibility(View.GONE);
                // android 6.0 以下通过title获取
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    if (title.contains("404") || title.contains("500") || title.contains("Error")) {
                        view.loadUrl("about:blank");// 避免出现默认的错误界面
                        webView.setVisibility(View.GONE);
                        webError.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }


}
