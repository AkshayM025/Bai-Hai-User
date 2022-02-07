package com.techno.baihai.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.CustomSnakbar;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class RatingActivity extends AppCompatActivity {

    private static final String TAG = "RatingActivity";
    String uid, image, pathOfImg;
    String latitude, longitude, product_status;
    EditText et_productDesc, et_productName;
    private final Context mContext = this;
    private Boolean isInternetPresent = false;


    private CardView Id_card_submit;
    private RatingBar Id_rating;
    private EditText et_feedbackId;
    private String provider_id;
    private String request_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_rating);

        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);


        User user = PrefManager.getInstance(mContext).getUser();

        uid = String.valueOf(user.getId());
        Log.i(TAG, "user_id: " + uid);


        getCurrentLocation();


        Id_rating = findViewById(R.id.Id_rating);
        et_feedbackId = findViewById(R.id.et_feedbackId);
        Id_card_submit = findViewById(R.id.Id_card_submit);


        //provider_id = getIntent().getStringExtra("driver_id");
        request_id = getIntent().getStringExtra("request_id");


        Id_card_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetPresent) {
                    RatingValidateApi(view);
                } else {
                    PrefManager prefManager = new PrefManager(mContext);
                    PrefManager.showSettingsAlert(mContext);
                }

            }
        });

    }


    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(mContext);
        if (track.canGetLocation()) {
            latitude = String.valueOf(track.getLatitude());
            Log.e("lat=>", "-------->" + latitude);

            longitude = String.valueOf(track.getLongitude());
            Log.e("lon=>", "-------->" + longitude);

        } else {
            track.showSettingsAlert();
        }
    }


    private void RatingValidateApi(View view) {

        String user_rating = String.valueOf(Id_rating.getRating());
        String user_feedback = et_feedbackId.getText().toString().trim();


        if (Id_rating.getRating() == 0.0) {
            CustomSnakbar.showDarkSnakabar(mContext, view, "You not giving a rating!!");
            Id_rating.requestFocus();
        } else if (user_feedback.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, view, "Please give a feedback!");
            et_feedbackId.requestFocus();
        } else {

            UserFeedbackApi(user_rating, user_feedback, view);

        }
    }

    private void UserFeedbackApi(final String user_rating, String user_feedback, final View view) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", uid);
        parms.put("provider_id", "60");
        parms.put("request_id", request_id);
        parms.put("rating", user_rating);
        parms.put("review", user_feedback);
/*
        HashMap<String, File> fileHashMap = new HashMap<String,File>();
        fileHashMap.put("image1",file);
*/
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "add_rating?")
                .setParam(parms)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        progressDialog.dismiss();
                        Log.e("ResponsRating", "" + response);

                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {


                                startActivity(new Intent(mContext, HomeActivity.class));
                                Animatoo.animateInAndOut(mContext);


                            } else {
                                progressDialog.dismiss();

                                CustomSnakbar.showDarkSnakabar(mContext, view, "" + message);
                            }

                        } catch (JSONException e) {

                            progressDialog.dismiss();
                            // Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public void OnBackRating(View view) {
        onBackPressed();

    }
}