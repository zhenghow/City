package com.example.edz.myapplication.utile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.edz.myapplication.R;

/**
 * Created by EDZ on 2018/4/2.
 */

public class WebViewUtil {
    public static Activity context;

    public WebViewUtil(Activity context) {
        this.context = context;
    }


    public static void init(final WebView webView, final FrameLayout loadingLayout, final LinearLayout webError) {
        //屏蔽长按事件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示

        WebSettings webSettings = webView.getSettings();
        //支持App内部JavaScript交互
        webSettings.setJavaScriptEnabled(true);
        //隐藏webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        // 设置可以支持缩放
        webSettings.setSupportZoom(false);
        // 为图片添加放大缩小功能
        webSettings.setUseWideViewPort(false);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN );
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextZoom(100);


        //设置缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);


        //屏幕回退
//        webView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//                    webView.goBack();
//                    return true;
//                }
//                return false;
//            }
//        });


        webView.setWebViewClient(new WebViewClient() {
            //禁止调用外部浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
////                Log.e(webView.toString(), "111111111"+"url="+url);
//                loadingLayout.setVisibility(View.VISIBLE);
//                webView.setVisibility(View.GONE);
//                webError.setVisibility(View.GONE);
//            }


            //网络连接失败处理//6.0以下执行
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl); //6.0以下执行
//                Log.i(TAG, "onReceivedError: ------->errorCode" + errorCode + ":" + description); //网络未连接
                view.loadUrl("about:blank");// 避免出现默认的错误界面


                webView.setVisibility(View.GONE);
                webError.setVisibility(View.VISIBLE);
            }


            //网络连接失败处理//6.0以上执行
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                view.loadUrl("about:blank");// 避免出现默认的错误界面

                webView.setVisibility(View.GONE);
                webError.setVisibility(View.VISIBLE);
            }
        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

            //设置响应js 的Confirm()函数
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                b.create().show();
                return true;
            }

            //设置响应js 的Prompt()函数
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                final View v = View.inflate(context, R.layout.dialog_alert, null);
                ((TextView) v.findViewById(R.id.text_alert)).setText(message);
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                b.setView(v);
                b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                b.create().show();
                return true;
            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                webView.setVisibility(View.VISIBLE);
//                if (newProgress == 100) {
//                    Log.e(webView.toString(), "222222222");
//                    webView.setVisibility(View.VISIBLE);
//                    loadingLayout.setVisibility(View.GONE);
//                    webError.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

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
