package com.trident.scullwatchface.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.trident.scullwatchface.Constants;

public class BgActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setParameters();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setParameters() {
        this.configType = Constants.BACKGROUND;
        this.itemCount = 20;
        this.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.getWatchFaceManager().setBackground(
                        ((LinearLayoutManager) configRecyclerView.getLayoutManager())
                                .findFirstCompletelyVisibleItemPosition()
                );

                Intent intent = new Intent(BgActivity.this, ScullActivity.class);
                startActivity(intent);
            }
        };
    }
}
