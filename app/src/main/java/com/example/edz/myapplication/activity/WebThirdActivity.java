package com.example.edz.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class WebThirdActivity extends AppCompatActivity {

    private final String TAG = "WebThirdActivity";
    @Bind(R.id.img_finish)
    FrameLayout imgFinish;
    @Bind(R.id.title_webthird)
    TextView titleWebthird;
    @Bind(R.id.webView_webthird)
    WebView webViewWebthird;
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
        setContentView(R.layout.activity_web_third);
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
        titleWebthird.setText(title);

        WebViewUtil webViewUtil = new WebViewUtil(this);
        webViewUtil.init(webViewWebthird, loadingLayout, webError);

        //调用JS
        webViewWebthird.addJavascriptInterface(new JsHelper(this), "hello");
        //加载URL
        webViewWebthird.loadUrl(url);
    }

    @OnClick({R.id.img_finish, R.id.button_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.button_reload:
                Log.i(TAG, "onViewClicked: web_error");
                webError.setVisibility(View.GONE);
                initWeb();
                webViewWebthird.setVisibility(View.VISIBLE);
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
