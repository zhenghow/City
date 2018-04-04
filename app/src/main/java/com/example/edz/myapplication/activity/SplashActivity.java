package com.example.edz.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.utile.BitmapHelper;
import com.example.edz.myapplication.utile.SharedPreferencesHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    @Bind(R.id.text_time_welcome)
    TextView textTimeWelcome;
    @Bind(R.id.img_welcome)
    ImageView imgWelcome;

    private final String TAG = "SplashActivity";
    private Intent intent;
    private int time = 3;
    private String userId;
    private Thread thread;
    private boolean boo=false;


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

        initView();


    }

    private void initView() {
        SharedPreferencesHelper sharedPreferencesHelper=new SharedPreferencesHelper(this,"loginToken");
        String token= sharedPreferencesHelper.getString("token" ,null);
        Log.e(TAG, "token: "+token );
        if ( boo ){
            BitmapHelper.displayImage(this, imgWelcome, "http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg");
            initData();
        }else {
            if (token==null){
                intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

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


}
