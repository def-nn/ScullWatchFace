package com.trident.scullwatchface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Calendar;
import java.util.TimeZone;

public class TridentWatchFaceService extends CanvasWatchFaceService {
    private WatchFaceManager watchFaceManager;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine {
        private static final int MSG_UPDATE_TIME = 0;
        private static final int INTERACTIVE_UPDATE_RATE_MS = 60000;

        private static final float ARROW_LENGTH = 241f;
        private static final float TAIL_LENGTH = 23.5f;

        private static final float SCALE_CONST = 1.44f;

        private boolean registeredTimeZoneReceiver, inAmbientMode;

        Calendar calendar;

        Paint paint;
        Bitmap bgBitmap, scullBitmap, clockBitmap,
               bgBitmapScaled, scullBitmapScaled, clockBitmapScaled,
               bgBitmapAmbientScaled, scullBitmapAmbientScaled,
               arrowHour, arrowMin, arrowHourScaled, arrowMinScaled;

        final Handler updateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case MSG_UPDATE_TIME:
                        invalidate();
                        if (shouldTimerBeRunning()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs = INTERACTIVE_UPDATE_RATE_MS
                                    - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                            updateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                        }
                        break;
                }
            }
        };

        final BroadcastReceiver timeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                calendar.setTimeZone(TimeZone.getDefault());
                invalidate();
            }
        };

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            setWatchFaceStyle(new WatchFaceStyle.Builder(TridentWatchFaceService.this).build());

            watchFaceManager = ((ConstructorApp) getApplication()).getWatchFaceManager();
            setWatchFace();

            Drawable hourDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_h);
            Drawable minDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_m);

            arrowHour = ((BitmapDrawable) hourDrawable).getBitmap();
            arrowMin = ((BitmapDrawable) minDrawable).getBitmap();

            calendar = Calendar.getInstance();

            paint = new Paint();
            paint.setAntiAlias(true);
        }

        private void setWatchFace() {
            Resources resources = TridentWatchFaceService.this.getResources();

            Drawable bgDrawable = ContextCompat.getDrawable(
                    getApplicationContext(), resources.getIdentifier(
                            Constants.BG_PATH + watchFaceManager.getBackground(),
                            "drawable",
                            getApplicationContext().getPackageName()
                    ));
            Drawable scullDrawable = ContextCompat.getDrawable(
                    getApplicationContext(), resources.getIdentifier(
                            Constants.SCULL_PATH + watchFaceManager.getScull(),
                            "drawable",
                            getApplicationContext().getPackageName()
                    ));
            Drawable clockDrawable = ContextCompat.getDrawable(
                    getApplicationContext(), resources.getIdentifier(
                            Constants.CLOCK_FACE_PATH + watchFaceManager.getClockFace(),
                            "drawable",
                            getApplicationContext().getPackageName()
                    ));

            bgBitmap = ((BitmapDrawable) bgDrawable).getBitmap();
            scullBitmap = ((BitmapDrawable) scullDrawable).getBitmap();
            clockBitmap = ((BitmapDrawable) clockDrawable).getBitmap();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            Log.d("myLogs", "ambient mode");
            super.onAmbientModeChanged(inAmbientMode);

            this.inAmbientMode = inAmbientMode;

            invalidate();
            updateTimer();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            calendar.setTimeInMillis(System.currentTimeMillis());

            int width = bounds.width();
            int height = bounds.height();

            final float TWO_PI = (float) Math.PI * 2f;

            if (watchFaceManager.isWatchFaceUpdated()) {
                setWatchFace();
                bgBitmapScaled = Bitmap.createScaledBitmap(bgBitmap, width, height, true);
                scullBitmapScaled = Bitmap.createScaledBitmap(scullBitmap, width, height, true);
                clockBitmapScaled = Bitmap.createScaledBitmap(clockBitmap, width, height, true);

                watchFaceManager.setWatchFaceUpdated(false);
            }

            if (!inAmbientMode) {
                canvas.drawBitmap(bgBitmapScaled, 0, 0, null);
                canvas.drawBitmap(clockBitmapScaled, 0, 0, null);
            } else
                canvas.drawBitmap(bgBitmapAmbientScaled, 0, 0, null);

            canvas.drawBitmap(scullBitmapScaled, 0, 0, null);

            float centerX = width / 2f;
            float centerY = height / 2f;

            // Compute rotations and lengths for the clock hands.
            float minutes = calendar.get(Calendar.MINUTE);
            float minRot = minutes / 60f * TWO_PI * - 1 + (float) Math.PI / 2;
            float hours = calendar.get(Calendar.HOUR) + minutes / 60f;
            float hourRot = hours / 12f * TWO_PI * - 1 + (float) Math.PI / 2;

            float length = Math.min(width, height) * 0.5f;
            float tail_length = length * TAIL_LENGTH / ARROW_LENGTH;

            int x1 = (int) (centerX - tail_length * Math.cos(hourRot));
            int y1 = (int) (centerY + tail_length * Math.sin(hourRot));

            int x2 = (int) (centerX - tail_length * Math.cos(minRot));
            int y2 = (int) (centerY + tail_length * Math.sin(minRot));

            Matrix matrix = new Matrix();
            matrix.postRotate(hours / 12f * 360 + 225, 0, 0);
            matrix.postTranslate(x1, y1);
            canvas.drawBitmap(arrowHourScaled, matrix, paint);

            matrix = new Matrix();
            matrix.postRotate(minutes / 60f * 360 + 225, 0, 0);
            matrix.postTranslate(x2, y2);
            canvas.drawBitmap(arrowMinScaled, matrix, paint);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (bgBitmapScaled == null
                    || bgBitmapScaled.getWidth() != width
                    || bgBitmapScaled.getHeight() != height) {
                bgBitmapScaled = Bitmap.createScaledBitmap(bgBitmap, width, height, true);
                scullBitmapScaled = Bitmap.createScaledBitmap(scullBitmap, width, height, true);
                clockBitmapScaled = Bitmap.createScaledBitmap(clockBitmap, width, height, true);

                bgBitmapAmbientScaled = Bitmap.createScaledBitmap(
                        ((BitmapDrawable) ContextCompat.getDrawable(
                                getApplicationContext(),
                                R.drawable.bg_0)
                        ).getBitmap(),
                        width, height, true);

                int scale = (int) (Math.min(width, height) / 2 /  SCALE_CONST);
                arrowMinScaled = Bitmap.createScaledBitmap(arrowMin, scale, scale, true);
                arrowHourScaled = Bitmap.createScaledBitmap(arrowHour, scale, scale, true);
            }
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();
                calendar.setTimeZone(TimeZone.getDefault());
            } else {
                unregisterReceiver();
            }

            updateTimer();
        }

        private void updateTimer() {
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        private void registerReceiver() {
            if (registeredTimeZoneReceiver) return;

            registeredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            TridentWatchFaceService.this.registerReceiver(timeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!registeredTimeZoneReceiver) return;

            registeredTimeZoneReceiver = false;
            TridentWatchFaceService.this.unregisterReceiver(timeZoneReceiver);
        }
    }
}
