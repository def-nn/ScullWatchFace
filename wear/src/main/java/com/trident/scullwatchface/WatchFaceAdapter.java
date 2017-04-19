package com.trident.scullwatchface;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class WatchFaceAdapter extends RecyclerView.Adapter<WatchFaceAdapter.ViewHolder>{
    private int config_type;
    private int itemCount;
    private int bgId, scullId;
    private ConstructorApp app;
    private View.OnClickListener onClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bgWatchFace, scullWatchFace, clockWatchFace;
        FrameLayout rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            bgWatchFace = (ImageView) itemView.findViewById(R.id.watch_face_bg);
            scullWatchFace = (ImageView) itemView.findViewById(R.id.watch_face_scull);
            clockWatchFace = (ImageView) itemView.findViewById(R.id.watch_face_clock);
            rootView = (FrameLayout) itemView.findViewById(R.id.item_container);
        }
    }

    public WatchFaceAdapter(int config_type, int itemCount, ConstructorApp app,
                            View.OnClickListener onClickListener) {
        this.config_type = config_type;
        this.itemCount = itemCount;
        this.app = app;
        this.onClickListener = onClickListener;

        switch (config_type) {
            case Constants.CLOCK_FACE:
                scullId = app.getResources().getIdentifier(
                        Constants.SCULL_PATH + app.getWatchFaceManager().getScull(),
                        "drawable",
                        app.getPackageName());
            case Constants.SCULL:
                bgId = app.getResources().getIdentifier(
                        Constants.BG_PATH + app.getWatchFaceManager().getBackground(),
                        "drawable",
                        app.getPackageName());
        }
    }

    @Override
    public WatchFaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.constructor_item, parent, false);
        return  new WatchFaceAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(WatchFaceAdapter.ViewHolder holder, int position) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                (int) (app.getDisplayWidth() * 0.7),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        if (position == 0) {
            layoutParams.leftMargin = (int) (app.getDisplayWidth() * 0.125);
            layoutParams.rightMargin = (int) (app.getDisplayWidth() * 0.05);
        } else if (position == itemCount - 1)
            layoutParams.rightMargin = (int) (app.getDisplayWidth() * 0.175);
        else
            layoutParams.rightMargin = (int) (app.getDisplayWidth() * 0.05);

        holder.rootView.setPadding((int) (app.getDisplayWidth() * 0.05), 0, 0, 0);
        holder.rootView.setLayoutParams(layoutParams);
        holder.rootView.setOnClickListener(onClickListener);

        int id;
        switch (config_type) {
            case Constants.BACKGROUND:
                Glide.with(app)
                        .load(Constants.bg_id[position])
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(Constants.bg_id[position])
                        .into(holder.bgWatchFace);
                break;
            case Constants.SCULL:
                Glide.with(app)
                        .load(bgId)
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(bgId)
                        .into(holder.bgWatchFace);
                Glide.with(app)
                        .load(Constants.scull_id[position])
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(Constants.scull_id[position])
                        .into(holder.scullWatchFace);
                break;
            case Constants.CLOCK_FACE:
                Glide.with(app)
                        .load(bgId)
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(bgId)
                        .into(holder.bgWatchFace);
                Glide.with(app)
                        .load(scullId)
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(scullId)
                        .into(holder.scullWatchFace);
                Glide.with(app)
                        .load(Constants.cf_id[position])
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(Constants.cf_id[position])
                        .into(holder.clockWatchFace);
                break;
            case Constants.EXAMPLE:
                int bgId = Constants.bg_id[Constants.examples[position][0]];
                int scullId = Constants.scull_id[Constants.examples[position][1]];
                int clockFaceId = Constants.cf_id[Constants.examples[position][2]];
                Glide.with(app)
                        .load(bgId)
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(bgId)
                        .into(holder.bgWatchFace);
                Glide.with(app)
                        .load(scullId)
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(scullId)
                        .into(holder.scullWatchFace);
                Glide.with(app)
                        .load(clockFaceId)
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(clockFaceId)
                        .into(holder.clockWatchFace);
        }
    }

    @Override
    public int getItemCount() {
        return this.itemCount;
    }
}
