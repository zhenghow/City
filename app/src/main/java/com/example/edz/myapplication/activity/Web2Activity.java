package com.example.edz.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.utile.JsHelper;
import com.example.edz.myapplication.utile.WebViewUtil;
import com.umeng.analytics.MobclickAgent;

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
    }

    private void initWeb() {
        textWeb2title.setText(title);

        WebViewUtil webViewUtil = new WebViewUtil(this);
        webViewUtil.init(webViewWeb2, loadingLayout, webError);

        //调用JS
        webViewWeb2.addJavascriptInterface(new JsHelper(this), "hello");
        //加载URL
        webViewWeb2.loadUrl(url);
    }

    @OnClick({R.id.img_finish_web2, R.id.button_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish_web2:
                finish();
                break;
            case R.id.button_reload:
                Log.i(TAG, "onViewClicked: web_error");
                webError.setVisibility(View.GONE);
                initWeb();
                webViewWeb2.setVisibility(View.VISIBLE);
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
}
