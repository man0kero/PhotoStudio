package com.mobile.photoeffect.photostudio.free;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.ads.AdRequest;
import com.mobile.photoeffect.photostudio.free.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private InterstitialAdImpl interstitialAd;
    private ActivityMainBinding binding;
    public static Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Application application = getApplication();
        ((AdOpen) application).showAdIfAvailable(MainActivity.this,
                new AdOpen.OnShowAdCompleteListener() {
                    @Override
                    public void onShowAdComplete() {
                        Log.d(TAG, "onShowAdComplete: ");
                    }
                });

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.mainAd.loadAd(adRequest);
        interstitialAd = new InterstitialAdImpl();
        interstitialAd.loadInterstitialAd(this);

        binding.btnTakePhoto.setOnClickListener(view -> {
            ImagePicker.with(MainActivity.this)
                    .cameraOnly()
                    .start();
        });

        binding.btnCameraRoll.setOnClickListener(view -> {
            ImagePicker.with(MainActivity.this)
                    .galleryOnly()
                    .start();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        binding.load.setVisibility(View.VISIBLE);

        try {
            imgUri = data.getData();
            if(!imgUri.equals("")) {
                interstitialAd.showAds(this);
                startActivity(new Intent(MainActivity.this, FinalActivity.class));
            }
        } catch (Exception e){

        }
    }
}