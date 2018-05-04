package com.example.edz.myapplication.global;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

import static anet.channel.util.Utils.context;

/**
 * Created by EDZ on 2018/3/27.
 */

public class MyApplication extends Application {


    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);

        mContext = getApplicationContext();

        //初始化OkGo
        initOkGo();

        //初始化push推送
        initPush();
    }

    /**
     * 推送
     */
    private void initPush() {
        //通用push
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "f3067acdceb5255c31d3caba7ddb1213");
        //小米push
        MiPushRegistar.register(mContext, "2882303761517775526", "5871777524526");
        //华为push
        HuaWeiRegister.register(mContext);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.getRegistrationId();
        Log.e("device token1", "device token: " + mPushAgent.getRegistrationId().toString());
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("device token", "device token: " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
            }
        });

        UMConfigure.setEncryptEnabled(true);
    }

    /**
     * 网络连接
     */
    private void initOkGo() {
        //必须调用初始化
        OkGo.getInstance().init(this);

    }


}
