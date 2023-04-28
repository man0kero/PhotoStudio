package com.mobile.photoeffect.photostudio.free;

import static com.mobile.photoeffect.photostudio.free.MainActivity.imgUri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.google.android.gms.ads.AdRequest;
import com.mobile.photoeffect.photostudio.free.databinding.ActivityFinalBinding;

public class FinalActivity extends AppCompatActivity {
    private InterstitialAdImpl interstitialAd;
    private ActivityFinalBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_final);

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.finalAd.loadAd(adRequest);
        binding.recAd.loadAd(adRequest);
        interstitialAd = new InterstitialAdImpl();
        interstitialAd.loadInterstitialAd(this);

        Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
        dsPhotoEditorIntent.setData(imgUri);

        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, R.string.app_name);

        startActivityForResult(dsPhotoEditorIntent, 200);

        binding.btnBack.setOnClickListener(view -> {
            interstitialAd.showAds(this);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    interstitialAd.showAds(this);
                    binding.recAd.setVisibility(View.INVISIBLE);
                    Uri outputUri = data.getData();
                    binding.text.setText(getApplicationContext().getText(R.string.image_was_saved));
                    Toast.makeText(this, R.string.toast, Toast.LENGTH_LONG).show();
                    Glide.with(this)
                            .load(outputUri)
                            .override(binding.imageView.getWidth(), binding.imageView.getHeight())
                            .into(binding.imageView);
            }
        }
    }
}