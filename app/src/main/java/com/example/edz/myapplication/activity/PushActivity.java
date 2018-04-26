package com.example.edz.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.bean.PushInfo;
import com.example.edz.myapplication.utile.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotifyClickActivity;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;

import java.util.Map;

import static anet.channel.util.Utils.context;

public class PushActivity  extends UmengNotifyClickActivity {
    private static String TAG = "PushActivity";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        PushAgent.getInstance(context).onAppStart();
    }
    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);

        Log.d("TEST", "body：" + body);
        UMessage uMessage = new Gson().fromJson(body, UMessage.class);
        Message message = Message.obtain();
        message.obj = uMessage;
        Log.d("TEST", "message.obj：" + message.obj);
        handler.sendMessage(message);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initView();
//             intent = new Intent(PushActivity.this, SplashActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            addMessageToIntent(intent, (UMessage) msg.obj);
//            Log.d("TEST", "uMessage：" + msg.obj.toString());
//            startActivity(intent);
            finish();
        }
    };
    /**
     * 用于将UMessage中自定义参数的值放到intent中传到SplashActivity中，SplashActivity中对友盟推送时自定义消息作了专门处理
     * @param intent 需要增加值得intent
     * @param msg    需要增加到intent中的msg
     */
    private void addMessageToIntent(Intent intent, UMessage msg) {

        if (intent == null || msg == null || msg.extra == null) {
            return;
        }

        for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null) {
                intent.putExtra(key, value);
            }
        }

    }
    private void initView() {
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this, "loginToken");
        String token = sharedPreferencesHelper.getString("token", null);
        Log.e(TAG, "token: " + token);

        if (token == null) {
            intent = new Intent(PushActivity.this, LoginActivity.class);
            intent.putExtra("type", "0");
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(PushActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
