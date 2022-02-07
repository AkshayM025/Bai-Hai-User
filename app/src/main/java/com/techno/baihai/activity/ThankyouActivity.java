package com.techno.baihai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.techno.baihai.R;

public class ThankyouActivity extends AppCompatActivity {

    private String org_address;
    private TextView locatioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_thankyou);


        org_address = getIntent().getStringExtra("org_address");
        locatioID = findViewById(R.id.locatioID);
        if (org_address != null) {
            locatioID.setText(org_address);
        } else {
            Toast.makeText(this, "Address Not Found..!!", Toast.LENGTH_SHORT).show();
        }

    }

    public void backCancelBtn(View view) {
        startActivity(new Intent(ThankyouActivity.this, HomeActivity.class));
        finish();
    }
}
