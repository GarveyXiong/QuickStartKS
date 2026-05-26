package com.kozen.quickstartks;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private boolean isActionBarInitialized = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isActionBarInitialized) {
            setCustomActionBar();
            isActionBarInitialized = true;
        }
    }

    private void setCustomActionBar() {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(mActionBarView, lp);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @SuppressLint("NewApi")
    public void playSound(int rawResId) {
        android.media.SoundPool.Builder builder = new android.media.SoundPool.Builder();
        builder.setMaxStreams(10);
        builder.setAudioAttributes(new android.media.AudioAttributes.Builder()
                .setUsage(android.media.AudioAttributes.USAGE_GAME)
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build());
        android.media.SoundPool soundPool = builder.build();

        final int soundId = soundPool.load(this, rawResId, 0);

        soundPool.setOnLoadCompleteListener(new android.media.SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(android.media.SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        });
    }
}