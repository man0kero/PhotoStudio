package com.mobile.photoeffect.free;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingAdActivity extends AppCompatActivity {
    private static final long COUNTER_TIME = 2;
    private long secondsRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_ad);
        createTimer(COUNTER_TIME);
    }

    private void createTimer(long seconds) {
        CountDownTimer countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUnitFinished) {
                secondsRemaining = ((millisUnitFinished / 1000) + 1);
            }

            @Override
            public void onFinish() {
                secondsRemaining = 0;
                startMainActivity();
            }
        };
        countDownTimer.start();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}