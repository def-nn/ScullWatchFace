package com.trident.scullwatchface.activities;

import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.trident.scullwatchface.Constants;
import com.trident.scullwatchface.WatchFaceAdapter;

public class ExampleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setParameters();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setParameters() {
        this.configType = Constants.EXAMPLE;
        this.itemCount = 10;

        this.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = ((LinearLayoutManager) configRecyclerView.getLayoutManager())
                                    .findFirstCompletelyVisibleItemPosition();

                app.getWatchFaceManager().setBackground(Constants.examples[index][0]);
                app.getWatchFaceManager().setScull(Constants.examples[index][1]);
                app.getWatchFaceManager().setClockFace(Constants.examples[index][2]);


                app.getWatchFaceManager().setWatchFaceUpdated(true);

                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ExampleActivity.this.startActivity(intent);
                finishAffinity();
            }
        };
    }
}
