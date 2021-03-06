package com.example.edz.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.bean.BaseBean;
import com.example.edz.myapplication.global.BaseActivity;
import com.example.edz.myapplication.utile.CountDownTimerUtils;
import com.example.edz.myapplication.utile.RegexUtils;
import com.example.edz.myapplication.utile.SharedPreferencesHelper;
import com.example.edz.myapplication.utile.Urls;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册与登陆页面
 */
public class LoginActivity extends BaseActivity {

    private final String TAG = "LoginActivity";
    @Bind(R.id.text_smscode)
    TextView textsmsCode;
    @Bind(R.id.button_login)
    Button buttonLogin;
    @Bind(R.id.edit_phoneNB)
    EditText editPhoneNB;
    @Bind(R.id.edit_verification)
    EditText editVerification;
    @Bind(R.id.layout_register)
    LinearLayout layoutRegister;
    @Bind(R.id.edit_invitecode)
    EditText editInvitecode;
    @Bind(R.id.edit_nickname)
    EditText editNickname;
    @Bind(R.id.text_protocol)
    TextView textProtocol;
    @Bind(R.id.checkBox)
    CheckBox checkBox;

    private BaseBean baseBean;
    private String phoneNB;
    private String verification;
    private String url = Urls.Url_Login;
    private Gson gson;
    private int num = 1;
    private String inviteCode;
    private String nickname;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private CountDownTimerUtils mCountDownTimerUtils;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        layoutRegister.setVisibility(View.GONE);
        sharedPreferencesHelper = new SharedPreferencesHelper(LoginActivity.this, "loginToken");
    }

    //手机号码隔断
//    private final TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            if (count == 1) {
//                int length = s.toString().length();
//                if (length == 3 || length == 8) {
//                    editPhoneNB.setText(s + " ");
//                    editPhoneNB.setSelection(editPhoneNB.getText().toString().length());
//                }
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };

    @OnClick({R.id.text_smscode, R.id.button_login, R.id.text_protocol})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.text_smscode://发短信
                geteditData();
                if (RegexUtils.isMobile(phoneNB)) {
                    mCountDownTimerUtils = new CountDownTimerUtils(textsmsCode, 60000, 1000);
                    Log.e(TAG, "smscode: ");
                    OkGo.<String>post(Urls.Url_smscode).tag(this)
                            .params("mobile", phoneNB)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    gson = new Gson();
                                    baseBean = gson.fromJson(response.body(), BaseBean.class);
                                    selectionState(baseBean.getCode());
                                    Log.e(TAG, "smscode: " + baseBean.getCode());
                                }

                                @Override
                                public void onError(Response<String> response) {
                                    super.onError(response);
                                    Toast.makeText(LoginActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onError: " + response.body());
                                }
                            });
                    mCountDownTimerUtils.start();
                } else {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.button_login://登录/注册
                Log.i(TAG, "button_login_onClicked: url" + url + "@@@@@num" + num);
                if (!checkBox.isChecked()) {
                    Toast.makeText(LoginActivity.this, "请勾选服务协议后再登录", Toast.LENGTH_SHORT).show();
                } else {
                    geteditData();
                    if (url == null) {
                        Toast.makeText(LoginActivity.this, "请完成所有输入再登录", Toast.LENGTH_SHORT).show();
                    } else {
                        if (RegexUtils.strRealLength(nickname)) {
                            switch (num) {
                                case 0://注册
                                    Log.e(TAG, "phoneNB:== " + phoneNB + "&&&&&verification===" + verification
                                            + "%%%%%inviteCode==" + inviteCode + "&&&&&&&nickname==" + nickname);
                                    if (phoneNB.equals("") || verification.equals("") || inviteCode.equals("") || nickname.equals("")) {
                                        Toast.makeText(this, "请完成所有输入再登录", Toast.LENGTH_SHORT).show();
                                    } else {
                                        OkGo.<String>post(url).tag(this)
                                                .params("mobile", phoneNB)
                                                .params("smsCode", verification)
                                                .params("inviteCode", inviteCode)
                                                .params("nickname", nickname)
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(Response<String> response) {
                                                        gson = new Gson();
                                                        baseBean = gson.fromJson(response.body(), BaseBean.class);
                                                        selectionState(baseBean.getCode());
                                                    }
                                                });
                                    }

                                    break;
                                case 1://登录
                                    Log.e(TAG, "phoneNB:== " + phoneNB + "&&&&&verification===" + verification);
                                    if (phoneNB.equals("") || verification.equals("")) {
                                        Toast.makeText(this, "请完成所有输入再登录", Toast.LENGTH_SHORT).show();
                                    } else {
                                        OkGo.<String>post(url).tag(this)
                                                .params("mobile", phoneNB)
                                                .params("smsCode", verification)
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(Response<String> response) {
                                                        gson = new Gson();
                                                        baseBean = gson.fromJson(response.body(), BaseBean.class);
                                                        selectionState(baseBean.getCode());
                                                        Log.i(TAG, "登录onSuccess: " + url + "@@@@baseBean_Code" + baseBean.getCode());
                                                    }
                                                });
                                    }

                                    break;
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "昵称格式不符", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                break;

            case R.id.text_protocol:
                intent = new Intent(this, WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", Urls.Url_webProtocol);
                bundle.putString("title", "用户协议");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private void geteditData() {
        phoneNB = editPhoneNB.getText().toString();
        verification = editVerification.getText().toString();
        inviteCode = editInvitecode.getText().toString();
        nickname = editNickname.getText().toString();
//        Log.e(TAG, "phoneNB==" + phoneNB
//                + "verification==" + verification
//                + "inviteCode==" + inviteCode
//                + "nickname==" + nickname);
    }

    private void selectionState(int code) {
        switch (code) {
            case 10000:
                SharedPreferences userInfo = getSharedPreferences("loginToken", MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();//获取Editor //得到Editor后，写入需要保存的数据
//                editor.remove("token");
                editor.putString("token", baseBean.getObject().toString());
                editor.commit();//提交修改
                Log.i(TAG, "token: " + baseBean.getObject());
//                Toast.makeText(LoginActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
                break;

            case 10100://验证码注册
                Toast.makeText(LoginActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                layoutRegister.setVisibility(View.VISIBLE);
                url = Urls.Url_Register;
                num = 0;
                break;
            case 10110://验证码登录
                Toast.makeText(LoginActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                if (layoutRegister.getVisibility() == View.VISIBLE) {
                    layoutRegister.setVisibility(View.GONE);
                }
                url = Urls.Url_Login;
                num = 1;
                break;
            case 10120://验证码时间错误
//                long time=timeBean.getObject();
//                mCountDownTimerUtils = new CountDownTimerUtils(textsmsCode, time, 1000);
//                Log.e(TAG, "time: "+time);Integer.decode(baseBean.getObject())/1000
                Toast.makeText(LoginActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(LoginActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
                break;

//            case 10130://内测名额已满，敬请期待第二批名额发放！
//                Toast.makeText(LoginActivity.this, timeBean.getMsg(), Toast.LENGTH_SHORT).show();
//                break;
//            case 10200://验证码错误
//                Toast.makeText(LoginActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
//                break;
//            case 10210://已注册
//                Toast.makeText(LoginActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
//                break;
//            case 10220://注册,,昵称已存在
//                Toast.makeText(LoginActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
//                break;
//            case 10230://邀请码不存在或邀请次数已达上限
//                Toast.makeText(LoginActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
//                break;
//            case 10300://登录未注册
//                Toast.makeText(LoginActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
//                break;
//            case 10310://登录验证码错误
//                Toast.makeText(LoginActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
//                break;

        }
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