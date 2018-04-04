package com.example.edz.myapplication.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.edz.myapplication.activity.SetActivity;
import com.example.edz.myapplication.utile.JsHelper;
import com.example.edz.myapplication.utile.SharedPreferencesHelper;
import com.example.edz.myapplication.utile.Urls;
import com.example.edz.myapplication.utile.WebViewUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

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
    private WebSettings webSettings;
    private Activity context;
    private String userId;

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
        webHome.goBack();
    }

    public MyFragment(Activity context) {
        // Required empty public constructor
        this.context = context;
    }


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
        WebViewUtil webViewUtil=new WebViewUtil();
        webViewUtil.init(webHome,loadingLayout,webError);

        //调用JS
        webHome.addJavascriptInterface(new JsHelper(getActivity()), "hello");

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "loginToken");
        String token = sharedPreferencesHelper.getString("token", null);
        Log.e(TAG, "token: " + token);
        //加载URL
        webHome.loadUrl(Urls.Url_webHome + "token=" + token);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        LoadData();
    }

    public void LoadData() {
        //指定操作的文件名称
        SharedPreferences share = getActivity().getSharedPreferences("loginToken", MODE_PRIVATE);
        userId = share.getString("token", null);

    }

    /**
     * 显示自定义错误提示页面，用一个View覆盖在WebView
     */
    private void showErrorPage() {
        webHome.setVisibility(View.GONE);
        webError.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.img_set, R.id.button_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_set:
                Intent intent = new Intent(context, SetActivity.class);
                context.startActivity(intent);
                break;
            case R.id.button_reload:
                initView();
                loadingLayout.setVisibility(View.VISIBLE);
                webError.setVisibility(View.GONE);


                break;
        }
    }
}