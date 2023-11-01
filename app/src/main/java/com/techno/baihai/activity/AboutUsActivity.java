package com.techno.baihai.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class
AboutUsActivity extends AppCompatActivity {
    ProgressBar progressBar = null;
    VideoView videoView = null;
    String videoUrl = "https://bai-hai.com/uploads/video/Bai-Hai.mp4";
    TextView tv_cancel;
    private final Context mContext = this;

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
        setLog("Se envio a facebook  desde menu de acerca de nosotros");
        String fbPageId = "-102707731998257";
        String fbPageUrl = "https://m.facebook.com/byehiapp/";

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
        Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + "com.techno.baihai");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
           // Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private void InstaShare() {
        setLog("Se envio a cuenta instragram  desde menu de acerca de nosotros");
        Uri uri = Uri.parse("https://instagram.com/bye_hi_app?igshid=1f00e9w7qaxas");
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
        setLog("Se envio al play store desde menu de acerca de nosotros");
        launchMarket();
    }

    public void LegalInfo(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.legal_information));
        setLog("Se notifica informacion legal desde menu de acerca de nosotros");
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.legal_mesagge))
                .setCancelable(false)
                .setNeutralButton(getResources().getString(R.string.legal_cancel), (dialog, which) -> {
                    //Either of the following two lines should work.
                    dialog.cancel();
                }).create().show();

    }

    public void ShareApp() {
        setLog("Envia a Compartir aplicacion  desde menu de acerca de nosotros");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + getPackageName());
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

    private void setLog(String message) {
        User user = PrefManager.getInstance(mContext).getUser();
        String id ;
        if (user.getId().equals("")) {
            id = "1";
        } else {
            id = user.getId();
        }

        HashMap<String, String> parms1 = new HashMap<>();
        parms1.put("user_id", id);
        parms1.put("activity", message);
        ApiCallBuilder.build(mContext)
                .setUrl(Constant.BASE_URL + Constant.LOG_APP)
                .setParam(parms1)
                .execute(new ApiCallBuilder.onResponse() {
                    public void Success(String response) {
                        try {
                            Log.e("selectedresponse=>", "-------->" + response);
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            if(status.equals("true")){
                                Log.e("selectedresponse=>", "-------->exitoso" );
                            }


                        } catch (JSONException e) {


                            //Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    public void Failed(String error) {
                    }
                });
    }
}
