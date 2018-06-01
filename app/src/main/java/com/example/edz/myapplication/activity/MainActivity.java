package com.example.edz.myapplication.activity;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.example.edz.myapplication.R;
import com.example.edz.myapplication.bean.VersionBean;
import com.example.edz.myapplication.fragment.BaseFragment;
import com.example.edz.myapplication.fragment.MyFragment;
import com.example.edz.myapplication.global.BaseActivity;
import com.example.edz.myapplication.utile.SharedPreferencesHelper;
import com.example.edz.myapplication.utile.Urls;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

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
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;


    private String TAG = "MainActivity";
    private BaseFragment baseFragment;
    private MyFragment myFragment;
    private String updataUrl;
    private String versionContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //友盟账号统计
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this, "loginToken");
        String token = sharedPreferencesHelper.getString("token", null);
        MobclickAgent.onProfileSignIn(token);
        //创建fragment
        baseFragment = new BaseFragment();
        myFragment = new MyFragment();

        //获得Fragment管理器
        FragmentManager fragmentManager = getSupportFragmentManager();
        //使用管理器开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //添加fragment
        fragmentTransaction.add(R.id.frameLayout, baseFragment);
        //执行
        fragmentTransaction.commitAllowingStateLoss();

        rbBase.setChecked(true);
        mRadioGroup.setOnCheckedChangeListener(this);

        //获取权限
        initauthority();
        //检查版本更新
        appUpData();

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
        //获得Fragment管理器
        FragmentManager fragmentManager = getSupportFragmentManager();
        //使用管理器开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (checkId) {
            case R.id.rb_base:
                if (baseFragment.isAdded()) {
                    fragmentTransaction.show(baseFragment);
                    if (myFragment.isAdded()) {
                        fragmentTransaction.hide(myFragment);
                    }
                } else {
                    fragmentTransaction.add(R.id.frameLayout, baseFragment);
                }
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.rb_my:
                if (myFragment.isAdded()) {
                    fragmentTransaction.show(myFragment);
                    if (baseFragment.isAdded()) {
                        fragmentTransaction.hide(baseFragment);
                    }
                } else {
                    fragmentTransaction.add(R.id.frameLayout, myFragment);
                }
                fragmentTransaction.commitAllowingStateLoss();
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        //使用管理器开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //添加Fragment的时候，要传入标记
        fragmentTransaction.add(R.id.frameLayout, baseFragment).commit();
        rbBase.setChecked(true);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    //获取权限
    private void initauthority() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }

    private DownloadBuilder downloadBuilder;
    private VersionBean versionBean;
    private String versionMax;
    private String versionMin;

    private void appUpData() {
        downloadBuilder = AllenVersionChecker
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
                            updataUrl = versionBean.getObject().getUpdateUrl();

                            if (TextUtils.isEmpty(updataUrl)) {
                                versionContent = "感谢使用City!本次更新修复了一些已知Bug，并且添加了一些新的内容。";
                            } else {
                                versionContent = versionBean.getObject().getContent();
                            }


                            Log.i(TAG, "版本更新: " + "versionMax==" + versionMax
                                    + "#####  versionMin==" + versionMin
                                    + "#####  updataUrl==" + updataUrl
                                    + "#####  updataContent==" + versionContent
                                    + "#####  "+TextUtils.isEmpty(updataUrl));

                            try {
                                // 0代表相等，1代表version1大于version2，-1代表version1小于version2
                                Log.e(TAG, "版本更新大小判断（Max）: " + compareVersion(getVersionName(), versionMax));

                                switch (compareVersion(getVersionName(), versionMax)) {
                                    case 0://过不需要更新
                                        break;
                                    case 1://不会出现
                                        break;
                                    case -1://提示更新
                                        Log.e(TAG, "版本更新大小判断(Min): " + compareVersion(getVersionName(), versionMin));
                                        switch (compareVersion(getVersionName(), versionMin)) {
                                            case -1://强制更新
                                                downloadBuilder.setForceUpdateListener(new ForceUpdateListener() {
                                                    @Override
                                                    public void onShouldForceUpdate() {
                                                        forceUpdate();
                                                    }
                                                });
                                                return crateUIData();
                                            case 1://需要更新
                                                return crateUIData();
                                            case 0:
                                                return crateUIData();
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
                        Toast.makeText(MainActivity.this, "网络连接失败，请检查网络", Toast.LENGTH_SHORT).show();

                    }
                });

        downloadBuilder.setShowDownloadingDialog(false);//是否显示下载对话框
        downloadBuilder.setShowNotification(true);//是否显示通知栏
        downloadBuilder.setForceRedownload(true);//下载忽略本地缓存
//        downloadBuilder.setSilentDownload(true);//静默下载
        downloadBuilder.excuteMission(this);
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
//        Toast.makeText(this, "force update handle", Toast.LENGTH_SHORT).show();
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
        uiData.setTitle("发现新版本:" + versionMax);
        uiData.setDownloadUrl(updataUrl);
        uiData.setContent(versionContent);
        return uiData;
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
