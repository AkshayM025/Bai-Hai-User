package com.techno.baihai.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.databinding.ActivityRewardPointsBinding;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class RewardPointsActivity extends AppCompatActivity {


    private Boolean isInternetPresent = false;
    private final Context mContext = this;
    private String uid;
    private String TAG="RewardPointsActivity";
    ActivityRewardPointsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reward_points);

        PrefManager.isConnectingToInternet(this);
        isInternetPresent = PrefManager.isNetworkConnected(this);

        binding.rewardCancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

        User user = PrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getId());
        Log.e("red_ID", "-------->" + uid);


        if (isInternetPresent) {
            GetRewardPoints();
        } else {
            //PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
            /*AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
        }
        Log.i(TAG, "user_id: " + uid);
    }


    private void GetRewardPoints() {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(RewardPointsActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", uid);
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_profile?")
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("RespRewardPoints=>", "" + response);
                        progressDialog.dismiss();
                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {

                                //   https://www.shipit.ng/BaiHai/webservice/get_profile?user_id=1

                                JSONObject result = object.optJSONObject("result");
                                //String id = result.optString("id");
                                //Log.e("resultt=>", "" + response);


                                String rewardPoints = "null";
                                if (result != null) {
                                    rewardPoints = result.optString("total_coins");
                                    binding.rewardsPointsID.setText(rewardPoints);
                                }






                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                            Log.i(TAG, "error: " + e);
                            //Toast.makeText(EditAccountActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void Failed(String error) {
                        // progess.dismiss();
                        progressDialog.dismiss();

                        // CustomSnakbar.showDarkSnakabar(EditAccountActivity.this, mview, "" + error);
                        Toast.makeText(mContext, "Failed" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }

}