package com.trident.scullwatchface.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.trident.scullwatchface.Constants;

public class ScullActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setParameters();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setParameters() {
        this.configType = Constants.SCULL;
        this.itemCount = 10;
        this.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.getWatchFaceManager().setScull(
                        ((LinearLayoutManager) configRecyclerView.getLayoutManager())
                                .findFirstCompletelyVisibleItemPosition()
                );

                Intent intent = new Intent(ScullActivity.this, ClockFaceActivity.class);
                startActivity(intent);
            }
        };
    }
}
