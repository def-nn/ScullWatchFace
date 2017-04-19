package com.trident.scullwatchface.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.wearable.view.WatchViewStub;
import android.view.View;

import com.trident.scullwatchface.ConstructorApp;
import com.trident.scullwatchface.R;
import com.trident.scullwatchface.WatchFaceAdapter;

public abstract class BaseActivity extends Activity {
    protected int configType, itemCount;
    protected View.OnClickListener onClickListener;

    protected RecyclerView configRecyclerView;
    protected ConstructorApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        app = (ConstructorApp) getApplication();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                configRecyclerView = (RecyclerView) findViewById(R.id.configRecyclerView);
                configRecyclerView.setLayoutManager(new LinearLayoutManager(
                        getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                configRecyclerView.setAdapter(new WatchFaceAdapter(configType, itemCount, app, onClickListener));

                SnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(configRecyclerView);
            }
        });
    }

    protected abstract void setParameters();
}
