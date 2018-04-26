package com.example.edz.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.activity.WebActivity;
import com.example.edz.myapplication.utile.JsHelper;
import com.example.edz.myapplication.utile.SharedPreferencesHelper;
import com.example.edz.myapplication.utile.Urls;
import com.example.edz.myapplication.utile.WebViewUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    @Bind(R.id.web)
    WebView webView;
    @Bind(R.id.loadingLayout)
    FrameLayout loadingLayout;
    @Bind(R.id.button_reload)
    Button buttonReload;
    @Bind(R.id.web_error)
    LinearLayout webError;

    private String TAG = "BaseFragment";

    public BaseFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        ButterKnife.bind(this, view);
        webView.setVisibility(View.VISIBLE);
        initView();

        return view;
    }

    private void initView() {
        WebSettings webSettings = webView.getSettings();
        WebViewUtil webViewUtil = new WebViewUtil(getActivity());

        //调用JS
        webView.addJavascriptInterface(new JsHelper(getActivity()), "hello");

        webViewUtil.init(webView, loadingLayout, webError);

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "loginToken");
        String token = sharedPreferencesHelper.getString("token", null);

        //设置缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        String url = Urls.Url_webBase + "token=" + token;

        //加载URL
        webView.loadUrl(url);

        Log.i(TAG, "token: ==" + token);
        Log.i(TAG, "url: " + url);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.button_reload)
    public void onViewClicked() {
        Log.i(TAG, "onViewClicked: web_error");
        webError.setVisibility(View.GONE);
        initView();
        webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }


}
