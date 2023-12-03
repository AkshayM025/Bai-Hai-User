package com.techno.baihai.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.techno.baihai.R;
import com.techno.baihai.activity.DonateToFoundationFragment;
import com.techno.baihai.activity.ThankyouActivity;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;


public class PersonalDonateFragment extends AppCompatActivity {

    private static final String TAG = "PersonalDonateFragment";
    Context mContext;
    //FragmentListener listener;
    ImageView iv_back;
    CardView iv_card1, iv_card2, iv_card3;
    private Boolean isInternetPresent = false;
    private String org_id;
    private String org_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.fragment_personal_donate);

        mContext = PersonalDonateFragment.this;

        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);


        User user = PrefManager.getInstance(mContext).getUser();
        String uid = String.valueOf(user.getId());
        Log.e("user_id: ", uid);

        iv_back = findViewById(R.id.iv_back);


        org_name = getIntent().getStringExtra("org_name");

        TextView foundationNameId = findViewById(R.id.foundationNameId);
        foundationNameId.setText(org_name);

        org_id = getIntent().getStringExtra("org_id");


    }

    public void cardOne(View view) {
        if (org_id != null) {

            if (isInternetPresent) {
                GetFoundations(org_id);
            } else {
                // PrefManager prefManager = new PrefManager(mContext);
                PrefManager.showSettingsAlert(mContext);

            }

        } else {
            Toast.makeText(mContext, "Non Profit Not Found..!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void iv_back(View view) {
        onBackPressed();
        finish();
    }

    public void cardTwo(View view) {

        startActivity(new Intent(this, DonateToFoundationFragment.class).putExtra("orgName", org_name).putExtra("orgId", org_id));
    }


    private void GetFoundations(String org_id) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(PersonalDonateFragment.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        //http://bai-hai.com/webservice/get_organization_details?organization_id=1

        HashMap<String, String> param = new HashMap<>();
        param.put("organization_id", org_id);

        ApiCallBuilder.build(PersonalDonateFragment.this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_organization_details?")
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("Response=>", "" + response);

                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {

                                JSONObject result = object.optJSONObject("result");
                                if (result != null) {
                                    String org_name = result.getString("org_name");
                                }
                                if (result != null) {
                                    String contact_name = result.getString("contact_name");
                                }
                                if (result != null) {
                                    String email = result.getString("email");
                                }
                                if (result != null) {
                                    String mobile = result.getString("phone_no");
                                }
                                String location = "null";
                                if (result != null) {
                                    location = result.getString("location");
                                }
                                if (result != null) {
                                    String description = result.getString("description");
                                }
                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PersonalDonateFragment.this, ThankyouActivity.class);
                                intent.putExtra("org_address", location);
                                progressDialog.dismiss();
                                startActivity(intent);


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "Data Not Found" + message, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            //Toast.makeText(PersonalDonateFragment.this, "" + e, Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                        //Toast.makeText(PersonalDonateFragment.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                });


    }


}