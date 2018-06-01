package com.example.edz.myapplication.utile;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.edz.myapplication.R;

/**
 * Created by EDZ on 2018/4/2.
 */

public class WebViewUtil {


    public static final String TAG = "WebViewUtil";
    public static Activity context;

    public WebViewUtil(Activity context) {
        this.context = context;
    }


    public static void init(final WebView webView, final FrameLayout loadingLayout, final LinearLayout webError) {

        //设置背景色
        webView.setBackgroundColor(0);
        //屏蔽长按事件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        //屏蔽滚动条
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
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);
        //设置字体相关
        webSettings.setTextZoom(100);


        //设置缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);


        //屏幕回退
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
//                context.finish();
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

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
//                Log.i("weberror", "errortitle: "+title);
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                    if ( title.contains("500") || title.contains("Error")) {
//                        view.loadUrl("about:blank");// 加载空白页，避免出现默认的错误界面
//
//                        webView.setVisibility(View.GONE);
//                        webError.setVisibility(View.VISIBLE);
//                    }
//                }
            }

            //设置响应js的 Alert()函数
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

        });


        webView.setWebViewClient(new WebViewClient() {
            //禁止调用外部浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.i("webView&2&:", String.valueOf(webView.getId()).toString() + "$$url" + url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("webView&finish&", "url=" + url + "##################################################################################################");
            }


//

            /**
             * 会出404
             * @param view
             * @param request
             * @param errorResponse
             */
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                // 这个方法在6.0才出现
                Log.i("weberror", "errorstatusCode: " + errorResponse.getStatusCode());
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                // 断网或者网络连接超时
                if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                    Log.i("weberror", "errorCode: " + errorCode);
                    view.loadUrl("about:blank"); // 加载空白页，避免出现默认的错误界面
                    webView.setVisibility(View.GONE);
                    webError.setVisibility(View.VISIBLE);
                }
            }

        });

    }


}
