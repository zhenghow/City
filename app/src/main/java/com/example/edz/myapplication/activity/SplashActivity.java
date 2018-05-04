package com.example.edz.myapplication.activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.example.edz.myapplication.R;
import com.example.edz.myapplication.bean.VersionBean;
import com.example.edz.myapplication.global.BaseActivity;
import com.example.edz.myapplication.utile.BitmapHelper;
import com.example.edz.myapplication.utile.SharedPreferencesHelper;
import com.example.edz.myapplication.utile.Urls;
import com.google.gson.Gson;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Thread.sleep;

/**
 * 欢迎页
 */
public class SplashActivity extends BaseActivity {


    @Bind(R.id.img_welcome)
    ImageView imgWelcome;

    private final String TAG = "SplashActivity";
    private Intent intent;
    private int time = 3;
    private String userId;
    private Thread thread;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //欢迎页睡眠2秒
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


    }

    /**
     * 友盟日志加密设置
     */
    private void LogsEncryption() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            MobclickAgent.enableEncrypt(true);//6.0.0版本及以后
        }
//        else {
//            AnalyticsConfig.enableEncrypt(true);//6.0.0版本以前
//        }

    }


    /*
     * 打开设置网络界面
     */
    private void showSetNetworkUI() {
        // 提示对话框
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("网络设置提示")
                .setMessage("网络连接不可用,是否进行设置?")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent intent = null;
                        // 判断手机系统的版本 即API大于10 就是3.0或以上版本
                        if (android.os.Build.VERSION.SDK_INT > 10) {
                            intent = new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                        } else {
                            intent = new Intent();
                            ComponentName component = new ComponentName(
                                    "com.android.settings",
                                    "com.android.settings.WirelessSettings");
                            intent.setComponent(component);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        dialog.dismiss();
                        startActivity(intent);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).create();
        Log.w(TAG, "showSetNetworkUI: "+"AlertDialog1");
        dialogBuilder.show();

        Log.w(TAG, "showSetNetworkUI: "+"AlertDialog2");
    }



    private void initView() {
        /**
         *自动化测试
         */
//        String usertoken ="1a89a9ed944a44bb9bc1b7ff9d8d0b2b6512bd43d9caa6e02c990b0a82652dca9d5ed678fe57bcca610140957afab57145c48cce2e2d7fbdea1afc51c7c6ad26f09564c9ca56850d4cd6b3319e541aeecfcd208495d565ef66e7dff9f98764da";
//        SharedPreferences userInfo = getSharedPreferences("loginToken", MODE_PRIVATE);
//        SharedPreferences.Editor editor = userInfo.edit();//获取Editor //得到Editor后，写入需要保存的数据
//        editor.putString("token", usertoken);
//        editor.commit();//提交修改
        /**
         *
         */

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this, "loginToken");
        String token = sharedPreferencesHelper.getString("token", null);
        Log.e(TAG, "token: " + token);

        if (token == null) {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.putExtra("type", "0");
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //检查网络
        if (isNetworkAvailable(this)){
            //友盟日志加密设置
            LogsEncryption();
            //初始化
            initView();
        }else {
            showSetNetworkUI();
        }

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
