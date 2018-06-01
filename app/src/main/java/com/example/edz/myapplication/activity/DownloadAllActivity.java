package com.example.edz.myapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.edz.myapplication.R;
import com.example.edz.myapplication.adapter.DownloadAdapter;
import com.example.edz.myapplication.global.BaseActivity;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.task.XExecutor;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 游戏下载，下载管理页面
 *
 */
public class DownloadAllActivity extends BaseActivity implements XExecutor.OnAllTaskEndListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    private DownloadAdapter adapter;
    private OkDownload okDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_all);
        ButterKnife.bind(this);


        okDownload = OkDownload.getInstance();
        adapter = new DownloadAdapter(this);
        adapter.updateData(DownloadAdapter.TYPE_ALL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        okDownload.addOnAllTaskEndListener(this);
    }

    @Override
    public void onAllTaskEnd() {
        showToast("所有下载任务已结束");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        okDownload.removeOnAllTaskEndListener(this);
        adapter.unRegister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.removeAll)
    public void removeAll(View view) {
        okDownload.removeAll();
        adapter.updateData(DownloadAdapter.TYPE_ALL);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.pauseAll)
    public void pauseAll(View view) {
        okDownload.pauseAll();
    }

    @OnClick(R.id.startAll)
    public void startAll(View view) {
        okDownload.startAll();
    }
}
