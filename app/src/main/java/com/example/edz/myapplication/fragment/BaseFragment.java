package com.example.edz.myapplication.fragment;


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
import com.example.edz.myapplication.utile.JsHelper;
import com.example.edz.myapplication.utile.SharedPreferencesHelper;
import com.example.edz.myapplication.utile.Urls;
import com.example.edz.myapplication.utile.WebViewUtil;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        WebViewUtil webViewUtil = new WebViewUtil();
        webViewUtil.init(webView, loadingLayout, webError);
        //调用JS
        webView.addJavascriptInterface(new JsHelper(getActivity()), "hello");

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), "loginToken");
        String token = sharedPreferencesHelper.getString("token", null);
        Log.e(TAG, "token: " + token);
        //加载URL
        webView.loadUrl(Urls.Url_webBase + "token=" + token);
//        webView.loadUrl("file:///android_asset/javascript.html");
//        webView.loadUrl("javascript:android(true)");


    }


//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        SharedPreferences share = getActivity().getSharedPreferences("loginToken", MODE_PRIVATE);
//        String token = share.getString("token", null);
//        if (hidden) {
//
//        } else {
//            webView.loadUrl(Urls.Url_webBase + "token=" + token);
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick(R.id.button_reload)
    public void onViewClicked() {
        initView();
        loadingLayout.setVisibility(View.VISIBLE);
        webError.setVisibility(View.GONE);
    }
}
