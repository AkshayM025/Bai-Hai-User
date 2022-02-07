package com.techno.baihai.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techno.baihai.R;
import com.techno.baihai.adapter.CustomRecyclerAdapter;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.FoundationsList;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.AlertConnection;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.develpoeramit.mapicall.ApiCallBuilder;


public class YourFoundations extends AppCompatActivity {

    private static final String TAG = "YourFoundations";
    private static boolean FInish;
    RecyclerView recyclerView1;
    RecyclerView.Adapter mAdapter1;
    RecyclerView.LayoutManager layoutManager;
    Context mcontext = this;
    String latitude, longitude;
    String regID;
    String uid;
    List<FoundationsList> foundationsLists;
    private Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_your_foundations);

        PrefManager.isConnectingToInternet(this);
        isInternetPresent = PrefManager.isNetworkConnected(this);

        recyclerView1 = findViewById(R.id.recycleViewYourfoundations);
        recyclerView1.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView1.setLayoutManager(layoutManager);

        foundationsLists = new ArrayList<>();
        User user = PrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getId());
        Log.i(TAG, "user_id: " + uid);


        if (isInternetPresent) {
            GetFoundations();
        } else {
            PrefManager.showSettingsAlert(mcontext);
            }


    }


    private void GetFoundations() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(YourFoundations.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", uid);
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_organization_by_user")
                .setParam(param)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        Log.e("Response=>", "" + response);

                        progressDialog.dismiss();

                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.optString("status");
                            String message = object.optString("message");
                            if (status.equals("1")) {


                                JSONArray result = object.optJSONArray("result");

                               // Log.e("result=>", "" + result);


                                if (result != null) {
                                    for (int i = 0; i < result.length(); i++) {

                                        JSONObject object1 = result.getJSONObject(i);

                                        String org_name = object1.getString("org_name");
                                        String contact_name = object1.getString("contact_name");


                                        foundationsLists.add(new FoundationsList(org_name, contact_name));


                                    }
                                }

                            } else if (status.equals("0")) {
                                progressDialog.dismiss();
                                AlertConnection.showAlertDialog(mcontext, "Please Wait..!!",
                                        "Your Foundation Not Verify", false);
                                // Toast.makeText(mcontext, "Your Foundation Not Verify...Please Wait..!!", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Log.e("json=>", "" + e);

                        }

                        mAdapter1 = new CustomRecyclerAdapter(YourFoundations.this, foundationsLists);

                        recyclerView1.setAdapter(mAdapter1);


                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                    }
                });


    }

    public void backFromYourFoundationInit(View view) {
        onBackPressed();
        finish();
    }
}