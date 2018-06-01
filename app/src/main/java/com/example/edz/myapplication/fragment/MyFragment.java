package com.example.edz.myapplication.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.activity.SetActivity;
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
@SuppressLint("ValidFragment")
public class MyFragment extends Fragment {

    private final String TAG = "MyFragment";
    @Bind(R.id.img_set)
    FrameLayout imgSet;
    @Bind(R.id.web_home)
    WebView webHome;
    @Bind(R.id.web_error)
    LinearLayout webError;
    @Bind(R.id.loadingLayout)
    FrameLayout loadingLayout;
    @Bind(R.id.button_reload)
    Button buttonReload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        WebViewUtil webViewUtil = new WebViewUtil(getActivity());

        //调用JS
        webHome.addJavascriptInterface(new JsHelper(getActivity()), "hello");
        //初始化web工具
        webViewUtil.init(webHome, loadingLayout, webError);
        //获取token
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "loginToken");
        String token = sharedPreferencesHelper.getString("token", null);

        //加载URL
        webHome.loadUrl(Urls.Url_webHome + "token=" + token);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.img_set, R.id.button_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_set://set设置
                Intent intent = new Intent(getActivity(), SetActivity.class);
                intent.putExtra("type", "1");
                getActivity().startActivity(intent);
                break;
            case R.id.button_reload://重新加载
                Log.i(TAG, "onViewClicked: web_error");
                webError.setVisibility(View.GONE);
                initView();
                webHome.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

}