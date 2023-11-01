package com.techno.baihai.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.techno.baihai.R;
import com.techno.baihai.adapter.MyAdapter;
import com.techno.baihai.api.APIClient;
import com.techno.baihai.api.APIInterface;
import com.techno.baihai.api.Constant;
import com.techno.baihai.model.User;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class MyPaymentHistoryActivity extends AppCompatActivity {


    //FragmentBookingBinding binding;
    private Context mContext;
    private APIInterface apiInterface;
    private Utils utils;
    private Boolean isInternetPresent = false;
    public TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_my_payment_history);
        mContext = MyPaymentHistoryActivity.this;

        utils = new Utils(mContext);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        String lang = PrefManager.get(mContext, "lang");
        if (lang.equals("es") && lang != null) {
            setLog("Ingreso para ver historial de pagos");
            tabLayout.addTab(tabLayout.newTab().setText("Historial Fundacion"));
            tabLayout.addTab(tabLayout.newTab().setText("Historial Bye-Hi"));



        } else {

            tabLayout.addTab(tabLayout.newTab().setText("Foundation History"));
            tabLayout.addTab(tabLayout.newTab().setText("Bye-Hi History"));


        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setAdapter(new MyAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void backFromPaymentInit(View view) {
        onBackPressed();
        finish();
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