package com.techno.baihai.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.marozzi.roundbutton.RoundButton;
import com.techno.baihai.R;
import com.techno.baihai.adapter.FaqListAdapter;
import com.techno.baihai.api.Constant;
import com.techno.baihai.listner.FAQListener;
import com.techno.baihai.model.FAQModelList;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class SupportActivity extends AppCompatActivity implements FAQListener {

    private final String TAG = "SupportActivity";

    ImageView iv_back;
    Context mContext = this;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<FAQModelList> faqModelLists;
    SwipeRefreshLayout swipLayout;
    private String request_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_support);


        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        User user = PrefManager.getInstance(this).getUser();
        String uid = String.valueOf(user.getId());

        PrefManager.save(mContext, "uid", uid);

        recyclerView = findViewById(R.id.recycleViewFAQ);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        faqModelLists = new ArrayList<>();

        swipLayout = findViewById(R.id.swipLayout);

        swipLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetPickupRequestApi();
            }
        });

        GetPickupRequestApi();
        // handlerMethod();

    }


    private void GetPickupRequestApi() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String langua="EN";
        String lang=PrefManager.get(mContext,"lang");
        if (lang.equals("es")&& lang!=null){
        langua="ES";
        }
        HashMap<String, String> parms = new HashMap<>();
        parms.put("language", langua);
        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_faq_topic")
                .setParam(parms)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        progressDialog.dismiss();
                        swipLayout.setRefreshing(false);

                        Log.e("pickup_response", String.valueOf(response));


                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            Log.d(TAG, "STATUS_:" + status);

                            if (status.equals("1")) {

                                JSONArray result = object.optJSONArray("result");
                                //   Log.e(TAG, "result=>" + result);

                                faqModelLists.clear();


                                if (result != null) {
                                    for (int i = 0; i < result.length(); i++) {

                                        JSONObject object1 = result.getJSONObject(i);


                                        Log.e(TAG, "resulti=>" + i);
                                        String faq_id = object1.optString("faq_id");
                                        String faq_Name = object1.optString("faq");
                                        String faq_status = object1.optString("status");
                                        String faq_language = object1.optString("language");


                                        faqModelLists.add(new FAQModelList(faq_id, faq_Name, faq_status, faq_language));


                                    }
                                }


                            } else if (status.equals("0")) {

                                progressDialog.dismiss();
                                swipLayout.setRefreshing(false);
                            }


                        } catch (JSONException e) {

                            progressDialog.dismiss();
                            swipLayout.setRefreshing(false);
                            Log.i(TAG, "errorL", e);
                            e.printStackTrace();
                        }

                        mAdapter = new FaqListAdapter(getApplicationContext(), faqModelLists, SupportActivity.this);
                        recyclerView.removeAllViews();
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                        swipLayout.setRefreshing(false);
                        //Toast.makeText(mContext, "Check Your Connection " + error, Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void click(String faq_id) {
 setLog("Se solicita informacion de faq"+faq_id+"");
        GetFAQResultApi(faq_id);

    }

    private void GetFAQResultApi(String faq_id) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        HashMap<String, String> parms = new HashMap<>();
        parms.put("faq_id", faq_id);

        ApiCallBuilder.build(this)
                .isShowProgressBar(false)
                .setUrl(Constant.BASE_URL + "get_faq_ans?")
                .setParam(parms)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        progressDialog.dismiss();
                        swipLayout.setRefreshing(false);

                        Log.e("faq_response", String.valueOf(response));


                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            Log.d(TAG, "STATUS_:" + status);
                            try {


                                if (status.equals("1")) {
                                    JSONObject result = object.getJSONObject("result");
                                    String description = result.getString("description");
                                    Log.d("desc", description);

                                    final Dialog dialog = new Dialog(mContext);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.dialog_f_a_q);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                    WebView mywebview = dialog.findViewById(R.id.webView);

                                    mywebview.loadData(description, "text/html", "UTF-8");
                                    final RoundButton bt = (RoundButton)dialog.findViewById(R.id.bt_success);
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
                                                    dialog.dismiss();
                                                    // bt.revertAnimation();
                                                }
                                            }, 2000);

                                        }
                                    });
                                    dialog.show();

                                } else if (status.equals("0")) {

                                    progressDialog.dismiss();
                                    swipLayout.setRefreshing(false);
                                  }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                        } catch (JSONException e) {

                            progressDialog.dismiss();
                            swipLayout.setRefreshing(false);
                            Log.i(TAG, "errorL", e);
                            //Toast.makeText(mContext, "Check Your Network "+e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void Failed(String error) {
                        progressDialog.dismiss();
                        swipLayout.setRefreshing(false);
                        //Toast.makeText(mContext, "Check Your Connection " + error, Toast.LENGTH_LONG).show();
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
