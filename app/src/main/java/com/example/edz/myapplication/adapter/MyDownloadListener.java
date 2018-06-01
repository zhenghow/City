package com.example.edz.myapplication.adapter;

import com.example.edz.myapplication.activity.GameLoadActivity;
import com.example.edz.myapplication.utile.ApkUtils;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.List;

/**
 * Created by EDZ on 2018/5/8.
 * 游戏下载监听
 */

public class MyDownloadListener extends DownloadListener {
    private GameLoadActivity context;
    private int type;
    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;
    private List<DownloadTask> values;


    public MyDownloadListener(Object tag, GameLoadActivity context) {
        super(tag);
        this.context=context;
    }

    @Override
    public void onStart(Progress progress) {

    }

    @Override
    public void onProgress(Progress progress) {
        context.refresh(progress);

    }

    @Override
    public void onError(Progress progress) {

    }

    @Override
    public void onFinish(File file, Progress progress) {
//        Toast.makeText(context, "下载完成:" + progress.filePath, Toast.LENGTH_SHORT).show();
//        Log.i("MyDownloadListener", "filePath: "+progress.filePath);
        updateData(type);
        if (ApkUtils.isAvailable(context, new File(progress.filePath))) {
            ApkUtils.uninstall(context, ApkUtils.getPackageName(context, progress.filePath));
        } else {
            ApkUtils.install(context, new File(progress.filePath));
        }
    }

    @Override
    public void onRemove(Progress progress) {

    }

    public void updateData(int type) {
        //这里是将数据库的数据恢复
        this.type = type;
        if (type == TYPE_ALL) values = OkDownload.restore(DownloadManager.getInstance().getAll());
        if (type == TYPE_FINISH) values = OkDownload.restore(DownloadManager.getInstance().getFinished());
        if (type == TYPE_ING) values = OkDownload.restore(DownloadManager.getInstance().getDownloading());

    }


}
