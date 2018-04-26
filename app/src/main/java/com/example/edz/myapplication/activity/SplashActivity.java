package com.example.edz.myapplication.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

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

public class SplashActivity extends BaseActivity {

    @Bind(R.id.text_time_welcome)
    TextView textTimeWelcome;
    @Bind(R.id.img_welcome)
    ImageView imgWelcome;

    private final String TAG = "SplashActivity";
    private Intent intent;
    private int time = 3;
    private String userId;
    private Thread thread;
    private DownloadBuilder builder;
    private VersionBean versionBean;
    private String versionMax;
    private String versionMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
//
        MobclickAgent.enableEncrypt(true);


        if (isNetworkAvailable(this)) {
            appUpData();
        }

//        uninstallApk(this,"com.example.edz.myapplication");
    }

    //    /* 卸载apk */
//    public static void uninstallApk(Context context, String packageName) {
//        Uri uri = Uri.parse("package:" + packageName);
//        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
//        context.startActivity(intent);
//    }
    private void appUpData() {
        builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl(Urls.Url_Version)
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
                        Gson gson = new Gson();
                        versionBean = gson.fromJson(result, VersionBean.class);
                        if (versionBean.getCode() == 10000) {
                            versionMax = versionBean.getObject().getVersionMax();
                            versionMin = versionBean.getObject().getVersionMin();
                            Log.i(TAG, "versioncode: "+versionMax+"#####"+versionMin);

                            try {
                                // 0代表相等，1代表version1大于version2，-1代表version1小于version2
                                switch (compareVersion(getVersionName(), versionMax)) {
                                    case 0://过不需要更新
                                        initView();
                                        break;
                                    case 1://不会出现
                                        initView();
                                        break;
                                    case -1://提示更新
                                        Log.e(TAG, "sdfsdfsdfsd: " + compareVersion(getVersionName(), versionMin));
                                        switch (compareVersion(getVersionName(), versionMin)) {
                                            case -1://强制更新
                                                builder.setForceUpdateListener(new ForceUpdateListener() {
                                                    @Override
                                                    public void onShouldForceUpdate() {
                                                        forceUpdate();
                                                    }
                                                });
                                                return crateUIData();
                                            case 1://需要更新
                                                initView();
                                                return crateUIData();
                                            case 0:
                                                initView();
                                                break;
                                        }
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {
                        Toast.makeText(SplashActivity.this, "网络连接失败，请检查网络", Toast.LENGTH_SHORT).show();

                    }
                });
        builder.excuteMission(this);
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

    private void initData() {
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (!Thread.interrupted()) {
                        for (int i = 1; i < 4; i++) {

                            sleep(1000);
                            time--;
                        }
                        mhandler.sendEmptyMessage(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    textTimeWelcome.setText("跳过");
                    break;
                case 1:
                    thread.interrupt();
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };


    @OnClick(R.id.text_time_welcome)
    public void onViewClicked() {
//        intent = new Intent(SplashActivity.this, MainActivity.class);
//        startActivity(intent);

        mhandler.sendEmptyMessage(1);
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

    /**
     * 禁用返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 版本号比较 * * @param version1 * @param version2 * @return
     * 0代表相等，1代表version1大于version2，-1代表version1小于version2
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        Log.d("HomePageActivity", "version1Array==" + version1Array.length);
        Log.d("HomePageActivity", "version2Array==" + version2Array.length);
        int index = 0; // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0; // 循环判断每位的大小
        Log.d("HomePageActivity", "verTag2=2222=" + version1Array[index]);
        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) { // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    /**
     * 获取当前程序的版本名
     */
    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        Log.e(TAG, "版本号" + packInfo.versionCode);
        Log.e(TAG, "版本名" + packInfo.versionName);

        return packInfo.versionName;
    }

    /**
     * 强制更新操作
     * 通常关闭整个activity所有界面，这里方便测试直接关闭当前activity
     */
    private void forceUpdate() {
        Toast.makeText(this, "force update handle", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * @return
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData 内部是一个Bundle
     */
    private UIData crateUIData() {
        UIData uiData = UIData.create();
        uiData.setTitle("版本更新");
        uiData.setDownloadUrl("http://140.143.53.254/cityChain.apk");
        uiData.setContent("1.修改了一些已知Bug");
        return uiData;
    }

    /**
     * 务必用库传回来的context 实例化你的dialog
     * 自定义的dialog UI参数展示，使用versionBundle
     *
     * @return
     */
//    private CustomVersionDialogListener createCustomDialogOne() {
//        return (context, versionBundle) -> {
//            BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_dialog_one_layout);
//            TextView textView = baseDialog.findViewById(R.id.tv_msg);
//            textView.setText(versionBundle.getContent());
//            return baseDialog;
//        };
//    }
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
