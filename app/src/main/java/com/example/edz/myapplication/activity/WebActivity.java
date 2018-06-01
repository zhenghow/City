package com.example.edz.myapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.global.BaseActivity;
import com.example.edz.myapplication.utile.JsHelper;
import com.example.edz.myapplication.utile.WebViewUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * 通用WebView
 */
public class WebActivity extends BaseActivity {

    private final String TAG = "WebActivity";
    @Bind(R.id.webView_web)
    WebView webView;
    @Bind(R.id.img_finish)
    FrameLayout imgFinish;
    @Bind(R.id.text_webtitle)
    TextView textWebtitle;
    @Bind(R.id.loadingLayout)
    FrameLayout loadingLayout;
    @Bind(R.id.button_reload)
    Button buttonReload;
    @Bind(R.id.web_error)
    LinearLayout webError;
    @Bind(R.id.layout_web)
    LinearLayout layoutWeb;

    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        initData();
        initWeb();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        url = bundle.getString("url");
        title = bundle.getString("title");
        Log.e(TAG, "url: " + url);
    }

    private void initWeb() {
        textWebtitle.setText(title);

        WebViewUtil webViewUtil = new WebViewUtil(this);
        webViewUtil.init(webView, loadingLayout, webError);

        //调用JS
        webView.addJavascriptInterface(new JsHelper(this), "hello");

        Log.i(TAG, "initWeb: " + url);
        //加载URL
        webView.loadUrl(url);
    }


    @OnClick({R.id.img_finish, R.id.button_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.button_reload:
                Log.i(TAG, "onViewClicked: web_error");
                initWeb();
                webError.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initWeb();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        if (webView != null) {
            layoutWeb.removeView(webView);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }
}
