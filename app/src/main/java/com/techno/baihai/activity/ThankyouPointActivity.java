package com.techno.baihai.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class ThankyouPointActivity extends AppCompatActivity {
    private TextView textInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_thankyou_point);
        textInfo = findViewById(R.id.text_info);




    }

    @Override
    protected void onResume() {
        super.onResume();
        GetRewardPoints();
    }

    private void GetRewardPoints() {

        User user = PrefManager.getInstance(this).getUser();
        String id =user.getId();

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", "104");
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_profile?")
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("RespRewardPoints=>", "" + response);
                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {

                                //   https://www.shipit.ng/BaiHai/webservice/get_profile?user_id=1

                                JSONObject result = object.optJSONObject("result");
                                JSONObject result2 = object.optJSONObject("result2");


                                String rewardPoints = "null";
                                if (result != null) {
                                    rewardPoints = result.optString("total_coins");
                                    if(result2 != null){
                                        rewardPoints="Bye-Hi is in process of approving the product your coins are "+rewardPoints+" and you need "+result.optString("min_coin")+ "coins  to next award";
                                    }else{
                                        rewardPoints="Bye-Hi is in process of approving the product your coins are "+rewardPoints+" and you have all gifts. congratulations";

                                    }
                                    textInfo.setText(rewardPoints);
                                }

                            } else {

                                //Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void Failed(String error) {

                        //Toast.makeText(mContext, "Failed" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }
    public void backCancelBtn(View view) {
        startActivity(new Intent(ThankyouPointActivity.this, HomeActivity.class));
        finish();
    }
}
