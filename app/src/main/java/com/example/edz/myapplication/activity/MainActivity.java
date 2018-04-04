package com.example.edz.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.example.edz.myapplication.R;
import com.example.edz.myapplication.bean.VersionBean;
import com.example.edz.myapplication.fragment.BaseFragment;
import com.example.edz.myapplication.fragment.MyFragment;
import com.example.edz.myapplication.utile.Urls;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.edz.myapplication.utile.JsHelper.context;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.rb_base)
    RadioButton rbBase;
    @Bind(R.id.rb_my)
    RadioButton rbMy;
    @Bind(R.id.mRadioGroup)
    RadioGroup mRadioGroup;

    private String TAG = "MainActivity";
    private BaseFragment baseFragment;
    private MyFragment myFragment;
    // fragment对象的标记
    public static final String fragmentTag1 = "fragment1";
    public static final String fragmentTag2 = "fragment2";
    private Fragment blankFragment1;
    private Fragment blankFragment2;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Intent intent;
    private String userId;
    private VersionBean versionBean;
    private String versionMax;
    private String versionMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        appUpData();
//        Confirmlogin();

//        firstStart();

        init();
    }

    private void init() {
        initView();
        mRadioGroup.check(R.id.rb_base);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    private void appUpData() {
        OkGo.<String>post(Urls.Url_Version).tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        versionBean = gson.fromJson(response.body(), VersionBean.class);
                        if (versionBean.getCode() == 10000) {
                            versionMax = versionBean.getObject().getVersionMax();
                            versionMin = versionBean.getObject().getVersionMin();
                        }

                    }
                });

        try {
            // 0代表相等，1代表version1大于version2，-1代表version1小于version2
            switch (compareVersion(getVersionName(), versionMax)) {
                case 0://过
                    Toast.makeText(this, "不需要更新", Toast.LENGTH_SHORT).show();
                    break;
                case 1://不会出现
                    break;
                case -1://提示更新
                    switch (compareVersion(getVersionName(), versionMin)) {
                        case 0://强制更新
                            Toast.makeText(this, "强制更新", Toast.LENGTH_SHORT).show();
                            break;
                        case 1://需要更新
//                                    AllenVersionChecker
//                                            .getInstance()
//                                            .downloadOnly(
//                                                    UIData.create().setDownloadUrl(downloadUrl)
//                                            )
//                                            .excuteMission(context);
                            Toast.makeText(this, "需要更新", Toast.LENGTH_SHORT).show();
                            break;
                        case -1://不会出现
                            break;
                    }

                    break;

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void firstStart() {
        LoadData();
        if (userId == null) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void LoadData() {
        //指定操作的文件名称
        SharedPreferences share = getSharedPreferences("loginToken", MODE_PRIVATE);
        userId = share.getString("token", null);
        Log.e(TAG, "token: " + userId);
    }

    private void initView() {

        baseFragment = new BaseFragment();
        myFragment = new MyFragment(MainActivity.this);

        //获得Fragment管理器
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        //使用管理器开启事务
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        //添加Fragment的时候，要传入标记
        fragmentTransaction1.add(R.id.frameLayout, baseFragment, fragmentTag1).commit();

        //获得Fragment管理器
        FragmentManager fragmentManager2 = getSupportFragmentManager();
        //使用管理器开启事务
        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
        //添加Fragment的时候，要传入标记
        fragmentTransaction2.add(R.id.frameLayout, myFragment, fragmentTag2);
        fragmentTransaction2.hide(myFragment);
        fragmentTransaction2.commit();


//        //定义底部标签图片大小
//        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher_round);
//        drawable.setBounds(0, 0, 50,50);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
//        rbBase.setCompoundDrawables(null, drawable, null, null);//只放上面


    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
//      用对应的标记在内存中找对应的Fragment对象
        blankFragment1 = fragmentManager.findFragmentByTag(fragmentTag1);
        blankFragment2 = fragmentManager.findFragmentByTag(fragmentTag2);
        switch (checkId) {
            case R.id.rb_base:
                fragmentTransaction.show(blankFragment1);
                fragmentTransaction.hide(blankFragment2);
                fragmentTransaction.commit();
                break;
            case R.id.rb_my:
                fragmentTransaction.hide(blankFragment1);
                fragmentTransaction.show(blankFragment2);
                fragmentTransaction.commit();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        LoadData();
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

    /*
     * 获取当前程序的版本名
     */
    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        Log.e("TAG", "版本号" + packInfo.versionCode);
        Log.e("TAG", "版本名" + packInfo.versionName);

        return packInfo.versionName;
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

}
