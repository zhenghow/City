package com.example.edz.myapplication.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.edz.myapplication.R;
import com.example.edz.myapplication.fragment.BaseFragment;
import com.example.edz.myapplication.fragment.MyFragment;
import com.example.edz.myapplication.utile.SharedPreferencesHelper;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.rb_base)
    RadioButton rbBase;
    @Bind(R.id.rb_my)
    RadioButton rbMy;
    @Bind(R.id.mRadioGroup)
    RadioGroup mRadioGroup;
    @Bind(R.id.button_reload)
    Button buttonReload;
    @Bind(R.id.layout_error)
    LinearLayout layoutError;

    private String TAG = "MainActivity";
    private BaseFragment baseFragment;
    private MyFragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this, "loginToken");

        String token = sharedPreferencesHelper.getString("token", null);
        MobclickAgent.onProfileSignIn(token);


        baseFragment=new BaseFragment();
        myFragment=new MyFragment();

        //获得Fragment管理器
        FragmentManager  fragmentManager = getSupportFragmentManager();
        //使用管理器开启事务
        FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();
        //添加fragment
        fragmentTransaction.add(R.id.frameLayout,baseFragment);
        //执行
        fragmentTransaction.commit();

        rbBase.setChecked(true);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
        //获得Fragment管理器
        FragmentManager  fragmentManager = getSupportFragmentManager();
        //使用管理器开启事务
        FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();
        switch (checkId) {
            case R.id.rb_base:
                if (baseFragment.isAdded()){
                    fragmentTransaction.show(baseFragment);
                    if (myFragment.isAdded()){
                        fragmentTransaction.hide(myFragment);
                    }
                }else {
                    fragmentTransaction.add(R.id.frameLayout, baseFragment);
                }
                fragmentTransaction.commit();
                break;
            case R.id.rb_my:
                if (myFragment.isAdded()){
                    fragmentTransaction.show(myFragment);
                    if (baseFragment.isAdded()){
                        fragmentTransaction.hide(baseFragment);
                    }
                }else {
                    fragmentTransaction.add(R.id.frameLayout,myFragment);
                }
                fragmentTransaction.commit();
                break;
        }
    }

    private long firstTime = 0;

    /**
     * 通过监听keyUp
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyUp(keyCode, event);
    }


    @OnClick(R.id.button_reload)
    public void onViewClicked() {
        layoutError.setVisibility(View.GONE);
        reload();
    }

    private void reload() {
        //获得Fragment管理器
        FragmentManager  fragmentManager = getSupportFragmentManager();
        //使用管理器开启事务
        FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();
        //添加Fragment的时候，要传入标记
        fragmentTransaction.add(R.id.frameLayout, baseFragment).commit();
        rbBase.setChecked(true);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }




}
