package com.techno.baihai.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.databinding.ActivityRewardPointsBinding;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class RewardPointsActivity extends AppCompatActivity {


    private Boolean isInternetPresent = false;
    private final Context mContext = this;
    private String uid;
    private String TAG = "RewardPointsActivity";
    private ImageView rewardCancelId;
    private TextView rewards_pointsID,thanks_points;
    ActivityRewardPointsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reward_points);

        PrefManager.isConnectingToInternet(this);
        isInternetPresent = PrefManager.isNetworkConnected(this);
        rewardCancelId = findViewById(R.id.reward_cancelId);
        rewards_pointsID= findViewById(R.id.rewards_pointsID);
        thanks_points= findViewById(R.id.thanks_points);
        rewardCancelId.setOnClickListener(new View.OnClickListener() {
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
            PrefManager.showSettingsAlert(mContext);

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
                                JSONObject result2 = object.optJSONObject("result2");
                                JSONObject result3 = object.optJSONObject("result3");

                                String rewardPoints = "null";
                                if (result != null) {
                                    rewardPoints = "You have "+result.optString("total_coins");
                                    if(result2 != null){
                                        rewardPoints=rewardPoints+" coins and you need "+result2.optString("min_coin")+" coins  to next award";
                                    }
                                    String text ="Your last activity that give  you points was: "+result3.optString("description");
                                    thanks_points.setText(text);
                                    rewards_pointsID.setText(rewardPoints);
                                }
                                setLog("el usuario reviso su puntaje");

                            } else {
                                progressDialog.dismiss();

                                //Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                            Log.i(TAG, "error: " + e);
                        }
                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();

                        //Toast.makeText(mContext, "Failed" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private void setLog(String message) {
        User user = PrefManager.getInstance(mContext).getUser();
        String id = null;
        if (user.getId() == "") {
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