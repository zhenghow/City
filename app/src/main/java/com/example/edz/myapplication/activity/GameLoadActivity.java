package com.example.edz.myapplication.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.adapter.MyDownloadListener;
import com.example.edz.myapplication.bean.ApkModel;
import com.example.edz.myapplication.global.BaseActivity;
import com.example.edz.myapplication.utile.ApkUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 游戏详情页面，包含webView
 */
public class GameLoadActivity extends BaseActivity {
    private static final int REQUEST_PERMISSION_STORAGE = 0x01;
    @Bind(R.id.comment_layout)
    LinearLayout commentLayout;
    private Context context;
    private DownloadTask task;
    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Bind(R.id.text_progress)
    TextView textProgress;
    @Bind(R.id.frameLayout_progress)
    FrameLayout frameLayoutProgress;
    @Bind(R.id.loadAll_layout)
    LinearLayout loadAllLayout;
    private Progress progress;
    private ApkModel apk;
    private NumberFormat numberFormat;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_load);
        ButterKnife.bind(this);
        OkDownload.getInstance().setFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cityChain/");
        OkDownload.getInstance().getThreadPool().setCorePoolSize(3);
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        /** 检查SD卡权限 */
        checkSDCardPermission();

        apk = new ApkModel();
        apk.name = "爱奇艺";
        apk.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0c10c4c0155c9adf1282af008ed329378d54112ac";
        apk.url = "http://121.29.10.1/f5.market.mi-img.com/download/AppStore/0b8b552a1df0a8bc417a5afae3a26b2fb1342a909/com.qiyi.video.apk";


    }

    /**
     * 检查SD卡权限
     */
    protected void checkSDCardPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }
    }

    /**
     * 检查SD卡权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取权限
            } else {
                showToast("权限被禁止，无法下载文件！");
            }
        }
    }


    @OnClick({R.id.frameLayout_progress, R.id.loadAll_layout, R.id.comment_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.frameLayout_progress:

                GetRequest<File> request = OkGo.<File>get(apk.url);

                //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
                DownloadTask task = OkDownload.request(apk.url, request)
                        .extra1(apk)//
                        .save()
                        .register(new MyDownloadListener(apk.url, this));
                task.start();

                progress = task.progress;

                switch (progress.status) {
                    case Progress.PAUSE:
                    case Progress.ERROR:
                        task.start();
                        break;
                    case Progress.LOADING:
                        task.pause();
                        break;
                    case Progress.FINISH:
                        if (ApkUtils.isAvailable(this, new File(progress.filePath))) {
                            ApkUtils.uninstall(this, ApkUtils.getPackageName(context, progress.filePath));
                        } else {
                            ApkUtils.install(this, new File(progress.filePath));
                        }
                        break;
                }

                mhandler.sendEmptyMessage(0);
                break;

            case R.id.loadAll_layout://下载管理
                intent = new Intent(GameLoadActivity.this, DownloadAllActivity.class);
                startActivity(intent);
                break;

            case R.id.comment_layout://点评
                intent = new Intent(GameLoadActivity.this, CommentActivity.class);
                startActivity(intent);
                break;

        }

    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                refresh(progress);
            }
        }
    };

    public void refresh(Progress progress) {

        switch (progress.status) {
            case Progress.NONE://初始化后，开始下载之前
                textProgress.setText("下载");
                break;
            case Progress.PAUSE://下载暂停
                textProgress.setText("继续");
                break;
            case Progress.ERROR://出错
                textProgress.setText("出错");
                break;
            case Progress.WAITING://等待连接
                textProgress.setText(numberFormat.format(progress.fraction));
                break;
            case Progress.FINISH://下载完成
                textProgress.setText("安装");
                break;
            case Progress.LOADING://下载中
                textProgress.setText(numberFormat.format(progress.fraction));
                break;
        }

        //下载进度条
        progressBar.setMax(10000);
        progressBar.setProgress((int) (progress.fraction * 10000));

    }


}
