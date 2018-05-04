package com.example.edz.myapplication.global;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.message.PushAgent;

import static anet.channel.util.Utils.context;

/**
 * Created by EDZ on 2018/4/23.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //push推送
        PushAgent.getInstance(context).onAppStart();
    }
}
