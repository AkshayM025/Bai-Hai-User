package com.techno.baihai.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.marozzi.roundbutton.RoundButton;
import com.techno.baihai.R;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.CustomSnakbar;
import com.techno.baihai.utils.GPSTracker;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

import static com.techno.baihai.utils.PrefManager.getData;

public class SubscribeFoundationActivity extends AppCompatActivity {


    private static final String TAG = SubscribeFoundationActivity.class.getSimpleName();
    private static final int AUTOCOMPLETE_REQUEST_CODE = 111;
    ImageView et_locationIcon;
    double lat, lng;
    String latitude, longitude;
    String regID;
    private EditText et_orgname, et_contact, et_desc, et_email, et_number,et_mobileId,et_webpage;
    private final Context mContext = this;
    private ProgressBar progressBar;
    private Boolean isInternetPresent = false;
    private String uid;
    private TextView et_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.fragment_subscribe_foundation);

        PrefManager.isConnectingToInternet(this);
        isInternetPresent = PrefManager.isNetworkConnected(this);

        User user = PrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getId());

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        et_orgname = findViewById(R.id.et_orgname);
        et_email = findViewById(R.id.et_Semail);
        et_number = findViewById(R.id.et_number);
        et_location = findViewById(R.id.et_location);
        et_contact = findViewById(R.id.et_contact);
        et_desc = findViewById(R.id.et_desc);

        et_mobileId=findViewById(R.id.et_mobileId);
        et_webpage=findViewById(R.id.et_webpage);
        et_locationIcon = findViewById(R.id.et_locationIcon);


        et_locationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SubscribeFoundationActivity.this,
                        PinLocationActivity.class), AUTOCOMPLETE_REQUEST_CODE);

            }
        });


        regID = getData(getApplicationContext(), "regId", null);
        Log.e("red_ID", "-------->" + regID);
        getCurrentLocation();


    }

    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(SubscribeFoundationActivity.this);
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


    public void AddtoOrganaizationInit(View view) {

        if (isInternetPresent) {
            registerOrganizationApi(view);
        } else {
            PrefManager.showSettingsAlert(mContext);
           /* AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);*/
        }
    }


    private void registerOrganizationApi(View view) {

        final String org_name = et_orgname.getText().toString().trim();
        final String email = et_email.getText().toString().trim();
        final String number = et_number.getText().toString().trim();
        final String location_address = et_location.getText().toString().trim();
        final String contat_name = et_contact.getText().toString().trim();
        final String description = et_desc.getText().toString().trim();
        final String mobile =et_mobileId.getText().toString().trim();
        final String webpage =et_webpage.getText().toString().trim();

        progressBar = findViewById(R.id.progressBar);


        //first we will do the validations
        int length = 10;
        if (org_name.equalsIgnoreCase("")) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter Organization Name!");
            et_orgname.requestFocus();
        }/* else if (email.equalsIgnoreCase("")) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter Email!");
            et_email.requestFocus();
        } else if (number.equalsIgnoreCase("")) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter Number!");
            et_number.requestFocus();

        }*/else if(mobile.equalsIgnoreCase("")){
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter PhoneNumber!");
            et_mobileId.requestFocus();
        }else if(webpage.equalsIgnoreCase("")){
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter Webpage!");
            et_webpage.requestFocus();
        }else if (location_address.equalsIgnoreCase("")) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter Location!");
            et_location.requestFocus();
        } else if (contat_name.equalsIgnoreCase("")) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter Purpose of organization");
            et_contact.requestFocus();
        } else if (description.equalsIgnoreCase("")) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Enter Description!");
            et_desc.requestFocus();
        } else {
            // https://www.shipit.ng/BaiHai/webservice/register_organization?user_id=17&org_name=test&phone_no=78974&contact_name=ritesh&email=rite@gmail.com&description=this%20is%20test%20desdescription&location=indore%20india&lat=7987&lon=464
            progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> parms = new HashMap<>();
            parms.put("org_name", org_name);
            parms.put("email", "email");
            parms.put("phone_no", mobile);
            parms.put("webpage", webpage);
            parms.put("location", location_address);
            parms.put("contact_name", contat_name);
            parms.put("description", description);
            parms.put("lat", String.valueOf(lat));
            parms.put("lon", String.valueOf(lng));
            parms.put("user_id", uid);

            ApiCallBuilder.build(this)
                    .isShowProgressBar(false)
                    .setUrl(Constant.BASE_URL + Constant.RAGISTER_ORGANIZATION)
                    .setParam(parms)
                    .execute(new ApiCallBuilder.onResponse() {
                        @Override
                        public void Success(String response) {
                            progressBar.setVisibility(View.GONE);

                            Log.e("responsee1", String.valueOf(response));
                            // do anything with response
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.optString("status");
                                String message = object.optString("message");
                                Log.e("status", status);
                                Log.d(TAG, "STATUS_:" + status);

                                if (status.equals("1")) {
                                    progressBar.setVisibility(View.GONE);

                                    et_orgname.setText("");
                                    et_location.setText("");
                                    et_contact.setText("");
                                    et_desc.setText("");
                                    et_email.setText("");
                                    et_webpage.setText("");
                                    et_mobileId.setText("");
                                    et_number.setText("");
                                    CustomSnakbar.showSnakabar(mContext, view, "Thankyou for registering\nplease check your email for further instructions!");
                                    RegisterFoundationDailog();
                                   /* AlertDialog.Builder builder = new AlertDialog.Builder(
                                            SubscribeFoundationActivity.this);
                                    builder.setTitle("Donation");
                                    builder.setMessage("Thankyou for registering\nplease check your email for further instructions!");
                                    builder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    finish();
                                                    startActivity(new Intent(mContext, HomeActivity.class));
                                                    Animatoo.animateInAndOut(mContext);
                                                }
                                            });
                                    builder.show();
*/

                                    //JSONObject result = object.getJSONObject("result");


                                    //SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);


                                } else if (status.equals("0")) {

                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(mContext, "Check Your Connection ", Toast.LENGTH_SHORT).show();

                                }


                            } catch (JSONException e) {

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(mContext, "Check Your Network" + e, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void Failed(String error) {
                            //CustomSnakbar.showSnakabar(mContext, tv_Skip, "" + error);
                            Toast.makeText(mContext, "check your network" + error, Toast.LENGTH_SHORT).show();

                        }
                    });


        }


    }

    private void RegisterFoundationDailog(){

        final AlertDialog.Builder alert = new AlertDialog.Builder(SubscribeFoundationActivity.this);

        View mView = getLayoutInflater().inflate(R.layout.custom_foundation_dailog,null);
        final RoundButton bt = (RoundButton)mView.findViewById(R.id.registerFndId);
        final ImageView fond_cancelId = mView.findViewById(R.id.fond_cancelId);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);



        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.startAnimation();
                bt.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bt.setResultState(RoundButton.ResultState.SUCCESS);
                    }
                }, 1000);
                bt.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                        bt.revertAnimation();
                        finish();
                        //startActivity(new Intent(StripePaymentActivity.this, ThankyouPointActivity.class));
                    }
                }, 2000);

            }
        });


        fond_cancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

        alertDialog.show();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            lat = data.getExtras().getDouble("lat");
            lng = data.getExtras().getDouble("lng");
            et_location.setText(Tools.getCompleteAddressString(this, lat, lng));
        }

    }


}