package com.example.edz.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.bean.SettingBean;
import com.example.edz.myapplication.utile.SharedPreferencesHelper;
import com.example.edz.myapplication.utile.Urls;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetActivity extends AppCompatActivity {

private final String TAG="SetActivity";
    @Bind(R.id.img_finish)
    FrameLayout imgFinish;
    @Bind(R.id.bt_logOut)
    Button btLogOut;
    @Bind(R.id.text_versionName)
    TextView textVersionName;
    @Bind(R.id.text_mobile_setting)
    TextView textMobileSetting;
    @Bind(R.id.text_name_setting)
    TextView textNameSetting;
    @Bind(R.id.text_idcard_setting)
    TextView textIdcardSetting;
    @Bind(R.id.text_telephone_setting)
    TextView textTelephoneSetting;

    private String token;
    private SettingBean settingBean;
    private SettingBean.ObjectBean objectBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        OkGo.<String>post(Urls.Url_Setting).tag(this)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        settingBean = gson.fromJson(response.body(), SettingBean.class);
                        if (settingBean.getCode()==10000) {
                            objectBean=settingBean.getObject();
//                            String idcard = objectBean.getIdcard();//身份证
                            String mobile = objectBean.getMobile();
//                            String name = objectBean.getName();
                            String telephone = objectBean.getTelephone();


                            textMobileSetting.setText(mobile);
//                            textNameSetting.setText(name);
//                        textIdcardSetting.setText(idcard);
                            textTelephoneSetting.setText(telephone);
                        }    else {
                            Log.e(TAG, "error: "+settingBean.getCode()+settingBean.getMsg() );
                        }
//
                    }
                });
    }

    private void initView() {
        SharedPreferences share = getSharedPreferences("loginToken", MODE_PRIVATE);
        token = share.getString("token", null);
        Log.e(TAG, "token: "+token );
        try {
            textVersionName.setText(getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    @OnClick({R.id.img_finish, R.id.bt_logOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.bt_logOut:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
