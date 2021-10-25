package com.techno.baihai.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.techno.baihai.R;
import com.techno.baihai.activity.StripePaymentActivity;
import com.techno.baihai.utils.PrefManager;

public class DeliveryFragment extends AppCompatActivity {

    Context mContext;
    // FragmentListener listener;
    ImageView iv_back, check, uncheck;
    private Boolean baihaiStatus=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.fragment_delivery);


        iv_back = findViewById(R.id.iv_backFromDelivery);

        check = findViewById(R.id.check);

        uncheck = findViewById(R.id.uncheck);

    }

    public void iv_backFromDelivery(View view) {
        onBackPressed();
        finish();
    }

    public void ConifirmDelivery(View view) {
        baihaiStatus= true;
        PrefManager.setBoolean(PrefManager.KEY_BaiHai_Status,baihaiStatus);
        startActivity(new Intent(this, StripePaymentActivity.class));
        finish();
    }

    public void CancelDeliveryInit(View view) {
        onBackPressed();
        finish();
    }
}
