package com.example.edz.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.utile.JsHelper;
import com.example.edz.myapplication.utile.WebViewUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebActivity extends AppCompatActivity {

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
        //加载URL
        webView.loadUrl(url);
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }
}
