package com.techno.baihai.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.techno.baihai.BuildConfig;
import com.techno.baihai.R;

public class
AboutUsActivity extends AppCompatActivity {
    ProgressBar progressBar = null;
    VideoView videoView = null;
    String videoUrl = "https://bai-hai.com/uploads/video/Bai-Hai.mp4";
    TextView tv_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_about_us);

        tv_cancel = findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(v -> finish());

        videoView = findViewById(R.id.videoview);
        progressBar = findViewById(R.id.progressbar);


        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);


        Uri videoUri = Uri.parse(videoUrl);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.start();
        progressBar.setVisibility(View.VISIBLE);
        videoView.setOnPreparedListener(mp -> {
            // TODO Auto-generated method stub
            mp.start();
            mp.setOnVideoSizeChangedListener((mp1, arg1, arg2) -> {
                // TODO Auto-generated method stub
                progressBar.setVisibility(View.GONE);
                mp1.start();
            });
        });

    }


    public void faceBookLike(View view) {
        String fbPageId = "717369165113807";
        String fbPageUrl = "https://m.facebook.com/BaiHaiorg/";

        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            Intent fb = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + fbPageId));

            startActivity(fb);
        } catch (Exception e) {
            Intent fb = new Intent(Intent.ACTION_VIEW, Uri.parse(fbPageUrl));
            startActivity(fb);
        }
    }

    private void launchMarket() {
        Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + "com.facebook.katana");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private void InstaShare() {
        Uri uri = Uri.parse("https://instagram.com/bai_hai_org?igshid=1f00e9w7qaxas");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(String.valueOf(uri))));
        }
    }

    public void RateOnPlayStoreInit(View view) {
        launchMarket();
    }

    public void LegalInfo(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.legal_information));

        alertDialogBuilder
                .setMessage(getResources().getString(R.string.legal_mesagge))
                .setCancelable(false)
                .setNeutralButton(getResources().getString(R.string.legal_cancel), (dialog, which) -> {
                    //Either of the following two lines should work.
                    dialog.cancel();
                }).create().show();

    }

    private void ShareApp() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    @Override
    public void onResume() {
        super.onResume();
        videoView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoView.suspend();
    }

    public void instaShare(View view) {
        InstaShare();
    }
}
