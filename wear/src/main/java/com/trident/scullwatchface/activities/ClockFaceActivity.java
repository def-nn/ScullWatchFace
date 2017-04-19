package com.trident.scullwatchface.activities;

import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.trident.scullwatchface.Constants;

public class ClockFaceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setParameters();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setParameters() {
        this.configType = Constants.CLOCK_FACE;
        this.itemCount = 12;

        this.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.getWatchFaceManager().setClockFace(
                        ((LinearLayoutManager) configRecyclerView.getLayoutManager())
                                .findFirstCompletelyVisibleItemPosition()
                );
                app.getWatchFaceManager().setWatchFaceUpdated(true);

                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ClockFaceActivity.this.startActivity(intent);
                finishAffinity();
            }
        };
    }
}
