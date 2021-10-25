package com.techno.baihai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.techno.baihai.R;

public class ThankyouPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_thankyou_point);
    }

    public void backCancelBtn(View view) {
        startActivity(new Intent(ThankyouPointActivity.this, HomeActivity.class));
        finish();
    }
}
