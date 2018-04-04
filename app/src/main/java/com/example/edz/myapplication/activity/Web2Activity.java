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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Web2Activity extends AppCompatActivity {

    private final String TAG = "Web2Activity";
    @Bind(R.id.img_finish_web2)
    FrameLayout imgFinishWeb2;
    @Bind(R.id.text_web2title)
    TextView textWeb2title;
    @Bind(R.id.webView_web2)
    WebView webViewWeb2;

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
    private void webViewGoBack() {
        webViewWeb2.goBack();
    }
    private String url;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web2);
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
        textWeb2title.setText(title);

        //支持App内部JavaScript交互
        webViewWeb2.getSettings().setJavaScriptEnabled(true);
        //自适应屏幕
        webViewWeb2.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //屏幕回退
        webViewWeb2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webViewWeb2.canGoBack()) {
                    mhandler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }
        });
        //禁止调用外部浏览器
        webViewWeb2.setWebViewClient(new WebViewClient());
        //加载URL
        webViewWeb2.loadUrl(url);
    }

    @OnClick(R.id.img_finish_web2)
    public void onViewClicked() {
        finish();
    }
}
