package com.techno.baihai.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.CustomSnakbar;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePassword";
    TextView current_PassId, new_passId, confirm_passId;
    String uid, password;
    private final Context mContext = this;
    private Boolean isInternetPresent = false;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        PrefManager.isConnectingToInternet(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        isInternetPresent = PrefManager.isNetworkConnected(this);


        progressBar = findViewById(R.id.progressBar);

        current_PassId = findViewById(R.id.current_PassId);
        new_passId = findViewById(R.id.new_passId);
        confirm_passId = findViewById(R.id.confirm_passId);

        User user = PrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getId());
        password = user.getPassword();
        Log.i(TAG, "pass: " + password);

        Log.i(TAG, "user_id: " + uid);
    }

    public void resetPassInit(View view) {
        if (isInternetPresent) {
            Validate(view);
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
            /*AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
        }

    }

    private void Validate(View view) {

        String currentPass = current_PassId.getText().toString().trim();
        String newPass = new_passId.getText().toString().trim();
        String confirm_newPass = confirm_passId.getText().toString().trim();


        if (current_PassId.getText().toString().isEmpty()) {
            current_PassId.setError(getResources().getString(R.string.required));
            current_PassId.requestFocus();

        } else if (new_passId.getText().toString().isEmpty()) {
            new_passId.setError(getResources().getString(R.string.required));
            new_passId.requestFocus();

        } else if (confirm_passId.getText().toString().isEmpty()) {
            confirm_passId.setError(getResources().getString(R.string.required));
            confirm_passId.requestFocus();

        } else if (!current_PassId.getText().toString().equals(password)) {
            current_PassId.setError(getResources().getString(R.string.required1));
            current_PassId.requestFocus();

        } else if (!newPass.equals(confirm_newPass)) {
            confirm_passId.setError(getResources().getString(R.string.required1));
            confirm_passId.requestFocus();
        } else {

            ChangePasswordApi(uid, currentPass, newPass, confirm_newPass, view);

        }
    }


    private void ChangePasswordApi(String uid, String currentPass, String newPass, String confirm_newPass, View view) {

        //https://www.shipit.ng/BaiHai/webservice/change_password?user_id=1&password=123456

        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", uid);
        parms.put("password", confirm_newPass);
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "change_password?")
                .setParam(parms)
//                    .setFile("image", "file_path")
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {


                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {
                                CustomSnakbar.showDarkSnakabar(ChangePasswordActivity.this, view, "Reset Password Successfully!");
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                        PrefManager.getInstance(getApplicationContext()).logout();
                                        Animatoo.animateZoom(mContext);
                                        Toast.makeText(mContext, "You changed a password, Login again!", Toast.LENGTH_LONG).show();
                                    }
                                }, 1000);
                            } else {
                                CustomSnakbar.showDarkSnakabar(ChangePasswordActivity.this, view, "" + message);
                            }

                        } catch (JSONException e) {

                            // progressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void Failed(String error) {
                        // progressBar.setVisibility(View.GONE);
                        Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void changePassBackInit(View view) {
        onBackPressed();
        finish();
    }
}