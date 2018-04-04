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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.utile.JsHelper;

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

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1: {
                    webViewGoBack();
                }
                break;
            }
        }
    };
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


        //支持App内部JavaScript交互
        webView.getSettings().setJavaScriptEnabled(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
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
        webView.setWebViewClient(new WebViewClient());
        //调用JS
        webView.addJavascriptInterface(new JsHelper(this), "hello");
        //加载URL
        webView.loadUrl(url);
    }

    private void webViewGoBack() {
        webView.goBack();
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }
}
