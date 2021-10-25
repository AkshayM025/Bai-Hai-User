package com.techno.baihai.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.techno.baihai.R;
import com.techno.baihai.adapter.MyAdapter;
import com.techno.baihai.api.APIClient;
import com.techno.baihai.api.APIInterface;
import com.techno.baihai.utils.PrefManager;
import com.techno.baihai.utils.Utils;

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
        mContext =MyPaymentHistoryActivity.this;

        utils = new Utils(mContext);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        PrefManager.isConnectingToInternet(mContext);
        isInternetPresent = PrefManager.isNetworkConnected(mContext);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);


        tabLayout.addTab(tabLayout.newTab().setText("Foundation History"));
        tabLayout.addTab(tabLayout.newTab().setText("Bai-Hai History"));
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
}