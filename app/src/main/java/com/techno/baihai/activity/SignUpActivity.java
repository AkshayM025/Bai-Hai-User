package com.techno.baihai.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.utils.CustomSnakbar;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Preference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

import static com.techno.baihai.utils.PrefManager.getData;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    String latitude, longitude;
    String regID;
    private EditText et_Username, et_email, et_number, et_password, et_Cpassword;
    private final Context mContext = this;
    private ProgressBar progressBar;
    private Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_sign_up);
        PrefManager.isConnectingToInternet(this);
        isInternetPresent = PrefManager.isNetworkConnected(this);

        //if the user is already logged in we will directly start the profile activity
        if (PrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            Animatoo.animateInAndOut(mContext);
            return;
        }


        et_Username = findViewById(R.id.et_Username);
        et_email = findViewById(R.id.et_Email);
        et_number = findViewById(R.id.et_PhoneNo);
        et_password = findViewById(R.id.et_Password);
        et_Cpassword = findViewById(R.id.et_ConfirmPassword);


        regID = getData(getApplicationContext(), "regId", null);
        Log.e("red_ID", "-------->" + regID);
        getCurrentLocation();


        try {

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SignUpActivity.this,
                    new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String newToken = instanceIdResult.getToken();
                            Log.e("newToken=>", newToken);
                            Preference.save(SignUpActivity.this, Preference.REGISTER_ID, newToken);


                        }
                    });

        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(SignUpActivity.this);
        if (track.canGetLocation()) {
            latitude = String.valueOf(track.getLatitude());
            Log.e("lat=>", "-------->" + latitude);

            longitude = String.valueOf(track.getLongitude());
            Log.e("lon=>", "-------->" + longitude);

            //latLng = new LatLng(latitude, longitude);

        } else {
            track.showSettingsAlert();
        }
    }

    public void doSignUpIn(View view) {

        if (isInternetPresent) {
            registerUser(view);
        } else {
            PrefManager prefManager = new PrefManager(mContext);
            PrefManager.showSettingsAlert(mContext);
            /*AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
        }

    }

    private void registerUser(View view) {

        final String name = et_Username.getText().toString().trim();
        final String email = et_email.getText().toString().trim();
        final String number = et_number.getText().toString().trim();
        final String password = et_password.getText().toString().trim();
        final String confirm_password = et_Cpassword.getText().toString().trim();

        String MobilePattern = "[0-9]{10}";
        //String email1 = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        progressBar = findViewById(R.id.progressBar);


        //first we will do the validations

        if (name.equalsIgnoreCase("")) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter UserName!");
            et_Username.requestFocus();
        } else if (email.equalsIgnoreCase("")) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter Email!");
            et_email.requestFocus();
        } else if (password.equalsIgnoreCase("")) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter Password!");
            et_password.requestFocus();
        } else if (confirm_password.equalsIgnoreCase("")) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Re-Enter Password!");
            et_Cpassword.requestFocus();
        } else if (!password.equals(confirm_password)) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Confirm Password!");
            et_Cpassword.requestFocus();
        }   else if(!number.matches(MobilePattern)) {

                Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
        } else {

            progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> parms = new HashMap<>();
            parms.put("name", name);
            parms.put("email", email);
            parms.put("password", password);
            parms.put("confirm_password", confirm_password);
            parms.put("mobile", number);
            parms.put("type", "User");
            parms.put("lat", latitude);
            parms.put("lon", longitude);
            parms.put("register_id", Preference.get(SignUpActivity.this, Preference.REGISTER_ID));

            ApiCallBuilder.build(this)
                    .isShowProgressBar(false)
                    .setUrl(Constant.BASE_URL + Constant.POST_CREATE_AN_USER)
                    .setParam(parms)
                    .execute(new ApiCallBuilder.onResponse() {
                        @Override
                        public void Success(String response) {
                            progressBar.setVisibility(View.GONE);

                            Log.e("responseee", String.valueOf(response));
                            // do anything with response
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.optString("status");
                                String message = object.optString("message");
                                Log.e("status", status);
                                Log.d(TAG, "STATUS_:" + status);

                                if (status.equals("1")) {
                                    progressBar.setVisibility(View.GONE);

                                    Toast.makeText(mContext, "Register Sucessfully..Sent a Verification Mail..!!",
                                            Toast.LENGTH_LONG).show();


                                    JSONObject result = object.getJSONObject("result");


                          /*          User user = new User(
                                            result.getString("id"),
                                            result.getString("name"),
                                            result.getString("email"),
                                            result.getString("password"),
                                            result.getString("mobile"),
                                            result.getString("image"));

                                    PrefManager.setString(Constant.USER_ID, result.optString("id"));


                                    PrefManager.getInstance(getApplicationContext()).userLogin(user);*/
                                    startActivity(new Intent(mContext, LoginActivity.class).
                                            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_NEW_TASK));
                                    Animatoo.animateSlideLeft(mContext);

                                } else if (status.equals("0")) {

                                    progressBar.setVisibility(View.GONE);
                                    String result = object.optString("result");

                                    Toast.makeText(mContext, "" + result, Toast.LENGTH_SHORT).show();

                                }


                            } catch (JSONException e) {

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(mContext, "Check Your Network:" + e, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void Failed(String error) {
                            //CustomSnakbar.showSnakabar(mContext, tv_Skip, "" + error);
                            Toast.makeText(mContext, "check your network:" + error, Toast.LENGTH_SHORT).show();

                        }
                    });


        }


    }


    public void backLoginInit(View view) {
        //if user pressed on login
        //we will open the login screen
        finish();
        startActivity(new Intent(mContext, LoginActivity.class));
        Animatoo.animateSwipeLeft(mContext);

    }


}