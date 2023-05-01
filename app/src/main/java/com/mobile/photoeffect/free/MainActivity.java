package com.mobile.photoeffect.free;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.material.snackbar.Snackbar;
import com.mobile.photoeffect.free.databinding.ActivityMainBinding;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSIONS = 111;
    private static final int CAMERA_PERMISSIONS = 222;

    public static Uri imgUri;
    private String[] mime;
    private InterstitialAdImpl interstitialAd;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mime = new String[]{"image/png", "image/jpg", "image/jpeg"};
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
            if (isCameraGranted()) {
                ImagePicker.with(MainActivity.this)
                        .cameraOnly()
                        .start();
            } else {
                askPermission();
                showSnack(getString(R.string.snackbar_text), getString(R.string.snackbar_action));
            }
        });

        binding.btnCameraRoll.setOnClickListener(view -> {
            if (isStorageGranted()) {
                ImagePicker.with(MainActivity.this)
                        .galleryOnly()
                        .galleryMimeTypes(mime)
                        .start();
            } else {
                askPermission();
                showSnack(getString(R.string.snackbar_storage_text), getString(R.string.snackbar_action));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            imgUri = data.getData();
            if (!imgUri.equals("")) {
                interstitialAd.showAds(this);
                startActivity(new Intent(MainActivity.this, FinalActivity.class));
            }
        } catch (Exception e) {

        }
    }

    private void askPermission() {
        if (!isCameraGranted()) {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.snackbar_text),
                    CAMERA_PERMISSIONS,
                    Manifest.permission.CAMERA);
        }

        if (!isStorageGranted()) {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.snackbar_storage_text),
                    STORAGE_PERMISSIONS,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }

    }

    private boolean isCameraGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private boolean isStorageGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private void showSnack(String text1, String text2) {
        Snackbar.make(
                        binding.btnTakePhoto, text1, Snackbar.LENGTH_LONG)
                .setAction(text2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                        startActivity(intent);
                    }
                }).show();
    }
}