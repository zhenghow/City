package com.example.edz.myapplication.global;

import android.app.Application;
import android.content.Context;
import com.lzy.okgo.OkGo;

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
        initOkGo();
    }

    private void initOkGo() {
        //必须调用初始化
        OkGo.getInstance().init(this);

    }
}
